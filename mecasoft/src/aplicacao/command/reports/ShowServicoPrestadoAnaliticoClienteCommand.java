package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
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

import tela.dialog.ParametroRelatorioServicoAnaliticoClienteDialog;
import aplicacao.command.ReportCommand;
import aplicacao.helper.FileHelper;
import aplicacao.helper.ReportHelper;
import aplicacao.helper.UsuarioHelper;
import banco.modelo.Configuracao;
import banco.modelo.ItemServico;
import banco.modelo.ServicoPrestado;
import banco.modelo.report.ServicoOrcamentoAnaliticoCliente;

public class ShowServicoPrestadoAnaliticoClienteCommand extends ReportCommand{

	private boolean openConfirmation;
	private ServicoPrestado servico;
	private ParametroRelatorioServicoAnaliticoClienteDialog prsacd;

	public ShowServicoPrestadoAnaliticoClienteCommand(boolean openConfirmation, ServicoPrestado servico) {
		this.openConfirmation = openConfirmation;
		this.servico = servico;
	}
	
	public ShowServicoPrestadoAnaliticoClienteCommand() {
		this.openConfirmation = true;
		servico = null;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prsacd = new ParametroRelatorioServicoAnaliticoClienteDialog(getActiveShell());
		
		if(openConfirmation)
			if(prsacd.open() != IDialogConstants.OK_ID)
				return null;
		
		servico = openConfirmation ? prsacd.getServico() : servico;

		if (servico == null || servico.getListaServicos().isEmpty()) {
			openWarning("N�o h� informa��es para mostrar no relat�rio.");
			return null;
		}

		List<ServicoOrcamentoAnaliticoCliente> listaLinha = gerarLinhaRelatorio(servico);
		JasperPrint jPrint = getReport(ReportHelper.SERVICO_ANALITICO_CLIENTE, listaLinha);

		if (!jPrint.getPages().isEmpty())
			getView().setReport(jPrint, "Relat�rio de servi�os (Analitico)");
		else
			openError("N�o h� informa��es suficientes para gerar o relat�rio.");	
			
		
		return null;
	}
	
	public List<ServicoOrcamentoAnaliticoCliente> gerarLinhaRelatorio(ServicoPrestado servicoPrestado){
		List<ServicoOrcamentoAnaliticoCliente> listaLinha = new ArrayList<ServicoOrcamentoAnaliticoCliente>();
		
		for(ItemServico servico : servicoPrestado.getListaServicos()){
			//servico
			ServicoOrcamentoAnaliticoCliente linhaServico = new ServicoOrcamentoAnaliticoCliente();
			linhaServico.setDescricao(servico.getDescricao());
			linhaServico.setValorTotal(servico.getTotal());
			linhaServico.setDataAbertura(servico.getServicoPrestado().getDataAbertura());
			linhaServico.setDataFechamento(servico.getServicoPrestado().getDataFechamento());
			linhaServico.setServicoExecutado(!servicoPrestado.isEmExecucao());
			linhaServico.setVeiculo(servicoPrestado.getVeiculo());
			linhaServico.setIss(servicoPrestado.getIss());
			listaLinha.add(linhaServico);
			
			if(servico.getServicoPrestado().getOrcamento() != null)
				linhaServico.setNumeroOrcamento(servico.getServicoPrestado().getOrcamento().getNumero());
			
			//produtos do servico
			for(ItemServico produto : servico.getListaItem()){
				
				if(!produto.isFornecedorVisivel())
					linhaServico.setValorTotal(linhaServico.getValorTotal().add(produto.getTotal()));
				else{
					ServicoOrcamentoAnaliticoCliente linhaProduto = new ServicoOrcamentoAnaliticoCliente();
					linhaProduto.setDescricao(produto.getDescricao());
					linhaProduto.setValorTotal(produto.getTotal());
					linhaProduto.setDataAbertura(servicoPrestado.getDataAbertura());
					linhaProduto.setDataFechamento(servicoPrestado.getDataFechamento());
					linhaProduto.setServicoExecutado(!servicoPrestado.isEmExecucao());
					linhaProduto.setVeiculo(servicoPrestado.getVeiculo());
					linhaProduto.setIss(servicoPrestado.getIss());
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
