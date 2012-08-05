package aplicacao.service;

import java.util.List;

import banco.modelo.Pessoa;
import banco.utils.PessoaUtils;

public class PessoaService extends MecasoftService<Pessoa>{

	private Pessoa pessoa;
	
	@Override
	public PessoaUtils getDAO() {
		return getInjector().getInstance(PessoaUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(pessoa);
	}

	@Override
	public void delete() {
		getDAO().delete(pessoa);
	}
	
	public Pessoa find(Long id){
		return getDAO().find(id);
	}
	
	public List<Pessoa> findAll(){
		return getDAO().findAll();
	}
	
	public List<Pessoa> findAllAtivos(){
		return getDAO().findAllAtivos();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}


}
