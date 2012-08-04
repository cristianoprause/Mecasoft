package tela.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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

import tela.editor.editorInput.PessoaEditorInput;
import tela.filter.PessoaFilter;
import aplicacao.service.PessoaService;
import banco.modelo.Pessoa;

public class PessoaView extends ViewPart {

	public static final String ID = "tela.view.PessoaView"; //$NON-NLS-1$
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFiltro;
	private Table table;
	private TableViewer tvPessoa;
	private Action actionAtualizar;
	private Action actionNovo;

	private PessoaFilter filter = new PessoaFilter();
	private PessoaService service = new PessoaService();
	
	public PessoaView() {
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
		
		Form frmListaDePessoas = formToolkit.createForm(container);
		frmListaDePessoas.setImage(ResourceManager.getPluginImage("mecasoft", "assents/pessoa/pessoa32.png"));
		formToolkit.paintBordersFor(frmListaDePessoas);
		frmListaDePessoas.setText("Lista de pessoas");
		frmListaDePessoas.getBody().setLayout(new GridLayout(2, false));
		
		Label lblBuscar = new Label(frmListaDePessoas.getBody(), SWT.NONE);
		lblBuscar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblBuscar, true, true);
		lblBuscar.setText("Buscar:");
		
		txtFiltro = new Text(frmListaDePessoas.getBody(), SWT.BORDER);
		txtFiltro.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filter.setSearch(txtFiltro.getText());
				tvPessoa.refresh();
			}
		});
		txtFiltro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtFiltro.setMessage("Filtro...");
		formToolkit.adapt(txtFiltro, true, true);
		
		tvPessoa = new TableViewer(frmListaDePessoas.getBody(), SWT.BORDER | SWT.FULL_SELECTION);
		table = tvPessoa.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tvPessoa.setContentProvider(ArrayContentProvider.getInstance());
		tvPessoa.addFilter(filter);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tvcNomeFantasia = new TableViewerColumn(tvPessoa, SWT.NONE);
		tvcNomeFantasia.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		TableColumn tblclmnNomefantasia = tvcNomeFantasia.getColumn();
		tblclmnNomefantasia.setWidth(175);
		tblclmnNomefantasia.setText("Nome(fantasia)");
		
		TableViewerColumn tvcTipo = new TableViewerColumn(tvPessoa, SWT.NONE);
		tvcTipo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Pessoa p = (Pessoa) element;
				String tipo  = "";
				if(p.getTipoCliente())
					tipo = "Cliente";
				if(p.getTipoFornecedor()){
					if(!tipo.isEmpty())
						tipo += ", ";
					tipo += "Fornecedor";
				}
				if(p.getTipoFuncionario()){
					if(!tipo.isEmpty())
						tipo += ", ";
					tipo += "Funcionário";
				}
				
				return tipo;
			}
		});
		TableColumn tblclmnTipo = tvcTipo.getColumn();
		tblclmnTipo.setWidth(166);
		tblclmnTipo.setText("Tipo");
		
		TableViewerColumn tvcSituacao = new TableViewerColumn(tvPessoa, SWT.NONE);
		tvcSituacao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Pessoa p = (Pessoa)element;
				if(p.getAtivo())
					return "Ativo";
				
				return "Desativado";
			}
		});
		TableColumn tblclmnSituao = tvcSituacao.getColumn();
		tblclmnSituao.setWidth(100);
		tblclmnSituao.setText("Situa\u00E7\u00E3o");
		
		TableViewerColumn tvcFoneFax = new TableViewerColumn(tvPessoa, SWT.NONE);
		tvcFoneFax.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getFoneFax();
			}
		});
		TableColumn tblclmnFonefax = tvcFoneFax.getColumn();
		tblclmnFonefax.setWidth(100);
		tblclmnFonefax.setText("Fone/Fax");
		
		TableViewerColumn tvcCelular = new TableViewerColumn(tvPessoa, SWT.NONE);
		tvcCelular.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getCelular();
			}
		});
		TableColumn tblclmnCelular = tvcCelular.getColumn();
		tblclmnCelular.setWidth(100);
		tblclmnCelular.setText("Celular");
		frmListaDePessoas.getToolBarManager().add(actionAtualizar);
		frmListaDePessoas.getToolBarManager().add(actionNovo);
		frmListaDePessoas.updateToolBar();

	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			actionAtualizar = new Action("Atualizar") {				@Override
				public void run() {
					tvPessoa.setInput(service.findAll());
					tvPessoa.refresh();
				}
			};
			actionAtualizar.setImageDescriptor(ResourceManager.getPluginImageDescriptor("mecasoft", "assents/funcoes/refresh16.png"));
		}
		{
			actionNovo = new Action("Novo") {				@Override
				public void run() {
					try {
						getSite().getPage().openEditor(new PessoaEditorInput(), "tela.editor.PessoaEditor");
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
