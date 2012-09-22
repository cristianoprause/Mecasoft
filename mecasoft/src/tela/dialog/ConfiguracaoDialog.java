package tela.dialog;

import static aplicacao.helper.MessageHelper.openInformation;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.ConfiguracaoService;
import aplicacao.service.PessoaService;
import aplicacao.service.StatusService;
import aplicacao.service.UsuarioService;
import banco.connection.HibernateConnection;
import banco.modelo.Configuracao;
import banco.modelo.Pessoa;
import banco.modelo.Status;
import banco.modelo.Usuario;

public class ConfiguracaoDialog extends TitleAreaDialog {
	private Text txtEmpresa;
	private MecasoftText txtInicioTarde;
	private MecasoftText txtInicioManha;
	private MecasoftText txtFinalManha;
	private MecasoftText txtFinalTarde;
	private Text txtSenhaAtual;
	private Text txtNovaSenha;
	private Text txtConfirmarSenha;
	
	private ConfiguracaoService service = new ConfiguracaoService();
	private UsuarioService usuarioService = new UsuarioService();
	private StatusService statusService = new StatusService();
	private List<Status> statusIniciais;
	private List<Status> statusFinais;
	private ComboViewer cvStatusInicio;
	private ComboViewer cvStatusFinal;
	private Combo cbStatusInicio;
	private Combo cbStatusFinal;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfiguracaoDialog(Shell parentShell) {
		super(parentShell);
		
		//pega as configurações caso o usuário ja tenha salvo
		if(UsuarioHelper.getConfiguracaoPadrao() == null)
			service.setConfiguracao(new Configuracao());
		else
			service.setConfiguracao(service.find(UsuarioHelper.getConfiguracaoPadrao().getId()));
		
		//busca os status
		statusIniciais = statusService.findAllAtivosByFuncao(false);
		statusFinais = statusService.findAllAtivosByFuncao(true);
		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Selecione a pessoa cadastrada que representa a empresa e informe os horarios de espediente");
		setTitle("Configura\u00E7\u00F5es");
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));
		
		Group grpEmpresa = new Group(area, SWT.NONE);
		grpEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEmpresa.setLayout(new GridLayout(5, false));
		grpEmpresa.setText("Empresa");
		
		Label lblEmpresa = new Label(grpEmpresa, SWT.NONE);
		lblEmpresa.setText("Empresa:");
		
		txtEmpresa = new Text(grpEmpresa, SWT.BORDER);
		txtEmpresa.setEnabled(false);
		txtEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnSelecionar = new Button(grpEmpresa, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa p = selecionarPessoa();
				if(p != null){
					service.getConfiguracao().setRepresentanteEmpresa(p);
					txtEmpresa.setText(p.getNomeFantasia());
				}
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");
		
		Label lblInicioManha = new Label(grpEmpresa, SWT.NONE);
		lblInicioManha.setText("In\u00EDcio manh\u00E3:");
		
		txtInicioManha = new MecasoftText(grpEmpresa, SWT.NONE);
		GridData gridData = (GridData) txtInicioManha.text.getLayoutData();
		gridData.widthHint = 100;
		gridData.grabExcessHorizontalSpace = false;
		txtInicioManha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtInicioManha.setOptions(MecasoftText.NUMEROS, 5);
		txtInicioManha.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtInicioManha.setText(service.getConfiguracao().getDtInicioManha() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtInicioManha()));
		
		Label lblStatusInicio = new Label(grpEmpresa, SWT.NONE);
		lblStatusInicio.setText("Status para iniciar:");
		
		cvStatusInicio = new ComboViewer(grpEmpresa, SWT.READ_ONLY);
		cbStatusInicio = cvStatusInicio.getCombo();
		cbStatusInicio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblFinalManha = new Label(grpEmpresa, SWT.NONE);
		lblFinalManha.setText("Final manh\u00E3:");
		
		txtFinalManha = new MecasoftText(grpEmpresa, SWT.NONE);
		GridData gridData_1 = (GridData) txtFinalManha.text.getLayoutData();
		gridData_1.widthHint = 100;
		gridData_1.grabExcessHorizontalSpace = false;
		txtFinalManha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtFinalManha.setOptions(MecasoftText.NUMEROS, 5);
		txtFinalManha.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtFinalManha.setText(service.getConfiguracao().getDtFinalManha() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtFinalManha()));
		new Label(grpEmpresa, SWT.NONE);
		new Label(grpEmpresa, SWT.NONE);
		new Label(grpEmpresa, SWT.NONE);
		
		Label lblInicioTarde = new Label(grpEmpresa, SWT.NONE);
		lblInicioTarde.setText("In\u00EDcio tarde:");
		
		txtInicioTarde = new MecasoftText(grpEmpresa, SWT.NONE);
		txtInicioTarde.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtInicioTarde.setOptions(MecasoftText.NUMEROS, 5);
		txtInicioTarde.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtInicioTarde.setText(service.getConfiguracao().getDtInicioTarde() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtInicioTarde()));
		
		Label lblStatusFinal = new Label(grpEmpresa, SWT.NONE);
		lblStatusFinal.setText("Status para finalizar:");
		
		cvStatusFinal = new ComboViewer(grpEmpresa, SWT.READ_ONLY);
		cbStatusFinal = cvStatusFinal.getCombo();
		cbStatusFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblFinalTarde = new Label(grpEmpresa, SWT.NONE);
		lblFinalTarde.setText("Final tarde:");
		
		txtFinalTarde = new MecasoftText(grpEmpresa, SWT.NONE);
		GridData gridData_2 = (GridData) txtFinalTarde.text.getLayoutData();
		gridData_2.grabExcessHorizontalSpace = false;
		gridData_2.widthHint = 100;
		txtFinalTarde.setOptions(MecasoftText.NUMEROS, 5);
		txtFinalTarde.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtFinalTarde.setText(service.getConfiguracao().getDtFinalTarde() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtFinalTarde()));
		new Label(grpEmpresa, SWT.NONE);
		new Label(grpEmpresa, SWT.NONE);
		new Label(grpEmpresa, SWT.NONE);
		
		Group grpSenha = new Group(area, SWT.NONE);
		grpSenha.setLayout(new GridLayout(2, false));
		grpSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpSenha.setText("Senha");
		
		Label lblSenhaAtual = new Label(grpSenha, SWT.NONE);
		lblSenhaAtual.setText("Senha atual:");
		
		txtSenhaAtual = new Text(grpSenha, SWT.BORDER | SWT.PASSWORD);
		txtSenhaAtual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNovaSenha = new Label(grpSenha, SWT.NONE);
		lblNovaSenha.setText("Nova senha:");
		
		txtNovaSenha = new Text(grpSenha, SWT.BORDER | SWT.PASSWORD);
		txtNovaSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConfirmarSenha = new Label(grpSenha, SWT.NONE);
		lblConfirmarSenha.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmarSenha.setText("Confirmar senha:");
		
		txtConfirmarSenha = new Text(grpSenha, SWT.BORDER | SWT.PASSWORD);
		txtConfirmarSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		initDataBindings();

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void okPressed() {
		
		try{
			//verificar senha do usuario
			if(!txtSenhaAtual.getText().isEmpty() || !txtNovaSenha.getText().isEmpty()){
				if(!txtSenhaAtual.getText().equals(UsuarioHelper.getUsuarioLogado().getSenha())){
					setErrorMessage("Senha incorreta. Informe a senha atual corretamente para alterar a senha ou apague o campo \"Senha atual\".");
					return;
				}
				
				if(!txtNovaSenha.getText().equals(txtConfirmarSenha.getText())){
					setErrorMessage("Nova senha e confirmar senha não estão batendo.");
					return;
				}
				
				Usuario user = UsuarioHelper.getUsuarioLogado();
				user.setSenha(txtNovaSenha.getText());
				usuarioService.setUsuario(user);
				usuarioService.saveOrUpdate();
			}
			
			//verificar horario
			if(!txtInicioManha.getText().isEmpty())
				service.getConfiguracao().setDtInicioManha(FormatterHelper.DATEFOTMATHORA.parse(txtInicioManha.getText()));
			
			if(!txtFinalManha.getText().isEmpty())
				service.getConfiguracao().setDtFinalManha(FormatterHelper.DATEFOTMATHORA.parse(txtFinalManha.getText()));
			
			if(!txtInicioTarde.getText().isEmpty())
				service.getConfiguracao().setDtInicioTarde(FormatterHelper.DATEFOTMATHORA.parse(txtInicioTarde.getText()));
			
			if(!txtFinalTarde.getText().isEmpty())
				service.getConfiguracao().setDtFinalTarde(FormatterHelper.DATEFOTMATHORA.parse(txtFinalTarde.getText()));
			
			//verifica se o usuário informou algum horario para iniciar os serviços, mas não colocou status para inicia-los
			if((service.getConfiguracao().getDtInicioManha() != null || service.getConfiguracao().getDtInicioTarde() != null)
				&& (service.getConfiguracao().getStatusInicio() == null)){
				setErrorMessage("Selecione um status para iniciar os serviços no horário configurado.");
				return;
			}
			
			//verifica se o usuário informou algum horario para finalizar os serviços, mas não colocou status para finaliza-los
			if((service.getConfiguracao().getDtFinalManha() != null || service.getConfiguracao().getDtFinalTarde() != null)
					&& (service.getConfiguracao().getStatusFinal() == null)){
				setErrorMessage("Selecione um status para finalizar os serviços no horário configurado.");
				return;
			}
			
			service.saveOrUpdate();
			
			UsuarioHelper.setConfiguracaoPadrao(service.getConfiguracao());
			openInformation("Configuraçõe salvas com sucesso!");
			HibernateConnection.commit();
			super.okPressed();
			
		}catch(Exception e){
			setErrorMessage("Informe os horários corretamente");
		}
		
	}
	
	private Pessoa selecionarPessoa(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		
		sid.setElements(new PessoaService().findAllAtivos().toArray());
		
		return (Pessoa)sid.getElementoSelecionado();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(462, 386);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtEmpresaObserveTextObserveWidget = SWTObservables.observeText(txtEmpresa, SWT.Modify);
		IObservableValue servicegetConfiguracaoRepresentanteEmpresanomeFantasiaObserveValue = PojoObservables.observeValue(service.getConfiguracao(), "representanteEmpresa.nomeFantasia");
		bindingContext.bindValue(txtEmpresaObserveTextObserveWidget, servicegetConfiguracaoRepresentanteEmpresanomeFantasiaObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider.getKnownElements(), Status.class, "descricao");
		cvStatusInicio.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvStatusInicio.setContentProvider(listContentProvider);
		//
		WritableList writableList = new WritableList(statusIniciais, Status.class);
		cvStatusInicio.setInput(writableList);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
		IObservableMap observeMap_1 = PojoObservables.observeMap(listContentProvider_1.getKnownElements(), Status.class, "descricao");
		cvStatusFinal.setLabelProvider(new ObservableMapLabelProvider(observeMap_1));
		cvStatusFinal.setContentProvider(listContentProvider_1);
		//
		WritableList writableList_1 = new WritableList(statusFinais, Status.class);
		cvStatusFinal.setInput(writableList_1);
		//
		IObservableValue cvStatusInicioObserveSingleSelection = ViewersObservables.observeSingleSelection(cvStatusInicio);
		IObservableValue servicegetConfiguracaoStatusInicioObserveValue = PojoObservables.observeValue(service.getConfiguracao(), "statusInicio");
		bindingContext.bindValue(cvStatusInicioObserveSingleSelection, servicegetConfiguracaoStatusInicioObserveValue, null, null);
		//
		IObservableValue cvStatusFinalObserveSingleSelection = ViewersObservables.observeSingleSelection(cvStatusFinal);
		IObservableValue servicegetConfiguracaoStatusFinalObserveValue = PojoObservables.observeValue(service.getConfiguracao(), "statusFinal");
		bindingContext.bindValue(cvStatusFinalObserveSingleSelection, servicegetConfiguracaoStatusFinalObserveValue, null, null);
		//
		return bindingContext;
	}
}
