package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioServicoAnaliticoDialog;
import aplicacao.command.ReportCommand;
import aplicacao.helper.ReportHelper;

public class ShowServicoAnaliticoCommand extends ReportCommand{

	private ParametroRelatorioServicoAnaliticoDialog prsad;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		prsad = new ParametroRelatorioServicoAnaliticoDialog(getActiveShell());
		if(prsad.open() == IDialogConstants.OK_ID){
		
			JasperPrint jPrint = getReport(ReportHelper.SERVICO_ANALITICO);
			
			if(!jPrint.getPages().isEmpty())
				getView().setReport(jPrint, "Relatório de serviços (analítico)");
			else
				openError("Não há serviços disponíveis para mostrar no relatório.");
		
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("DATA_INICIAL", prsad.getDtInicial());
		param.put("DATA_FINAL", prsad.getDtFinal());
		param.put("NUMERO", prsad.getNumeroServico());
		param.put("CLIENTE_ID", prsad.getCliente() == null ? null : prsad.getCliente().getId());
		param.put("VEICULO_ID", prsad.getVeiculo() == null ? null : prsad.getVeiculo().getId());
		param.put("PRODUTOSERVICO_ID", prsad.getProdutoServico() == null ? null : prsad.getProdutoServico().getId());
		param.put("VALOR_INICIAL", prsad.getValorInicial());
		param.put("VALOR_FINAL", prsad.getValorFinal());
		
		return param;
	}

}
