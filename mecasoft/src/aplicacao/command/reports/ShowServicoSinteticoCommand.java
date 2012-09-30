package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioServicoSinteticoDialog;

import aplicacao.command.ReportCommand;
import aplicacao.helper.ReportHelper;

public class ShowServicoSinteticoCommand extends ReportCommand{

	private ParametroRelatorioServicoSinteticoDialog prssd;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prssd = new ParametroRelatorioServicoSinteticoDialog(getActiveShell());
		
		if(prssd.open() == IDialogConstants.OK_ID){
		
			JasperPrint print = getReport(ReportHelper.SERVICO_SINTETICO);
			
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
		
		param.put("NUMERO", prssd.getNumeroServico());
		param.put("CLIENTE_ID", prssd.getCliente() == null ? null : prssd.getCliente().getId());
		param.put("VALOR_INICIAL", prssd.getValorInicial());
		param.put("VALOR_FINAL", prssd.getValorFinal());
		
		return param;
	}

}
