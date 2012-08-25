package tela.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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

import tela.editor.VeiculoEditor;
import tela.editor.editorInput.VeiculoEditorInput;
import tela.filter.VeiculoFilter;
import aplicacao.service.VeiculoService;
import banco.modelo.Veiculo;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

public class VeiculoView extends ViewPart {

	public static final String ID = "tela.view.VeiculoView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private TableViewer tvVeiculo;
	private Action actionAtualizar;
	private Action actionNovo;
	
	private VeiculoService service = new VeiculoService();
	private VeiculoFilter filtro = new VeiculoFilter();

	public VeiculoView() {
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
		
		Form frmListaDeVeculos = formToolkit.createForm(container);
		frmListaDeVeculos.setImage(ResourceManager.getPluginImage("mecasoft", "assents/veiculo/veiculo32.png"));
		formToolkit.paintBordersFor(frmListaDeVeculos);
		frmListaDeVeculos.setText("Lista de ve\u00EDculos");
		frmListaDeVeculos.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDeVeculos.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDeVeculos.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvVeiculo.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		tvVeiculo = new TableViewer(frmListaDeVeculos.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvVeiculo.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				try {
					IStructuredSelection selecao = (IStructuredSelection)event.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					Veiculo v = (Veiculo)selecao.getFirstElement();
					
					getSite().getPage().openEditor(new VeiculoEditorInput(v), VeiculoEditor.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
				
			}
		});
		table = tvVeiculo.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		formToolkit.paintBordersFor(table);
		tvVeiculo.setContentProvider(ArrayContentProvider.getInstance());
		tvVeiculo.addFilter(filtro);
		
		TableViewerColumn tvcModelo = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcModelo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getModelo();
			}
		});
		TableColumn tblclmnModelo = tvcModelo.getColumn();
		tblclmnModelo.setWidth(100);
		tblclmnModelo.setText("Modelo");
		
		TableViewerColumn tvcMarca = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcMarca.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getMarca();
			}
		});
		TableColumn tblclmnMarca = tvcMarca.getColumn();
		tblclmnMarca.setWidth(100);
		tblclmnMarca.setText("Marca");
		
		TableViewerColumn tvcTipo = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcTipo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getTipo().getNome();
			}
		});
		TableColumn tblclmnTipo = tvcTipo.getColumn();
		tblclmnTipo.setWidth(100);
		tblclmnTipo.setText("Tipo");
		
		TableViewerColumn tvcSituacao = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcSituacao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getStatus();
			}
		});
		TableColumn tblclmnSitucao = tvcSituacao.getColumn();
		tblclmnSitucao.setWidth(100);
		tblclmnSitucao.setText("Situa\u00E7\u00E3o");
		
		TableViewerColumn tvcProprietario = new TableViewerColumn(tvVeiculo, SWT.NONE);
		tvcProprietario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getCliente().getNomeFantasia();
			}
		});
		TableColumn tblclmnProprietario = tvcProprietario.getColumn();
		tblclmnProprietario.setWidth(100);
		tblclmnProprietario.setText("Propriet\u00E1rio");
		frmListaDeVeculos.getToolBarManager().add(actionAtualizar);
		frmListaDeVeculos.getToolBarManager().add(actionNovo);
		frmListaDeVeculos.updateToolBar();
		
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvVeiculo.setInput(service.findAll());
					tvVeiculo.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new VeiculoEditorInput(), VeiculoEditor.ID);
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
