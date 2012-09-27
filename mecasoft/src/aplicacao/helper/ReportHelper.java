package aplicacao.helper;

public class ReportHelper {

	public static String getReport(String nome){
		return "C:/Users/Cristiano/git/Mecasoft/mecasoft/reports/".concat(nome.concat(".jasper"));
	}
	
	public static String SERVICO_SINTETICO = getReport("servicoSintetico");
	
}
