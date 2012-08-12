package tela.editor.editorInput;

import banco.modelo.ProdutoServico;

public class ProdutoEditorInput extends MecasoftEditorInput{

	private ProdutoServico produtoServico;
	
	public ProdutoEditorInput() {
		produtoServico = new ProdutoServico();
	}
	
	public ProdutoEditorInput(ProdutoServico produtoServico){
		this.produtoServico = produtoServico;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
	
}
