package tela.dialog;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.ValidatorHelper.validar;

import java.util.Calendar;
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
import aplicacao.exception.ValidationException;
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
	private ComboViewer cvFinalizarServico;

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

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Selecione a pessoa cadastrada que representa a empresa e informe os horarios de espediente");
		setTitle("Configura\u00E7\u00F5es");
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));
		
		Group grpEmpresa = new Group(area, SWT.NONE);
		grpEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEmpresa.setLayout(new GridLayout(6, false));
		grpEmpresa.setText("Empresa");
		
		Label lblEmpresa = new Label(grpEmpresa, SWT.NONE);
		lblEmpresa.setText("Empresa:");
		
		txtEmpresa = new Text(grpEmpresa, SWT.BORDER);
		txtEmpresa.setEnabled(false);
		GridData gd_txtEmpresa = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtEmpresa.widthHint = 254;
		txtEmpresa.setLayoutData(gd_txtEmpresa);
		
		Button btnSelecionar = new Button(grpEmpresa, SWT.NONE);
		btnSelecionar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
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
			FormatterHelper.getDateFormatData("HH:mm").format(service.getConfiguracao().getDtInicioManha()));
		
		Group grpStatusParaPeriodos = new Group(grpEmpresa, SWT.NONE);
		grpStatusParaPeriodos.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 2));
		grpStatusParaPeriodos.setText("Status para per\u00EDodos");
		grpStatusParaPeriodos.setLayout(new GridLayout(2, false));
		
		Label lblStatusInicio = new Label(grpStatusParaPeriodos, SWT.NONE);
		lblStatusInicio.setText("Iniciar:");
		
		cvStatusInicio = new ComboViewer(grpStatusParaPeriodos, SWT.READ_ONLY);
		cbStatusInicio = cvStatusInicio.getCombo();
		cbStatusInicio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStatusFinal = new Label(grpStatusParaPeriodos, SWT.NONE);
		lblStatusFinal.setText("Finalizar:");
		
		cvStatusFinal = new ComboViewer(grpStatusParaPeriodos, SWT.READ_ONLY);
		cbStatusFinal = cvStatusFinal.getCombo();
		cbStatusFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFinalManha = new Label(grpEmpresa, SWT.NONE);
		lblFinalManha.setText("Final manh\u00E3:");
		
		txtFinalManha = new MecasoftText(grpEmpresa, SWT.NONE);
		GridData gridData_1 = (GridData) txtFinalManha.text.getLayoutData();
		gridData_1.verticalAlignment = SWT.TOP;
		gridData_1.widthHint = 100;
		gridData_1.grabExcessHorizontalSpace = false;
		txtFinalManha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtFinalManha.setOptions(MecasoftText.NUMEROS, 5);
		txtFinalManha.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtFinalManha.setText(service.getConfiguracao().getDtFinalManha() == null ? "" :
			FormatterHelper.getDateFormatData("HH:mm").format(service.getConfiguracao().getDtFinalManha()));
		
		Label lblInicioTarde = new Label(grpEmpresa, SWT.NONE);
		lblInicioTarde.setText("In\u00EDcio tarde:");
		
		txtInicioTarde = new MecasoftText(grpEmpresa, SWT.NONE);
		txtInicioTarde.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtInicioTarde.setOptions(MecasoftText.NUMEROS, 5);
		txtInicioTarde.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtInicioTarde.setText(service.getConfiguracao().getDtInicioTarde() == null ? "" :
			FormatterHelper.getDateFormatData("HH:mm").format(service.getConfiguracao().getDtInicioTarde()));
		
		Group grpStatusParaServios = new Group(grpEmpresa, SWT.NONE);
		grpStatusParaServios.setLayout(new GridLayout(2, false));
		grpStatusParaServios.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 2));
		grpStatusParaServios.setText("Status para servi\u00E7os");
		
		Label lblFinalizarServico = new Label(grpStatusParaServios, SWT.NONE);
		lblFinalizarServico.setText("Finalizar:");
		
		cvFinalizarServico = new ComboViewer(grpStatusParaServios, SWT.READ_ONLY);
		Combo cbFinalizarServico = cvFinalizarServico.getCombo();
		cbFinalizarServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFinalTarde = new Label(grpEmpresa, SWT.NONE);
		lblFinalTarde.setText("Final tarde:");
		
		txtFinalTarde = new MecasoftText(grpEmpresa, SWT.NONE);
		GridData gridData_2 = (GridData) txtFinalTarde.text.getLayoutData();
		gridData_2.grabExcessHorizontalSpace = false;
		gridData_2.widthHint = 100;
		txtFinalTarde.setOptions(MecasoftText.NUMEROS, 5);
		txtFinalTarde.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtFinalTarde.setText(service.getConfiguracao().getDtFinalTarde() == null ? "" :
			FormatterHelper.getDateFormatData("HH:mm").format(service.getConfiguracao().getDtFinalTarde()));
		
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
			validar(service.getConfiguracao());
			
			//verificar senha do usuario
			if(!txtSenhaAtual.getText().isEmpty() || !txtNovaSenha.getText().isEmpty()){
				if(!txtSenhaAtual.getText().equals(UsuarioHelper.getUsuarioLogado().getSenha())){
					setErrorMessage("Senha incorreta. Informe a senha atual corretamente para alterar a senha ou apague o campo \"Senha atual\".");
					return;
				}
				
				if(!txtNovaSenha.getText().equals(txtConfirmarSenha.getText())){
					setErrorMessage("A senha não coincide com a confirmação.");
					return;
				}
				
				Usuario user = UsuarioHelper.getUsuarioLogado();
				user.setSenha(txtNovaSenha.getText());
				usuarioService.setUsuario(user);
				usuarioService.saveOrUpdate();
			}
			
			//verifica se foi informado ao menos um horario para inciar e outro para concluir
			if((txtInicioManha.getText().isEmpty() && txtInicioTarde.getText().isEmpty())
				|| (txtFinalManha.getText().isEmpty() && txtFinalTarde.getText().isEmpty())){
				setErrorMessage("Informe ao menos um horário para inciar os serviços e outro para concluir.");
				return;
			}
			
			//verificar horario
			//inicio manhã
			if(!txtInicioManha.getText().isEmpty())
				service.getConfiguracao().setDtInicioManha(FormatterHelper.getDateFormatData("HH:mm").parse(txtInicioManha.getText()));
			else
				service.getConfiguracao().setDtInicioManha(null);
			
			//final manhã
			if(!txtFinalManha.getText().isEmpty())
				service.getConfiguracao().setDtFinalManha(FormatterHelper.getDateFormatData("HH:mm").parse(txtFinalManha.getText()));
			else
				service.getConfiguracao().setDtFinalManha(null);
			
			//inicio tarde 
			if(!txtInicioTarde.getText().isEmpty())
				service.getConfiguracao().setDtInicioTarde(FormatterHelper.getDateFormatData("HH:mm").parse(txtInicioTarde.getText()));
			else
				service.getConfiguracao().setDtInicioTarde(null);
			
			//final tarde
			if(!txtFinalTarde.getText().isEmpty())
				service.getConfiguracao().setDtFinalTarde(FormatterHelper.getDateFormatData("HH:mm").parse(txtFinalTarde.getText()));
			else
				service.getConfiguracao().setDtFinalTarde(null);
			
			Calendar c = Calendar.getInstance();
			//verifica se o inicio manha é um horario da manhã mesmo
			if(service.getConfiguracao().getDtInicioManha() != null){
				c.setTime(service.getConfiguracao().getDtInicioManha());
				if(c.get(Calendar.AM_PM) == Calendar.PM){
					setErrorMessage("Horário inválido para o início da manhã.");
					return;
				}
			}
			
			//verifica se o final manha é um horario da manhã mesmo
			if(service.getConfiguracao().getDtFinalManha() != null){
				c.setTime(service.getConfiguracao().getDtFinalManha());
				if(c.get(Calendar.AM_PM) == Calendar.PM && c.get(Calendar.HOUR) != 0){
					setErrorMessage("Horário inválido para o final da manhã.");
					return;
				}
			}
			
			//verifica se o final da manhã é maior que o inicio da manhã
			if(service.getConfiguracao().getDtInicioManha() != null && service.getConfiguracao().getDtFinalManha() != null){
				if(service.getConfiguracao().getDtFinalManha().compareTo(service.getConfiguracao().getDtInicioManha()) <= 0){
					setErrorMessage("Horário do final da manhã deve ser maior que o horário do início da manhã.");
					return;
				}
			}
			
			//verfica se o horário inicio tarde é um horario da tarde mesmo
			if(service.getConfiguracao().getDtInicioTarde() != null){
				c.setTime(service.getConfiguracao().getDtInicioTarde());
				if(c.get(Calendar.AM_PM) == Calendar.AM){
					setErrorMessage("Horário inválido para o início da tarde.");
					return;
				}
			}
			
			//verifica se o horário final tarde é um horario da tarde mesmo
			if(service.getConfiguracao().getDtFinalTarde() != null){
				c.setTime(service.getConfiguracao().getDtFinalTarde());
				if(c.get(Calendar.AM_PM) == Calendar.AM && c.get(Calendar.HOUR) != 0){
					setErrorMessage("Horário inválido para o final da tarde.");
					return;
				}
			}
			
			//verifica se hora final tarde é maior que hora inicio tarde
			if(service.getConfiguracao().getDtFinalTarde() != null && service.getConfiguracao().getDtInicioTarde() != null){
				if(service.getConfiguracao().getDtFinalTarde().compareTo(service.getConfiguracao().getDtInicioTarde()) <= 0){
					setErrorMessage("Horário do final da tarde deve ser superior que o horário do início da tarde.");
					return;
				}
			}
			
			service.saveOrUpdate();
			
			UsuarioHelper.setConfiguracaoPadrao(service.getConfiguracao());
			openInformation("Configurações salvas com sucesso!");
			HibernateConnection.commitSemClear();
			super.okPressed();
			
		}catch (ValidationException e) {
			setErrorMessage(e.getMessage());
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

	@Override
	protected Point getInitialSize() {
		return new Point(488, 493);
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
		ObservableListContentProvider listContentProvider_3 = new ObservableListContentProvider();
		IObservableMap observeMap_3 = PojoObservables.observeMap(listContentProvider_3.getKnownElements(), Status.class, "descricao");
		cvFinalizarServico.setLabelProvider(new ObservableMapLabelProvider(observeMap_3));
		cvFinalizarServico.setContentProvider(listContentProvider_3);
		//
		cvFinalizarServico.setInput(writableList_1);
		//
		IObservableValue cvFinalizarServicoObserveSingleSelection = ViewersObservables.observeSingleSelection(cvFinalizarServico);
		IObservableValue servicegetConfiguracaoStatusFinalizarServicoObserveValue = PojoObservables.observeValue(service.getConfiguracao(), "statusFinalizarServico");
		bindingContext.bindValue(cvFinalizarServicoObserveSingleSelection, servicegetConfiguracaoStatusFinalizarServicoObserveValue, null, null);
		//
		return bindingContext;
	}
}
