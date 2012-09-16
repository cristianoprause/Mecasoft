package aplicacao.helper;

import banco.modelo.Configuracao;
import banco.modelo.Usuario;

public class UsuarioHelper {

	private static Usuario usuarioLogado;
	private static Configuracao configuracaoPadrao;
	
	public static Usuario getUsuarioLogado(){
		return usuarioLogado;
	}
	
	public static void setUsuario(Usuario usuario){
		usuarioLogado = usuario;
	}

	public static Configuracao getConfiguracaoPadrao() {
		return configuracaoPadrao;
	}

	public static void setConfiguracaoPadrao(Configuracao configuracao) {
		configuracaoPadrao = configuracao;
	}
	
}
