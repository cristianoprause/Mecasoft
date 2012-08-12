package banco.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ForneceProdutoId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7243217096674766756L;

	@ManyToOne
	@JoinColumn(name="fornecedor_id", nullable=false)
	private Pessoa pessoa;
	
	@ManyToOne
	@JoinColumn(name="produto_id", nullable = false)
	private ProdutoServico produto;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
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
		ForneceProdutoId other = (ForneceProdutoId) obj;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}
	
}
