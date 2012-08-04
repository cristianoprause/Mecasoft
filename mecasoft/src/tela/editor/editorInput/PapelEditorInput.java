package tela.editor.editorInput;

import banco.modelo.Papel;


public class PapelEditorInput extends MecasoftEditorInput{

	private Papel papel;
	
	public PapelEditorInput() {
		this.papel = new Papel();
	}
	
	public PapelEditorInput(Papel papel){
		this.papel = papel;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}
	
}
