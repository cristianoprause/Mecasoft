package banco.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import aplicacao.helper.PadraoHelper;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"login"}, name="loginUnico"))
public class Usuario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1926193181906226009L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private boolean ativo = true;
	
	@Column(unique=true)
	@NotEmpty(message="Informe o login.")
	private String login;
	
	@Column
	private String senha;
	
	@OneToOne
	@NotNull(message="Selecione um funcionário.")
	private Pessoa funcionario;
	
	@ManyToOne
	@JoinColumn(name="papel_id", nullable=false)
	@NotNull(message="Selecione um papel.")
	private Papel papel;
	
	public String getStatus(){
		if(ativo)
			return PadraoHelper.ATIVO;
		else
			return PadraoHelper.DESATIVADO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
