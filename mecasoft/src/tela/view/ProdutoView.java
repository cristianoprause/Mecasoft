package tela.view;

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

import tela.editor.editorInput.ProdutoEditorInput;
import tela.filter.ProdutoFilter;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.ProdutoServicoService;
import banco.modelo.ProdutoServico;

public class ProdutoView extends ViewPart {

	public static final String ID = "tela.view.ProdutoView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	private Action actionNovo;
	private TableViewer tvProduto;
	
	private ProdutoServicoService service = new ProdutoServicoService();
	private ProdutoFilter filter = new ProdutoFilter();

	public ProdutoView() {
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
		{
			Form frmListaDeProdutos = formToolkit.createForm(container);
			frmListaDeProdutos.setImage(ResourceManager.getPluginImage("mecasoft", "assents/produto/produto32.png"));
			formToolkit.paintBordersFor(frmListaDeProdutos);
			frmListaDeProdutos.setText("Lista de produtos");
			frmListaDeProdutos.getBody().setLayout(new GridLayout(2, false));
			{
				Label lblBuscar = new Label(frmListaDeProdutos.getBody(), SWT.NONE);
				lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				formToolkit.adapt(lblBuscar, true, true);
				lblBuscar.setText("Buscar:");
			}
			{
				txtFiltro = new Text(frmListaDeProdutos.getBody(), SWT.BORDER);
				txtFiltro.setMessage("Filtro...");
				txtFiltro.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						filter.setSearch(txtFiltro.getText());
						tvProduto.refresh();
					}
				});
				txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				formToolkit.adapt(txtFiltro, true, true);
			}
			{
				tvProduto = new TableViewer(frmListaDeProdutos.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
				tvProduto.addDoubleClickListener(new IDoubleClickListener() {
					public void doubleClick(DoubleClickEvent event) {
						try {
							IStructuredSelection selecao = (IStructuredSelection)event.getSelection();
							
							if(selecao.isEmpty())
								return;
							
							ProdutoServico ps = (ProdutoServico)selecao.getFirstElement();
							getSite().getPage().openEditor(new ProdutoEditorInput(ps), "tela.editor.ProdutoEditor");
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				});
				table = tvProduto.getTable();
				table.setLinesVisible(true);
				table.setHeaderVisible(true);
				table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
				tvProduto.addFilter(filter);
				tvProduto.setContentProvider(ArrayContentProvider.getInstance());
				formToolkit.paintBordersFor(table);
				{
					TableViewerColumn tvcDescricao = new TableViewerColumn(tvProduto, SWT.NONE);
					tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
						@Override
						public String getText(Object element) {
							return ((ProdutoServico)element).getDescricao();
						}
					});
					TableColumn tblclmnDescricao = tvcDescricao.getColumn();
					tblclmnDescricao.setWidth(250);
					tblclmnDescricao.setText("Descrição");
				}
				{
					TableViewerColumn tvcSituacao = new TableViewerColumn(tvProduto, SWT.NONE);
					tvcSituacao.setLabelProvider(new ColumnLabelProvider(){
						@Override
						public String getText(Object element) {
							ProdutoServico ps = (ProdutoServico)element;
							
							if(ps.getAtivo())
								return "Ativo";
							else
								return "Desativado";
						}
					});
					TableColumn tblclmnSituacao = tvcSituacao.getColumn();
					tblclmnSituacao.setWidth(100);
					tblclmnSituacao.setText("Situação");
				}
				{
					TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvProduto, SWT.NONE);
					tvcValorUnitario.setLabelProvider(new ColumnLabelProvider(){
						@Override
						public String getText(Object element) {
							return FormatterHelper.DECIMALFORMAT.format(((ProdutoServico)element).getValorUnitario());
						}
					});
					TableColumn tblclmnValorUnitario = tvcValorUnitario.getColumn();
					tblclmnValorUnitario.setWidth(100);
					tblclmnValorUnitario.setText("Valor Unitário");
				}
				{
					TableViewerColumn tvcQuantEstoque = new TableViewerColumn(tvProduto, SWT.NONE);
					tvcQuantEstoque.setLabelProvider(new ColumnLabelProvider(){
						@Override
						public String getText(Object element) {
							return ((ProdutoServico)element).getQuantidade().toString();
						}
					});
					TableColumn tblclmnQuantidadeEstoque = tvcQuantEstoque.getColumn();
					tblclmnQuantidadeEstoque.setWidth(146);
					tblclmnQuantidadeEstoque.setText("Quantidade Estoque");
				}
			}
			frmListaDeProdutos.getToolBarManager().add(actionAtualizar);
			frmListaDeProdutos.getToolBarManager().add(actionNovo);
			frmListaDeProdutos.updateToolBar();
		}

	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {
				@Override
				public void run() {
					tvProduto.setInput(service.findAllProdutos());
					tvProduto.refresh();
				}
				
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new ProdutoEditorInput(), "tela.editor.ProdutoEditor");
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
