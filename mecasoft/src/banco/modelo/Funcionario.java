package banco.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@PrimaryKeyJoinColumn(name="pessoa_id")
@Inheritance(strategy=InheritanceType.JOINED)
public class Funcionario extends Pessoa{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8191945343575687545L;

	@Column
	@NotEmpty(message="Informe a serie da carteira de trabalho.")
	private String serie;
	
	@Column(precision=14, scale=2)
	@NotNull(message="Informe o salário.")
	private BigDecimal salario;
	
	@Column
	@Temporal(value = TemporalType.DATE)
	private Date dataUltimoPagto;
	
	@Column
	@NotEmpty(message="Informe o número da carteira de trabalho.")
	private String carteiraNum;
	
	@ManyToOne
	@NotNull(message="Selecione o tipo de funcionário.")
	private TipoFuncionario tipo;

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public Date getDataUltimoPagto() {
		return dataUltimoPagto;
	}

	public void setDataUltimoPagto(Date dataUltimoPagto) {
		this.dataUltimoPagto = dataUltimoPagto;
	}

	public String getCarteiraNum() {
		return carteiraNum;
	}

	public void setCarteiraNum(String carteiraNum) {
		this.carteiraNum = carteiraNum;
	}

	public TipoFuncionario getTipo() {
		return tipo;
	}

	public void setTipo(TipoFuncionario tipo) {
		this.tipo = tipo;
	}
	
}
