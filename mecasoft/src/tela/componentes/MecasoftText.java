package tela.componentes;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;

public class MecasoftText extends Composite {

	public static Integer AMBOS = -1;
	public static Integer NUMEROS = 0;
	public static Integer LETRAS = 1;

	private boolean passou;
	private Integer max;
	private Integer aceita;
	private String caracteres;
	private Integer[] posicoes;
	private String texto;
	private String textoRetorno;

	public Text text;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MecasoftText(Composite parent, int style) {
		super(parent, style);
		
		posicoes = new Integer[0];
		caracteres = "";
		aceita = AMBOS;
		max = -1;
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);

		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				passou = validar(e.keyCode);
				if (!passou)
					e.doit = false;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				formatar();
			}
		});

	}

	public void formatar() {
		if(passou){
			texto = text.getText();
			for(int c = 0; c < caracteres.length(); c++)
				texto = StringUtils.remove(texto, caracteres.charAt(c));
			
			textoRetorno = "";
			for(int c1 = 0; c1 < texto.length(); c1++){
				for(int c2 = 0; c2 < caracteres.length(); c2++){
					if(posicoes[c2].compareTo(c1) == 0)
						textoRetorno += caracteres.charAt(c2);
					
					else if(c1 == (texto.length() + posicoes[c2]))
						textoRetorno += caracteres.charAt(c2);
				}
				
				textoRetorno += texto.charAt(c1);
			}
			
			text.setText(textoRetorno);
			text.setSelection(textoRetorno.length());
			
		}
		
	}

	public boolean validar(int keyCode) {
		switch (keyCode) {
		case SWT.BS:
		case SWT.ARROW_UP:
		case SWT.ARROW_DOWN:
		case SWT.ARROW_LEFT:
		case SWT.ARROW_RIGHT:
		case SWT.DEL:
		case SWT.HOME:
		case SWT.END:
			return true;
		}

		if ((text.getText().length() < max) || (max == -1)) {
			if (aceita.equals(AMBOS))
				return true;

			if (aceita.equals(LETRAS) && (keyCode >= 97 && keyCode <= 122))
				return true;

			if (aceita.equals(NUMEROS)
					&& ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 16777225 && keyCode <= 16777221)))
				return true;

		}

		return false;
	}

	public void setOptions(Integer aceita, Integer max) {
		this.aceita = aceita;
		this.max = max;
	}

	public void addChars(String caracteres, Integer posicoes[]) {
		this.caracteres = caracteres;
		this.posicoes = posicoes;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
	}
	
	@Override
	public boolean getEnabled() {
		return text.getEnabled();
	}
	
	public void setEditable(boolean editable){
		text.setEditable(editable);
	}
	
	public boolean getEditable(boolean editable){
		return text.getEditable();
	}

}