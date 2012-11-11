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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import tela.editor.editorInput.TipoVeiculoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.service.TipoVeiculoService;
import aplicacao.service.VeiculoService;
import banco.connection.HibernateConnection;
import banco.modelo.TipoVeiculo;
import banco.modelo.Veiculo;

public class TipoVeiculoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.TipoVeiculoEditor"; //$NON-NLS-1$
	private Text txtNome;
	
	private TipoVeiculoService service = new TipoVeiculoService();
	private Button btnHodmetro;
	private Button btnHormetro;

	public TipoVeiculoEditor() {
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {

		compositeConteudo.setLayout(new GridLayout(2, false));
		
		Label lblNome = new Label(compositeConteudo, SWT.NONE);
		lblNome.setText("Nome:");
		
		txtNome = new Text(compositeConteudo, SWT.BORDER);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnHodmetro = new Button(compositeConteudo, SWT.RADIO);
		btnHodmetro.setSelection(true);
		btnHodmetro.setText("Hodômetro");
		
		btnHormetro = new Button(compositeConteudo, SWT.RADIO);
		btnHormetro.setText("Horímetro");
		initDataBindings();
	}
	
	@Override
	public void salvarRegistro() throws ValidationException{
		validar(service.getTipoVeiculo());
		service.saveOrUpdate();
		openInformation("Tipo de veículo cadastrado com sucesso!");
	}

	@Override
	public void excluirRegistro() {
		List<Veiculo> listaVeiculos = new VeiculoService().findAllByTipo(service.getTipoVeiculo());
		if(listaVeiculos.size() > 0){
			openError("Não é possível excluir, pois existem veículos utilizando este tipo.");
			return;
		}
		
		if(openQuestion("Deseja realmente apagar este tipo de veículo?")){
			service.delete();
			openInformation("Tipo de veículo excluido com sucesso!");
			closeThisEditor();
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		TipoVeiculoEditorInput tvei = (TipoVeiculoEditorInput)input;
		
		setShowExcluir(tvei.getTipo().getId() != null);
		
		if(tvei.getTipo().getId() != null){
			service.setTipoVeiculo(service.find(tvei.getTipo().getId()));
			this.setPartName("Tipo de veículo: " + service.getTipoVeiculo().getNome());
		}else
			service.setTipoVeiculo(new TipoVeiculo());
		
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
		IObservableValue servicegetTipoVeiculoNomeObserveValue = PojoObservables.observeValue(service.getTipoVeiculo(), "nome");
		bindingContext.bindValue(txtNomeObserveTextObserveWidget, servicegetTipoVeiculoNomeObserveValue, null, null);
		//
		IObservableValue btnHodmetroObserveSelectionObserveWidget = SWTObservables.observeSelection(btnHodmetro);
		IObservableValue servicegetTipoVeiculoHodometroObserveValue = PojoObservables.observeValue(service.getTipoVeiculo(), "hodometro");
		bindingContext.bindValue(btnHodmetroObserveSelectionObserveWidget, servicegetTipoVeiculoHodometroObserveValue, null, null);
		//
		IObservableValue btnHormetroObserveSelectionObserveWidget = SWTObservables.observeSelection(btnHormetro);
		IObservableValue servicegetTipoVeiculoHorimetroObserveValue = PojoObservables.observeValue(service.getTipoVeiculo(), "horimetro");
		bindingContext.bindValue(btnHormetroObserveSelectionObserveWidget, servicegetTipoVeiculoHorimetroObserveValue, null, null);
		//
		return bindingContext;
	}

	@Override
	public void setFocus() {
		if(HibernateConnection.isSessionRefresh(service.getTipoVeiculo()) && service.getTipoVeiculo().getId() != null)
			service.setTipoVeiculo(service.find(service.getTipoVeiculo().getId()));
		
		initDataBindings();
	}

}
