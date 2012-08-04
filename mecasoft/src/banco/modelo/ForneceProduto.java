package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ForneceProduto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2207350225950948776L;

	@Id
	@ManyToOne
	@JoinColumn(name="fornecedor_id", nullable=false)
	private Pessoa pessoa;
	
	@Id
	@ManyToOne
	@JoinColumn(name="produto_id", nullable = false)
	private ProdutoServico produto;
	
	@Column(precision=14, scale=2)
	private BigDecimal valorUnitario;

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public ProdutoServico getProduto() {
		return produto;
	}

	public void setProduto(ProdutoServico produto) {
		this.produto = produto;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
}
