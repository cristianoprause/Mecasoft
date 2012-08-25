package tela.editor;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.ResourceManager;

public class AbrirOrdemServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.AbrirOrdemServicoEditor"; //$NON-NLS-1$
	private Text txtCliente;
	private Text txtVeiculo;
	private Table tableServicos;
	private Table table;

	public AbrirOrdemServicoEditor() {
	}

	@Override
	public void salvarRegistro() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluirRegistro() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblCliente = new Label(compositeConteudo, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(compositeConteudo, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setEditable(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionarCliente = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setText("Selecionar");
		
		Label lblVeculo = new Label(compositeConteudo, SWT.NONE);
		lblVeculo.setText("Ve\u00EDculo:");
		
		txtVeiculo = new Text(compositeConteudo, SWT.BORDER);
		txtVeiculo.setEnabled(false);
		txtVeiculo.setEditable(false);
		txtVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionarVeiculo = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarVeiculo.setText("Selecionar");
		
		Label lblServicosPrestados = new Label(compositeConteudo, SWT.NONE);
		lblServicosPrestados.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblServicosPrestados.setText("Servi\u00E7os prestados:");
		
		TableViewer tvServico = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableServicos = tvServico.getTable();
		tableServicos.setLinesVisible(true);
		tableServicos.setHeaderVisible(true);
		GridData gd_tableServicos = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_tableServicos.heightHint = 95;
		tableServicos.setLayoutData(gd_tableServicos);
		
		TableViewerColumn tvcServico = new TableViewerColumn(tvServico, SWT.NONE);
		TableColumn tblclmnServico = tvcServico.getColumn();
		tblclmnServico.setWidth(100);
		tblclmnServico.setText("Servi\u00E7o");
		
		TableViewerColumn tvcValorBase = new TableViewerColumn(tvServico, SWT.NONE);
		TableColumn tblclmnValorBase = tvcValorBase.getColumn();
		tblclmnValorBase.setWidth(100);
		tblclmnValorBase.setText("Valor Base");
		
		TableViewerColumn tvcDesconto = new TableViewerColumn(tvServico, SWT.NONE);
		TableColumn tblclmnDesconto = tvcDesconto.getColumn();
		tblclmnDesconto.setWidth(100);
		tblclmnDesconto.setText("Desconto");
		
		TableViewerColumn tvcAcrescimo = new TableViewerColumn(tvServico, SWT.NONE);
		TableColumn tblclmnAcrescimo = tvcAcrescimo.getColumn();
		tblclmnAcrescimo.setWidth(100);
		tblclmnAcrescimo.setText("Acrescimo");
		
		Button btnAdicionarServio = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarServio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/plusServico16.png"));
		btnAdicionarServio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdicionarServio.setText("Adicionar Servi\u00E7o");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverServio = new Button(compositeConteudo, SWT.NONE);
		btnRemoverServio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/lessServico16.png"));
		btnRemoverServio.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoverServio.setText("Remover Servi\u00E7o");
		
		Label lblItensUtilizados = new Label(compositeConteudo, SWT.NONE);
		lblItensUtilizados.setText("Itens utilizados:");
		
		TableViewer tableViewer = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_table.heightHint = 95;
		table.setLayoutData(gd_table);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDescricao = tableViewerColumn.getColumn();
		tblclmnDescricao.setWidth(100);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnQuantidade = tableViewerColumn_1.getColumn();
		tblclmnQuantidade.setWidth(100);
		tblclmnQuantidade.setText("Quantidade");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnValorUnitario = tableViewerColumn_2.getColumn();
		tblclmnValorUnitario.setWidth(100);
		tblclmnValorUnitario.setText("Valor Unit\u00E1rio");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnDesconto_1 = tableViewerColumn_3.getColumn();
		tblclmnDesconto_1.setWidth(100);
		tblclmnDesconto_1.setText("Desconto");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnAcrescimoProduto = tableViewerColumn_4.getColumn();
		tblclmnAcrescimoProduto.setWidth(100);
		tblclmnAcrescimoProduto.setText("Acrescimo");
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTotal = tableViewerColumn_5.getColumn();
		tblclmnTotal.setResizable(false);
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		
		Button btnAdicionarItem = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarItem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionarItem.setText("Adicionar item");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemoverItem = new Button(compositeConteudo, SWT.NONE);
		btnRemoverItem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverItem.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemoverItem.setText("Remover item");
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}
}
