package aplicacao.service;

import java.util.List;

import banco.modelo.Pessoa;
import banco.modelo.TipoFuncionario;
import banco.modelo.Usuario;
import banco.utils.PessoaUtils;

public class PessoaService extends MecasoftService<Pessoa>{

	@Override
	public PessoaUtils getDAO() {
		return getInjector().getInstance(PessoaUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(modelo);
	}

	@Override
	public void delete() {
		getDAO().delete(modelo);
	}
	
	public Pessoa find(Long id){
		return getDAO().find(id);
	}
	
	public List<Pessoa> findAll(){
		return getDAO().findAll();
	}
	
	public List<Pessoa> findAllByTipoFuncionario(TipoFuncionario tipo){
		return getDAO().findAllByTipoFuncionario(tipo);
	}
	
	public List<Pessoa> findAllClientesAtivos(){
		return getDAO().findAllByTipoAndStatus(Pessoa.CLIENTE, true);
	}
	
	public List<Pessoa> findAllFornecedoresAtivos(){
		return getDAO().findAllByTipoAndStatus(Pessoa.FORNECEDOR, true);
	}
	
	public List<Pessoa> findAllFuncionariosAtivos(){
		return getDAO().findAllByTipoAndStatus(Pessoa.FUNCIONARIO, true);
	}
	
	public List<Pessoa> findAllAtivosSemUsuario(){
		List<Pessoa> pessoas = getDAO().findAllByTipoAndStatus(Pessoa.FUNCIONARIO, true);
		List<Usuario> usuarios = new UsuarioService().findAllAtivos();
		
		for(Usuario u : usuarios)
			pessoas.remove(u.getFuncionario());
		
		return pessoas;
	}
	
	public List<Pessoa> findAllFuncionarioAPagar(){
		return getDAO().findAllFuncionarioAPagar();
	}
	
	public List<Pessoa> findAllAtivos(){
		return getDAO().findAllByTipoAndStatus("", true);
	}

	public Pessoa getPessoa() {
		return getModelo();
	}

	public void setPessoa(Pessoa pessoa) {
		setModelo(pessoa);
	}


}
