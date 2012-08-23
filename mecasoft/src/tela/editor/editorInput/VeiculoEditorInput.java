package tela.editor.editorInput;

import aplicacao.service.PessoaService;
import banco.modelo.Veiculo;

public class VeiculoEditorInput extends MecasoftEditorInput{

	private Veiculo veiculo;
	private PessoaService pessoaService;
	
	public VeiculoEditorInput() {
		veiculo = new Veiculo();
		pessoaService = null;
	}
	
	public VeiculoEditorInput(Veiculo veiculo){
		this.veiculo = veiculo;
		this.pessoaService = null;
	}
	
	public VeiculoEditorInput(PessoaService pessoaService){
		this.veiculo = new Veiculo();
		this.pessoaService = pessoaService;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public PessoaService getFuncionarioService() {
		return pessoaService;
	}

	public void setFuncionarioService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
}
