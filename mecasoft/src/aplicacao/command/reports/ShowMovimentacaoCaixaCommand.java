package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioMovimentacaoCaixaDialog;

import aplicacao.command.ReportCommand;
import aplicacao.helper.ReportHelper;

public class ShowMovimentacaoCaixaCommand extends ReportCommand{

	private ParametroRelatorioMovimentacaoCaixaDialog prmcd;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		prmcd = new ParametroRelatorioMovimentacaoCaixaDialog(getActiveShell());
		if(prmcd.open() == IDialogConstants.OK_ID){
			JasperPrint jPrint = getReport(ReportHelper.MOVIMENTACAO_CAIXA);
			
			if(!jPrint.getPages().isEmpty())
				getView().setReport(jPrint, "Relatório de movimentação de caixa");
			else
				openError("Não há movimentações disponíveis para mostrar no relatório.");
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getParametros() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("CAIXA_NUMERO", prmcd.getNumeroCaixa());
		param.put("MOVIMENTACAO_NUMERO", prmcd.getNumeroMovimentacao());
		param.put("DATA_INICIAL", prmcd.getDtInicial());
		param.put("DATA_FINAL", prmcd.getDtFinal());
		param.put("VALOR_INICIAL", prmcd.getValorInicial());
		param.put("VALOR_FINAL", prmcd.getValorFinal());
		param.put("TIPO", prmcd.getTipo());
		return param;
	}

}
