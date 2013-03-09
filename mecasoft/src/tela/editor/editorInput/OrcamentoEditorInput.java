package tela.editor.editorInput;

import banco.modelo.Orcamento;

public class OrcamentoEditorInput extends MecasoftEditorInput{

	private Orcamento orcamento;
	
	public OrcamentoEditorInput(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	
	public OrcamentoEditorInput() {
		this.orcamento = new Orcamento();
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	
}
