package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Veiculo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7458004935003004021L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Boolean ativo = true;
	
	@Column
	private String marca;
	
	@Column
	@NotEmpty(message="Informe o modelo.")
	private String modelo;
	
	@Column
	private String placa;
	
	@Column
	private Long hodometro;
	
	@Column(precision = 14, scale = 1)
	private BigDecimal horimetro;
	
	@ManyToOne
	@NotNull(message="Selecione o cliente.")
	@JoinColumn(name="cliente_id", nullable=false)
	private Pessoa cliente;
	
	@ManyToOne
	@NotNull(message="Selecione o tipo de veículo.")
	private TipoVeiculo tipo;

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

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Long getHodometro() {
		return hodometro;
	}

	public void setHodometro(Long hodometro) {
		this.hodometro = hodometro;
	}

	public BigDecimal getHorimetro() {
		return horimetro;
	}

	public void setHorimetro(BigDecimal horimetro) {
		this.horimetro = horimetro;
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public TipoVeiculo getTipo() {
		return tipo;
	}

	public void setTipo(TipoVeiculo tipo) {
		this.tipo = tipo;
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
		Veiculo other = (Veiculo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
