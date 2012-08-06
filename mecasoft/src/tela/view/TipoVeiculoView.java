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

import banco.modelo.TipoVeiculo;

import tela.editor.editorInput.TipoVeiculoEditorInput;
import tela.filter.TipoVeiculoFilter;
import aplicacao.service.TipoVeiculoService;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

public class TipoVeiculoView extends ViewPart {

	public static final String ID = "tela.view.TipoVeiculoView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	private Action actionNovo;
	private TableViewer tvTipoVeiculo;
	//
	private TipoVeiculoService service = new TipoVeiculoService();
	private TipoVeiculoFilter filtro = new TipoVeiculoFilter();

	public TipoVeiculoView() {
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
		
		Form frmListaDeTipos = formToolkit.createForm(container);
		frmListaDeTipos.setImage(ResourceManager.getPluginImage("mecasoft", "assents/veiculo/tipoVeiculo32.png"));
		formToolkit.paintBordersFor(frmListaDeTipos);
		frmListaDeTipos.setText("Lista de tipos de ve\u00EDculos");
		frmListaDeTipos.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDeTipos.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDeTipos.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvTipoVeiculo.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		tvTipoVeiculo = new TableViewer(frmListaDeTipos.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvTipoVeiculo.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection selecao = (IStructuredSelection) tvTipoVeiculo.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					getSite().getPage().openEditor(new TipoVeiculoEditorInput((TipoVeiculo)selecao.getFirstElement()), "tela.editor.TipoVeiculoEditor");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		table = tvTipoVeiculo.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvTipoVeiculo.setContentProvider(ArrayContentProvider.getInstance());
		tvTipoVeiculo.addFilter(filtro);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvTipoVeiculo, SWT.NONE);
		tvcNome.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((TipoVeiculo)element).getNome();
			}
		});
		TableColumn tblclmnNome = tvcNome.getColumn();
		tblclmnNome.setWidth(344);
		tblclmnNome.setText("Nome");
		
		TableViewerColumn tvcMedida = new TableViewerColumn(tvTipoVeiculo, SWT.NONE);
		tvcMedida.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				TipoVeiculo tipo = (TipoVeiculo)element;
				
				if(tipo.getHodometro())
					return "Hodômetro";
				else
					return "Horímetro";
			}
		});
		TableColumn tblclmnMedida = tvcMedida.getColumn();
		tblclmnMedida.setWidth(235);
		tblclmnMedida.setText("Medida");
		frmListaDeTipos.getToolBarManager().add(actionAtualizar);
		frmListaDeTipos.getToolBarManager().add(actionNovo);
		frmListaDeTipos.updateToolBar();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvTipoVeiculo.setInput(service.findAll());
					tvTipoVeiculo.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new TipoVeiculoEditorInput(), "tela.editor.TipoVeiculoEditor");
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
