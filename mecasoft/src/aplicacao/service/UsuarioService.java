package aplicacao.service;

import java.util.List;

import banco.modelo.Usuario;
import banco.utils.UsuarioUtils;

public class UsuarioService extends MecasoftService<Usuario>{

	@Override
	public UsuarioUtils getDAO() {
		return getInjector().getInstance(UsuarioUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(modelo);
	}
	
	public void delete(){
		getDAO().delete(modelo);
	}
	
	public Usuario find(Long id){
		return getDAO().find(id);
	}
	
	public Usuario login(String login, String senha){
		return getDAO().findByLoginSenhaStatus(login, senha, true);
	}
	
	public List<Usuario> findByLogin(String login){
		return getDAO().findAllByLoginAndStatus(login, true);
	}
	
	public List<Usuario> findAll(){
		return getDAO().findAll();
	}
	
	public List<Usuario> findAllAtivos(){
		return getDAO().findAllByStatus(true);
	}

	public Usuario getUsuario() {
		return getModelo();
	}

	public void setUsuario(Usuario usuario) {
		setModelo(usuario);
	}
	
}
