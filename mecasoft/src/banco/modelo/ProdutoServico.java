package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class ProdutoServico implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4257821782999163779L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Boolean ativo = true;
	
	@Column
	private String descricao;
	
	@Column
	private Integer quantidade;
	
	@Column
	private String tipo;
	
	@Column(precision=14, scale=2)
	@NotNull(message="Informe o valor.")
	private BigDecimal valorUnitario;
	
	@Column(precision=14,scale=2)
	private BigDecimal lucro;
	
	@Column(precision=14,scale=2)
	private BigDecimal custo;
	
	@OneToMany(mappedBy="produto")
	private List<ForneceProduto> listaFornecedores;
	
	@ManyToMany
	@JoinTable(name="prosutoServicoParametrizado",
		joinColumns={
			@JoinColumn(name="servico_id", referencedColumnName = "id")
		},
		inverseJoinColumns={
			@JoinColumn(name="produto_id", referencedColumnName = "id")
		}
	)
	private List<ProdutoServico> listaProduto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getLucro() {
		return lucro;
	}

	public void setLucro(BigDecimal lucro) {
		this.lucro = lucro;
	}

	public BigDecimal getCusto() {
		return custo;
	}

	public void setCusto(BigDecimal custo) {
		this.custo = custo;
	}

	public List<ForneceProduto> getListaFornecedores() {
		return listaFornecedores;
	}

	public void setListaFornecedores(List<ForneceProduto> listaFornecedores) {
		this.listaFornecedores = listaFornecedores;
	}

	public List<ProdutoServico> getListaProduto() {
		return listaProduto;
	}

	public void setListaProduto(List<ProdutoServico> listaProduto) {
		this.listaProduto = listaProduto;
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
		ProdutoServico other = (ProdutoServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
