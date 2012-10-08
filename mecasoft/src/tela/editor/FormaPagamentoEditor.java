package tela.editor;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.ValidatorHelper.validar;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import tela.editor.editorInput.FormaPagamentoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.service.FormaPagamentoService;

public class FormaPagamentoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.FormaPagamentoEditor"; //$NON-NLS-1$
	private Text txtNome;
	
	private FormaPagamentoService service = new FormaPagamentoService();
	private Button btnGeraPagamento;
	private Button btnGeraDuplicatas;
	private Button btnAtivo;

	public FormaPagamentoEditor() {
	}

	@Override
	public void salvarRegistro() throws ValidationException {
		validar(service.getForma());
			
		service.saveOrUpdate();
		openInformation("Forma de pagamento cadastrada com sucesso");
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblNome = new Label(compositeConteudo, SWT.NONE);
		lblNome.setText("Nome:");
		
		txtNome = new Text(compositeConteudo, SWT.BORDER);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnGeraPagamento = new Button(compositeConteudo, SWT.RADIO);
		btnGeraPagamento.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnGeraPagamento.setText("Gera pagamento \u00E0 vista");
		
		btnGeraDuplicatas = new Button(compositeConteudo, SWT.RADIO);
		btnGeraDuplicatas.setText("Gera duplicatas");
		
		btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setText("Ativo");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setShowExcluir(false);
		FormaPagamentoEditorInput fpei = (FormaPagamentoEditorInput)input;
		
		if(fpei.getForma().getId() != null){
			service.setForma(service.find(fpei.getForma().getId()));
			setPartName("Forma: " + service.getForma().getNome());
		}else
			service.setForma(fpei.getForma());
		
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
		IObservableValue servicegetFormaNomeObserveValue = PojoObservables.observeValue(service.getForma(), "nome");
		bindingContext.bindValue(txtNomeObserveTextObserveWidget, servicegetFormaNomeObserveValue, null, null);
		//
		IObservableValue btnGeraPagamentoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnGeraPagamento);
		IObservableValue servicegetFormaGeraPagVistaObserveValue = PojoObservables.observeValue(service.getForma(), "geraPagVista");
		bindingContext.bindValue(btnGeraPagamentoObserveSelectionObserveWidget, servicegetFormaGeraPagVistaObserveValue, null, null);
		//
		IObservableValue btnGeraDuplicatasObserveSelectionObserveWidget = SWTObservables.observeSelection(btnGeraDuplicatas);
		IObservableValue servicegetFormaGeraDuplicataObserveValue = PojoObservables.observeValue(service.getForma(), "geraDuplicata");
		bindingContext.bindValue(btnGeraDuplicatasObserveSelectionObserveWidget, servicegetFormaGeraDuplicataObserveValue, null, null);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetFormaAtivoObserveValue = PojoObservables.observeValue(service.getForma(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetFormaAtivoObserveValue, null, null);
		//
		return bindingContext;
	}
}
