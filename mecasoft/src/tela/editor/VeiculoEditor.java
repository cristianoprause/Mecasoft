package tela.editor;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.ValidatorHelper.validar;

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

import tela.componentes.MecasoftText;
import tela.dialog.SelecionarItemDialog;
import tela.editor.editorInput.VeiculoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.LayoutHelper;
import aplicacao.service.PessoaService;
import aplicacao.service.TipoVeiculoService;
import aplicacao.service.VeiculoService;
import banco.connection.HibernateConnection;
import banco.modelo.Pessoa;
import banco.modelo.TipoVeiculo;

public class VeiculoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.VeiculoEditor"; //$NON-NLS-1$
	private Text txtMarca;
	private Text txtModelo;
	private Text txtCliente;
	
	private List<TipoVeiculo> tipos;
	
	private VeiculoService service = new VeiculoService();
	private TipoVeiculoService tipoService = new TipoVeiculoService();
	private PessoaService pessoaService;
	private ComboViewer cvTipo;
	private Button btnAtivo;
	private MecasoftText txtPlaca;
	private Button btnSelecionarCliente;
	private MecasoftText txtHodometro;
	private MecasoftText txtHorimetro;

	public VeiculoEditor() {
		tipos = tipoService.findAll();
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		
		compositeConteudo.setLayout(new GridLayout(4, false));
		
		Label lblMarca = new Label(compositeConteudo, SWT.NONE);
		lblMarca.setText("Marca:");
		
		txtMarca = new Text(compositeConteudo, SWT.BORDER);
		txtMarca.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblModelo = new Label(compositeConteudo, SWT.NONE);
		lblModelo.setText("Modelo:");
		
		txtModelo = new Text(compositeConteudo, SWT.BORDER);
		txtModelo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblTipo = new Label(compositeConteudo, SWT.NONE);
		lblTipo.setText("Tipo:");
		
		cvTipo = new ComboViewer(compositeConteudo, SWT.READ_ONLY);
		Combo cbTipo = cvTipo.getCombo();
		cbTipo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initDataBindings();
			}
		});
		cbTipo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cbTipo.select(0);
		
		btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setText("Ativo");
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblPlaca = new Label(compositeConteudo, SWT.NONE);
		lblPlaca.setText("Placa:");
		
		txtPlaca = new MecasoftText(compositeConteudo, SWT.NONE);
		txtPlaca.setEditable(true);
		txtPlaca.setOptions(MecasoftText.AMBOS, 8);
		txtPlaca.addChars("-", new Integer[]{3}, null, null);
		txtPlaca.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblHodometro = new Label(compositeConteudo, SWT.NONE);
		lblHodometro.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHodometro.setText("Hod\u00F4metro:");
		
		txtHodometro = new MecasoftText(compositeConteudo, SWT.NONE);
		txtHodometro.setEnabled(false);
		txtHodometro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtHodometro.setOptions(MecasoftText.NUMEROS, -1);
		
		Label lblHormetro = new Label(compositeConteudo, SWT.NONE);
		lblHormetro.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHormetro.setText("Hor\u00EDmetro:");
		
		txtHorimetro = new MecasoftText(compositeConteudo, SWT.NONE);
		txtHorimetro.setEnabled(false);
		txtHorimetro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtHorimetro.setOptions(MecasoftText.NUMEROS, -1);
		
		Label lblCliente = new Label(compositeConteudo, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(compositeConteudo, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setEditable(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(pessoaService != null)
			if(service.getVeiculo().getCliente().getNomeFantasia() != null)
				txtCliente.setText(service.getVeiculo().getCliente().getNomeFantasia());
		
		btnSelecionarCliente = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarCliente.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa p = selecionarCliente();
				if(p != null){
					service.getVeiculo().setCliente(p);
					txtCliente.setText(p.getNomeFantasia());
				}
			}
		});
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnSelecionarCliente.setText("Selecionar");
		if(pessoaService != null)
			btnSelecionarCliente.setEnabled(false);
		
		initDataBindings();
		
	}

	@Override
	public void salvarRegistro() throws ValidationException{
		validar(service.getVeiculo());
			
		if(service.getVeiculo().getTipo() != null){
				
			if(service.getVeiculo().getTipo().getHodometro()){
				if(service.getVeiculo().getHodometro() == null)
					throw new ValidationException("Informe o hod�metro.");
				
			}else if(service.getVeiculo().getTipo().getHorimetro()){
				if(service.getVeiculo().getHorimetro() == null)
					throw new ValidationException("Informe o hor�metro.");
			}
				
		}
			
		if(pessoaService != null)
			pessoaService.getPessoa().getListaVeiculo().add(service.getVeiculo());
		else
			service.saveOrUpdate();
			
		openInformation("Ve�culo cadastrado com sucesso!");
	}

	@Override
	public void excluirRegistro() {}
	

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		VeiculoEditorInput vei = (VeiculoEditorInput)input;
		
		if(vei.getVeiculo().getId() == null){
			service.setVeiculo(vei.getVeiculo());
		}else{
			service.setVeiculo(service.find(vei.getVeiculo().getId()));
			this.setPartName("Ve�culo: " + service.getVeiculo().getModelo());
		}
		
		pessoaService = vei.getFuncionarioService();
		if(vei.getFuncionarioService() != null)
			service.getVeiculo().setCliente((Pessoa)pessoaService.getPessoa());
		
		setShowExcluir(false);
		
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	
	public Pessoa selecionarCliente(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		sid.setElements(new PessoaService().findAllClientesAtivos().toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	@Override
	public void setFocus() {
		
		if(HibernateConnection.isSessionRefresh(service.getVeiculo()) && service.getVeiculo().getId() != null)
			service.setVeiculo(service.find(service.getVeiculo().getId()));
		
		tipos = tipoService.findAll();
		initDataBindings();
		
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtMarcaObserveTextObserveWidget = SWTObservables.observeText(txtMarca, SWT.Modify);
		IObservableValue servicegetVeiculoMarcaObserveValue = PojoObservables.observeValue(service.getVeiculo(), "marca");
		bindingContext.bindValue(txtMarcaObserveTextObserveWidget, servicegetVeiculoMarcaObserveValue, null, null);
		//
		IObservableValue txtModeloObserveTextObserveWidget = SWTObservables.observeText(txtModelo, SWT.Modify);
		IObservableValue servicegetVeiculoModeloObserveValue = PojoObservables.observeValue(service.getVeiculo(), "modelo");
		bindingContext.bindValue(txtModeloObserveTextObserveWidget, servicegetVeiculoModeloObserveValue, null, null);
		//
		IObservableValue btnAtivoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnAtivo);
		IObservableValue servicegetVeiculoAtivoObserveValue = PojoObservables.observeValue(service.getVeiculo(), "ativo");
		bindingContext.bindValue(btnAtivoObserveSelectionObserveWidget, servicegetVeiculoAtivoObserveValue, null, null);
		//
		IObservableValue txtClienteObserveTextObserveWidget = SWTObservables.observeText(txtCliente, SWT.Modify);
		IObservableValue servicegetVeiculoClientenomeFantasiaObserveValue = PojoObservables.observeValue(service.getVeiculo(), "cliente.nomeFantasia");
		bindingContext.bindValue(txtClienteObserveTextObserveWidget, servicegetVeiculoClientenomeFantasiaObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider.getKnownElements(), TipoVeiculo.class, "nome");
		cvTipo.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvTipo.setContentProvider(listContentProvider);
		//
		WritableList writableList = new WritableList(tipos, TipoVeiculo.class);
		cvTipo.setInput(writableList);
		//
		IObservableValue cvTipoObserveSingleSelection = ViewersObservables.observeSingleSelection(cvTipo);
		IObservableValue servicegetVeiculoTipoObserveValue = PojoObservables.observeValue(service.getVeiculo(), "tipo");
		bindingContext.bindValue(cvTipoObserveSingleSelection, servicegetVeiculoTipoObserveValue, null, null);
		//
		IObservableValue txtPlacatextObserveTextObserveWidget = SWTObservables.observeText(txtPlaca.text, SWT.Modify);
		IObservableValue servicegetVeiculoPlacaObserveValue = PojoObservables.observeValue(service.getVeiculo(), "placa");
		bindingContext.bindValue(txtPlacatextObserveTextObserveWidget, servicegetVeiculoPlacaObserveValue, null, null);
		//
		IObservableValue txtHodometrotextObserveTextObserveWidget = SWTObservables.observeText(txtHodometro.text, SWT.Modify);
		IObservableValue servicegetVeiculoHodometroObserveValue = PojoObservables.observeValue(service.getVeiculo(), "hodometro");
		bindingContext.bindValue(txtHodometrotextObserveTextObserveWidget, servicegetVeiculoHodometroObserveValue, null, null);
		//
		IObservableValue mecasoftTexttextObserveTextObserveWidget = SWTObservables.observeText(txtHorimetro.text, SWT.Modify);
		IObservableValue servicegetVeiculoHorimetroObserveValue = PojoObservables.observeValue(service.getVeiculo(), "horimetro");
		bindingContext.bindValue(mecasoftTexttextObserveTextObserveWidget, servicegetVeiculoHorimetroObserveValue, null, null);
		//
		IObservableValue txtHodometroObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtHodometro);
		IObservableValue servicegetVeiculoTipohodometroObserveValue = PojoObservables.observeValue(service.getVeiculo(), "tipo.hodometro");
		bindingContext.bindValue(txtHodometroObserveEnabledObserveWidget, servicegetVeiculoTipohodometroObserveValue, null, null);
		//
		IObservableValue txtHorimetroObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtHorimetro);
		IObservableValue servicegetVeiculoTipohorimetroObserveValue = PojoObservables.observeValue(service.getVeiculo(), "tipo.horimetro");
		bindingContext.bindValue(txtHorimetroObserveEnabledObserveWidget, servicegetVeiculoTipohorimetroObserveValue, null, null);
		//
		return bindingContext;
	}
}
