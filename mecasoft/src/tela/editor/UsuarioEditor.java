package tela.editor;

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
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.ResourceManager;

import tela.dialog.SelecionarItemDialog;
import tela.editor.editorInput.UsuarioEditorInput;
import aplicacao.helper.LayoutHelper;
import aplicacao.helper.MessageHelper;
import aplicacao.helper.ValidatorHelper;
import aplicacao.service.FuncionarioService;
import aplicacao.service.PapelService;
import aplicacao.service.UsuarioService;
import banco.modelo.Funcionario;
import banco.modelo.Papel;

public class UsuarioEditor extends MecasoftEditor{
	public static final String ID = "tela.editor.usuarioEditor"; //$NON-NLS-1$
	
	private Text txtNomeFuncionario;
	private Text txtLogin;
	private Text txtSenha;
	private Text txtConfirmarSenha;
	private Button btnAtivo;
	
	private UsuarioService service = new UsuarioService();
	private List<Papel> papeis;
	
	private ComboViewer comboViewer;
	
	public UsuarioEditor() {
		papeis = new PapelService().findAll();
	}
	
	@Override
	public void addComponentes(Composite compositeConteudo) {

		compositeConteudo.setLayout(new GridLayout(4, false));
		
		Label lblFuncionario = new Label(compositeConteudo, SWT.NONE);
		lblFuncionario.setText("Funcionario");
		
		txtNomeFuncionario = new Text(compositeConteudo, SWT.BORDER);
		txtNomeFuncionario.setEnabled(false);
		txtNomeFuncionario.setEditable(false);
		GridData gd_txtNomeFuncionario = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txtNomeFuncionario.widthHint = 247;
		txtNomeFuncionario.setLayoutData(gd_txtNomeFuncionario);
		
		Button btnSelecionar = new Button(compositeConteudo, SWT.NONE);
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Funcionario f  = selecionarFuncionario();
				if(f != null)
					service.getUsuario().setFuncionario(f);
				
				initDataBindings();
			}
		});
		btnSelecionar.setText("Selecionar");
		
		Label lblLogin = new Label(compositeConteudo, SWT.NONE);
		lblLogin.setText("Login:");
		
		txtLogin = new Text(compositeConteudo, SWT.BORDER);
		txtLogin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPapel = new Label(compositeConteudo, SWT.NONE);
		lblPapel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPapel.setText("Papel:");
		
		comboViewer = new ComboViewer(compositeConteudo, SWT.READ_ONLY);
		Combo cbPapel = comboViewer.getCombo();
		cbPapel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblSenha = new Label(compositeConteudo, SWT.NONE);
		lblSenha.setText("Senha:");
		
		txtSenha = new Text(compositeConteudo, SWT.BORDER | SWT.PASSWORD);
		txtSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setSelection(true);
		btnAtivo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnAtivo.setText("Ativo");
		
		Label lblConfirmarSenha = new Label(compositeConteudo, SWT.NONE);
		lblConfirmarSenha.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmarSenha.setText("Confirmar senha:");
		
		txtConfirmarSenha = new Text(compositeConteudo, SWT.BORDER | SWT.PASSWORD);
		txtConfirmarSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);

		initDataBindings();
		txtConfirmarSenha.setText(txtSenha.getText());
	}

	@Override
	public void salvarRegistro() {
		try {
			ValidatorHelper.validar(service.getUsuario());
			
			if(!txtSenha.getText().equals(txtConfirmarSenha.getText())){
				setErroMessage("Senha e confirmar senha não estão batendo.");
				return;
			}
			
			service.saveOrUpdate();
			MessageHelper.openInformation("Usuário salvo com sucesso!");
			closeThisEditor();
		} catch (aplicacao.exception.ValidationException e) {
			setErroMessage(e.getMessage());
		}
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		UsuarioEditorInput uei = (UsuarioEditorInput)input;
		setShowExcluir(false);
		
		if(uei.getUsuario().getId() != null)
			service.setUsuario(service.find(uei.getUsuario().getId()));
		else
			service.setUsuario(uei.getUsuario());
		
		setSite(site);
		setInput(input);
		
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	
	public Funcionario selecionarFuncionario(){
		SelecionarItemDialog dialog = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Funcionario)element).getNomeFantasia();
			}
		});
		dialog.setElements(new FuncionarioService().findAllSemUsuario().toArray());
		
		return (Funcionario) dialog.elementoSelecionado();
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtLoginObserveTextObserveWidget = SWTObservables.observeText(txtLogin, SWT.Modify);
		IObservableValue servicegetUsuarioLoginObserveValue = PojoObservables.observeValue(service.getUsuario(), "login");
		bindingContext.bindValue(txtLoginObserveTextObserveWidget, servicegetUsuarioLoginObserveValue, null, null);
		//
		IObservableValue txtSenhaObserveTextObserveWidget = SWTObservables.observeText(txtSenha, SWT.Modify);
		IObservableValue servicegetUsuarioSenhaObserveValue = PojoObservables.observeValue(service.getUsuario(), "senha");
		bindingContext.bindValue(txtSenhaObserveTextObserveWidget, servicegetUsuarioSenhaObserveValue, null, null);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetUsuarioAtivoObserveValue = PojoObservables.observeValue(service.getUsuario(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetUsuarioAtivoObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider.getKnownElements(), Papel.class, "nome");
		comboViewer.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		comboViewer.setContentProvider(listContentProvider);
		//
		WritableList writableList = new WritableList(papeis, Papel.class);
		comboViewer.setInput(writableList);
		//
		IObservableValue comboViewerObserveSingleSelection = ViewersObservables.observeSingleSelection(comboViewer);
		IObservableValue servicegetUsuarioPapelObserveValue = PojoObservables.observeValue(service.getUsuario(), "papel");
		bindingContext.bindValue(comboViewerObserveSingleSelection, servicegetUsuarioPapelObserveValue, null, null);
		//
		IObservableValue txtNomeFuncionarioObserveTextObserveWidget = SWTObservables.observeText(txtNomeFuncionario, SWT.Modify);
		IObservableValue servicegetUsuarioFuncionarionomeFantasiaObserveValue = PojoObservables.observeValue(service.getUsuario(), "funcionario.nomeFantasia");
		bindingContext.bindValue(txtNomeFuncionarioObserveTextObserveWidget, servicegetUsuarioFuncionarionomeFantasiaObserveValue, null, null);
		return bindingContext;
	}
}
