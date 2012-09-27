package aplicacao.command.reports;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import aplicacao.command.ReportCommand;
import aplicacao.helper.ReportHelper;

public class ShowServicoSinteticoCommand extends ReportCommand{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		JasperPrint print = getReport(ReportHelper.SERVICO_SINTETICO, getParametros());
		
		if(print.getPages().isEmpty())
			System.out.println("PHEW!");
		else{
			getView().setReport(print);
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("NUMERO", null);
		param.put("CLIENTE_ID", null);
		param.put("VALOR_INICIAL", null);
		param.put("VALOR_FINAL", null);
		
		return param;
	}

}
