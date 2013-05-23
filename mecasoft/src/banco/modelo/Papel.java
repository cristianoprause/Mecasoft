package banco.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

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
	@NotNull(message="Informe o nome.")
	private String nome;
	
	@Column
	private boolean cadPessoa;
	
	@Column
	private boolean cadVeiculo;
	
	@Column
	private boolean cadServico;
	
	@Column
	private boolean cadProduto;
	
	@Column
	private boolean cadFormaPagto;
	
	@Column
	private boolean cadUsuario;
	
	@Column
	private boolean gerDuplicata;
	
	@Column
	private boolean gerServico;
	
	@Column
	private boolean gerCaixa;
	
	@Column
	private boolean gerarRelatorio;
	
	@OneToMany(mappedBy="papel", cascade=CascadeType.ALL)
	private List<Usuario> listaUsuarios = new ArrayList<Usuario>();

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

	public boolean getCadPessoa() {
		return cadPessoa;
	}

	public void setCadPessoa(boolean cadPessoa) {
		this.cadPessoa = cadPessoa;
	}

	public boolean getCadVeiculo() {
		return cadVeiculo;
	}

	public void setCadVeiculo(boolean cadVeiculo) {
		this.cadVeiculo = cadVeiculo;
	}

	public boolean getCadServico() {
		return cadServico;
	}

	public void setCadServico(boolean cadServico) {
		this.cadServico = cadServico;
	}

	public boolean getCadProduto() {
		return cadProduto;
	}

	public void setCadProduto(boolean cadProduto) {
		this.cadProduto = cadProduto;
	}

	public boolean getCadFormaPagto() {
		return cadFormaPagto;
	}

	public void setCadFormaPagto(boolean cadFormaPagto) {
		this.cadFormaPagto = cadFormaPagto;
	}

	public boolean getCadUsuario() {
		return cadUsuario;
	}

	public void setCadUsuario(boolean cadUsuario) {
		this.cadUsuario = cadUsuario;
	}

	public boolean getGerDuplicata() {
		return gerDuplicata;
	}

	public void setGerDuplicata(boolean gerDuplicata) {
		this.gerDuplicata = gerDuplicata;
	}

	public boolean getGerServico() {
		return gerServico;
	}

	public void setGerServico(boolean gerServico) {
		this.gerServico = gerServico;
	}

	public boolean getGerCaixa() {
		return gerCaixa;
	}

	public void setGerCaixa(boolean gerCaixa) {
		this.gerCaixa = gerCaixa;
	}

	public boolean getGerarRelatorio() {
		return gerarRelatorio;
	}

	public void setGerarRelatorio(boolean gerarRelatorio) {
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
