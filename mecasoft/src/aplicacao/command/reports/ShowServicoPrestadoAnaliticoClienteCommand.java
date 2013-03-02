package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;
import static aplicacao.helper.MessageHelper.openWarning;

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
import aplicacao.service.ServicoPrestadoService;
import banco.modelo.ItemServico;
import banco.modelo.ServicoPrestado;
import banco.modelo.report.ServicoPrestadoAnaliticoCliente;

public class ShowServicoPrestadoAnaliticoClienteCommand extends ReportCommand{

	private ParametroRelatorioServicoAnaliticoClienteDialog prsacd;
	private ServicoPrestadoService service = new ServicoPrestadoService();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prsacd = new ParametroRelatorioServicoAnaliticoClienteDialog(getActiveShell());
		if(prsacd.open() == IDialogConstants.OK_ID){
			ServicoPrestado servico = service.find(prsacd.getNumero());
			
			if(servico == null || servico.getListaServicos().isEmpty()){
				openWarning("Não há informações para mostrar no relatório.");
				return null;
			}
			
			List<ServicoPrestadoAnaliticoCliente> listaLinha = gerarLinhaRelatorio(servico);
			JasperPrint jPrint = getReport(ReportHelper.SERVICO_ANALITICO_CLIENTE, listaLinha);
			
			if(!jPrint.getPages().isEmpty())
				getView().setReport(jPrint, "Relatório de serviços (Analitico)");
			else
				openError("Não há informações suficientes para gerar o relatório.");	
			
		}
		
		return null;
	}
	
	public List<ServicoPrestadoAnaliticoCliente> gerarLinhaRelatorio(ServicoPrestado servicoPrestado){
		List<ServicoPrestadoAnaliticoCliente> listaLinha = new ArrayList<ServicoPrestadoAnaliticoCliente>();
		
		for(ItemServico servico : servicoPrestado.getListaServicos()){
			//servico
			ServicoPrestadoAnaliticoCliente linhaServico = new ServicoPrestadoAnaliticoCliente();
			linhaServico.setDescricao(servico.getDescricao());
			linhaServico.setValorTotal(servico.getTotal());
			linhaServico.setDataAbertura(servico.getServicoPrestado().getDataAbertura());
			linhaServico.setDataFechamento(servico.getServicoPrestado().getDataFechamento());
			linhaServico.setServicoExecutado(!servicoPrestado.isEmExecucao());
			linhaServico.setVeiculo(servicoPrestado.getVeiculo());
			listaLinha.add(linhaServico);
			
			//produtos do servico
			for(ItemServico produto : servico.getListaItem()){
				
				if(!produto.isFornecedorVisivel())
					linhaServico.setValorTotal(linhaServico.getValorTotal().add(produto.getTotal()));
				else{
					ServicoPrestadoAnaliticoCliente linhaProduto = new ServicoPrestadoAnaliticoCliente();
					linhaProduto.setDescricao(produto.getDescricao());
					linhaProduto.setValorTotal(produto.getTotal());
					linhaProduto.setDataAbertura(servicoPrestado.getDataAbertura());
					linhaProduto.setDataFechamento(servicoPrestado.getDataFechamento());
					linhaProduto.setServicoExecutado(!servicoPrestado.isEmExecucao());
					linhaProduto.setVeiculo(servicoPrestado.getVeiculo());
					
					listaLinha.add(linhaProduto);
				}
				
			}
		}
		
		return listaLinha;
	}
	
	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("CAMINHO_IMAGEM", FileHelper.caminhoLogoEmpresa().isEmpty() ? null : StringUtils.removeStart(FileHelper.caminhoLogoEmpresa(), "/"));
		return param;
	}

}
