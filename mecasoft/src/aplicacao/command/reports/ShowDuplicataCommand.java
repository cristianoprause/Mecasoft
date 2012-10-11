package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioDuplicataDialog;

import aplicacao.command.ReportCommand;
import aplicacao.helper.ReportHelper;

public class ShowDuplicataCommand extends ReportCommand{
	
	private ParametroRelatorioDuplicataDialog prdd;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prdd = new ParametroRelatorioDuplicataDialog(getActiveShell());
		
		if(prdd.open() == IDialogConstants.OK_ID){
			JasperPrint jPrint = getReport(ReportHelper.DUPLICATA);
			
			if(!jPrint.getPages().isEmpty())
				getView().setReport(jPrint, "Relatório de duplicatas");
			else
				openError("Não há duplicatas disponíveis para mostrar no relatório.");
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("DATA_INICIAL", prdd.getDtInicial());
		param.put("DATA_FINAL", prdd.getDtFinal());
		param.put("CLIENTE_ID", prdd.getCliente() == null ? null : prdd.getCliente().getId());
		param.put("FUNCIONARIO_ID", prdd.getFuncionario() == null ? null : prdd.getFuncionario().getId());
		param.put("SERVICO_ID", prdd.getNumeroServico());
		param.put("PAGO", prdd.isPago());
		
		return param;
	}

}
