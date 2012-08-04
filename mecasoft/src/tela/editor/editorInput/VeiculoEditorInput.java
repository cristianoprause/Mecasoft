package tela.editor.editorInput;

import banco.modelo.Veiculo;

public class VeiculoEditorInput extends MecasoftEditorInput{

	private Veiculo veiculo;
	
	public VeiculoEditorInput() {
		veiculo = new Veiculo();
	}
	
	public VeiculoEditorInput(Veiculo veiculo){
		this.veiculo = veiculo;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
}
