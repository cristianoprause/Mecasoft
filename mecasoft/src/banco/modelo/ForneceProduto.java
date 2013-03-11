package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class ForneceProduto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3732028380422990967L;

	@EmbeddedId
	private ForneceProdutoId id = new ForneceProdutoId();
	
	@Column(precision=14, scale=2)
	@NotNull(message="Não foi informado o valor de um mais fornecedores do produto.")
	private BigDecimal valorUnitario = BigDecimal.ZERO;

	public ForneceProdutoId getId() {
		return id;
	}

	public void setId(ForneceProdutoId id) {
		this.id = id;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((valorUnitario == null) ? 0 : valorUnitario.hashCode());
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
		ForneceProduto other = (ForneceProduto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valorUnitario == null) {
			if (other.valorUnitario != null)
				return false;
		} else if (!valorUnitario.equals(other.valorUnitario))
			return false;
		return true;
	}
	
}
