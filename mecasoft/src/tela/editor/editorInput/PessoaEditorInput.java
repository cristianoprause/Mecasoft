package tela.editor.editorInput;

import banco.modelo.Funcionario;

public class PessoaEditorInput extends MecasoftEditorInput{

	private Funcionario pessoa;
	
	public PessoaEditorInput() {
		pessoa = new Funcionario();
	}
	
	public PessoaEditorInput(Funcionario pessoa){
		this.pessoa = pessoa;
	}

	public Funcionario getPessoa() {
		return pessoa;
	}

	public void setPessoa(Funcionario pessoa) {
		this.pessoa = pessoa;
	}
	
}
