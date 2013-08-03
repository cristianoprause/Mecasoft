package tela.editor;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;
import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.MessageHelper.openWarning;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.ResourceManager;

import tela.contentProvider.ItemServicoContentProvider;
import tela.dialog.ConfiguracaoDialog;
import tela.dialog.SelecionarItemDialog;
import tela.editingSupport.FornecedorItemServicoEditingSupport;
import tela.editingSupport.ItemVisivelItemServicoEditingSupport;
import tela.editingSupport.QuantidadeItemServicoEditingSupport;
import tela.editingSupport.ValorUnitarioItemServico;
import tela.editor.editorInput.AbrirOrdemServicoEditorInput;
import tela.editor.editorInput.FecharOrdemServicoEditorInput;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.ItemServicoService;
import aplicacao.service.MecasoftService;
import aplicacao.service.OrcamentoService;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import aplicacao.service.ServicoPrestadoService;
import aplicacao.service.VeiculoService;
import banco.modelo.ForneceProduto;
import banco.modelo.ItemServico;
import banco.modelo.Orcamento;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;
import banco.modelo.ServicoPrestado;
import banco.modelo.Veiculo;

public class AbrirOrdemServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.AbrirOrdemServicoEditor"; //$NON-NLS-1$
	
	private Logger log = Logger.getLogger(getClass());
	private ServicoPrestadoService service = new ServicoPrestadoService();
	private ProdutoServicoService prodServService = new ProdutoServicoService();
	private OrcamentoService orcamentoService = new OrcamentoService();
	private PessoaService pessoaService = new PessoaService();
	private ItemServicoService itemService = new ItemServicoService();
	private VeiculoService veiculoService = new VeiculoService();
	private List<ItemServico> listaProdutoRemovido;
	private FecharOrdemServicoEditorInput fosei;
	
	private Text txtCliente;
	private Text txtVeiculo;
	private Button btnSelecionarCliente;
	private Button btnSelecionarVeiculo;
	private Button btnAdicionarServio;
	private Button btnRemoverServio;
	private Button btnAdicionarItem;
	private Button btnRemoverItem;
	private Button btnCancelarOrdem;
	private Button btnFecharOrdem;
	private Tree tree;
	private TreeViewer tvServicoProduto;
	private TreeColumn trclmnDescrio;
	private TreeViewerColumn tvcDescricao;
	private TreeColumn trclmnPrestadorfornecedor;
	private TreeViewerColumn tvcPrestadorFornecedor;
	private TreeColumn trclmnValor;
	private TreeViewerColumn tvcValor;
	private TreeColumn trclmnQuantidade;
	private TreeViewerColumn tvcQuantidade;
	private TreeColumn trclmnTotal;
	private TreeViewerColumn tvcTotal;
	private TreeColumn trclmnVisivel;
	private TreeViewerColumn tvcVisivel;
	private Label lblOrcamentoN;
	private Text txtOrcamento;
	private Button btnSelecionarOrcamento;

	public AbrirOrdemServicoEditor() {
		listaProdutoRemovido = new ArrayList<ItemServico>();
	}

	@Override
	public void salvarRegistro() throws ValidationException {
		validar(service.getServicoPrestado());
		
		//verifica se o orcamento ja esta salvo e se ainda esta pendente, caso esteja, salva...
		Orcamento orcamento = service.getServicoPrestado().getOrcamento();
		if(orcamento != null && (orcamento.getId() == null || orcamento.isPendente())){
			orcamento.setPendente(false);
			orcamentoService.setModelo(orcamento);
			orcamentoService.saveOrUpdate();
		}
		
		//GAMBIARRA
		for(ItemServico item : listaProdutoRemovido){
			itemService.setModelo(item);
			itemService.delete();
		}
		
		calcularTotais();
			
		service.saveOrUpdate();
		openInformation("Ordem de serviço registrada com sucesso!");
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(5, false));
		
		lblOrcamentoN = new Label(compositeConteudo, SWT.NONE);
		lblOrcamentoN.setText("Or\u00E7amento N\u00BA:");
		
		txtOrcamento = new Text(compositeConteudo, SWT.BORDER);
		txtOrcamento.setEnabled(false);
		txtOrcamento.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		btnSelecionarOrcamento = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarOrcamento.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarOrcamento.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.getServicoPrestado().setOrcamento(selecionarOrcamento());
				if(service.getServicoPrestado().getOrcamento() != null)
					atualizarServicoComOrcamento();
				
				initDataBindings();
				btnSelecionarOrcamento.setEnabled(service.getServicoPrestado().getOrcamento() == null);
			}
		});
		btnSelecionarOrcamento.setText("Selecionar");
		
		Label lblCliente = new Label(compositeConteudo, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(compositeConteudo, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setEditable(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		btnSelecionarCliente = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarCliente.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa p = selecionarCliente();
				if(p != null){
					service.getServicoPrestado().setCliente(p);
					txtCliente.setText(p.getNomeFantasia());
					
					//remove o veículo para evitar que o veículo nao pertença ao dono
					service.getServicoPrestado().setVeiculo(null);
					txtVeiculo.setText("");
				}else{
					service.getServicoPrestado().setCliente(null);
					txtCliente.setText("");
					
					//remove o veículo
					service.getServicoPrestado().setVeiculo(null);
					txtVeiculo.setText("");
				}
			}
		});
		btnSelecionarCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setText("Selecionar");
//		btnSelecionarCliente.setEnabled(service.getServicoPrestado().getId() == null);
		
		Label lblVeculo = new Label(compositeConteudo, SWT.NONE);
		lblVeculo.setText("Ve\u00EDculo:");
		
		txtVeiculo = new Text(compositeConteudo, SWT.BORDER);
		txtVeiculo.setEnabled(false);
		txtVeiculo.setEditable(false);
		txtVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		btnSelecionarVeiculo = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarVeiculo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Veiculo v = selecionarVeiculo();
				if(v != null){
					txtVeiculo.setText(v.getModelo());
					service.getServicoPrestado().setVeiculo(v);
				}else{
					txtVeiculo.setText("");
					service.getServicoPrestado().setVeiculo(null);
				}
			}
		});
		btnSelecionarVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarVeiculo.setText("Selecionar");
		
		Label lblItemServico = new Label(compositeConteudo, SWT.NONE);
		lblItemServico.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblItemServico.setText("Itens do servi\u00E7o:");
		
		tvServicoProduto = new TreeViewer(compositeConteudo, SWT.BORDER);
		tree = tvServicoProduto.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 4));
		tvServicoProduto.setContentProvider(new ItemServicoContentProvider());
		
		tvcDescricao = new TreeViewerColumn(tvServicoProduto, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getDescricao();
			}
		});
		trclmnDescrio = tvcDescricao.getColumn();
		trclmnDescrio.setWidth(158);
		trclmnDescrio.setText("Descrição");
		
		tvcPrestadorFornecedor = new TreeViewerColumn(tvServicoProduto, SWT.NONE);
		tvcPrestadorFornecedor.setEditingSupport(new FornecedorItemServicoEditingSupport(tvServicoProduto));
		tvcPrestadorFornecedor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				ItemServico item = (ItemServico)element;
				return item.getFornecedor() == null ? "" :
					   item.getFornecedor().getNome();
			}
		});
		trclmnPrestadorfornecedor = tvcPrestadorFornecedor.getColumn();
		trclmnPrestadorfornecedor.setWidth(187);
		trclmnPrestadorfornecedor.setText("Prestador/Fornecedor");
		
		tvcValor = new TreeViewerColumn(tvServicoProduto, SWT.NONE);
		tvcValor.setEditingSupport(new ValorUnitarioItemServico(tvServicoProduto));
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getValorUnitario());
			}
		});
		trclmnValor = tvcValor.getColumn();
		trclmnValor.setWidth(75);
		trclmnValor.setText("Valor");
		
		tvcQuantidade = new TreeViewerColumn(tvServicoProduto, SWT.NONE);
		tvcQuantidade.setEditingSupport(new QuantidadeItemServicoEditingSupport(tvServicoProduto));
		tvcQuantidade.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getQuantidade().toString();
			}
		});
		trclmnQuantidade = tvcQuantidade.getColumn();
		trclmnQuantidade.setWidth(74);
		trclmnQuantidade.setText("Quantidade");
		
		tvcTotal = new TreeViewerColumn(tvServicoProduto, SWT.NONE);
		tvcTotal.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getTotal());
			}
		});
		trclmnTotal = tvcTotal.getColumn();
		trclmnTotal.setWidth(89);
		trclmnTotal.setText("Total");
		
		tvcVisivel = new TreeViewerColumn(tvServicoProduto, SWT.NONE);
		tvcVisivel.setEditingSupport(new ItemVisivelItemServicoEditingSupport(tvServicoProduto));
		tvcVisivel.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}

			@Override
			public Image getImage(Object element) {
				ItemServico is = (ItemServico) element;

				if(is.getItem().getTipo().equals(ProdutoServico.TIPOSERVICO))
					return null;
				if (is.isFornecedorVisivel())
					return ResourceManager.getPluginImage("mecasoft", "assents/funcoes/checked16.png");
				else
					return ResourceManager.getPluginImage("mecasoft", "assents/funcoes/unChecked16.png");
			}

		});
		trclmnVisivel = tvcVisivel.getColumn();
		trclmnVisivel.setWidth(100);
		trclmnVisivel.setText("Vis\u00EDvel");
		
		btnAdicionarServio = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarServio.addSelectionListener(new SelectionAdapter() {
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
					for(ItemServico is : service.getServicoPrestado().getListaServicos())
						if((is.getItem().equals(ps) && is.getFornecedor().equals(prestador)) || !is.getItem().getAtivo())
							return;
						
					ItemServico is = new ItemServico();
					is.setDescricao(ps.getDescricao());
					is.setTotal(ps.getValorUnitario());
					is.setQuantidade(1);
					is.setItem(ps);
					is.setFornecedor(prestador);
					is.setServicoPrestado(service.getServicoPrestado());
					is.setValorUnitario(ps.getValorUnitario());
					service.getServicoPrestado().getListaServicos().add(is);
					
					setEnableButtons();
					
					for(ProdutoServico item : ps.getListaProduto())
						if(item.getAtivo())
							adicionarItens(item, is, null);
					
					tvServicoProduto.refresh();
				}
			}
		});
		btnAdicionarServio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/plusServico16.png"));
		btnAdicionarServio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdicionarServio.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		btnRemoverServio = new Button(compositeConteudo, SWT.NONE);
		btnRemoverServio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvServicoProduto.getSelection();
				
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
					
					service.getServicoPrestado().getListaServicos().remove(is);
					setEnableButtons();
					tvServicoProduto.refresh();
				}
			}
		});
		btnRemoverServio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/lessServico16.png"));
		btnRemoverServio.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoverServio.setText("Remover");
		new Label(compositeConteudo, SWT.NONE);
		
		btnAdicionarItem = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//pega o serviço selecionado pelo usuário
				IStructuredSelection selecao = (IStructuredSelection) tvServicoProduto.getSelection();
				
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
				}
			}
		});
		btnAdicionarItem.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdicionarItem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productAdd16.png"));
		btnAdicionarItem.setText("Adicionar");
		new Label(compositeConteudo, SWT.NONE);
		
		btnRemoverItem = new Button(compositeConteudo, SWT.NONE);
		btnRemoverItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)tvServicoProduto.getSelection();
				
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
					setEnableButtons();
					tvServicoProduto.refresh();
				}
			}
		});
		btnRemoverItem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverItem.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoverItem.setText("Remover");
		
		btnCancelarOrdem = createNewButton();
		btnCancelarOrdem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(openQuestion("Deseja realmente cancelar esta ordem de serviço?")){
					
					//verifica se o usuario ja configurou os status
					if(UsuarioHelper.getConfiguracaoPadrao() == null){
						openError("Registre nas configurações os status padrões para poder cancelar a ordem de serviço.");
						ConfiguracaoDialog cd = new ConfiguracaoDialog(getActiveShell());
						if(cd.open() != IDialogConstants.OK_ID)
							return;
					}
					
					service.getServicoPrestado().setAtivo(false);
					service.getServicoPrestado().setEmExecucao(false);
					
					calcularTotais();
					
					service.saveOrUpdate();

					//caso o fechar ordem de serviço esteja aberto
					IEditorPart iep = getIEPFecharOrdem();
					if(iep != null)
						getSite().getPage().closeEditor(iep, false);
					
					openInformation("Ordem de serviço cancelada com sucesso!");
					
					
					closeThisEditor();
				}
			}
		});
		btnCancelarOrdem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/cancel32.png"));
		btnCancelarOrdem.setText("Cancelar ordem");
		
		if(!getShowSalvar())
			btnCancelarOrdem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		btnFecharOrdem = createNewButton();
		btnFecharOrdem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					
					validar(service.getServicoPrestado());
					
					calcularTotais();
					
					//caso o editor de fechar ordem esteja aberto
					IEditorPart iep = getIEPFecharOrdem();
					if(iep != null)
						return;
					
					getSite().getPage().openEditor(fosei, FecharOrdemServicoEditor.ID);
				} catch (PartInitException e1) {
					log.error(e1);
				} catch (ValidationException e2) {
					setErroMessage(e2.getMessage());
				}
			}
		});
		btnFecharOrdem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/servicoPrestado/closeService32.png"));
		btnFecharOrdem.setText("Fechar Ordem");
		
		setEnableButtons();
		initDataBindings();
		
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		AbrirOrdemServicoEditorInput aosei = (AbrirOrdemServicoEditorInput)input;
		
		if(aosei.getServicoPrestado().getId() != null)
			service.setServicoPrestado(service.find(aosei.getServicoPrestado().getId()));
		else{
			service.setServicoPrestado(aosei.getServicoPrestado());
			
			if(service.getServicoPrestado().getOrcamento() != null)
				atualizarServicoComOrcamento();
		}
		
		setShowExcluir(false);
		
		fosei = new FecharOrdemServicoEditorInput(service.getServicoPrestado());
		
		//abre os 2 editors de abrir e fechar ordens de serviço para mostrar a ordem completa
		if(!service.getServicoPrestado().isEmExecucao())
			site.getPage().openEditor(fosei, FecharOrdemServicoEditor.ID);
		
		setSite(site);
		setInput(input);
		
	}
	
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
	
	private Veiculo selecionarVeiculo(){
		if(service.getServicoPrestado().getCliente() == null){
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
		List<Veiculo> listaVeiculo = veiculoService.findAllByPessoa(service.getServicoPrestado().getCliente());
		List<Veiculo> listaResult = new ArrayList<Veiculo>();
		List<ServicoPrestado> listaServico = service.findAllNaoConcluidos();
		
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
	
	private Orcamento selecionarOrcamento(){
		SelecionarItemDialog sid = new SelecionarItemDialog(getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Orcamento)element).getNumero() + " - " + ((Orcamento)element).getCliente().getNome();
			}
		});
		sid.setElements(orcamentoService.findAllPendente().toArray());
		
		return (Orcamento)sid.getElementoSelecionado();
	}
	
	public BigDecimal calculaTotal(ItemServico is){
		return is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade()));
	}
	
	public void adicionarItens(ProdutoServico ps, ItemServico servicoPertence, Pessoa fornecedor){
		//verifica se é o mesmo produto, fornecedor e servico
		for(ItemServico item : service.getServicoPrestado().getListaServicos()){
			if(item.getItem().equals(ps) &&
			   item.getFornecedor() != null && item.getFornecedor().equals(fornecedor) &&
			   item.getServico().equals(servicoPertence)){
				item.setQuantidade(item.getQuantidade() + 1);
				item.setTotal(calculaTotal(item));
				tvServicoProduto.refresh();
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
//		is.setServicoPrestado(service.getServicoPrestado());
//		service.getServicoPrestado().getListaServicos().add(is);
		
		//adiciona o produto ao servico
		servicoPertence.getListaItem().add(is);
		
		setEnableButtons();
		tvServicoProduto.refresh();
	}
	
	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	
	@Override
	public void setFocus() {
		
		initDataBindings();
		if(!service.getServicoPrestado().isEmExecucao())
			disposeSalvar();
		
		setEnableButtons();
		btnSelecionarCliente.setEnabled(service.getServicoPrestado().getId() == null);
		btnSelecionarVeiculo.setEnabled(service.getServicoPrestado().getId() == null);
	}
	
	public void setEnableButtons(){
		btnCancelarOrdem.setEnabled(service.getServicoPrestado().isAtivo()
				&& service.getServicoPrestado().isEmExecucao()
				&& service.getServicoPrestado().getId() != null
				&& service.getServicoPrestado().getListaServicos().size() == 0);
		
		btnFecharOrdem.setEnabled(service.getServicoPrestado().isEmExecucao());
		
		btnSelecionarOrcamento.setEnabled(service.getServicoPrestado().getOrcamento() == null && service.getServicoPrestado().getId() == null);
	}
	
	private void calcularTotais(){
		BigDecimal totalServicos = BigDecimal.ZERO;
		BigDecimal totalItens = BigDecimal.ZERO;
		BigDecimal total;
		
		for(ItemServico servico : service.getServicoPrestado().getListaServicos()){
			totalServicos = totalServicos.add(servico.getTotal());
			
			for(ItemServico produto : servico.getListaItem())
				totalItens = totalItens.add(produto.getTotal());
			
		}
		
		total = totalServicos.add(totalItens);
		
		service.getServicoPrestado().setTotalItens(totalItens);
		service.getServicoPrestado().setTotalServico(totalServicos);
		service.getServicoPrestado().setValorTotal(total);
		
	}
	
	@SuppressWarnings("deprecation")
	private IEditorPart getIEPFecharOrdem(){
		IEditorPart iepResult = null;
		
		for(IEditorPart iep : getSite().getPage().getEditors()){
			if(iep.getEditorInput().equals(fosei))
				iepResult = iep;
		}
		
		return iepResult;
	}
	
	private void atualizarServicoComOrcamento(){
		ServicoPrestado servicoPrestado = service.getServicoPrestado();
		Orcamento orcamento = service.getServicoPrestado().getOrcamento();
		
		orcamento.setServico(servicoPrestado);
		
		servicoPrestado.setCliente(orcamento.getCliente());
		servicoPrestado.setVeiculo(orcamento.getVeiculo());
		
		for(ItemServico servico : orcamento.getListaServico()){
			servicoPrestado.getListaServicos().add(servico);
			servico.setServicoPrestado(servicoPrestado);
		}
		
		calcularTotais();
		
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtClienteObserveTextObserveWidget = SWTObservables.observeText(txtCliente, SWT.Modify);
		IObservableValue servicegetServicoPrestadoClientenomeFantasiaObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "cliente.nomeFantasia");
		bindingContext.bindValue(txtClienteObserveTextObserveWidget, servicegetServicoPrestadoClientenomeFantasiaObserveValue, null, null);
		//
		IObservableValue txtVeiculoObserveTextObserveWidget = SWTObservables.observeText(txtVeiculo, SWT.Modify);
		IObservableValue servicegetServicoPrestadoVeiculomodeloObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "veiculo.modelo");
		bindingContext.bindValue(txtVeiculoObserveTextObserveWidget, servicegetServicoPrestadoVeiculomodeloObserveValue, null, null);
		//
		IObservableValue observeTextTxtOrcamentoObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtOrcamento);
		IObservableValue orcamentonumeroServicegetServicoPrestadoObserveValue = PojoProperties.value("orcamento.numero").observe(service.getServicoPrestado());
		bindingContext.bindValue(observeTextTxtOrcamentoObserveWidget, orcamentonumeroServicegetServicoPrestadoObserveValue, null, null);
		//
		IObservableList listaServicosServicegetServicoPrestadoObserveList = PojoProperties.list("listaServicos").observe(service.getServicoPrestado());
		tvServicoProduto.setInput(listaServicosServicegetServicoPrestadoObserveList);
		//
		return bindingContext;
	}

	@Override
	public MecasoftService<?> getService() {
		return service;
	}

}
