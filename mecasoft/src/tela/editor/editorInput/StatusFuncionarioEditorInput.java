package tela.editor.editorInput;

import banco.modelo.Pessoa;

public class StatusFuncionarioEditorInput extends MecasoftEditorInput{

	private Pessoa funcionario;
	
	public StatusFuncionarioEditorInput(Pessoa funcionario) {
		this.funcionario = funcionario;
	}
	
	public StatusFuncionarioEditorInput() {
		this.funcionario = new Pessoa();
	}

	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}
	
}
