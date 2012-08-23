package banco.utils;

import java.util.List;

import banco.modelo.Usuario;

public interface UsuarioUtils extends MecasoftUtils<Usuario>{

	Usuario findByLoginSenha(String login, String senha);
	List<Usuario> findAllByStatus(boolean status);
	
}
