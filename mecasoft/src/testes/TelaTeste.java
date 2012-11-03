package testes;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import tela.componentes.MecasoftText;


public class TelaTeste {

	protected Shell shell;
	private MecasoftText text;
	private Calendar dtInicial;
	private Calendar periodo;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TelaTeste window = new TelaTeste();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		text = new MecasoftText(shell, SWT.BORDER);
		text.setLayout(new GridLayout(1, false));
		
		Button btnCalcularHora = new Button(text, SWT.NONE);
		btnCalcularHora.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(dtInicial == null)
					dtInicial = Calendar.getInstance();
					
				periodo = Calendar.getInstance();
				
				periodo.add(Calendar.DAY_OF_MONTH, dtInicial.get(Calendar.DAY_OF_MONTH) * -1 + 1);
				periodo.add(Calendar.MONTH, dtInicial.get(Calendar.MONTH) * -1 + 1);
				periodo.add(Calendar.YEAR, dtInicial.get(Calendar.YEAR) * -1 + 1);
				periodo.add(Calendar.HOUR, dtInicial.get(Calendar.HOUR) * -1);
				periodo.add(Calendar.MINUTE, dtInicial.get(Calendar.MINUTE) * -1);
				periodo.add(Calendar.SECOND, dtInicial.get(Calendar.SECOND) * -1);
				
				//formatar texto
				String tempo = "";

				if((periodo.get(Calendar.MONTH) - 1) > 0)
					tempo = tempo.concat((periodo.get(Calendar.MONTH) - 1) + " mês, ");
				
				if((periodo.get(Calendar.DAY_OF_MONTH) - 1) > 0)
					tempo = tempo.concat((periodo.get(Calendar.DAY_OF_MONTH) - 1) + " dias, ");
				
				tempo = tempo.concat(periodo.get(Calendar.HOUR) + " horas, ");
				tempo = tempo.concat(periodo.get(Calendar.MINUTE) + " minutos, ");
				tempo = tempo.concat(periodo.get(Calendar.SECOND) + " segundos.");
				
				text.setText(tempo);
			}
		});
		btnCalcularHora.setText("Calcular hora");

	}

}
