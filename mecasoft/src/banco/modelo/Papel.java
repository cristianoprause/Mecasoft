package banco.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Papel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5036690189700653713L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	@NotEmpty(message="Informe o nome.")
	private String nome;
	
	@Column
	private Boolean cadPessoa;
	
	@Column
	private Boolean cadVeiculo;
	
	@Column
	private Boolean cadServico;
	
	@Column
	private Boolean cadProduto;
	
	@Column
	private Boolean cadFormaPagto;
	
	@Column
	private Boolean cadUsuario;
	
	@Column
	private Boolean gerDuplicata;
	
	@Column
	private Boolean gerServico;
	
	@Column
	private Boolean gerCaixa;
	
	@Column
	private Boolean gerarRelatorio;
	
	@OneToMany(mappedBy="papel", cascade=CascadeType.ALL)
	private List<Usuario> listaUsuarios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getCadPessoa() {
		return cadPessoa;
	}

	public void setCadPessoa(Boolean cadPessoa) {
		this.cadPessoa = cadPessoa;
	}

	public Boolean getCadVeiculo() {
		return cadVeiculo;
	}

	public void setCadVeiculo(Boolean cadVeiculo) {
		this.cadVeiculo = cadVeiculo;
	}

	public Boolean getCadServico() {
		return cadServico;
	}

	public void setCadServico(Boolean cadServico) {
		this.cadServico = cadServico;
	}

	public Boolean getCadProduto() {
		return cadProduto;
	}

	public void setCadProduto(Boolean cadProduto) {
		this.cadProduto = cadProduto;
	}

	public Boolean getCadFormaPagto() {
		return cadFormaPagto;
	}

	public void setCadFormaPagto(Boolean cadFormaPagto) {
		this.cadFormaPagto = cadFormaPagto;
	}

	public Boolean getCadUsuario() {
		return cadUsuario;
	}

	public void setCadUsuario(Boolean cadUsuario) {
		this.cadUsuario = cadUsuario;
	}

	public Boolean getGerDuplicata() {
		return gerDuplicata;
	}

	public void setGerDuplicata(Boolean gerDuplicata) {
		this.gerDuplicata = gerDuplicata;
	}

	public Boolean getGerServico() {
		return gerServico;
	}

	public void setGerServico(Boolean gerServico) {
		this.gerServico = gerServico;
	}

	public Boolean getGerCaixa() {
		return gerCaixa;
	}

	public void setGerCaixa(Boolean gerCaixa) {
		this.gerCaixa = gerCaixa;
	}

	public Boolean getGerarRelatorio() {
		return gerarRelatorio;
	}

	public void setGerarRelatorio(Boolean gerarRelatorio) {
		this.gerarRelatorio = gerarRelatorio;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Papel other = (Papel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
