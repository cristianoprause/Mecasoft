package tela.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.action.Action;
import org.eclipse.wb.swt.ResourceManager;

import tela.editor.ServicoEditor;
import tela.editor.editorInput.ServicoEditorInput;
import tela.filter.ServicoFilter;

import aplicacao.helper.FormatterHelper;
import aplicacao.service.ProdutoServicoService;
import banco.modelo.ProdutoServico;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;

public class ServicoView extends ViewPart {

	public static final String ID = "tela.view.ServicoView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private Action actionAtualizar;
	private Action actionNovo;
	private TableViewer tvServico;
	
	private ProdutoServicoService service = new ProdutoServicoService();
	private ServicoFilter filtro = new ServicoFilter();

	public ServicoView() {
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
		
		Form frmListaDeServios = formToolkit.createForm(container);
		frmListaDeServios.setImage(ResourceManager.getPluginImage("mecasoft", "assents/servico/servico32.png"));
		formToolkit.paintBordersFor(frmListaDeServios);
		frmListaDeServios.setText("Lista de servi\u00E7os");
		frmListaDeServios.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDeServios.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDeServios.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filtro.setSearch(txtFiltro.getText());
				tvServico.refresh();
			}
		});
		txtFiltro.setMessage("Filtro...");
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(txtFiltro, true, true);
		
		tvServico = new TableViewer(frmListaDeServios.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		tvServico.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection selecao = (IStructuredSelection)tvServico.getSelection();
					
					if(selecao.isEmpty())
						return;
					
					ProdutoServico ps = (ProdutoServico)selecao.getFirstElement();
					getSite().getPage().openEditor(new ServicoEditorInput(ps), ServicoEditor.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		table = tvServico.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvServico.addFilter(filtro);
		tvServico.setContentProvider(ArrayContentProvider.getInstance());
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvServico, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ProdutoServico)element).getDescricao();
			}
		});
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(378);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tvcValorBase = new TableViewerColumn(tvServico, SWT.NONE);
		tvcValorBase.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ProdutoServico)element).getValorUnitario());
			}
		});
		TableColumn tblclmnValorBase = tvcValorBase.getColumn();
		tblclmnValorBase.setWidth(100);
		tblclmnValorBase.setText("Valor Base");
		
		TableViewerColumn tvcSituaçao = new TableViewerColumn(tvServico, SWT.NONE);
		tvcSituaçao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				ProdutoServico ps = (ProdutoServico)element;
				
				if(ps.getAtivo())
					return "Ativo";
				else
					return "Desativado";
			}
		});
		TableColumn tblclmnSituao = tvcSituaçao.getColumn();
		tblclmnSituao.setWidth(100);
		tblclmnSituao.setText("Situação");
		frmListaDeServios.getToolBarManager().add(actionAtualizar);
		frmListaDeServios.getToolBarManager().add(actionNovo);
		frmListaDeServios.updateToolBar();

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
					tvServico.setInput(service.findAllServicos());
					tvServico.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new ServicoEditorInput(), ServicoEditor.ID);
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
