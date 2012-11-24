package aplicacao.helper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import banco.modelo.StatusServico;

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
		dateFormatData = new SimpleDateFormat("dd/MM/yyyy");
		dateFormatData.setLenient(false);
		return dateFormatData;
	}

	public static SimpleDateFormat getDateFormatData(String pattern) {
		dateFormatData = new SimpleDateFormat(pattern);
		dateFormatData.setLenient(false);
		return dateFormatData;
	}

	public static String formatarTempo(Calendar calendarTempo) {
		String tempo = "";
		int dias = calendarTempo.get(Calendar.DAY_OF_MONTH) - 1;

		// calcula o tempo em dias para de pois calcular em horas (caso tenha mais de 1 ano)
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
		
		//é verificado se esta em AM ou PM porque quando era PM e davam 17H, ele retornava 5
		if(calendarTempo.get(Calendar.AM_PM) == Calendar.PM)
			horas += 12;

		tempo = tempo.concat(horas + " horas, ");
		tempo = tempo.concat(calendarTempo.get(Calendar.MINUTE) + " minutos, ");
		tempo = tempo.concat(calendarTempo.get(Calendar.SECOND) + " segundos.");

		return tempo;
	}

	public static Calendar calculaPeriodoStatusServico(
			List<StatusServico> listaStatus) {

		// tempo total
		Calendar tempoTotal = Calendar.getInstance();
		tempoTotal.set(1, 1, 1, 0, 0, 0);

		// calendar para calcular o periodo entre o status de iniciado e parado
		// começa nullo para saber no loop se ja foi encontrado algum status que
		// inicie o servico
		Calendar dtInicial = null;
		for (StatusServico status : listaStatus) {

			// pega a data do primeiro status inicial caso ainda não tenha pego
			if (dtInicial == null && !status.getStatus().isPausar()) {
				dtInicial = Calendar.getInstance();
				dtInicial.setTime(status.getData());
			}

			// ja tem um status inicial e encontrou um status final, agora pode
			// calcular o periodo
			else if (dtInicial != null && status.getStatus().isPausar()) {
				Calendar periodo = Calendar.getInstance();
				periodo.setTime(status.getData());

				// retira da data final a data inicial para sobrar apenas o
				// periodo trabalhado
				periodo.add(Calendar.DAY_OF_MONTH,
						dtInicial.get(Calendar.DAY_OF_MONTH) * -1 + 1);
				periodo.add(Calendar.MONTH, dtInicial.get(Calendar.MONTH) * -1
						+ 1);
				periodo.add(Calendar.YEAR, dtInicial.get(Calendar.YEAR) * -1
						+ 1);
				periodo.add(Calendar.HOUR, dtInicial.get(Calendar.HOUR) * -1);
				periodo.add(Calendar.MINUTE, dtInicial.get(Calendar.MINUTE)
						* -1);
				periodo.add(Calendar.SECOND, dtInicial.get(Calendar.SECOND)
						* -1);

				// adiciona ao total
				tempoTotal.add(Calendar.DAY_OF_MONTH,
						periodo.get(Calendar.DAY_OF_MONTH) - 1);
				tempoTotal.add(Calendar.MONTH, periodo.get(Calendar.MONTH) - 1);
				tempoTotal.add(Calendar.HOUR, periodo.get(Calendar.HOUR));
				tempoTotal.add(Calendar.MINUTE, periodo.get(Calendar.MINUTE));
				tempoTotal.add(Calendar.SECOND, periodo.get(Calendar.SECOND));

				// deixa o dtInicial como nulo para pegar o proximo periodo
				// inicial
				dtInicial = null;

			}

		}

		// caso o ultimo status nao seja o final deve ser calculado o periodo
		// entre este status e a data atual
		if (dtInicial != null) {
			Calendar periodo = Calendar.getInstance();

			// retira da data final a data inicial para sobrar apenas o periodo
			// trabalhado
			periodo.add(Calendar.DAY_OF_MONTH,
					dtInicial.get(Calendar.DAY_OF_MONTH) * -1 + 1);
			periodo.add(Calendar.MONTH, dtInicial.get(Calendar.MONTH) * -1 + 1);
			periodo.add(Calendar.YEAR, dtInicial.get(Calendar.YEAR) * -1 + 1);
			periodo.add(Calendar.HOUR, dtInicial.get(Calendar.HOUR) * -1);
			periodo.add(Calendar.MINUTE, dtInicial.get(Calendar.MINUTE) * -1);
			periodo.add(Calendar.SECOND, dtInicial.get(Calendar.SECOND) * -1);

			// adiciona ao total
			tempoTotal.add(Calendar.MONTH, periodo.get(Calendar.MONTH) - 1);
			tempoTotal.add(Calendar.DAY_OF_MONTH,
					periodo.get(Calendar.DAY_OF_MONTH) - 1);
			tempoTotal.add(Calendar.HOUR, periodo.get(Calendar.HOUR));
			tempoTotal.add(Calendar.MINUTE, periodo.get(Calendar.MINUTE));
			tempoTotal.add(Calendar.SECOND, periodo.get(Calendar.SECOND));
		}

		return tempoTotal;
	}

}
