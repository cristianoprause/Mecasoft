package tela.editor.editorInput;

import banco.modelo.Status;

public class StatusEditorInput extends MecasoftEditorInput{

	private Status status;
	
	public StatusEditorInput(Status status) {
		this.status = status;
	}
	
	public StatusEditorInput() {
		this.status = new Status();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
