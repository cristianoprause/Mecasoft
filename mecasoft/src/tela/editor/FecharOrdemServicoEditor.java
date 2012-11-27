package tela.editor;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.MessageHelper.openWarning;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.componentes.MecasoftText;
import tela.dialog.AdicionarFormaPagamentoDialog;
import tela.editor.editorInput.FecharOrdemServicoEditorInput;
import aplicacao.command.ReportCommand;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.helper.ReportHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.DuplicataService;
import aplicacao.service.MovimentacaoCaixaService;
import aplicacao.service.ServicoPrestadoService;
import banco.connection.HibernateConnection;
import banco.modelo.Duplicata;
import banco.modelo.FormaPagtoUtilizada;
import banco.modelo.ItemServico;
import banco.modelo.MovimentacaoCaixa;
import banco.modelo.ProdutoServico;
import banco.modelo.StatusServico;

public class FecharOrdemServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.FecharOrdemServicoEditor"; //$NON-NLS-1$
	private Label lblValorTotal;
	private Table table;
	private MecasoftText txtTotalServico;
	private MecasoftText txtTotalItem;
	private MecasoftText txtLocomocao;
	private MecasoftText txtMaoObra;
	private MecasoftText txtDesconto;
	private MecasoftText txtTroco;
	private TableViewer tvFormaPagamento;
	private MecasoftText txtJuros;
	private Button btnAdicionar;
	
	private ServicoPrestadoService service = new ServicoPrestadoService();
	private DuplicataService duplicataService = new DuplicataService();
	private MovimentacaoCaixaService movimentacaoService = new MovimentacaoCaixaService();
	private BigDecimal totalPago = BigDecimal.ZERO;
	private List<Duplicata> listaDuplicatas = new ArrayList<Duplicata>();
	private BigDecimal troco;

	public FecharOrdemServicoEditor() {
	}

	@Override
	public void salvarRegistro() throws ValidationException {
		validar(service.getServicoPrestado());
			
		if(service.getServicoPrestado().getListaFormaPagto() == null 
				|| service.getServicoPrestado().getListaFormaPagto().isEmpty())
			throw new ValidationException("Adicione ao menos uma forma de pagamento");
		
		if(totalPago.compareTo(service.getServicoPrestado().getValorTotal()) < 0)
			throw new ValidationException("A soma do valor das formas de pagamento não pode ser inferior ao total do serviço.");
			
		//para fechar uma ordem de serviço, o usuário deve ter adicionado nas configurações qual status corresponde ao de serviço concluido
		if(UsuarioHelper.getConfiguracaoPadrao() == null)
			throw new ValidationException("Não é posível fechar a ordem de serviço.\n" +
					"Va em Arquivo/Configurações e selecione o status para fechar as ordens de serviço.");
		
		//verifica se o caixa esta aberto
		if(UsuarioHelper.getCaixa() == null)
			throw new ValidationException("O caixa esta fechado.\n" +
					"Abra-o primeiro para depois fechar a ordem de serviço.");
		
		//salva as duplicatas, caso geradas
		if(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraDuplicata()){
			for(Duplicata duplicata : listaDuplicatas){
				duplicataService.setDuplicata(duplicata);
				duplicataService.saveOrUpdate();
			}
		}
		
		//fera movimentação no caixa para o pagamento dos produtos
		BigDecimal valorCaixa = movimentacaoService.getTotalCaixa(UsuarioHelper.getCaixa());
		for(ItemServico item : service.getServicoPrestado().getListaProdutos()){
			if(item.getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO)){
				//caso o caixa não possua dinheiro suficiente, gera um suprimento
				if(valorCaixa.compareTo(item.getTotal()) < 0){
					MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
					movimentacao.setMotivo("Suprimento para pagamento de peças");
					movimentacao.setServicoPrestado(service.getServicoPrestado());
					movimentacao.setStatus(MovimentacaoCaixa.STATUSSUPRIMENTO);
					movimentacao.setTipo(MovimentacaoCaixa.TIPOENTRADA);
					movimentacao.setValor(item.getTotal());
					
					movimentacaoService.setMovimentacao(movimentacao);
					movimentacaoService.saveOrUpdate();
				}
				
				//sangria para o pagamento da peça
				MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
				movimentacao.setMotivo("Sangria para pagamento de peças");
				movimentacao.setServicoPrestado(service.getServicoPrestado());
				movimentacao.setStatus(MovimentacaoCaixa.STATUSSANGRIA);
				movimentacao.setTipo(MovimentacaoCaixa.TIPOSAIDA);
				movimentacao.setValor(item.getTotal());
				
				movimentacaoService.setMovimentacao(movimentacao);
				movimentacaoService.saveOrUpdate();
			}
		}
		
		//gera a movimentação no caixa
		if(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraPagVista()){
		
			if(troco.compareTo(valorCaixa) > 0)
				openWarning("Atenção, o caixa não possui dinheiro suficiente para o troco.");
			
			MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
			movimentacao.setMotivo("Serviço prestado para o cliente " + service.getServicoPrestado().getCliente().getNome());
			movimentacao.setServicoPrestado(service.getServicoPrestado());
			movimentacao.setStatus(MovimentacaoCaixa.STATUSSERVICO);
			movimentacao.setTipo(MovimentacaoCaixa.TIPOENTRADA);
			movimentacao.setValor(service.getServicoPrestado().getValorTotal());
			
			movimentacaoService.setMovimentacao(movimentacao);
			movimentacaoService.saveOrUpdate();
		
		//caso possua duplicatas, a movimentação só sera gerada se a forma de pagamento possuir valor de entrada
		}else if(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraDuplicata()
			&& service.getServicoPrestado().getValorEntrada().compareTo(BigDecimal.ZERO) > 0){
			
			MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
			movimentacao.setMotivo("Valor de entrada do serviço prestado para o cliente " + service.getServicoPrestado().getCliente().getNome());
			movimentacao.setServicoPrestado(service.getServicoPrestado());
			movimentacao.setStatus(MovimentacaoCaixa.STATUSSERVICO);
			movimentacao.setTipo(MovimentacaoCaixa.TIPOENTRADA);
			movimentacao.setValor(service.getServicoPrestado().getValorEntrada());
			
			movimentacaoService.setMovimentacao(movimentacao);
			movimentacaoService.saveOrUpdate();
			
		}
			
		//serviço concluido
		service.getServicoPrestado().setEmExecucao(false);
		service.getServicoPrestado().setDataFechamento(new Date());
		
		//cria o status de concluido
		StatusServico statusConcluido = new StatusServico();
		statusConcluido.setFuncionario(service.getServicoPrestado().getUltimoStatus().getFuncionario());
		statusConcluido.setServicoPrestado(service.getServicoPrestado());
		statusConcluido.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinalizarServico());

		//adiciona o status de concluido na lista de status do serviço
		service.getServicoPrestado().getListaStatus().add(statusConcluido);
		
		//remove todos os produtos com quantidade 0 da ordem de serviço antes de fecha-la
		List<ItemServico> listaItens = new ArrayList<ItemServico>();
		listaItens.addAll(service.getServicoPrestado().getListaServicos());
		
		for(ItemServico item : listaItens){
			if(item.getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO) && item.getQuantidade().compareTo(0) == 0){
				service.getServicoPrestado().getListaProdutos().remove(item);
				service.getServicoPrestado().getListaServicos().remove(item);
			}
		}
			
		service.saveOrUpdate();
		openInformation("Ordem de serviço fechada com sucesso!");
		
		//caso gere duplicatas, verifica se o usuário deseja imprimir o comprovante das duplicatas
		if(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraDuplicata()){
			if(openQuestion("Deseja imprimir o comprovante das duplicatas?")){
				try {
					ReportCommand report = new ReportCommand() {
						
						@Override
						public Object execute(ExecutionEvent event) throws ExecutionException {
							JasperPrint jPrint = getReport(ReportHelper.DUPLICATA);
							
							if(!jPrint.getPages().isEmpty())
								getView().setReport(jPrint, "Comprovante de duplicatas");
							return null;
						}
						
						@Override
						public Map<String, Object> getParametros() {
							Map<String, Object> param = new HashMap<String, Object>();
							
							param.put("DATA_INICIAL", null);
							param.put("DATA_FINAL", null);
							param.put("CLIENTE_ID", null);
							param.put("FUNCIONARIO_ID", null);
							param.put("SERVICO_ID", service.getServicoPrestado().getId());
							param.put("PAGO", false);
							param.put("ASSINATURA", true);
							
							return param;
						}
					};
					
					HibernateConnection.commit();
					report.execute(new ExecutionEvent());
				} catch (ExecutionException e) {
					setErroMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblTotalServicos = new Label(compositeConteudo, SWT.NONE);
		lblTotalServicos.setText("Total servi\u00E7os:");
		
		txtTotalServico = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTotalServico.setEnabled(false);
		txtTotalServico.setOptions(MecasoftText.NUMEROS, -1);
		txtTotalServico.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtTotalServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblTotalItens = new Label(compositeConteudo, SWT.NONE);
		lblTotalItens.setText("Total itens:");
		
		txtTotalItem = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTotalItem.setEnabled(false);
		txtTotalItem.setOptions(MecasoftText.NUMEROS, -1);
		txtTotalItem.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtTotalItem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblLocomocao = new Label(compositeConteudo, SWT.NONE);
		lblLocomocao.setText("Locomo\u00E7\u00E3o:");
		
		txtLocomocao = new MecasoftText(compositeConteudo, SWT.NONE);
		txtLocomocao.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularTotais();
			}
		});
		txtLocomocao.setOptions(MecasoftText.NUMEROS, -1);
		txtLocomocao.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtLocomocao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblMaoDeObra = new Label(compositeConteudo, SWT.NONE);
		lblMaoDeObra.setText("M\u00E3o de obra:");
		
		txtMaoObra = new MecasoftText(compositeConteudo, SWT.NONE);
		txtMaoObra.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularTotais();
			}
		});
		txtMaoObra.setOptions(MecasoftText.NUMEROS, -1);
		txtMaoObra.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtMaoObra.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblJuros = new Label(compositeConteudo, SWT.NONE);
		lblJuros.setText("Juros:");
		
		txtJuros = new MecasoftText(compositeConteudo, SWT.NONE);
		txtJuros.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularTotais();
			}
		});
		txtJuros.setOptions(MecasoftText.NUMEROS, -1);
		txtJuros.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtJuros.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblDesconto = new Label(compositeConteudo, SWT.NONE);
		lblDesconto.setText("Desconto:");
		
		txtDesconto = new MecasoftText(compositeConteudo, SWT.NONE);
		txtDesconto.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularTotais();
			}
		});
		txtDesconto.setOptions(MecasoftText.NUMEROS, -1);
		txtDesconto.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtDesconto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblTotal = new Label(compositeConteudo, SWT.NONE);
		lblTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblTotal.setText("Total:");
		
		lblValorTotal = new Label(compositeConteudo, SWT.NONE);
		lblValorTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblValorTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblFormasDePagamento = new Label(compositeConteudo, SWT.NONE);
		lblFormasDePagamento.setText("Formas de pagamento:");
		
		tvFormaPagamento = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tvFormaPagamento.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selecao = (IStructuredSelection)tvFormaPagamento.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				FormaPagtoUtilizada fpu = (FormaPagtoUtilizada)selecao.getFirstElement();
				AdicionarFormaPagamentoDialog afpd = new AdicionarFormaPagamentoDialog(getActiveShell(), service.getServicoPrestado(), fpu, true, listaDuplicatas);
				
				if(afpd.open() == IDialogConstants.OK_ID){
					calcularTotais();
					tvFormaPagamento.refresh();
					
					if(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraDuplicata())
						listaDuplicatas = afpd.getListaDuplicatas();
				}
			}
		});
		table = tvFormaPagamento.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_table.heightHint = 95;
		table.setLayoutData(gd_table);
		
		TableViewerColumn tvcForma = new TableViewerColumn(tvFormaPagamento, SWT.NONE);
		tvcForma.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((FormaPagtoUtilizada)element).getFormaPagamento().getNome();
			}
		});
		TableColumn tblclmnForma = tvcForma.getColumn();
		tblclmnForma.setWidth(310);
		tblclmnForma.setText("Forma");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tvFormaPagamento, SWT.NONE);
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((FormaPagtoUtilizada)element).getValor());
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(100);
		tblclmnValor.setText("Valor");
		
		btnAdicionar = new Button(compositeConteudo, SWT.NONE);
		btnAdicionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AdicionarFormaPagamentoDialog afpd = new AdicionarFormaPagamentoDialog(LayoutHelper.getActiveShell(), service.getServicoPrestado(), new FormaPagtoUtilizada(), false, null);
				if(afpd.open() == IDialogConstants.OK_ID){
					calcularTotais();
					tvFormaPagamento.refresh();
					
					//verifica se foram adicionadas duplicatas
					btnAdicionar.setEnabled(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraPagVista());
					
					if(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraDuplicata())
						listaDuplicatas = afpd.getListaDuplicatas();
					
				}
			}
		});
		btnAdicionar.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnRemover = new Button(compositeConteudo, SWT.NONE);
		btnRemover.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection) tvFormaPagamento.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				FormaPagtoUtilizada formaUtilizada = (FormaPagtoUtilizada)selecao.getFirstElement();
				
				if(openQuestion("Deseja realmente remover esta forma de pagamento da lista?")){
					service.getServicoPrestado().getListaFormaPagto().remove(formaUtilizada);
					calcularTotais();
					tvFormaPagamento.refresh();
					
					//reativa o botao de adicionar formas de pagamento
					//e apaga a lista de duplicatas
					if(service.getServicoPrestado().getListaFormaPagto().isEmpty()){
						btnAdicionar.setEnabled(true);
						listaDuplicatas.clear();
						return;
					}
					
					btnAdicionar.setEnabled(service.getServicoPrestado().getListaFormaPagto().get(0).getFormaPagamento().isGeraPagVista());
				}
			}
		});
		btnRemover.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnRemover.setText("Remover");
		
		Label lblTroco = new Label(compositeConteudo, SWT.NONE);
		lblTroco.setText("Troco:");
		
		txtTroco = new MecasoftText(compositeConteudo, SWT.NONE);
		txtTroco.setEnabled(false);
		txtTroco.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnFecharOrdem = createNewButton();
		btnFecharOrdem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					salvarRegistro();
					closeThisEditor();
				} catch (ValidationException e1) {
					setErroMessage(e1.getMessage());
				}
			}
		});
		btnFecharOrdem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnFecharOrdem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/servicoPrestado/closeService32.png"));
		btnFecharOrdem.setText("Fechar Ordem");
		
		initDataBindings();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setShowExcluir(false);
		setShowSalvar(false);
		
		FecharOrdemServicoEditorInput fosei = (FecharOrdemServicoEditorInput)input;
		
		if(fosei.getServicoPrestado().getId() != null)
			service.setServicoPrestado(service.find(fosei.getServicoPrestado().getId()));
		else
			service.setServicoPrestado(fosei.getServicoPrestado());
		
		setSite(site);
		setInput(input);
	}
	
	private void calcularTotais(){
		BigDecimal totalServico = service.getServicoPrestado().getTotalServico();
		BigDecimal totalItens = service.getServicoPrestado().getTotalItens();
		BigDecimal maoObra = service.getServicoPrestado().getTotalMaoObra();
		BigDecimal locomocao = service.getServicoPrestado().getTotalLocomocao();
		BigDecimal juros = service.getServicoPrestado().getJuros();
		
		service.getServicoPrestado().setValorTotal(totalServico.add(totalItens).add(maoObra).add(locomocao).add(juros));
		
		BigDecimal desconto = service.getServicoPrestado().getDesconto() == null ? BigDecimal.ZERO : service.getServicoPrestado().getDesconto();
		
		service.getServicoPrestado().setValorTotal(service.getServicoPrestado().getValorTotal().subtract(desconto));
		
		troco = BigDecimal.ZERO;
		totalPago = BigDecimal.ZERO;
		for(FormaPagtoUtilizada pagto : service.getServicoPrestado().getListaFormaPagto()){
			totalPago = totalPago.add(pagto.getValor());
		}
		troco = totalPago.subtract(service.getServicoPrestado().getValorTotal());
		
		service.getServicoPrestado().setTroco(troco.compareTo(BigDecimal.ZERO) > 0 ? troco : BigDecimal.ZERO);
		
		initDataBindings();
		
	}
	
	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtTotalServicotextObserveTextObserveWidget = SWTObservables.observeText(txtTotalServico.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalServicoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalServico");
		bindingContext.bindValue(txtTotalServicotextObserveTextObserveWidget, servicegetServicoPrestadoTotalServicoObserveValue, null, null);
		//
		IObservableValue txtTotalItemtextObserveTextObserveWidget = SWTObservables.observeText(txtTotalItem.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalItensObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalItens");
		bindingContext.bindValue(txtTotalItemtextObserveTextObserveWidget, servicegetServicoPrestadoTotalItensObserveValue, null, null);
		//
		IObservableValue txtLocomocaotextObserveTextObserveWidget = SWTObservables.observeText(txtLocomocao.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalLocomocaoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalLocomocao");
		bindingContext.bindValue(txtLocomocaotextObserveTextObserveWidget, servicegetServicoPrestadoTotalLocomocaoObserveValue, null, null);
		//
		IObservableValue txtMaoObratextObserveTextObserveWidget = SWTObservables.observeText(txtMaoObra.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTotalMaoObraObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "totalMaoObra");
		bindingContext.bindValue(txtMaoObratextObserveTextObserveWidget, servicegetServicoPrestadoTotalMaoObraObserveValue, null, null);
		//
		IObservableValue txtDescontotextObserveTextObserveWidget = SWTObservables.observeText(txtDesconto.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoDescontoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "desconto");
		bindingContext.bindValue(txtDescontotextObserveTextObserveWidget, servicegetServicoPrestadoDescontoObserveValue, null, null);
		//
		IObservableValue lblValorTotalObserveTextObserveWidget = SWTObservables.observeText(lblValorTotal);
		IObservableValue servicegetServicoPrestadoValorTotalObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "valorTotal");
		bindingContext.bindValue(lblValorTotalObserveTextObserveWidget, servicegetServicoPrestadoValorTotalObserveValue, null, null);
		//
		IObservableValue txtTrocotextObserveTextObserveWidget = SWTObservables.observeText(txtTroco.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoTrocoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "troco");
		bindingContext.bindValue(txtTrocotextObserveTextObserveWidget, servicegetServicoPrestadoTrocoObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), FormaPagtoUtilizada.class, new String[]{"formaPagamento.nome", "valor"});
//		tvFormaPagamento.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvFormaPagamento.setContentProvider(listContentProvider);
		//
		IObservableList servicegetServicoPrestadoListaFormaPagtoObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaFormaPagto");
		tvFormaPagamento.setInput(servicegetServicoPrestadoListaFormaPagtoObserveList);
		//
		IObservableValue txtJurostextObserveTextObserveWidget = SWTObservables.observeText(txtJuros.text, SWT.Modify);
		IObservableValue servicegetServicoPrestadoJurosObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "juros");
		bindingContext.bindValue(txtJurostextObserveTextObserveWidget, servicegetServicoPrestadoJurosObserveValue, null, null);
		//
		return bindingContext;
	}

	@Override
	public void setFocus() {
		if(HibernateConnection.isSessionRefresh(service.getServicoPrestado()) && service.getServicoPrestado().getId() != null)
			service.setServicoPrestado(service.find(service.getServicoPrestado().getId()));
	}
}
