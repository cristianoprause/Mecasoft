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

@Entity
@PrimaryKeyJoinColumn(name="pessoa_id")
@Inheritance(strategy=InheritanceType.JOINED)
public class Funcionario extends Pessoa{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8191945343575687545L;

	@Column
	private String serie = "";
	
	@Column(precision=14, scale=2)
	private BigDecimal salario;
	
	@Column
	@Temporal(value = TemporalType.DATE)
	private Date dataUltimoPagto;
	
	@Column
	private String carteiraNum = "";
	
	@ManyToOne
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
