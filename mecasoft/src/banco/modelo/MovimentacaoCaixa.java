package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import aplicacao.helper.UsuarioHelper;

@Entity
public class MovimentacaoCaixa implements Serializable{

	private static final long serialVersionUID = -5159734693323484076L;
	
	public static Character TIPOENTRADA = 'E';
	public static Character TIPOSAIDA = 'S';
	
	public static String STATUSSANGRIA = "Sangria";
	public static String STATUSSUPRIMENTO = "Suprimento";
	public static String STATUSSERVICO = "Serviço";
	public static String STATUSDUPLICATA = "Duplicata";
	public static String STATUSPAGAMENTOFUNCIONARIO =  "Pagamento de funcionário";
	public static String STATUSCOMPRA = "Compra";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@NotNull(message="Informe o valor.")
	private BigDecimal valor;
	
	@Column
	@NotNull(message="Informe o motivo.")
	private String motivo;
	
	@Column
	private Character tipo;
	
	@Column
	private String status;
	
	@Column
	private Date dataMovimentacao = new Date();
	
	@ManyToOne
	private Caixa caixa = UsuarioHelper.getCaixa();
	
	@ManyToOne
	private Pessoa funcionario;
	
	@ManyToOne
	private ServicoPrestado servicoPrestado;
	
	@ManyToOne
	private DuplicataPaga duplicataPaga;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDataMovimentacao() {
		return dataMovimentacao;
	}

	public void setDataMovimentacao(Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

	public ServicoPrestado getServicoPrestado() {
		return servicoPrestado;
	}

	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}

	public DuplicataPaga getDuplicataPaga() {
		return duplicataPaga;
	}

	public void setDuplicataPaga(DuplicataPaga duplicataPaga) {
		this.duplicataPaga = duplicataPaga;
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
		MovimentacaoCaixa other = (MovimentacaoCaixa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
