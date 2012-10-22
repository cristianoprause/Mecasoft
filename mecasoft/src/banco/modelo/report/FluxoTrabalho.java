package banco.modelo.report;

import java.util.Date;

public class FluxoTrabalho {

	private String nome;
	private String tipo;
	private Long numeroServico;
	private Date dataServico;
	private String tempoGasto;
	private String tempoTotal;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Long getNumeroServico() {
		return numeroServico;
	}
	public void setNumeroServico(Long numeroServico) {
		this.numeroServico = numeroServico;
	}
	public Date getDataServico() {
		return dataServico;
	}
	public void setDataServico(Date dataServico) {
		this.dataServico = dataServico;
	}
	public String getTempoGasto() {
		return tempoGasto;
	}
	public void setTempoGasto(String tempoGasto) {
		this.tempoGasto = tempoGasto;
	}
	public String getTempoTotal() {
		return tempoTotal;
	}
	public void setTempoTotal(String tempoTotal) {
		this.tempoTotal = tempoTotal;
	}
	
}
