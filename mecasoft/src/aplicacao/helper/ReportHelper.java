package aplicacao.helper;

public class ReportHelper {

	public static String getReport(String nome){
		return nome.concat(".jasper");
	}
	
	public static String SERVICO_SINTETICO = getReport("servicoSintetico");
	
}
