package banco.utils;

import banco.modelo.Usuario;

public interface UsuarioUtils extends MecasoftUtils<Usuario>{

	Usuario findByLoginSenha(String login, String senha);
	
}
