package tela.editor.editorInput;

import aplicacao.service.FuncionarioService;
import banco.modelo.Veiculo;

public class VeiculoEditorInput extends MecasoftEditorInput{

	private Veiculo veiculo;
	private FuncionarioService funcionarioService;
	
	public VeiculoEditorInput() {
		veiculo = new Veiculo();
		funcionarioService = null;
	}
	
	public VeiculoEditorInput(Veiculo veiculo){
		this.veiculo = veiculo;
		this.funcionarioService = null;
	}
	
	public VeiculoEditorInput(FuncionarioService funcionarioService){
		this.veiculo = new Veiculo();
		this.funcionarioService = funcionarioService;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}
	
}
