package banco.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Configuracao implements Serializable{

	private static final long serialVersionUID = 5712072341645546025L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Pessoa representanteEmpresa;
	
	@OneToOne
	@NotNull(message="Selecione o status para iniciar os serviços no periodo.")
	private Status statusInicio;
	
	@OneToOne
	@NotNull(message="Selecione o status para finalizar os serviços no periodo.")
	private Status statusFinal;
	
	@OneToOne
	@NotNull(message="Selecione o status final padrão de cada serviço.")
	private Status statusFinalizarServico;
	
	public Pessoa getRepresentanteEmpresa() {
		return representanteEmpresa;
	}

	public void setRepresentanteEmpresa(Pessoa representanteEmpresa) {
		this.representanteEmpresa = representanteEmpresa;
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
		Configuracao other = (Configuracao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatusInicio() {
		return statusInicio;
	}

	public void setStatusInicio(Status statusInicio) {
		this.statusInicio = statusInicio;
	}

	public Status getStatusFinal() {
		return statusFinal;
	}

	public void setStatusFinal(Status statusFinal) {
		this.statusFinal = statusFinal;
	}

	public Status getStatusFinalizarServico() {
		return statusFinalizarServico;
	}

	public void setStatusFinalizarServico(Status statusFinalizarServico) {
		this.statusFinalizarServico = statusFinalizarServico;
	}

}
