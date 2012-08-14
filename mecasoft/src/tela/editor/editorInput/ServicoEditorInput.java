package tela.editor.editorInput;

import banco.modelo.ProdutoServico;

public class ServicoEditorInput extends MecasoftEditorInput{

	private ProdutoServico produtoServico;
	
	public ServicoEditorInput(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
	
	public ServicoEditorInput() {
		this.produtoServico = new ProdutoServico();
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
	
}
