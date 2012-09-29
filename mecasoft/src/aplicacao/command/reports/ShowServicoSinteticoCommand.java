package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioServicoSintetico;

import aplicacao.command.ReportCommand;
import aplicacao.helper.ReportHelper;

public class ShowServicoSinteticoCommand extends ReportCommand{

	private ParametroRelatorioServicoSintetico prss;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prss = new ParametroRelatorioServicoSintetico(getActiveShell());
		
		if(prss.open() == IDialogConstants.OK_ID){
		
			JasperPrint print = getReport(ReportHelper.SERVICO_SINTETICO, getParametros());
			
			if(!print.getPages().isEmpty())
				getView().setReport(print, "Relatório de serviços (sintético)");
			else
				openError("Não há serviços disponíveis para mostrar no relatório.");
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("NUMERO", prss.getNumeroServico());
		param.put("CLIENTE_ID", prss.getCliente() == null ? null : prss.getCliente().getId());
		param.put("VALOR_INICIAL", prss.getValorInicial());
		param.put("VALOR_FINAL", prss.getValorFinal());
		
		return param;
	}

}
