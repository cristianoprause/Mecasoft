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

import tela.editor.editorInput.StatusEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.service.StatusService;

public class StatusEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.StatusEditor"; //$NON-NLS-1$
	private Text txtDescricao;
	
	private StatusService service = new StatusService();
	private Button btnPausa;
	private Button btnAtivo;
	private Button btnContinua;

	public StatusEditor() {
	}

	@Override
	public void salvarRegistro() {
		try {
			validar(service.getStatus());
			
			service.saveOrUpdate();
			openInformation("Status cadastrado com sucesso!");
			closeThisEditor();
		} catch (ValidationException e) {
			setErroMessage(e.getMessage());
		}
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblDescricao = new Label(compositeConteudo, SWT.NONE);
		lblDescricao.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescricao.setText("Descri\u00E7\u00E3o:");
		
		txtDescricao = new Text(compositeConteudo, SWT.BORDER);
		txtDescricao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnContinua = new Button(compositeConteudo, SWT.RADIO);
		btnContinua.setSelection(!service.getStatus().isPausar());
		btnContinua.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnContinua.setText("Continua servi\u00E7o");
		
		btnPausa = new Button(compositeConteudo, SWT.RADIO);
		btnPausa.setText("Pausa servi\u00E7o");
		
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
		
		StatusEditorInput sei = (StatusEditorInput)input;
		
		if(sei.getStatus().getId() != null){
			service.setStatus(service.find(sei.getStatus().getId()));
			this.setPartName("Status: " + service.getStatus().getDescricao());
		}else
			service.setStatus(sei.getStatus());
		
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
		IObservableValue txtDescricaoObserveTextObserveWidget = SWTObservables.observeText(txtDescricao, SWT.Modify);
		IObservableValue servicegetStatusDescricaoObserveValue = PojoObservables.observeValue(service.getStatus(), "descricao");
		bindingContext.bindValue(txtDescricaoObserveTextObserveWidget, servicegetStatusDescricaoObserveValue, null, null);
		//
		IObservableValue btnPausaObserveSelectionObserveWidget = SWTObservables.observeSelection(btnPausa);
		IObservableValue servicegetStatusPausarObserveValue = PojoObservables.observeValue(service.getStatus(), "pausar");
		bindingContext.bindValue(btnPausaObserveSelectionObserveWidget, servicegetStatusPausarObserveValue, null, null);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetStatusAtivoObserveValue = PojoObservables.observeValue(service.getStatus(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetStatusAtivoObserveValue, null, null);
		//
		return bindingContext;
	}
}
