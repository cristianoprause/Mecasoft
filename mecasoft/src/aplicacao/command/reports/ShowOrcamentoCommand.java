package aplicacao.command.reports;

import static aplicacao.helper.MessageHelper.openError;
import static aplicacao.helper.MessageHelper.openWarning;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioOrcamentoDialog;
import aplicacao.command.ReportCommand;
import aplicacao.helper.FileHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.helper.ReportHelper;
import aplicacao.helper.UsuarioHelper;
import banco.modelo.Configuracao;
import banco.modelo.ItemServico;
import banco.modelo.Orcamento;
import banco.modelo.report.ServicoOrcamentoAnaliticoCliente;

public class ShowOrcamentoCommand extends ReportCommand{

	private Orcamento orcamento;
	private ParametroRelatorioOrcamentoDialog prod;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prod = new ParametroRelatorioOrcamentoDialog(LayoutHelper.getActiveShell());
		
		if(prod.open() != IDialogConstants.OK_ID)
			return null;
		
		orcamento = prod.getOrcamento();
		
		if (orcamento == null || orcamento.getListaServico().isEmpty()) {
			openWarning("Não há informações para mostrar no relatório.");
			return null;
		}
		
		List<ServicoOrcamentoAnaliticoCliente> listaLinha = gerarLinhaRelatorio(orcamento);
		JasperPrint jPrint = getReport(ReportHelper.ORCAMENTO_ANALITICO_CLIENTE, listaLinha);

		if (!jPrint.getPages().isEmpty())
			getView().setReport(jPrint, "Relatório de orçamento");
		else
			openError("Não há informações suficientes para gerar o relatório.");
		
		return null;
	}
	
	private List<ServicoOrcamentoAnaliticoCliente> gerarLinhaRelatorio(Orcamento orcamento){
		List<ServicoOrcamentoAnaliticoCliente> listaLinha = new ArrayList<ServicoOrcamentoAnaliticoCliente>();
		
		for(ItemServico servico : orcamento.getListaServico()){
			//orcamento
			ServicoOrcamentoAnaliticoCliente linhaServico = new ServicoOrcamentoAnaliticoCliente();
			linhaServico.setDescricao(servico.getDescricao());
			linhaServico.setValorTotal(servico.getTotal());
			linhaServico.setDataAbertura(orcamento.getDtLancamento());
			linhaServico.setVeiculo(orcamento.getVeiculo());
			listaLinha.add(linhaServico);
			linhaServico.setNumeroOrcamento(orcamento.getNumero());
			
			//produtos do orcamento
			for(ItemServico produto : servico.getListaItem()){
				
				if(!produto.isFornecedorVisivel())
					linhaServico.setValorTotal(linhaServico.getValorTotal().add(produto.getTotal()));
				else{
					ServicoOrcamentoAnaliticoCliente linhaProduto = new ServicoOrcamentoAnaliticoCliente();
					linhaProduto.setDescricao(produto.getDescricao());
					linhaProduto.setValorTotal(produto.getTotal());
					linhaProduto.setDataAbertura(orcamento.getDtLancamento());
					linhaProduto.setVeiculo(orcamento.getVeiculo());
					linhaProduto.setNumeroOrcamento(linhaServico.getNumeroOrcamento());
					
					listaLinha.add(linhaProduto);
				}
				
			}
		}
		
		return listaLinha;
	}
	
	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		
		String caminho = null;
		Configuracao configuracao = UsuarioHelper.getConfiguracaoPadrao();
		if(configuracao != null && configuracao.getLogoEmpresa() != null && !configuracao.getLogoEmpresa().isEmpty()){
			File file = new File(StringUtils.removeStart(FileHelper.logoPath(), "/"));
			if(file.exists())
				caminho = file.getPath();
		}
		
		param.put("CAMINHO_IMAGEM", caminho);
		return param;
	}

}
