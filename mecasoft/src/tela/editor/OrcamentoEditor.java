package tela.editor;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;
import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.MessageHelper.openWarning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.contentProvider.ItemServicoContentProvider;
import tela.dialog.SelecionarItemDialog;
import tela.editingSupport.FornecedorItemServicoEditingSupport;
import tela.editingSupport.ItemVisivelItemServicoEditingSupport;
import tela.editingSupport.QuantidadeItemServicoEditingSupport;
import tela.editingSupport.ValorUnitarioItemServico;
import tela.editor.editorInput.AbrirOrdemServicoEditorInput;
import tela.editor.editorInput.OrcamentoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.helper.ValidatorHelper;
import aplicacao.service.ItemServicoService;
import aplicacao.service.MecasoftService;
import aplicacao.service.OrcamentoService;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import aplicacao.service.ServicoPrestadoService;
import banco.modelo.ForneceProduto;
import banco.modelo.ItemServico;
import banco.modelo.Orcamento;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;
import banco.modelo.ServicoPrestado;
import banco.modelo.Veiculo;

public class OrcamentoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.OrcamentoEditor"; //$NON-NLS-1$
	private Logger log = Logger.getLogger(getClass());
	private OrcamentoService service = new OrcamentoService();
	private PessoaService pessoaService = new PessoaService();
	private ItemServicoService itemService = new ItemServicoService();
	private ServicoPrestadoService servicoService = new ServicoPrestadoService();
	private ProdutoServicoService prodServService = new ProdutoServicoService();
	private List<ItemServico> listaProdutoRemovido;
	
	private Text txtNumero;
	private Text txtCliente;
	private Text txtVeiculo;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private TreeViewer tvItem;
	private Label lblValorTotal;
	private Button btnSelecionarCliente;
	private Button btnSelecionar;
	private Button btnAdicionarServico;
	private Button btnRemoverServico;
	private Button btnAdicionarProduto;
	private Button btnRemoverProduto;
	private Tree tree;

	public OrcamentoEditor() {
		listaProdutoRemovido = new ArrayList<ItemServico>();
	}

	@Override
	public void salvarRegistro() throws ValidationException {
		ValidatorHelper.validar(service.getModelo());
		
		//GAMBIARRA
		for(ItemServico item : listaProdutoRemovido){
			itemService.setModelo(item);
			itemService.delete();
		}
		
		calcularTotais();
		
		service.saveOrUpdate();
		openInformation("Orçamento registrado com sucesso!");
	}

	@Override
	public void excluirRegistro() {
		if(openQuestion("Deseja realmente excluir o orçamento?")){
			service.delete();
			openInformation("Orçamento excluido com sucesso!");
			closeThisEditor();
		}
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(3, false));
		
		Label lblNmero = new Label(compositeConteudo, SWT.NONE);
		lblNmero.setText("Número:");
		
		txtNumero = new Text(compositeConteudo, SWT.BORDER);
		txtNumero.setEnabled(false);
		txtNumero.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblCliente = new Label(compositeConteudo, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(compositeConteudo, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnSelecionarCliente = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarCliente.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa p = selecionarCliente();
				if(p != null){
					service.getModelo().setCliente(p);
					txtCliente.setText(p.getNomeFantasia());
					
					//remove o veículo para evitar que o veículo nao pertença ao dono
					service.getModelo().setVeiculo(null);
					txtVeiculo.setText("");
				}else{
					service.getModelo().setCliente(null);
					txtCliente.setText("");
					
					//remove o veículo
					service.getModelo().setVeiculo(null);
					txtVeiculo.setText("");
				}
			}
		});
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setText("Selecionar");
		
		Label lblVeculo = new Label(compositeConteudo, SWT.NONE);
		lblVeculo.setText("Ve\u00EDculo:");
		
		txtVeiculo = new Text(compositeConteudo, SWT.BORDER);
		txtVeiculo.setEnabled(false);
		txtVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnSelecionar = new Button(compositeConteudo, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Veiculo v = selecionarVeiculo();
				if(v != null){
					txtVeiculo.setText(v.getModelo());
					service.getModelo().setVeiculo(v);
				}else{
					txtVeiculo.setText("");
					service.getModelo().setVeiculo(null);
				}
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");
		
		Label lblItensDoOramento = new Label(compositeConteudo, SWT.NONE);
		lblItensDoOramento.setText("Itens do or\u00E7amento:");
		
		tvItem = new TreeViewer(compositeConteudo, SWT.BORDER);
		tree = tvItem.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 4));
		tvItem.setContentProvider(new ItemServicoContentProvider());
		
		TreeViewerColumn tvcDescricao = new TreeViewerColumn(tvItem, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getDescricao();
			}
		});
		TreeColumn trclmnDescricao = tvcDescricao.getColumn();
		trclmnDescricao.setWidth(140);
		trclmnDescricao.setText("Descrição");
		
		TreeViewerColumn tvcPrestadorFornecedor = new TreeViewerColumn(tvItem, SWT.NONE);
		tvcPrestadorFornecedor.setEditingSupport(new FornecedorItemServicoEditingSupport(tvItem){
			@Override
			protected void setValue(Object element, Object value) {
				super.setValue(element, value);
				calcularTotais();
			}
		});
		tvcPrestadorFornecedor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Pessoa fornecedor = ((ItemServico)element).getFornecedor();
				if(fornecedor != null)
					return fornecedor.getNome();
				
				return "";
			}
		});
		TreeColumn trclmnPrestadorfornecedor = tvcPrestadorFornecedor.getColumn();
		trclmnPrestadorfornecedor.setWidth(141);
		trclmnPrestadorfornecedor.setText("Prestador/Fornecedor");
		
		TreeViewerColumn tvcValor = new TreeViewerColumn(tvItem, SWT.NONE);
		tvcValor.setEditingSupport(new ValorUnitarioItemServico(tvItem){
			@Override
			protected void setValue(Object element, Object value) {
				super.setValue(element, value);
				calcularTotais();
			}
		});
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.formatMoedaDuasCasas(((ItemServico)element).getValorUnitario());
			}
		});
		TreeColumn trclmnValor = tvcValor.getColumn();
		trclmnValor.setWidth(100);
		trclmnValor.setText("Valor");
		
		TreeViewerColumn tvcQuantidade = new TreeViewerColumn(tvItem, SWT.NONE);
		tvcQuantidade.setEditingSupport(new QuantidadeItemServicoEditingSupport(tvItem){
			@Override
			protected void setValue(Object element, Object value) {
				super.setValue(element, value);
				calcularTotais();
			}
		});
		tvcQuantidade.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getQuantidade().toString();
			}
		});
		TreeColumn trclmnQuantidade = tvcQuantidade.getColumn();
		trclmnQuantidade.setWidth(100);
		trclmnQuantidade.setText("Quantidade");
		
		TreeViewerColumn tvcTotal = new TreeViewerColumn(tvItem, SWT.NONE);
		tvcTotal.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.formatMoedaDuasCasas(((ItemServico)element).getTotal());
			}
		});
		TreeColumn trclmnTotal = tvcTotal.getColumn();
		trclmnTotal.setWidth(100);
		trclmnTotal.setText("Total");
		
		TreeViewerColumn tvcVisivel = new TreeViewerColumn(tvItem, SWT.NONE);
		tvcVisivel.setEditingSupport(new ItemVisivelItemServicoEditingSupport(tvItem));
		tvcVisivel.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				ItemServico is = (ItemServico)element;
				
				if(is.getItem().getTipo().equals(ProdutoServico.TIPOSERVICO))
					return null;
				if (is.isFornecedorVisivel())
					return ResourceManager.getPluginImage("mecasoft", "assents/funcoes/checked16.png");
				else
					return ResourceManager.getPluginImage("mecasoft", "assents/funcoes/unChecked16.png");
			}
		});
		TreeColumn trclmnVisivel = tvcVisivel.getColumn();
		trclmnVisivel.setWidth(100);
		trclmnVisivel.setText("Visível");
		
		btnAdicionarServico = formToolkit.createButton(compositeConteudo, "Adicionar", SWT.NONE);
		btnAdicionarServico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProdutoServico ps = selecionarServico();
				if(ps != null){
					Pessoa prestador = UsuarioHelper.getConfiguracaoPadrao().getRepresentanteEmpresa();
					
					//verifica se o servico possui algum prestador, caso nao possua,
					//a empresa é a prestadora, caso possua, o usuario seleciona a empresa
					if(ps.getListaFornecedores().size() > 0)
						prestador = selecionarFornecedor(ps);
					
					//verifica se o serviço ja nao foi adicionado com mesmo prestador
					for(ItemServico is : service.getModelo().getListaServico())
						if((is.getItem().equals(ps) && is.getFornecedor().equals(prestador)) || !is.getItem().getAtivo())
							return;
						
					ItemServico is = new ItemServico();
					is.setDescricao(ps.getDescricao());
					is.setTotal(ps.getValorUnitario());
					is.setQuantidade(1);
					is.setItem(ps);
					is.setFornecedor(prestador);
					is.setOrcamento(service.getModelo());
					is.setValorUnitario(ps.getValorUnitario());
					service.getModelo().getListaServico().add(is);
					
					for(ProdutoServico item : ps.getListaProduto())
						if(item.getAtivo())
							adicionarItens(item, is, null);
					
					calcularTotais();
					tvItem.refresh();
				}
			}
		});
		btnAdicionarServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdicionarServico.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/plusServico16.png"));
		new Label(compositeConteudo, SWT.NONE);
		
		btnRemoverServico = formToolkit.createButton(compositeConteudo, "Remover", SWT.NONE);
		btnRemoverServico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvItem.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				ItemServico is = (ItemServico)selecao.getFirstElement();
				if(is.getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO)){
					openWarning("O item selecionado é um produto e não um serviço.");
					return;
				}
				
				if(openQuestion("Deseja realmente remover este serviço da lista?")){
					//GAMBIARRA
					listaProdutoRemovido.addAll(is.getListaItem());
					
					service.getModelo().getListaServico().remove(is);
					tvItem.refresh();
					calcularTotais();
				}
			}
		});
		btnRemoverServico.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemoverServico.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/lessServico16.png"));
		new Label(compositeConteudo, SWT.NONE);
		
		btnAdicionarProduto = formToolkit.createButton(compositeConteudo, "Adicionar", SWT.NONE);
		btnAdicionarProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//pega o serviço selecionado pelo usuário
				IStructuredSelection selecao = (IStructuredSelection) tvItem.getSelection();
				
				if(selecao.isEmpty()){
					openError("Para adicionar um produto, selecione antes o serviço ao qual ele pertence");
					return;
				}
				
				ItemServico servico = (ItemServico)selecao.getFirstElement();
				if(servico.getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO)){
					openError("Para adicionar um produto, deve ser selecionado um serviço e não outro produto.");
					return;
				}
				
				ProdutoServico ps = selecionarProduto();
				if(ps != null){
					Pessoa fornecedor = selecionarFornecedor(ps);
					adicionarItens(ps, servico, fornecedor);
					calcularTotais();
				}
			}
		});
		btnAdicionarProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionarProduto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(compositeConteudo, SWT.NONE);
		
		btnRemoverProduto = formToolkit.createButton(compositeConteudo, "Remover", SWT.NONE);
		btnRemoverProduto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvItem.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				ItemServico is = (ItemServico)selecao.getFirstElement();
				if(is.getItem().getTipo().equals(ProdutoServico.TIPOSERVICO)){
					openWarning("O item selecionado é um serviço e não um produto.");
					return;
				}
				
				if(openQuestion("Deseja realmente remover este item da lista?")){
					//GAMBIARRA
					listaProdutoRemovido.add(is);
					is.getServico().getListaItem().remove(is);
					
					calcularTotais();
					tvItem.refresh();
				}
			}
		});
		btnRemoverProduto.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverProduto.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		
		Label lblTotal = new Label(compositeConteudo, SWT.NONE);
		lblTotal.setText("Total:");
		lblTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		formToolkit.adapt(lblTotal, true, true);
		
		lblValorTotal = new Label(compositeConteudo, SWT.NONE);
		lblValorTotal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		lblValorTotal.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblValorTotal.setText(FormatterHelper.formatMoedaDuasCasas(service.getModelo().getValorTotal()));
		formToolkit.adapt(lblValorTotal, true, true);
		new Label(compositeConteudo, SWT.NONE);
		
		Button btnAprovarOrcamento = createNewButton("Aprovar Orçamento");
		btnAprovarOrcamento.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(openQuestion("Deseja realmente aprovar este orçamento?\nCaso aprovado, não poderá ser usado em outro serviço")){
					try {
						ValidatorHelper.validar(service.getModelo());
						calcularTotais();
						
						closeThisEditor();
						getSite().getPage().openEditor(new AbrirOrdemServicoEditorInput(service.getModelo()), AbrirOrdemServicoEditor.ID);
					} catch (PartInitException e2) {
						log.error(e2);
					} catch (ValidationException e1) {
						setErroMessage(e1.getMessage());
						log.error(e1);
					}
				}
			}
		});
		btnAprovarOrcamento.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/confirm32.png"));
		if(!service.getModelo().isPendente())
			btnAprovarOrcamento.dispose();
		
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		OrcamentoEditorInput oei = (OrcamentoEditorInput)input;
		
		if(oei.getOrcamento().getId() != null)
			service.setModelo(oei.getOrcamento());
		else
			service.setModelo(new Orcamento());
		
		setShowExcluir(service.getModelo().getId() != null && service.getModelo().isPendente());
		setShowSalvar(service.getModelo().isPendente());
		
		setSite(site);
		setInput(input);
		
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void setFocus() {}
	
	private Pessoa selecionarCliente(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		
		sid.setElements(pessoaService.findAllClientesAtivos().toArray());
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	private Veiculo selecionarVeiculo(){
		if(service.getModelo().getCliente() == null){
			openWarning("Selecione primeiro o cliente para selecionar um veículo.");
			return null;
		}
		
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Veiculo)element).getModelo();
			}
		});
		sid.setElements(possiveisVeiculos().toArray());
		
		return (Veiculo) sid.getElementoSelecionado();
	}
	
	private List<Veiculo> possiveisVeiculos(){
		List<Veiculo> listaVeiculo = service.getModelo().getCliente().getListaVeiculo();
		List<Veiculo> listaResult = new ArrayList<Veiculo>();
		List<ServicoPrestado> listaServico = servicoService.findAllNaoConcluidos();
		
		for(Veiculo v : listaVeiculo){
			boolean inConsert = false;
			for(ServicoPrestado s : listaServico){
				if(s.getVeiculo().equals(v))
					inConsert = true;
			}
			
			if(!inConsert)
				listaResult.add(v);
		}
		
		return listaResult;
	}
	
	private ProdutoServico selecionarServico(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return((ProdutoServico)element).getDescricao();
			}
		});
		
		sid.setElements(prodServService.findAllServicosAtivos().toArray());
		
		return (ProdutoServico)sid.getElementoSelecionado();
	}
	
	private Pessoa selecionarFornecedor(ProdutoServico produto){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ForneceProduto)element).getPessoa().getNome() + " - R$" +
						FormatterHelper.getDecimalFormat().format(((ForneceProduto)element).getValorUnitario());
			}
		});
		
		sid.setElements(produto.getListaFornecedores().toArray());
		
		ForneceProduto fp =  (ForneceProduto)sid.getElementoSelecionado();
		return fp == null ? null : fp.getPessoa();
	}
	
	public void adicionarItens(ProdutoServico ps, ItemServico servicoPertence, Pessoa fornecedor){
		//verifica se é o mesmo produto, fornecedor e servico
		for(ItemServico item : service.getModelo().getListaServico()){
			if(item.getItem().equals(ps) &&
			   item.getFornecedor() != null && item.getFornecedor().equals(fornecedor) &&
			   item.getServico().equals(servicoPertence)){
				item.setQuantidade(item.getQuantidade() + 1);
				item.setTotal(calculaTotal(item));
				tvItem.refresh();
				return;
			}
		}

		//pega o forneceproduto selecionado
		ForneceProduto fp = null;
		if(fornecedor != null){
			for(int c = 0; c < fornecedor.getListaProduto().size() && fp == null; c++){
				ForneceProduto fpFornecedor = fornecedor.getListaProduto().get(c);
				if(fpFornecedor.getProduto().getId().compareTo(ps.getId()) == 0)
					fp = fpFornecedor;
			}
		}
		
		ItemServico is = new ItemServico();
		is.setDescricao(ps.getDescricao());
		is.setValorUnitario(fp == null ? ps.getValorUnitario() : fp.getValorUnitario());
		is.setQuantidade(1);
		is.setTotal(fp == null ? ps.getValorUnitario() : fp.getValorUnitario());
		is.setItem(ps);
		is.setFornecedor(fornecedor);
		is.setServico(servicoPertence);
		
		//adiciona o produto ao servico
		servicoPertence.getListaItem().add(is);
		
		tvItem.refresh();
	}
	
	public BigDecimal calculaTotal(ItemServico is){
		return is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade()));
	}
	
	private ProdutoServico selecionarProduto(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ProdutoServico)element).getDescricao();
			}
		});
		
		sid.setElements(prodServService.findAllProdutosAtivos().toArray());
		return (ProdutoServico)sid.getElementoSelecionado();
	}
	
	private void calcularTotais(){
		BigDecimal totalServicos = BigDecimal.ZERO;
		BigDecimal totalItens = BigDecimal.ZERO;
		BigDecimal total;
		
		for(ItemServico servico : service.getModelo().getListaServico()){
				totalServicos = totalServicos.add(servico.getTotal());
				
				for(ItemServico produto : servico.getListaItem())
					totalItens = totalItens.add(produto.getTotal());
		}
		
		
		total = totalServicos.add(totalItens);
		
		service.getModelo().setValorTotal(total);
		lblValorTotal.setText(FormatterHelper.formatMoedaDuasCasas(total));
		
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtClienteObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtCliente);
		IObservableValue clientenomeServicegetModeloObserveValue = PojoProperties.value("cliente.nome").observe(service.getModelo());
		bindingContext.bindValue(observeTextTxtClienteObserveWidget, clientenomeServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeTextTxtVeiculoObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtVeiculo);
		IObservableValue veiculonomeServicegetModeloObserveValue = PojoProperties.value("veiculo.nome").observe(service.getModelo());
		bindingContext.bindValue(observeTextTxtVeiculoObserveWidget, veiculonomeServicegetModeloObserveValue, null, null);
		//
		IObservableList listaServicoServicegetModeloObserveList = PojoProperties.list("listaServico").observe(service.getModelo());
		tvItem.setInput(listaServicoServicegetModeloObserveList);
		//
		IObservableValue observeEnabledBtnSelecionarClienteObserveWidget = WidgetProperties.enabled().observe(btnSelecionarCliente);
		IObservableValue pendenteServicegetModeloObserveValue = PojoProperties.value("pendente").observe(service.getModelo());
		bindingContext.bindValue(observeEnabledBtnSelecionarClienteObserveWidget, pendenteServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeEnabledBtnSelecionarObserveWidget = WidgetProperties.enabled().observe(btnSelecionar);
		bindingContext.bindValue(observeEnabledBtnSelecionarObserveWidget, pendenteServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeEnabledBtnAdicionarServicoObserveWidget = WidgetProperties.enabled().observe(btnAdicionarServico);
		bindingContext.bindValue(observeEnabledBtnAdicionarServicoObserveWidget, pendenteServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeEnabledBtnRemoverServicoObserveWidget = WidgetProperties.enabled().observe(btnRemoverServico);
		bindingContext.bindValue(observeEnabledBtnRemoverServicoObserveWidget, pendenteServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeEnabledBtnAdicionarProdutoObserveWidget = WidgetProperties.enabled().observe(btnAdicionarProduto);
		bindingContext.bindValue(observeEnabledBtnAdicionarProdutoObserveWidget, pendenteServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeEnabledBtnRemoverProdutoObserveWidget = WidgetProperties.enabled().observe(btnRemoverProduto);
		bindingContext.bindValue(observeEnabledBtnRemoverProdutoObserveWidget, pendenteServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeEnabledTreeObserveWidget = WidgetProperties.enabled().observe(tree);
		bindingContext.bindValue(observeEnabledTreeObserveWidget, pendenteServicegetModeloObserveValue, null, null);
		//
		IObservableValue observeTextTxtNumeroObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtNumero);
		IObservableValue numeroServicegetModeloObserveValue = PojoProperties.value("numero").observe(service.getModelo());
		bindingContext.bindValue(observeTextTxtNumeroObserveWidget, numeroServicegetModeloObserveValue, null, null);
		//
		return bindingContext;
	}
	
	@Override
	public MecasoftService<?> getService() {
		return service;
	}
}
