package banco.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class TipoVeiculo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7496911935560983271L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@NotEmpty(message="Informe o nome.")
	private String nome;
	
	@Column
	private Boolean hodometro = true;
	
	@Column
	private Boolean horimetro;

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

	public Boolean getHodometro() {
		return hodometro;
	}

	public void setHodometro(Boolean hodometro) {
		this.hodometro = hodometro;
	}

	public Boolean getHorimetro() {
		return horimetro;
	}

	public void setHorimetro(Boolean horimetro) {
		this.horimetro = horimetro;
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
		TipoVeiculo other = (TipoVeiculo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
