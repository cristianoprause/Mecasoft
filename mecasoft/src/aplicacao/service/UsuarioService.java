package aplicacao.service;

import java.util.List;

import banco.modelo.Usuario;
import banco.utils.UsuarioUtils;

public class UsuarioService extends MecasoftService<Usuario>{

	private Usuario usuario;
	
	@Override
	public UsuarioUtils getDAO() {
		return getInjector().getInstance(UsuarioUtils.class);
	}
	
	public void saveOrUpdate(){
		getDAO().saveOrUpdate(usuario);
	}
	
	public void delete(){
		getDAO().delete(usuario);
	}
	
	public Usuario find(Long id){
		return getDAO().find(id);
	}
	
	public Usuario login(String login, String senha){
		return getDAO().findByLoginSenha(login, senha);
	}
	
	public List<Usuario> findAll(){
		return getDAO().findAll();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
