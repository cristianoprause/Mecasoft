package tela.editor.editorInput;

import banco.modelo.Pessoa;

public class PessoaEditorInput extends MecasoftEditorInput{

	private Pessoa pessoa;
	
	public PessoaEditorInput() {
		pessoa = new Pessoa();
	}
	
	public PessoaEditorInput(Pessoa pessoa){
		this.pessoa = pessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
}
