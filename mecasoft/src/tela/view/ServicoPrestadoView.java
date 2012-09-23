package tela.view;

import static aplicacao.helper.MessageHelper.openError;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import tela.componentes.MecasoftText;
import tela.editor.AbrirOrdemServicoEditor;
import tela.editor.editorInput.AbrirOrdemServicoEditorInput;
import tela.filter.ServicoPrestadoFilter;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.ServicoPrestadoService;
import banco.modelo.ServicoPrestado;
import banco.modelo.StatusServico;

import com.ibm.icu.util.Calendar;

public class ServicoPrestadoView extends ViewPart {

	public static final String ID = "tela.view.ServicoPrestadoView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private TableViewer tvServicoPrestado;
	private Action actionAtualizar;
	private Action actionNovo;
	
	private ServicoPrestadoService service = new ServicoPrestadoService();
	private ServicoPrestadoFilter filtro = new ServicoPrestadoFilter();
	private MecasoftText txtDataInicial;
	private MecasoftText txtDataFinal;

	public ServicoPrestadoView() {
		createActions();
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmServiosPrestados = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frmServiosPrestados);
		frmServiosPrestados.setText("Servi\u00E7os Prestados");
		frmServiosPrestados.getBody().setLayout(new GridLayout(5, false));
		
		Label lblBuscar = new Label(frmServiosPrestados.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmServiosPrestados.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvServicoPrestado.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		Label lblPeriodoDe = formToolkit.createLabel(frmServiosPrestados.getBody(), "Per\u00EDodo de", SWT.NONE);
		lblPeriodoDe.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		txtDataInicial = new MecasoftText(frmServiosPrestados.getBody(), SWT.NONE);
		txtDataInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDataInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		formToolkit.adapt(txtDataInicial);
		formToolkit.paintBordersFor(txtDataInicial);
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		txtDataInicial.setText(FormatterHelper.DATEFORMATDATA.format(c.getTime()));
		
		Label lblAte = new Label(frmServiosPrestados.getBody(), SWT.NONE);
		formToolkit.adapt(lblAte, true, true);
		lblAte.setText("at\u00E9");
		
		txtDataFinal = new MecasoftText(frmServiosPrestados.getBody(), SWT.NONE);
		txtDataFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		formToolkit.adapt(txtDataFinal);
		formToolkit.paintBordersFor(txtDataFinal);

		txtDataFinal.setText(FormatterHelper.DATEFORMATDATA.format(new Date()));
		
		tvServicoPrestado = new TableViewer(frmServiosPrestados.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvServicoPrestado.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection selecao = (IStructuredSelection)tvServicoPrestado.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					ServicoPrestado sp = (ServicoPrestado)selecao.getFirstElement();

					getSite().getPage().openEditor(new AbrirOrdemServicoEditorInput(sp), AbrirOrdemServicoEditor.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		table = tvServicoPrestado.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		tvServicoPrestado.setContentProvider(ArrayContentProvider.getInstance());
		tvServicoPrestado.addFilter(filtro);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNumero = new TableViewerColumn(tvServicoPrestado, SWT.NONE);
		tvcNumero.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ServicoPrestado)element).getId().toString();
			}
		});
		TableColumn tblclmnNumero = tvcNumero.getColumn();
		tblclmnNumero.setWidth(100);
		tblclmnNumero.setText("N\u00FAmero");
		
		TableViewerColumn tvcCliente = new TableViewerColumn(tvServicoPrestado, SWT.NONE);
		tvcCliente.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ServicoPrestado)element).getCliente().getNomeFantasia();
			}
		});
		TableColumn tblclmnCliente = tvcCliente.getColumn();
		tblclmnCliente.setWidth(203);
		tblclmnCliente.setText("Cliente");
		
		TableViewerColumn tvcVeiculo = new TableViewerColumn(tvServicoPrestado, SWT.NONE);
		tvcVeiculo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ServicoPrestado)element).getVeiculo().getModelo();
			}
		});
		TableColumn tblclmnVeculo = tvcVeiculo.getColumn();
		tblclmnVeculo.setWidth(148);
		tblclmnVeculo.setText("Ve\u00EDculo");
		
		TableViewerColumn tvcMecanico = new TableViewerColumn(tvServicoPrestado, SWT.NONE);
		tvcMecanico.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				service.setServicoPrestado((ServicoPrestado)element);
				StatusServico status = service.getServicoPrestado().getUltimoStatus();
				
				if(status != null)
					return status.getFuncionario().getNomeFantasia();
				
				return "";
			}
		});
		TableColumn tblclmnMecnico = tvcMecanico.getColumn();
		tblclmnMecnico.setWidth(152);
		tblclmnMecnico.setText("Mec\u00E2nico");
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvServicoPrestado, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				service.setServicoPrestado((ServicoPrestado)element);
				StatusServico status = service.getServicoPrestado().getUltimoStatus();
				
				if(status != null)
					return status.getStatus().getDescricao();
				
				return "";
			}
		});
		TableColumn tblclmnStatusAtual = tvcStatus.getColumn();
		tblclmnStatusAtual.setWidth(137);
		tblclmnStatusAtual.setText("Status Atual");
		frmServiosPrestados.getToolBarManager().add(actionAtualizar);
		frmServiosPrestados.getToolBarManager().add(actionNovo);
		frmServiosPrestados.updateToolBar();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					Date dtInicial = null;
					Date dtFinal = null;
					
					try{
						dtInicial = FormatterHelper.DATEFORMATDATA.parse(txtDataInicial.getText());
						dtFinal = FormatterHelper.DATEFORMATDATA.parse(txtDataFinal.getText());
					}catch(Exception e){
						openError("Informe as datas corretamente.");
						return;
					}

					tvServicoPrestado.setInput(service.findAllAtivosByPeriodo(dtInicial, dtFinal));
					tvServicoPrestado.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Abrir Nova Ordem") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new AbrirOrdemServicoEditorInput(), AbrirOrdemServicoEditor.ID);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			};
			actionNovo.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/add16.png"));
		}
	}

	@Override
	public void setFocus() {
		actionAtualizar.run();
	}

}
