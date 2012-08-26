package aplicacao.helper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class FormatterHelper {

	public static Locale BRAZIL = new Locale("pt", "BR");
	public static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	private static DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(BRAZIL);
	
	public static DecimalFormat getDecimalFormat(){
		decimalFormat.setMinimumFractionDigits(2);
		return decimalFormat;
	}
	
}
