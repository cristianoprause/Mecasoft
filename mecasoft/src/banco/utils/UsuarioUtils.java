package banco.utils;

import java.util.List;

import banco.modelo.Usuario;

public interface UsuarioUtils extends MecasoftUtils<Usuario>{

	Usuario findByLoginSenhaStatus(String login, String senha, Boolean status);
	List<Usuario> findAllByStatus(boolean status);
	
}
