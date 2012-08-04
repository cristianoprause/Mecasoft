package tela.editor.editorInput;

import banco.modelo.TipoVeiculo;

public class TipoVeiculoEditorInput extends MecasoftEditorInput{

	private TipoVeiculo tipo;
	
	public TipoVeiculoEditorInput() {
		this.tipo = new TipoVeiculo();
	}
	
	public TipoVeiculoEditorInput(TipoVeiculo tipo){
		this.tipo = tipo;
	}

	public TipoVeiculo getTipo() {
		return tipo;
	}

	public void setTipo(TipoVeiculo tipo) {
		this.tipo = tipo;
	}
	
}
