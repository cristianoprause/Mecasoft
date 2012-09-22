package banco.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Configuracao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5712072341645546025L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Date dtInicioManha;
	
	@Column
	private Date dtFinalManha;
	
	@Column
	private Date dtInicioTarde;
	
	@Column
	private Date dtFinalTarde;
	
	@OneToOne
	private Pessoa representanteEmpresa;
	
	@OneToOne
	private Status statusInicio;
	
	@OneToOne
	private Status statusFinal;
	
	public Date getDtInicioManha() {
		return dtInicioManha;
	}

	public void setDtInicioManha(Date dtInicioManha) {
		this.dtInicioManha = dtInicioManha;
	}

	public Date getDtFinalManha() {
		return dtFinalManha;
	}

	public void setDtFinalManha(Date dtFinalManha) {
		this.dtFinalManha = dtFinalManha;
	}

	public Date getDtInicioTarde() {
		return dtInicioTarde;
	}

	public void setDtInicioTarde(Date dtInicioTarde) {
		this.dtInicioTarde = dtInicioTarde;
	}

	public Date getDtFinalTarde() {
		return dtFinalTarde;
	}

	public void setDtFinalTarde(Date dtFinalTarde) {
		this.dtFinalTarde = dtFinalTarde;
	}

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

}
