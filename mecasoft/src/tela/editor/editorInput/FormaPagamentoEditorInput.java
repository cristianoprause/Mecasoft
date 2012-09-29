package tela.editor.editorInput;

import banco.modelo.FormaPagamento;

public class FormaPagamentoEditorInput extends MecasoftEditorInput{

	private FormaPagamento forma;
	
	public FormaPagamentoEditorInput(FormaPagamento forma) {
		this.forma = forma;
	}
	
	public FormaPagamentoEditorInput() {
		this.forma = new FormaPagamento();
	}

	public FormaPagamento getForma() {
		return forma;
	}

	public void setForma(FormaPagamento forma) {
		this.forma = forma;
	}
	
}
