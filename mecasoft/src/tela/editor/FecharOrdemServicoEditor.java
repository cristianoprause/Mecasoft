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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.helper.PadraoHelper;
import aplicacao.service.ServicoPrestadoService;

public class FecharOrdemServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.FecharOrdemServicoEditor"; //$NON-NLS-1$
	private Label lblValorTotal;
	private Table table;
	
	private ServicoPrestadoService service = new ServicoPrestadoService();

	public FecharOrdemServicoEditor() {
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
		
		Label lblTotalServicos = new Label(compositeConteudo, SWT.NONE);
		lblTotalServicos.setText("Total servi\u00E7os:");
		
		MecasoftText txtTotalServico = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTotalServico.setEnabled(false);
		txtTotalServico.setOptions(MecasoftText.NUMEROS, -1);
		txtTotalServico.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtTotalServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblTotalItens = new Label(compositeConteudo, SWT.NONE);
		lblTotalItens.setText("Total itens:");
		
		MecasoftText txtTotalItem = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTotalItem.setEnabled(false);
		txtTotalItem.setOptions(MecasoftText.NUMEROS, -1);
		txtTotalItem.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtTotalItem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblLocomocao = new Label(compositeConteudo, SWT.NONE);
		lblLocomocao.setText("Locomo\u00E7\u00E3o:");
		
		MecasoftText txtLocomocao = new MecasoftText(compositeConteudo, SWT.NONE);
		txtLocomocao.setEnabled(false);
		txtLocomocao.setOptions(MecasoftText.NUMEROS, -1);
		txtLocomocao.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtLocomocao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblMaoDeObra = new Label(compositeConteudo, SWT.NONE);
		lblMaoDeObra.setText("M\u00E3o de obra:");
		
		MecasoftText txtMaoObra = new MecasoftText(compositeConteudo, SWT.NONE);
		txtMaoObra.setEnabled(false);
		txtMaoObra.setOptions(MecasoftText.NUMEROS, -1);
		txtMaoObra.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtMaoObra.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblDesconto = new Label(compositeConteudo, SWT.NONE);
		lblDesconto.setText("Desconto:");
		
		MecasoftText txtDesconto = new MecasoftText(compositeConteudo, SWT.NONE);
		txtDesconto.setOptions(MecasoftText.NUMEROS, -1);
		txtDesconto.addChars(PadraoHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtDesconto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblTotal = new Label(compositeConteudo, SWT.NONE);
		lblTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblTotal.setText("Total:");
		
		lblValorTotal = new Label(compositeConteudo, SWT.NONE);
		lblValorTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblFormasDePagamento = new Label(compositeConteudo, SWT.NONE);
		lblFormasDePagamento.setText("Formas de pagamento:");
		
		TableViewer tableViewer = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_table.heightHint = 95;
		table.setLayoutData(gd_table);
		
		TableViewerColumn tvcForma = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnForma = tvcForma.getColumn();
		tblclmnForma.setWidth(310);
		tblclmnForma.setText("Forma");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(100);
		tblclmnValor.setText("Valor");
		
		Button btnAdicionar = new Button(compositeConteudo, SWT.NONE);
		btnAdicionar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnAdicionar.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemover = new Button(compositeConteudo, SWT.NONE);
		btnRemover.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		btnRemover.setText("Remover");
		
		Label lblTroco = new Label(compositeConteudo, SWT.NONE);
		lblTroco.setText("Troco:");
		
		MecasoftText txtTroco = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTroco.setEnabled(false);
		txtTroco.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setShowExcluir(false);
		setShowSalvar(false);
		
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}

}
