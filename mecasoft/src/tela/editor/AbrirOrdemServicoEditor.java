package tela.editor;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;
import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.MessageHelper.openWarning;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.dialog.ConfiguracaoDialog;
import tela.dialog.SelecionarItemDialog;
import tela.editingSupport.AcrescimoItemServicoEditingSupport;
import tela.editingSupport.DataStatusServicoEditinfSupport;
import tela.editingSupport.DescontoItemServicoEditingSupport;
import tela.editingSupport.QuantidadeItemServicoEditingSupport;
import tela.editor.editorInput.AbrirOrdemServicoEditorInput;
import tela.editor.editorInput.FecharOrdemServicoEditorInput;
import tela.filter.ServicoPrestadoProdutoFilter;
import tela.filter.ServicoPrestadoServicoFilter;
import tela.viewerSorter.TableStatusServicoViewerSorter;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;
import aplicacao.service.ServicoPrestadoService;
import aplicacao.service.StatusService;
import aplicacao.service.StatusServicoService;
import banco.modelo.ItemServico;
import banco.modelo.Pessoa;
import banco.modelo.ProdutoServico;
import banco.modelo.Status;
import banco.modelo.StatusServico;
import banco.modelo.Veiculo;

public class AbrirOrdemServicoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.AbrirOrdemServicoEditor"; //$NON-NLS-1$
	private Text txtCliente;
	private Text txtVeiculo;
	private Table tableServicos;
	private Table tableItens;
	private Text txtFuncionario;
	private Text txtStatusAtual;
	private Table tableStatus;
	private TableViewer tvServico;
	private TableViewer tvItens;
	private ComboViewer cvNovoStatus;
	private TableViewer tvStatus;
	private Button btnSelecionarCliente;
	private Button btnSelecionarVeiculo;
	private Button btnAdicionarServio;
	private Button btnRemoverServio;
	private Button btnAdicionarItem;
	private Button btnRemoverItem;
	private Button btnSelecionar;
	private Combo cbNovoStatus;
	private Button btnAlterarStatus;
	private Button btnCancelarOrdem;
	private Button btnFecharOrdem;
	private Button btnRemoverStatus;
	private ServicoPrestadoProdutoFilter produtoFilter = new ServicoPrestadoProdutoFilter();
	private ServicoPrestadoServicoFilter servicoFilter = new ServicoPrestadoServicoFilter();
	
	private ServicoPrestadoService service = new ServicoPrestadoService();
	private ProdutoServicoService prodServService = new ProdutoServicoService();
	private StatusService statusService = new StatusService();
	private PessoaService pessoaService = new PessoaService();
	private StatusServicoService statusServicoService = new StatusServicoService();
	private List<Status> listaStatus;
	private Pessoa funcionario;

	public AbrirOrdemServicoEditor() {
		listaStatus = statusService.findAllAtivos();
	}

	@Override
	public void salvarRegistro() throws ValidationException {
		validar(service.getServicoPrestado());
			
		calcularTotais();
			
		service.saveOrUpdate();
		openInformation("Ordem de serviço registrada com sucesso!");
		closeThisEditor();
	}

	@Override
	public void excluirRegistro() {}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(5, false));
		
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
				}
			}
		});
		btnSelecionarCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setText("Selecionar");
		btnSelecionarCliente.setEnabled(service.getServicoPrestado().getId() == null);
		
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
				}
			}
		});
		btnSelecionarVeiculo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarVeiculo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarVeiculo.setText("Selecionar");
		btnSelecionarVeiculo.setEnabled(service.getServicoPrestado().getId() == null);
		
		Label lblServicosPrestados = new Label(compositeConteudo, SWT.NONE);
		lblServicosPrestados.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblServicosPrestados.setText("Servi\u00E7os prestados:");
		
		tvServico = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableServicos = tvServico.getTable();
		tableServicos.setLinesVisible(true);
		tableServicos.setHeaderVisible(true);
		GridData gd_tableServicos = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 2);
		gd_tableServicos.widthHint = 658;
		gd_tableServicos.heightHint = 95;
		tvServico.addFilter(servicoFilter);
		tableServicos.setLayoutData(gd_tableServicos);
		
		TableViewerColumn tvcServico = new TableViewerColumn(tvServico, SWT.NONE);
		tvcServico.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getDescricao();
			}
		});
		TableColumn tblclmnServico = tvcServico.getColumn();
		tblclmnServico.setWidth(165);
		tblclmnServico.setText("Servi\u00E7o");
		
		TableViewerColumn tvcValorBase = new TableViewerColumn(tvServico, SWT.NONE);
		tvcValorBase.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getValorUnitario());
			}
		});
		TableColumn tblclmnValorBase = tvcValorBase.getColumn();
		tblclmnValorBase.setWidth(127);
		tblclmnValorBase.setText("Valor Base");
		
		TableViewerColumn tvcDesconto = new TableViewerColumn(tvServico, SWT.NONE);
		tvcDesconto.setEditingSupport(new DescontoItemServicoEditingSupport(tvServico));
		tvcDesconto.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getDesconto());
			}
		});
		TableColumn tblclmnDesconto = tvcDesconto.getColumn();
		tblclmnDesconto.setWidth(136);
		tblclmnDesconto.setText("Desconto");
		
		TableViewerColumn tvcAcrescimo = new TableViewerColumn(tvServico, SWT.NONE);
		tvcAcrescimo.setEditingSupport(new AcrescimoItemServicoEditingSupport(tvServico));
		tvcAcrescimo.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getAcrescimo());
			}
		});
		TableColumn tblclmnAcrescimo = tvcAcrescimo.getColumn();
		tblclmnAcrescimo.setWidth(136);
		tblclmnAcrescimo.setText("Acrescimo");
		
		TableViewerColumn tvcTotalServico = new TableViewerColumn(tvServico, SWT.NONE);
		tvcTotalServico.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getTotal());
			}
		});
		TableColumn tblclmnTotalServico = tvcTotalServico.getColumn();
		tblclmnTotalServico.setWidth(100);
		tblclmnTotalServico.setText("Total");
		
		btnAdicionarServio = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarServio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProdutoServico ps = selecionarServico();
				if(ps != null){
					
					for(ItemServico is : service.getServicoPrestado().getListaServicos())
						if(is.getItem().equals(ps) || !is.getItem().getAtivo())
							return;
						
					ItemServico is = new ItemServico();
					is.setDescricao(ps.getDescricao());
					is.setTotal(ps.getValorUnitario());
					is.setQuantidade(1);
					is.setItem(ps);
					is.setServicoPrestado(service.getServicoPrestado());
					is.setValorUnitario(ps.getValorUnitario());
					service.getServicoPrestado().getListaServicos().add(is);
					
					setEnableButtonCancelFechar();
					
					for(ProdutoServico item : ps.getListaProduto())
						if(item.getAtivo())
							adicionarItens(item);
					
					tvServico.refresh();
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
				IStructuredSelection selecao = (IStructuredSelection)tvServico.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(openQuestion("Deseja realmente remover este serviço da lista?")){
					ItemServico is = (ItemServico)selecao.getFirstElement();
					service.getServicoPrestado().getListaServicos().remove(is);
					
					setEnableButtonCancelFechar();
					
					tvServico.refresh();
				}
			}
		});
		btnRemoverServio.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/servico/lessServico16.png"));
		btnRemoverServio.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoverServio.setText("Remover");
		
		Label lblItensUtilizados = new Label(compositeConteudo, SWT.NONE);
		lblItensUtilizados.setText("Itens utilizados:");
		
		tvItens = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableItens = tvItens.getTable();
		tableItens.setLinesVisible(true);
		tableItens.setHeaderVisible(true);
		GridData gd_tableItens = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 2);
		gd_tableItens.widthHint = 628;
		gd_tableItens.heightHint = 95;
		tvItens.addFilter(produtoFilter);
		tableItens.setLayoutData(gd_tableItens);
		
		TableViewerColumn tvcDescricao = new TableViewerColumn(tvItens, SWT.NONE);
		tvcDescricao.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getDescricao();
			}
		});
		TableColumn tblclmnDescricao = tvcDescricao.getColumn();
		tblclmnDescricao.setWidth(143);
		tblclmnDescricao.setText("Descri\u00E7\u00E3o");
		
		TableViewerColumn tvcQuantidade = new TableViewerColumn(tvItens, SWT.NONE);
		tvcQuantidade.setEditingSupport(new QuantidadeItemServicoEditingSupport(tvItens));
		tvcQuantidade.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((ItemServico)element).getQuantidade().toString();
			}
		});
		TableColumn tblclmnQuantidade = tvcQuantidade.getColumn();
		tblclmnQuantidade.setWidth(100);
		tblclmnQuantidade.setText("Quantidade");
		
		TableViewerColumn tvcValorUnitario = new TableViewerColumn(tvItens, SWT.NONE);
		tvcValorUnitario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getValorUnitario());
			}
		});
		TableColumn tblclmnValorUnitario = tvcValorUnitario.getColumn();
		tblclmnValorUnitario.setWidth(100);
		tblclmnValorUnitario.setText("Valor Unit\u00E1rio");
		
		TableViewerColumn tvcDescontoItens = new TableViewerColumn(tvItens, SWT.NONE);
		tvcDescontoItens.setEditingSupport(new DescontoItemServicoEditingSupport(tvItens));
		tvcDescontoItens.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getDesconto());
			}
		});
		TableColumn tblclmnDesconto_1 = tvcDescontoItens.getColumn();
		tblclmnDesconto_1.setWidth(100);
		tblclmnDesconto_1.setText("Desconto");
		
		TableViewerColumn tvcAcrescimoItens = new TableViewerColumn(tvItens, SWT.NONE);
		tvcAcrescimoItens.setEditingSupport(new AcrescimoItemServicoEditingSupport(tvItens));
		tvcAcrescimoItens.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getAcrescimo());
			}
		});
		TableColumn tblclmnAcrescimoProduto = tvcAcrescimoItens.getColumn();
		tblclmnAcrescimoProduto.setWidth(100);
		tblclmnAcrescimoProduto.setText("Acrescimo");
		
		TableViewerColumn tvcTotal = new TableViewerColumn(tvItens, SWT.NONE);
		tvcTotal.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((ItemServico)element).getTotal());
			}
		});
		TableColumn tblclmnTotal = tvcTotal.getColumn();
		tblclmnTotal.setResizable(false);
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		
		btnAdicionarItem = new Button(compositeConteudo, SWT.NONE);
		btnAdicionarItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProdutoServico ps = selecionarProduto();
				if(ps != null){
					adicionarItens(ps);
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
				IStructuredSelection selecao = (IStructuredSelection)tvItens.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(openQuestion("Deseja realmente remover este item da lista?")){
					ItemServico is = (ItemServico)selecao.getFirstElement();
					
					//remove da lista de serviços apenas para evitar o erro do orphanremove
					service.getServicoPrestado().getListaServicos().remove(is);
					service.getServicoPrestado().getListaProdutos().remove(is);
					
					setEnableButtonCancelFechar();
					
					tvItens.refresh();
				}
			}
		});
		btnRemoverItem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/product/productRemove16.png"));
		btnRemoverItem.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoverItem.setText("Remover");
		
		Label lblFuncionario = new Label(compositeConteudo, SWT.NONE);
		lblFuncionario.setText("Funcion\u00E1rio:");
		
		txtFuncionario = new Text(compositeConteudo, SWT.BORDER);
		txtFuncionario.setEnabled(false);
		txtFuncionario.setEditable(false);
		txtFuncionario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		btnSelecionar = new Button(compositeConteudo, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				funcionario = selecionarFuncionario();
				if(funcionario != null)
					txtFuncionario.setText(funcionario.getNomeFantasia());
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");
		
		Label lblNovoStatus = new Label(compositeConteudo, SWT.NONE);
		lblNovoStatus.setText("Novo status:");
		
		cvNovoStatus = new ComboViewer(compositeConteudo, SWT.READ_ONLY);
		cbNovoStatus = cvNovoStatus.getCombo();
		GridData gd_cbNovoStatus = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cbNovoStatus.widthHint = 142;
		cbNovoStatus.setLayoutData(gd_cbNovoStatus);
		
		btnAlterarStatus = new Button(compositeConteudo, SWT.NONE);
		btnAlterarStatus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selecao = (IStructuredSelection)cvNovoStatus.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(funcionario == null){
					openWarning("Selecione primeiro o funcionário para alterar o status.");
					return;
				}

				//pega o novo status
				Status status = (Status)selecao.getFirstElement();
				
				//criado aqui para evitar problemas e conseguir verificar em linhas abaixo
				StatusServico statusParado = null;
				StatusServico statusAtualServico = null;
				
				if(!service.getServicoPrestado().getListaStatus().isEmpty()){
					statusAtualServico = service.getServicoPrestado().getUltimoStatus();
					
					//não pode adicionar um status de parar 
					if(!statusAtualServico.getFuncionario().equals(funcionario) && status.isPausar()){
						openError("Não é permitido adicionar o status \"" + status.getDescricao() + "\" para um funcionário diferente do atual");
						return;
					
					//se forem funcionarios diferentes adicionando status verde neste serviço, tem que ser adicionado
					//um status vermelho com o funcionario que estava antes
					}else if(!statusAtualServico.getStatus().isPausar() && !status.isPausar() &&
							!statusAtualServico.getFuncionario().equals(funcionario)){
						
						//verifica se p usuário permite adicionar um status de parado para o funcionario anterior
						if(openQuestion("O serviço esta em um status continuo para um funcionário diferente. Caso continue, " +
								"será adicionado o status \"" + UsuarioHelper.getConfiguracaoPadrao().getStatusFinal().getDescricao() + "\" para o funcionário que estava anteriormente.\n" +
								"Deseja continuar?")){
							
							//cria um status de parado para o funcionário anterior
							statusParado = new StatusServico();
							statusParado.setFuncionario(statusAtualServico.getFuncionario());
							statusParado.setServicoPrestado(service.getServicoPrestado());
							statusParado.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal());
							
						}else
							return;
						
					}
				}
				
				//pega o ultimo status do usuario
				StatusServico statusFuncionario = statusServicoService.findStatusFuncionario(funcionario);
				
				//verifica se nao foi adicionado algum status neste serviço para este funcionario e ainda nao foi salvo
				//caso tenha sido, esse status é o status atual do funcionario
				if(statusAtualServico != null && statusFuncionario != null){
					if(statusAtualServico.getFuncionario().equals(funcionario) 
						&& statusAtualServico.getData().compareTo(statusFuncionario.getData()) > 0)
							statusFuncionario = statusAtualServico;
				}
				
				if(statusFuncionario != null && !statusFuncionario.getServicoPrestado().equals(service.getServicoPrestado()) && status.isPausar()){
					openError("Não é permitido adicionar o status \"" + status.getDescricao() + "\" com este funcionário, pois ele esta em um outro serviço");
					return;
					
				//caso seja um status continuo em um outo serviço, ele mostra uma mensagem informando que tera que ser
				//adicionado um status de parado ao serviço que ele esta para adicionar um status verde para este usuario neste serviço
				}else if(statusFuncionario != null && !statusFuncionario.getStatus().isPausar() 
						&& !statusFuncionario.getServicoPrestado().equals(service.getServicoPrestado())
						&& !status.isPausar()){
					
					//verica se o usuário aprova
					if(openQuestion("O mecânico selecionado ja esta ativo em outro serviço.\n" +
							"Caso ele seja adicionado a este serviço, sera adicionado o status \"" + UsuarioHelper.getConfiguracaoPadrao().getStatusFinal().getDescricao() + "\" " +
							"no serviço anterior.\n" +
							"Deseja continuar?")){
						
						//cria um novo StatusServico para adicionar no serviço em que o funcionario estava
						StatusServico statusServicoAnterior = new StatusServico();
						statusServicoAnterior.setFuncionario(statusFuncionario.getFuncionario());
						statusServicoAnterior.setServicoPrestado(statusFuncionario.getServicoPrestado());
						statusServicoAnterior.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal());
						
						//salva este novo status
						statusServicoService.setStatusServico(statusServicoAnterior);
						statusServicoService.saveOrUpdate();
						
					}else
						return;
					
				}
				
				//adiciona na lista de status do serviço atual o status caso
				//o usuario aprovou o 1º if xD
				if(statusParado != null)
					service.getServicoPrestado().getListaStatus().add(statusParado);
				
				StatusServico ss = new StatusServico();
				ss.setFuncionario(funcionario);
				ss.setServicoPrestado(service.getServicoPrestado());
				ss.setStatus(status);
				
				
				service.getServicoPrestado().getListaStatus().add(ss);
				txtStatusAtual.setText(status.getDescricao());
				tvStatus.refresh();
				txtFuncionario.setText("");
				funcionario = null;
				
				
			}
		});
		btnAlterarStatus.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnAlterarStatus.setText("Alterar Status");
		
		btnRemoverStatus = new Button(compositeConteudo, SWT.NONE);
		btnRemoverStatus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection selecao = (IStructuredSelection)tvStatus.getSelection();
				
				if(selecao.isEmpty())
					return;
				
				if(openQuestion("Deseja realmente remover este status do serviço?")){
					StatusServico ss = (StatusServico)selecao.getFirstElement();
					service.getServicoPrestado().getListaStatus().remove(ss);
					tvStatus.refresh();
				}
				
			}
		});
		btnRemoverStatus.setText("Remover Status");
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblStatusAtual = new Label(compositeConteudo, SWT.NONE);
		lblStatusAtual.setText("Status atual:");
		
		txtStatusAtual = new Text(compositeConteudo, SWT.BORDER);
		txtStatusAtual.setEditable(false);
		txtStatusAtual.setEnabled(false);
		txtStatusAtual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		
		StatusServico ultimoStatus = service.getServicoPrestado().getUltimoStatus();
		if(ultimoStatus != null)
			txtStatusAtual.setText(ultimoStatus.getStatus().getDescricao());
		
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblStatus = new Label(compositeConteudo, SWT.NONE);
		lblStatus.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblStatus.setText("Status:");
		
		tvStatus = new TableViewer(compositeConteudo, SWT.BORDER | SWT.FULL_SELECTION);
		tableStatus = tvStatus.getTable();
		tableStatus.setLinesVisible(true);
		tableStatus.setHeaderVisible(true);
		GridData gd_tableStatus = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
		gd_tableStatus.widthHint = 593;
		gd_tableStatus.heightHint = 95;
		tableStatus.setLayoutData(gd_tableStatus);
		tvStatus.setSorter(new TableStatusServicoViewerSorter());
		
		TableViewerColumn tvcStatus = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcStatus.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((StatusServico)element).getStatus().getDescricao();
			}
			
			@Override
			public Color getForeground(Object element) {
				StatusServico ss = (StatusServico)element;
				
				if(ss.getStatus().isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				else
					return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
		});
		TableColumn tblclmnStatus = tvcStatus.getColumn();
		tblclmnStatus.setWidth(184);
		tblclmnStatus.setText("Status");
		
		TableViewerColumn tvcData = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcData.setEditingSupport(new DataStatusServicoEditinfSupport(tvStatus));
		tvcData.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDateFormatData("dd/MM/yyyy HH:mm").format(((StatusServico)element).getData());
			}

			@Override
			public Color getForeground(Object element) {
				StatusServico ss = (StatusServico)element;
				
				if(ss.getStatus().isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				else
					return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
		});
		TableColumn tblclmnData = tvcData.getColumn();
		tblclmnData.setWidth(159);
		tblclmnData.setText("Data");
		
		TableViewerColumn tvcFuncionario = new TableViewerColumn(tvStatus, SWT.NONE);
		tvcFuncionario.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((StatusServico)element).getFuncionario().getNomeFantasia();
			}

			@Override
			public Color getForeground(Object element) {
				StatusServico ss = (StatusServico)element;
				
				if(ss.getStatus().isPausar())
					return SWTResourceManager.getColor(SWT.COLOR_RED);
				else
					return SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
			}
		});
		TableColumn tblclmnFuncionario = tvcFuncionario.getColumn();
		tblclmnFuncionario.setWidth(299);
		tblclmnFuncionario.setText("Funcion\u00E1rio");
		new Label(compositeConteudo, SWT.NONE);
		
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
					
					//cria o status de concluido
					StatusServico statusConcluido = new StatusServico();
					statusConcluido.setFuncionario(service.getServicoPrestado().getUltimoStatus().getFuncionario());
					statusConcluido.setServicoPrestado(service.getServicoPrestado());
					statusConcluido.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinalizarServico());
			
					//adiciona o status de concluido na lista de status do serviço
					service.getServicoPrestado().getListaStatus().add(statusConcluido);
					
					calcularTotais();
					
					service.saveOrUpdate();
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
					calcularTotais();
					
					getSite().getPage().openEditor(new FecharOrdemServicoEditorInput(service.getServicoPrestado()), FecharOrdemServicoEditor.ID);
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnFecharOrdem.setImage(ResourceManager.getPluginImage("mecasoft", "assents/servicoPrestado/closeService32.png"));
		btnFecharOrdem.setText("Fechar Ordem");
		
		setEnableButtonCancelFechar();
		
		initDataBindings();
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		AbrirOrdemServicoEditorInput aosei = (AbrirOrdemServicoEditorInput)input;
		
		if(aosei.getServicoPrestado().getId() != null)
			service.setServicoPrestado(service.find(aosei.getServicoPrestado().getId()));
		else
			service.setServicoPrestado(aosei.getServicoPrestado());
		
		setShowExcluir(false);
		setShowSalvar(service.getServicoPrestado().isEmExecucao());
//		service.organizarListas();
		
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
		sid.setElements(service.getServicoPrestado().getCliente().getListaVeiculo().toArray());
		
		return (Veiculo) sid.getElementoSelecionado();
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
	
	private Pessoa selecionarFuncionario(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		sid.setElements(pessoaService.findAllFuncionariosAtivos().toArray());
		
		return (Pessoa) sid.getElementoSelecionado();
	}
	
	public BigDecimal calculaTotal(ItemServico is){
		return is.getValorUnitario().multiply(new BigDecimal(is.getQuantidade()))
			.subtract(is.getDesconto()).add(is.getAcrescimo());
	}
	
	public void adicionarItens(ProdutoServico ps){
		for(ItemServico item : service.getServicoPrestado().getListaProdutos()){
			if(item.getItem().equals(ps)){
				item.setQuantidade(item.getQuantidade() + 1);
				item.setTotal(calculaTotal(item));
				tvItens.refresh();
				return;
			}
		}

		ItemServico is = new ItemServico();
		is.setDescricao(ps.getDescricao());
		is.setValorUnitario(ps.getValorUnitario());
		is.setQuantidade(0);
		is.setTotal(BigDecimal.ZERO);
		is.setItem(ps);
		is.setServicoPrestado(service.getServicoPrestado());
		service.getServicoPrestado().getListaProdutos().add(is);
		service.getServicoPrestado().getListaServicos().add(is);
		
		setEnableButtonCancelFechar();
		tvItens.refresh();
	}
	
	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	
	@Override
	public void setFocus() {
		listaStatus = statusService.findAllAtivos();
		initDataBindings();
		if(!service.getServicoPrestado().isEmExecucao())
			disposeSalvar();
		
		setEnableButtonCancelFechar();
	}
	
	public void setEnableButtonCancelFechar(){
		btnCancelarOrdem.setEnabled(service.getServicoPrestado().isAtivo()
				&& service.getServicoPrestado().getId() != null
				&& service.getServicoPrestado().getListaServicos().size() == 0);
		
		btnFecharOrdem.setEnabled(service.getServicoPrestado().isEmExecucao());
	}
	
	private void calcularTotais(){
		BigDecimal totalServicos = BigDecimal.ZERO;
		BigDecimal totalItens = BigDecimal.ZERO;
		BigDecimal total;
		
		for(ItemServico item : service.getServicoPrestado().getListaServicos()){
			if(item.getItem().getTipo().equals(ProdutoServico.TIPOSERVICO))
				totalServicos = totalServicos.add(item.getTotal());
			else if(item.getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO))
				totalItens = totalItens.add(item.getTotal());
		}
		
		
		total = totalServicos.add(totalItens);
		
		service.getServicoPrestado().setTotalItens(totalItens);
		service.getServicoPrestado().setTotalServico(totalServicos);
		service.getServicoPrestado().setValorTotal(total);
		
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
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), ItemServico.class, new String[]{"descricao", "valorUnitario", "desconto", "acrescimo"});
//		tvServico.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvServico.setContentProvider(listContentProvider);
		//
		IObservableList servicegetServicoPrestadoListaServicosObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaServicos");
		tvServico.setInput(servicegetServicoPrestadoListaServicosObserveList);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
//		IObservableMap[] observeMaps_1 = PojoObservables.observeMaps(listContentProvider_1.getKnownElements(), ItemServico.class, new String[]{"descricao", "quantidade", "valorUnitario", "desconto", "acrescimo", "total"});
//		tvItens.setLabelProvider(new ObservableMapLabelProvider(observeMaps_1));
		tvItens.setContentProvider(listContentProvider_1);
		//
		IObservableList servicegetServicoPrestadoListaProdutosObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaProdutos");
		tvItens.setInput(servicegetServicoPrestadoListaProdutosObserveList);
		//
		ObservableListContentProvider listContentProvider_2 = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider_2.getKnownElements(), Status.class, "descricao");
		cvNovoStatus.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvNovoStatus.setContentProvider(listContentProvider_2);
		//
		WritableList writableList = new WritableList(listaStatus, Status.class);
		cvNovoStatus.setInput(writableList);
		//
		ObservableListContentProvider listContentProvider_3 = new ObservableListContentProvider();
//		IObservableMap[] observeMaps_2 = PojoObservables.observeMaps(listContentProvider_3.getKnownElements(), StatusServico.class, new String[]{"status.descricao", "data", "funcionario.nomeFantasia"});
//		tvStatus.setLabelProvider(new ObservableMapLabelProvider(observeMaps_2));
		tvStatus.setContentProvider(listContentProvider_3);
		//
		IObservableList servicegetServicoPrestadoListaStatusObserveList = PojoObservables.observeList(Realm.getDefault(), service.getServicoPrestado(), "listaStatus");
		tvStatus.setInput(servicegetServicoPrestadoListaStatusObserveList);
		//
		IObservableValue tableServicosObserveEnabledObserveWidget = SWTObservables.observeEnabled(tableServicos);
		IObservableValue servicegetServicoPrestadoConcluidoObserveValue = PojoObservables.observeValue(service.getServicoPrestado(), "emExecucao");
		bindingContext.bindValue(tableServicosObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue btnAdicionarServioObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnAdicionarServio);
		bindingContext.bindValue(btnAdicionarServioObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue btnRemoverServioObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnRemoverServio);
		bindingContext.bindValue(btnRemoverServioObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue tableItensObserveEnabledObserveWidget = SWTObservables.observeEnabled(tableItens);
		bindingContext.bindValue(tableItensObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue btnAdicionarItemObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnAdicionarItem);
		bindingContext.bindValue(btnAdicionarItemObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue btnRemoverItemObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnRemoverItem);
		bindingContext.bindValue(btnRemoverItemObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue btnSelecionarObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnSelecionar);
		bindingContext.bindValue(btnSelecionarObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue cbNovoStatusObserveEnabledObserveWidget = SWTObservables.observeEnabled(cbNovoStatus);
		bindingContext.bindValue(cbNovoStatusObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue btnAlterarStatusObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnAlterarStatus);
		bindingContext.bindValue(btnAlterarStatusObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue tableStatusObserveEnabledObserveWidget = SWTObservables.observeEnabled(tableStatus);
		bindingContext.bindValue(tableStatusObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		IObservableValue btnRemoverStatusObserveEnabledObserveWidget = SWTObservables.observeEnabled(btnRemoverStatus);
		bindingContext.bindValue(btnRemoverStatusObserveEnabledObserveWidget, servicegetServicoPrestadoConcluidoObserveValue, null, null);
		//
		return bindingContext;
	}
}
