package tela.editor.editorInput;

import banco.modelo.ServicoPrestado;

public class AbrirOrdemServicoEditorInput extends MecasoftEditorInput{

	private ServicoPrestado servicoPrestado;
	
	public AbrirOrdemServicoEditorInput(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}
	
	public AbrirOrdemServicoEditorInput() {
		this.servicoPrestado = new ServicoPrestado();
	}

	public ServicoPrestado getServicoPrestado() {
		return servicoPrestado;
	}

	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}
	
}
