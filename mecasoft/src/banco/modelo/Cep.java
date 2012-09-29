package banco.modelo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("webservicecep")
public class Cep {

	@XStreamAlias("uf")
	private String uf;

	@XStreamAlias("cidade")
	private String cidade;

	@XStreamAlias("bairro")
	private String bairro;

	@XStreamAlias("tipo_logradouro")
	private String tipo_logradouro;

	@XStreamAlias("logradouro")
	private String logradouro;

	@XStreamAlias("resultado")
	private String resultado;

	@XStreamAlias("resultado_txt")
	private String resultado_txt;

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getResultado_txt() {
		return resultado_txt;
	}

	public void setResultado_txt(String resultado_txt) {
		this.resultado_txt = resultado_txt;
	}

	public String getTipo_logradouro() {
		return tipo_logradouro;
	}

	public void setTipo_logradouro(String tipo_logradouro) {
		this.tipo_logradouro = tipo_logradouro;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
