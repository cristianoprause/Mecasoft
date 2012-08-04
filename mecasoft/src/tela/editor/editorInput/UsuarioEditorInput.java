package tela.editor.editorInput;

import banco.modelo.Usuario;

public class UsuarioEditorInput extends MecasoftEditorInput{

	private Usuario usuario;
	
	public UsuarioEditorInput(){
		usuario = new Usuario();
	}
	
	public UsuarioEditorInput(Usuario usuario){
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
