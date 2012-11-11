package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioLivroCaixaDialog;
import aplicacao.command.ReportCommand;
import aplicacao.helper.ReportHelper;

public class ShowLivroCaixaCommand extends ReportCommand{

	private ParametroRelatorioLivroCaixaDialog prlcd;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prlcd = new ParametroRelatorioLivroCaixaDialog(getActiveShell());
		
		if(prlcd.open() == IDialogConstants.OK_ID){
			JasperPrint jPrint = getReport(ReportHelper.LIVRO_CAIXA);
			
			if(!jPrint.getPages().isEmpty())
				getView().setReport(jPrint, "Relatório livro de caixa");
			else
				openError("Não há movimentações disponíveis para mostrar no relatório.");
		}
		return null;
	}

	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("CAIXA_NUMERO", prlcd.getNumeroCaixa());
		param.put("DATA_INICIAL", prlcd.getDtInicial());
		param.put("DATA_FINAL", prlcd.getDtFinal());
		param.put("TIPO_MOVIMENTACAO", prlcd.getTipo() == null ? null : prlcd.getTipo() + "");
		param.put("VALOR_ABERTURA_INICIAL", prlcd.getValorAberturaInicial());
		param.put("VALOR_ABERTURA_FINAL", prlcd.getValorAberturaFinal());
		param.put("VALOR_FECHAMENTO_INICIAL", prlcd.getValorFechamentoInicial());
		param.put("VALOR_FECHAMENTO_FINAL", prlcd.getValorFechamentoFinal());
		
		return param;
	}

}
