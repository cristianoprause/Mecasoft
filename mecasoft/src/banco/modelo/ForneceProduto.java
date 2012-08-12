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
	private BigDecimal valorUnitario;

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
	
}
