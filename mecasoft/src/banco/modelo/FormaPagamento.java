package banco.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

import aplicacao.helper.PadraoHelper;

@Entity
public class FormaPagamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6595066364014295223L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@NotEmpty(message="Informe o nome.")
	private String nome;
	
	@Column
	private boolean geraPagVista = true;
	
	@Column
	private boolean geraDuplicata;
	
	@Column
	private boolean ativo = true;
	
	public String getStatus(){
		if(ativo)
			return PadraoHelper.ATIVO;
		
		return PadraoHelper.DESATIVADO;
	}

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

	public boolean isGeraPagVista() {
		return geraPagVista;
	}

	public void setGeraPagVista(boolean geraPagVista) {
		this.geraPagVista = geraPagVista;
	}

	public boolean isGeraDuplicata() {
		return geraDuplicata;
	}

	public void setGeraDuplicata(boolean geraDuplicata) {
		this.geraDuplicata = geraDuplicata;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
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
		FormaPagamento other = (FormaPagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
