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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import tela.componentes.MecasoftText;
import tela.editor.editorInput.TipoFuncionarioEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.service.FuncionarioService;
import aplicacao.service.TipoFuncionarioService;
import banco.modelo.Funcionario;

public class TipoFuncionarioEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.TipoFuncionarioEditor"; //$NON-NLS-1$
	private TipoFuncionarioService service = new TipoFuncionarioService();
	private MecasoftText txtNome;

	public TipoFuncionarioEditor() {
	}

	@Override
	public void salvarRegistro() {
		try {
			validar(service.getTipo());
			
			service.saveOrUpdate();
			openInformation("Tipo de funcionário cadastrado com sucesso!");
			closeThisEditor();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void excluirRegistro() {
		List<Funcionario> listaFuncionarios = new FuncionarioService().findAllByTipo(service.getTipo());
		if(listaFuncionarios.size() != 0){
			openError("Não é possível excluir, pois existem funcionários utilizando este tipo.");
			return;
		}
		
		if(openQuestion("Deseja realmente apagar este tipo de funcionário?")){
			service.delete();
			openInformation("Tipo de funcionário excluido com sucesso!");
			closeThisEditor();
		}
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(2, false));
		
		Label lblNome = new Label(compositeConteudo, SWT.NONE);
		lblNome.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNome.setText("Nome:");
		
		txtNome = new MecasoftText(compositeConteudo, SWT.NONE);
		txtNome.setOptions(MecasoftText.LETRAS, -1);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		TipoFuncionarioEditorInput tfei = (TipoFuncionarioEditorInput) input;
		
		setShowExcluir(tfei.getTipo().getId() != null);
		
		if(tfei.getTipo().getId() != null)
			service.setTipo(service.find(tfei.getTipo().getId()));
		else
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
		IObservableValue mecasoftTexttextObserveTextObserveWidget = SWTObservables.observeText(txtNome.text, SWT.Modify);
		IObservableValue servicegetTipoNomeObserveValue = PojoObservables.observeValue(service.getTipo(), "nome");
		bindingContext.bindValue(mecasoftTexttextObserveTextObserveWidget, servicegetTipoNomeObserveValue, null, null);
		//
		return bindingContext;
	}
}
