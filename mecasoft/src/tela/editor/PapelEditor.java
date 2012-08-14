package tela.editor;

import static aplicacao.helper.MessageHelper.openQuestion;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.editor.editorInput.PapelEditorInput;

import aplicacao.exception.ValidationException;
import aplicacao.helper.MessageHelper;
import aplicacao.helper.ValidatorHelper;
import aplicacao.service.PapelService;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PapelEditor extends MecasoftEditor {
	
	public static final String ID = "tela.editor.PapelEditor"; //$NON-NLS-1$
	private Text txtNome;
	private Button btnCadPessoa;
	private Button btnCadVeiculo;
	private Button btnCadServico;
	private Button btnCadProduto;
	private Button btnCadFormaPagto;
	private Button btnUsuario;
	private Button btnDuplicatas;
	private Button btnServico;
	private Button btnCaixa;
	private Button btnGerarRelatorio;
	
	private PapelService service;

	public PapelEditor() {
		service = new PapelService();
	}

	@Override
	public void addComponentes(Composite compositeConteudo) {
		compositeConteudo.setLayout(new GridLayout(8, false));
		
		Label lblNome = new Label(compositeConteudo, SWT.NONE);
		lblNome.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNome.setText("Nome:");
		
		txtNome = new Text(compositeConteudo, SWT.BORDER);
		GridData gd_txtNome = new GridData(SWT.FILL, SWT.CENTER, true, false, 7, 1);
		gd_txtNome.widthHint = 381;
		txtNome.setLayoutData(gd_txtNome);
		
		final Button btnCad = new Button(compositeConteudo, SWT.CHECK);
		btnCad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//marca ou desmarca todas as opções de cadastro
				if(btnCad.getSelection()){
					service.getPapel().setCadPessoa(true);
					service.getPapel().setCadFormaPagto(true);
					service.getPapel().setCadProduto(true);
					service.getPapel().setCadServico(true);
					service.getPapel().setCadUsuario(true);
					service.getPapel().setCadVeiculo(true);
				}else{
					service.getPapel().setCadPessoa(false);
					service.getPapel().setCadFormaPagto(false);
					service.getPapel().setCadProduto(false);
					service.getPapel().setCadServico(false);
					service.getPapel().setCadUsuario(false);
					service.getPapel().setCadVeiculo(false);
				}
				
				initDataBindings();
			}
		});
		btnCad.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		btnCad.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnCad.setText("Cadastros/edi\u00E7\u00F5es");
		new Label(compositeConteudo, SWT.NONE);
		
		final Button btnFinanceiro = new Button(compositeConteudo, SWT.CHECK);
		btnFinanceiro.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//marca ou desmarca todas as opções do financeiro
				if(btnFinanceiro.getSelection()){
					service.getPapel().setGerCaixa(true);
					service.getPapel().setGerDuplicata(true);
					service.getPapel().setGerServico(true);
				}else{
					service.getPapel().setGerCaixa(false);
					service.getPapel().setGerDuplicata(false);
					service.getPapel().setGerServico(false);
				}
				
				initDataBindings();
			}
		});
		btnFinanceiro.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnFinanceiro.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		btnFinanceiro.setText("Financeiro");
		new Label(compositeConteudo, SWT.NONE);
		
		final Button btnRelatorio = new Button(compositeConteudo, SWT.CHECK);
		btnRelatorio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//marca ou desmarca todas as opções do relatorio
				if(btnRelatorio.getSelection()){
					service.getPapel().setGerarRelatorio(true);
				}else{
					service.getPapel().setGerarRelatorio(false);
				}
				
				initDataBindings();
			}
		});
		btnRelatorio.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnRelatorio.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		btnRelatorio.setText("Relatório");
		new Label(compositeConteudo, SWT.NONE);
		
		btnCadPessoa = new Button(compositeConteudo, SWT.CHECK);
		btnCadPessoa.setText("Pessoa");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnDuplicatas = new Button(compositeConteudo, SWT.CHECK);
		btnDuplicatas.setText("Duplicatas");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnGerarRelatorio = new Button(compositeConteudo, SWT.CHECK);
		btnGerarRelatorio.setText("Gerar relat\u00F3rio");
		new Label(compositeConteudo, SWT.NONE);
		
		btnCadVeiculo = new Button(compositeConteudo, SWT.CHECK);
		btnCadVeiculo.setText("Ve\u00EDculo");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnServico = new Button(compositeConteudo, SWT.CHECK);
		btnServico.setText("Servi\u00E7o");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnCadServico = new Button(compositeConteudo, SWT.CHECK);
		btnCadServico.setText("Servi\u00E7o");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnCaixa = new Button(compositeConteudo, SWT.CHECK);
		btnCaixa.setText("Caixa");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnCadProduto = new Button(compositeConteudo, SWT.CHECK);
		btnCadProduto.setText("Produto");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnCadFormaPagto = new Button(compositeConteudo, SWT.CHECK);
		btnCadFormaPagto.setText("Forma de pagamento");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		btnUsuario = new Button(compositeConteudo, SWT.CHECK);
		btnUsuario.setText("Usu\u00E1rio");
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		initDataBindings();
	}
	
	@Override
	public void salvarRegistro() {
		try {
			ValidatorHelper.validar(service.getPapel());
			
			service.saveOrUpdate();
			MessageHelper.openInformation("Papel cadastrado com sucesso!");
			closeThisEditor();
		} catch (ValidationException e) {
			setErroMessage(e.getMessage());
		}		
	}

	@Override
	public void excluirRegistro() {
		if(service.getPapel().getListaUsuarios().size() != 0){
			setErroMessage("Não é possível excluir, pois existem usuários utilizando este papel.");
			return;
		}
		if(openQuestion("Deseja realmente apagar este papel?")){
			service.delete();
			MessageHelper.openInformation("Papel excluido com sucesso");
			closeThisEditor();
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		PapelEditorInput pei = (PapelEditorInput)input;
		
		setShowExcluir(pei.getPapel().getId() != null);
		
		if(pei.getPapel().getId() != null){
			service.setPapel(service.find(pei.getPapel().getId()));
			this.setPartName("Papel: " + service.getPapel().getNome());
		}else
			service.setPapel(pei.getPapel());
		
		setSite(site);
		setInput(input);
		
	}

	@Override
	public boolean isDirty() {
		return service.isDirty();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue btnCadCliForObserveSelectionObserveWidget = SWTObservables.observeSelection(btnCadPessoa);
		IObservableValue servicegetPapelCadPessoaObserveValue = PojoObservables.observeValue(service.getPapel(), "cadPessoa");
		bindingContext.bindValue(btnCadCliForObserveSelectionObserveWidget, servicegetPapelCadPessoaObserveValue, null, null);
		//
		IObservableValue btnCadVeiculoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnCadVeiculo);
		IObservableValue servicegetPapelCadVeiculoObserveValue = PojoObservables.observeValue(service.getPapel(), "cadVeiculo");
		bindingContext.bindValue(btnCadVeiculoObserveSelectionObserveWidget, servicegetPapelCadVeiculoObserveValue, null, null);
		//
		IObservableValue btnCadServicoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnCadServico);
		IObservableValue servicegetPapelCadServicoObserveValue = PojoObservables.observeValue(service.getPapel(), "cadServico");
		bindingContext.bindValue(btnCadServicoObserveSelectionObserveWidget, servicegetPapelCadServicoObserveValue, null, null);
		//
		IObservableValue btnCadProdutoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnCadProduto);
		IObservableValue servicegetPapelCadProdutoObserveValue = PojoObservables.observeValue(service.getPapel(), "cadProduto");
		bindingContext.bindValue(btnCadProdutoObserveSelectionObserveWidget, servicegetPapelCadProdutoObserveValue, null, null);
		//
		IObservableValue btnCadFormaPagtoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnCadFormaPagto);
		IObservableValue servicegetPapelCadFormaPagtoObserveValue = PojoObservables.observeValue(service.getPapel(), "cadFormaPagto");
		bindingContext.bindValue(btnCadFormaPagtoObserveSelectionObserveWidget, servicegetPapelCadFormaPagtoObserveValue, null, null);
		//
		IObservableValue btnUsuarioObserveSelectionObserveWidget = SWTObservables.observeSelection(btnUsuario);
		IObservableValue servicegetPapelCadUsuarioObserveValue = PojoObservables.observeValue(service.getPapel(), "cadUsuario");
		bindingContext.bindValue(btnUsuarioObserveSelectionObserveWidget, servicegetPapelCadUsuarioObserveValue, null, null);
		//
		IObservableValue btnDuplicatasObserveSelectionObserveWidget = SWTObservables.observeSelection(btnDuplicatas);
		IObservableValue servicegetPapelGerDuplicataObserveValue = PojoObservables.observeValue(service.getPapel(), "gerDuplicata");
		bindingContext.bindValue(btnDuplicatasObserveSelectionObserveWidget, servicegetPapelGerDuplicataObserveValue, null, null);
		//
		IObservableValue btnServicoObserveSelectionObserveWidget = SWTObservables.observeSelection(btnServico);
		IObservableValue servicegetPapelGerServicoObserveValue = PojoObservables.observeValue(service.getPapel(), "gerServico");
		bindingContext.bindValue(btnServicoObserveSelectionObserveWidget, servicegetPapelGerServicoObserveValue, null, null);
		//
		IObservableValue btnCaixaObserveSelectionObserveWidget = SWTObservables.observeSelection(btnCaixa);
		IObservableValue servicegetPapelGerCaixaObserveValue = PojoObservables.observeValue(service.getPapel(), "gerCaixa");
		bindingContext.bindValue(btnCaixaObserveSelectionObserveWidget, servicegetPapelGerCaixaObserveValue, null, null);
		//
		IObservableValue btnGerarRelatorioObserveSelectionObserveWidget = SWTObservables.observeSelection(btnGerarRelatorio);
		IObservableValue servicegetPapelGerarRelatorioObserveValue = PojoObservables.observeValue(service.getPapel(), "gerarRelatorio");
		bindingContext.bindValue(btnGerarRelatorioObserveSelectionObserveWidget, servicegetPapelGerarRelatorioObserveValue, null, null);
		//
		IObservableValue txtNomeObserveTextObserveWidget = SWTObservables.observeText(txtNome, SWT.Modify);
		IObservableValue servicegetPapelNomeObserveValue = PojoObservables.observeValue(service.getPapel(), "nome");
		bindingContext.bindValue(txtNomeObserveTextObserveWidget, servicegetPapelNomeObserveValue, null, null);
		//
		return bindingContext;
	}

}
