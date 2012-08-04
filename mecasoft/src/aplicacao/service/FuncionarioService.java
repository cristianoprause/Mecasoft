package aplicacao.service;

import java.util.List;

import banco.modelo.Funcionario;
import banco.utils.FuncionarioUtils;

public class FuncionarioService extends MecasoftService<Funcionario>{

	private Funcionario funcionario;
	
	@Override
	public FuncionarioUtils getDAO() {
		return getInjector().getInstance(FuncionarioUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(funcionario);
	}
	
	public Funcionario find(Long id){
		return getDAO().find(id);
	}
	
	public List<Funcionario> findAll(){
		return getDAO().findAll();
	}
	
	public List<Funcionario> findAllSemUsuario(){
		return getDAO().findAllSemUsuario();
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

}
