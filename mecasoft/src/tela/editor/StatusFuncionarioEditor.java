package tela.editor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.editor.editorInput.StatusFuncionarioEditorInput;
import tela.filter.StatusFuncionarioAnaliticoFilter;
import tela.viewerSorter.TableStatusServicoViewerSorter;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.PessoaService;
import aplicacao.service.StatusServicoService;
import banco.connection.HibernateConnection;
import banco.modelo.Pessoa;
import banco.modelo.StatusServico;

public class StatusFuncionarioEditor extends EditorPart {

	public static final String ID = "tela.editor.StatusFuncionarioEditor"; //$NON-NLS-1$
	private Text txtNome;
	private Text txtTempoDia;
	private Text txtTempoSemana;
	private Text txtTempoMes;
	private Text txtFiltro;
	private Table tableStatus;
	private TableViewer tvStatus;
	
	private PessoaService service = new PessoaService();
	private StatusServicoService statusService = new StatusServicoService();
	private StatusFuncionarioAnaliticoFilter filtro = new StatusFuncionarioAnaliticoFilter();
	private List<StatusServico> listaStatus;

	public StatusFuncionarioEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		
		Label lblNome = new Label(container, SWT.NONE);
		lblNome.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblNome.setText("Nome:");
		
		txtNome = new Text(container, SWT.BORDER);
		txtNome.setEnabled(false);
		txtNome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTempoTrabalhadoNo = new Label(container, SWT.NONE);
		lblTempoTrabalhadoNo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblTempoTrabalhadoNo.setText("Tempo trabalhado no dia:");
		
		txtTempoDia = new Text(container, SWT.BORDER);
		txtTempoDia.setEnabled(false);
		txtTempoDia.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTempoDia.setText(getTempoDia());
		
		Label lblTempoTrabalhadoNa = new Label(container, SWT.NONE);
		lblTempoTrabalhadoNa.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblTempoTrabalhadoNa.setText("Tempo trabalhado na semana:");
		
		txtTempoSemana = new Text(container, SWT.BORDER);
		txtTempoSemana.setEnabled(false);
		txtTempoSemana.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTempoSemana.setText(getTempoSemana());
		
		Label lblTempoTrabalhadoNo_1 = new Label(container, SWT.NONE);
		lblTempoTrabalhadoNo_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblTempoTrabalhadoNo_1.setText("Tempo trabalhado no m\u00EAs:");
		
		txtTempoMes = new Text(container, SWT.BORDER);
		txtTempoMes.setEnabled(false);
		txtTempoMes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtTempoMes.setText(getTempoMes());
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblBuscar = new Label(container, SWT.NONE);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(container, SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvStatus.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		txtFiltro.setMessage("Filtro...");
		
		tvStatus = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableStatus = tvStatus.getTable();
		tableStatus.setHeaderVisible(true);
		tableStatus.setLinesVisible(true);
		tableStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		tvStatus.addFilter(filtro);
		tvStatus.setSorter(new TableStatusServicoViewerSorter());
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((StatusServico)element).getStatus().getDescricao();
			}
			
			@Override
			public Color getForeground(Object element) {
				StatusServico ss = (StatusServico)element;
				
				if(ss.getStatus().isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				
				return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(160);
		tblclmnStatus.setText("Status");
		
		TableViewerColumn tvcData = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcData.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDateFormatData("dd/MM/yyyy HH:mm").format(((StatusServico)element).getData());
			}
		});
		TableColumn tblclmnData = tvcData.getColumn();
		tblclmnData.setWidth(161);
		tblclmnData.setText("Data");
		
		TableViewerColumn tvcNumeroServico = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcNumeroServico.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((StatusServico)element).getServicoPrestado().getId().toString();
			}
		});
		TableColumn tblclmnNServio = tvcNumeroServico.getColumn();
		tblclmnNServio.setWidth(186);
		tblclmnNServio.setText("N\u00BA Servi\u00E7o");
		initDataBindings();

	}

	@Override
	public void setFocus() {
		if(HibernateConnection.isSessionRefresh(service.getPessoa()) && service.getPessoa().getId() != null)
			service.setPessoa(service.find(service.getPessoa().getId()));
		
		initDataBindings();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {}

	@Override
	public void doSaveAs() {}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		StatusFuncionarioEditorInput sfei = (StatusFuncionarioEditorInput)input;
		
		if(sfei.getFuncionario().getId() == null)
			service.setPessoa(new Pessoa());
		else
			service.setPessoa(service.find(sfei.getFuncionario().getId()));
		
		setSite(site);
		setInput(input);
		
	}
	
	private String getTempoDia(){
		List<StatusServico> listaStatusDia = statusService.findAllByFuncionarioAndPeriodo(service.getPessoa(), new Date(), new Date());
		return FormatterHelper.formatarTempo(FormatterHelper.calculaPeriodoStatusServico(listaStatusDia));
	}
	
	private String getTempoSemana(){
		//pega data de uma semana atraz
		Calendar dtInicial = Calendar.getInstance();
		dtInicial.set(Calendar.DAY_OF_MONTH, dtInicial.get(Calendar.DAY_OF_MONTH) - dtInicial.get(Calendar.DAY_OF_WEEK) + 1);
		
		List<StatusServico> listaStatusDia = statusService.findAllByFuncionarioAndPeriodo(service.getPessoa(), dtInicial.getTime(), new Date());
		return FormatterHelper.formatarTempo(FormatterHelper.calculaPeriodoStatusServico(listaStatusDia));
	}
	
	private String getTempoMes(){
		//pega data de um mes atraz
		Calendar dtInicial = Calendar.getInstance();
		dtInicial.set(Calendar.DAY_OF_MONTH, 1);
		
		listaStatus = statusService.findAllByFuncionarioAndPeriodo(service.getPessoa(), dtInicial.getTime(), new Date());
		return FormatterHelper.formatarTempo(FormatterHelper.calculaPeriodoStatusServico(listaStatus));
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtNomeObserveTextObserveWidget = SWTObservables.observeText(txtNome, SWT.Modify);
		IObservableValue servicegetPessoaNomeObserveValue = PojoObservables.observeValue(service.getPessoa(), "nome");
		bindingContext.bindValue(txtNomeObserveTextObserveWidget, servicegetPessoaNomeObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), StatusServico.class, new String[]{"status.descricao", "data", "servicoPrestado.id"});
//		tvStatus.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvStatus.setContentProvider(listContentProvider);
		//
		WritableList writableList = new WritableList(listaStatus, StatusServico.class);
		tvStatus.setInput(writableList);
		//
		return bindingContext;
	}
}
