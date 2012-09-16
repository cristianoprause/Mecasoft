package aplicacao.helper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class FormatterHelper {

	public static Locale BRAZIL = new Locale("pt", "BR");
	public static SimpleDateFormat DATEFORMATDATA = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat DATEFORMATDATAHORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static SimpleDateFormat DATEFOTMATHORA = new SimpleDateFormat("HH:mm");
	private static DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(BRAZIL);
	
	public static String MECASOFTTXTCPF = "..-";
	public static String MECASOFTTXTCNPJ = "../-";
	public static String MECASOFTTXTRG = ".. ";
	public static String MECASOFTTXTINSCRICAOESTADUAL = "...";
	public static String MECASOFTTXTSERIECARTEIRATRABALHO = "-";
	public static String MECASOFTTXTMOEDA = ",";
	public static String MECASOFTTXTTELEFONE = "() -";
	public static String MECASOFTTXTCEP = "-";
	public static String MECASOFTTXTDATA = "//";
	public static String MECASOFTTXTHORA = ":";
	
	
	public static DecimalFormat getDecimalFormat(){
		decimalFormat.setMinimumFractionDigits(2);
		return decimalFormat;
	}
	
}
