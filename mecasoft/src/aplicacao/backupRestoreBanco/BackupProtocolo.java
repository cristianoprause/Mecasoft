package aplicacao.backupRestoreBanco;

public enum BackupProtocolo {

	CLASSE((byte)-1),
	COLUNA((byte)-2);
	
	private byte valor;
	
	private BackupProtocolo(byte valor) {
		this.valor = valor;
	}

	public byte getValor() {
		return valor;
	}

	public void setValor(byte valor) {
		this.valor = valor;
	}
	
}
