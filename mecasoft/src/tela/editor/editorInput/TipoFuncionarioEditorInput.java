package tela.editor.editorInput;

import banco.modelo.TipoFuncionario;

public class TipoFuncionarioEditorInput extends MecasoftEditorInput{

	private TipoFuncionario tipo;
	
	public TipoFuncionarioEditorInput() {
		tipo = new TipoFuncionario();
	}
	
	public TipoFuncionarioEditorInput(TipoFuncionario tipo){
		this.tipo = tipo;
	}

	public TipoFuncionario getTipo() {
		return tipo;
	}

	public void setTipo(TipoFuncionario tipo) {
		this.tipo = tipo;
	}
	
}
