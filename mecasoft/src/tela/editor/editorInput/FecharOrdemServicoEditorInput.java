package tela.editor.editorInput;

import banco.modelo.ServicoPrestado;

public class FecharOrdemServicoEditorInput extends MecasoftEditorInput{
	
	private ServicoPrestado servicoPrestado;
	
	public FecharOrdemServicoEditorInput(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}
	
	public FecharOrdemServicoEditorInput() {
		this.servicoPrestado = new ServicoPrestado();
	}

	public ServicoPrestado getServicoPrestado() {
		return servicoPrestado;
	}

	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}

}
