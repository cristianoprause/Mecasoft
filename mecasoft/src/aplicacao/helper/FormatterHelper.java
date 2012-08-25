package aplicacao.helper;

import java.text.DecimalFormat;
import java.util.Locale;


public class FormatterHelper {

	public static Locale BRAZIL = new Locale("pt", "BR");
	private static DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(BRAZIL);
	
	public static DecimalFormat getDecimalFormat(){
		decimalFormat.setMinimumFractionDigits(2);
		return decimalFormat;
	}
	
}
