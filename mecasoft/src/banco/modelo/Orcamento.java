package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import aplicacao.helper.FormatterHelper;

@Entity
public class Orcamento implements Serializable{

	private static final long serialVersionUID = -3855387748352702246L;
	public static String APROVADO = "Aprovado";
	public static String PENDENTE = "Pendente";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtLancamento = new Date();
	
	@Column
	private BigDecimal valorTotal = BigDecimal.ZERO;
	
	/**
	 * Status do orcamento
	 * 
	 * true quando pendente
	 * false quando aprovado
	 */
	@Column
	private boolean pendente = true;
	
	@ManyToOne
	@NotNull(message="Selecione o cliente.")
	private Pessoa cliente;
	
	@ManyToOne
	@NotNull(message="Selecione o veículo.")
	private Veiculo veiculo;
	
	@OneToOne
	private ServicoPrestado servico;
	
	@OneToMany(mappedBy="orcamento", cascade=javax.persistence.CascadeType.ALL)
	@NotNull(message="Adicione ao menos um serviço.")
	private List<ItemServico> listaServico = new ArrayList<ItemServico>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDtLancamento() {
		return dtLancamento;
	}

	public void setDtLancamento(Date dtLancamento) {
		this.dtLancamento = dtLancamento;
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<ItemServico> getListaServico() {
		return listaServico;
	}

	public void setListaServico(List<ItemServico> listaServico) {
		this.listaServico = listaServico;
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
		Orcamento other = (Orcamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public ServicoPrestado getServico() {
		return servico;
	}

	public void setServico(ServicoPrestado servico) {
		this.servico = servico;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public String getNumero(){
		return id == null ? "" : FormatterHelper.formatDataInvertida(dtLancamento) + "-" + id;
	}
	
	public String getStatusOrcamento(){
		return pendente ? PENDENTE : APROVADO;
	}

	public boolean isPendente() {
		return pendente;
	}

	public void setPendente(boolean pendente) {
		this.pendente = pendente;
	}

}
