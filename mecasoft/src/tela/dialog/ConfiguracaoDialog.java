package tela.dialog;

import static aplicacao.helper.MessageHelper.openInformation;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import banco.connection.HibernateConnection;
import banco.modelo.Configuracao;
import banco.modelo.Pessoa;

public class ConfiguracaoDialog extends TitleAreaDialog {
	private Text txtEmpresa;
	private MecasoftText txtInicioTarde;
	private MecasoftText txtInicioManha;
	private MecasoftText txtFinalManha;
	private MecasoftText txtFinalTarde;
	
	private ConfiguracaoService service = new ConfiguracaoService();

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfiguracaoDialog(Shell parentShell) {
		super(parentShell);
		
		if(UsuarioHelper.getConfiguracaoPadrao() == null)
			service.setConfiguracao(new Configuracao());
		else
			service.setConfiguracao(service.find(UsuarioHelper.getConfiguracaoPadrao().getId()));
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
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(5, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblEmpresa = new Label(container, SWT.NONE);
		lblEmpresa.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmpresa.setText("Empresa:");
		
		txtEmpresa = new Text(container, SWT.BORDER);
		txtEmpresa.setEnabled(false);
		txtEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnSelecionar = new Button(container, SWT.NONE);
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
		
		Label lblPeriodoManh = new Label(container, SWT.NONE);
		lblPeriodoManh.setText("Per\u00EDodo manh\u00E3:");
		
		txtInicioManha = new MecasoftText(container, SWT.NONE);
		GridData gridData = (GridData) txtInicioManha.text.getLayoutData();
		gridData.widthHint = 100;
		gridData.grabExcessHorizontalSpace = false;
		txtInicioManha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtInicioManha.setOptions(MecasoftText.NUMEROS, 5);
		txtInicioManha.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtInicioManha.setText(service.getConfiguracao().getDtInicioManha() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtInicioManha()));
		
		Label lblAte = new Label(container, SWT.NONE);
		lblAte.setText("at\u00E9");
		
		txtFinalManha = new MecasoftText(container, SWT.NONE);
		GridData gridData_1 = (GridData) txtFinalManha.text.getLayoutData();
		gridData_1.widthHint = 100;
		gridData_1.grabExcessHorizontalSpace = false;
		txtFinalManha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtFinalManha.setOptions(MecasoftText.NUMEROS, 5);
		txtFinalManha.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtFinalManha.setText(service.getConfiguracao().getDtFinalManha() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtFinalManha()));
		
		new Label(container, SWT.NONE);
		
		Label lblPeriodoTarde = new Label(container, SWT.NONE);
		lblPeriodoTarde.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriodoTarde.setText("Per\u00EDodo tarde:");
		
		txtInicioTarde = new MecasoftText(container, SWT.BORDER);
		txtInicioTarde.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtInicioTarde.setOptions(MecasoftText.NUMEROS, 5);
		txtInicioTarde.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtInicioTarde.setText(service.getConfiguracao().getDtInicioTarde() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtInicioTarde()));
		
		Label lblAt = new Label(container, SWT.NONE);
		lblAt.setText("at\u00E9");
		
		txtFinalTarde = new MecasoftText(container, SWT.NONE);
		GridData gridData_2 = (GridData) txtFinalTarde.text.getLayoutData();
		gridData_2.grabExcessHorizontalSpace = false;
		gridData_2.widthHint = 100;
		txtFinalTarde.setOptions(MecasoftText.NUMEROS, 5);
		txtFinalTarde.addChars(FormatterHelper.MECASOFTTXTHORA, new Integer[]{-2}, null, null);
		txtFinalTarde.setText(service.getConfiguracao().getDtFinalTarde() == null ? "" :
			FormatterHelper.DATEFOTMATHORA.format(service.getConfiguracao().getDtFinalTarde()));
		new Label(container, SWT.NONE);

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
			if(!txtInicioManha.getText().isEmpty())
				service.getConfiguracao().setDtInicioManha(FormatterHelper.DATEFOTMATHORA.parse(txtInicioManha.getText()));
			
			if(!txtFinalManha.getText().isEmpty())
				service.getConfiguracao().setDtFinalManha(FormatterHelper.DATEFOTMATHORA.parse(txtFinalManha.getText()));
			
			if(!txtInicioTarde.getText().isEmpty())
				service.getConfiguracao().setDtInicioTarde(FormatterHelper.DATEFOTMATHORA.parse(txtInicioTarde.getText()));
			
			if(!txtFinalTarde.getText().isEmpty())
				service.getConfiguracao().setDtFinalTarde(FormatterHelper.DATEFOTMATHORA.parse(txtFinalTarde.getText()));

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
		return new Point(450, 238);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtEmpresaObserveTextObserveWidget = SWTObservables.observeText(txtEmpresa, SWT.Modify);
		IObservableValue servicegetConfiguracaoRepresentanteEmpresanomeFantasiaObserveValue = PojoObservables.observeValue(service.getConfiguracao(), "representanteEmpresa.nomeFantasia");
		bindingContext.bindValue(txtEmpresaObserveTextObserveWidget, servicegetConfiguracaoRepresentanteEmpresanomeFantasiaObserveValue, null, null);
		//
		return bindingContext;
	}
}
