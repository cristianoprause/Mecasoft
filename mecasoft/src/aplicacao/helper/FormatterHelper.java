package aplicacao.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatterHelper {

	public static Locale BRAZIL = new Locale("pt", "BR");
	private static DecimalFormat decimalFormat = new DecimalFormat("###0.00");
	private static SimpleDateFormat dateFormatData;

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

	public static DecimalFormat getDecimalFormat() {
		decimalFormat.setMinimumFractionDigits(2);
		return decimalFormat;
	}

	public static SimpleDateFormat getDateFormatData() {
		return getDateFormatData("dd/MM/yyyy");
	}

	public static SimpleDateFormat getDateFormatData(String pattern) {
		dateFormatData = new SimpleDateFormat(pattern);
		dateFormatData.setLenient(false);
		return dateFormatData;
	}

	public static String formatarTempo(Calendar calendarTempo) {
		String tempo = "";
		int dias = calendarTempo.get(Calendar.DAY_OF_MONTH) - 1;

		// calcula o tempo em dias para de pois calcular em horas (caso tenha
		// mais de 1 ano)
		if ((calendarTempo.get(Calendar.YEAR) - 1) > 0) {
			int ano = calendarTempo.get(Calendar.YEAR) - 1;

			Calendar clTempo = Calendar.getInstance();

			for (; ano > 0; ano--) {
				clTempo.set(ano, 12, 31);
				dias += clTempo.get(Calendar.DAY_OF_YEAR);
			}

		}

		// calcula a hora pegando o tempo dos dias mais as horas
		int horas = dias * 24 + calendarTempo.get(Calendar.HOUR);

		// é verificado se esta em AM ou PM porque quando era PM e davam 17H,
		// ele retornava 5
		if (calendarTempo.get(Calendar.AM_PM) == Calendar.PM)
			horas += 12;

		tempo = tempo.concat(horas + " horas, ");
		tempo = tempo.concat(calendarTempo.get(Calendar.MINUTE) + " minutos, ");
		tempo = tempo.concat(calendarTempo.get(Calendar.SECOND) + " segundos.");

		return tempo;
	}

	// moeda
	public static String formatMoedaDuasCasas(BigDecimal valor) {
		return getDecimalFormat().format(valor);
	}

	// tempo
	public static String formatDataInvertida(Date data) {
		return getDateFormatData("yyyyMMdd").format(data);
	}

	public static String formatarData(Date data) {
		return getDateFormatData().format(data);
	}

}
