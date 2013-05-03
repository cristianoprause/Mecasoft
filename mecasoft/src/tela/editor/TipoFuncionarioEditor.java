package tela.editor;

import static aplicacao.helper.MessageHelper.openError;
import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.ValidatorHelper.validar;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import tela.editor.editorInput.TipoFuncionarioEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.service.MecasoftService;
import aplicacao.service.PessoaService;
import aplicacao.service.TipoFuncionarioService;
import banco.modelo.Pessoa;

public class TipoFuncionarioEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.TipoFuncionarioEditor"; //$NON-NLS-1$
	private TipoFuncionarioService service = new TipoFuncionarioService();
	private Text txtNome;

	public TipoFuncionarioEditor() {
	}

	@Override
	public void salvarRegistro() throws ValidationException{
		validar(service.getTipo());
			
		service.saveOrUpdate();
		openInformation("Tipo de funcion�rio cadastrado com sucesso!");
	}

	@Override
	public void excluirRegistro() {
		List<Pessoa> listaFuncionarios = new PessoaService().findAllByTipoFuncionario(service.getTipo());
		if(listaFuncionarios.size() != 0){
			openError("N�o � poss�vel excluir, pois existem funcion�rios utilizando este tipo.");
			return;
		}
		
		if(openQuestion("Deseja realmente apagar este tipo de funcion�rio?")){
			service.delete();
			openInformation("Tipo de funcion�rio excluido com sucesso!");
			closeThisEditor();
		}
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(2, false));
		
		Label lblNome = new Label(compositeConteudo, SWT.NONE);
		lblNome.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNome.setText("Nome:");
		
		txtNome = new Text(compositeConteudo, SWT.BORDER);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		TipoFuncionarioEditorInput tfei = (TipoFuncionarioEditorInput) input;
		
		setShowExcluir(tfei.getTipo().getId() != null);
		
		if(tfei.getTipo().getId() != null){
			service.setTipo(service.find(tfei.getTipo().getId()));
			this.setPartName("Tipo de funcion�rio: " + service.getTipo().getNome());
		}else
			service.setTipo(tfei.getTipo());
		
		setSite(site);
		setInput(input);
		
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtNomeObserveTextObserveWidget = SWTObservables.observeText(txtNome, SWT.Modify);
		IObservableValue servicegetTipoNomeObserveValue = PojoObservables.observeValue(service.getTipo(), "nome");
		bindingContext.bindValue(txtNomeObserveTextObserveWidget, servicegetTipoNomeObserveValue, null, null);
		//
		return bindingContext;
	}

	@Override
	public void setFocus() {
	}

	@Override
	public MecasoftService<?> getService() {
		return service;
	}
}
