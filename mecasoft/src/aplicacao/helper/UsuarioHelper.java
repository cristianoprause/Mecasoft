package aplicacao.helper;

import banco.modelo.Usuario;

public class UsuarioHelper {

	private static Usuario usuarioLogado;
	
	public static Usuario getUsuarioLogado(){
		return usuarioLogado;
	}
	
	public static void setUsuario(Usuario usuario){
		usuarioLogado = usuario;
	}
	
}
