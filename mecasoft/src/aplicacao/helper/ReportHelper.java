package aplicacao.helper;

public class ReportHelper {

	public static String getReport(String nome){
		return nome.concat(".jasper");
	}
	
	public static String SERVICO_SINTETICO = getReport("servicoSintetico");
	public static String SERVICO_ANALITICO = getReport("servicoAnalitico");
	public static String DUPLICATA = getReport("duplicatas");
	public static String MOVIMENTACAO_CAIXA = getReport("movimentacaoCaixa");
	public static String LIVRO_CAIXA = getReport("livroCaixa");
	public static String SERVICO_ANALITICO_CLIENTE = getReport("servicoAnaliticoCliente");
	public static String ORCAMENTO_ANALITICO_CLIENTE = getReport("orcamentoAnaliticoCliente");
	
}
