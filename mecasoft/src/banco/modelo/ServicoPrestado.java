package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

@Entity
public class ServicoPrestado implements Serializable{

	private static final long serialVersionUID = 9195363929741867859L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private boolean ativo = true;
	
	@Column
	private boolean emExecucao = true;
	
	@Column
	private BigDecimal totalServico;
	
	@Column
	private BigDecimal totalItens;
	
	@Column
	private BigDecimal totalLocomocao = BigDecimal.ZERO;
	
	@Column
	private BigDecimal valorTotal;
	
	@Column
	private BigDecimal desconto = BigDecimal.ZERO;
	
	@Column
	private BigDecimal troco = BigDecimal.ZERO;
	
	@Column
	private BigDecimal valorEntrada = BigDecimal.ZERO;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAbertura = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFechamento;
	
	@Column
	private BigDecimal iss = BigDecimal.ZERO;
	
	@ManyToOne
	@NotNull(message="Selecione o cliente.")
	private Pessoa cliente;
	
	@ManyToOne
	@NotNull(message="Selecione o veículo")
	private Veiculo veiculo;
	
	@OneToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST})
	private Orcamento orcamento;
	
	@OneToMany(mappedBy="servicoPrestado", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ItemServico> listaServicos = new ArrayList<ItemServico>();
	
	@OneToMany(mappedBy="servicoPrestado", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<FormaPagtoUtilizada> listaFormaPagto = new ArrayList<FormaPagtoUtilizada>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotalServico() {
		return totalServico;
	}

	public void setTotalServico(BigDecimal totalServico) {
		this.totalServico = totalServico;
	}

	public BigDecimal getTotalItens() {
		return totalItens;
	}

	public void setTotalItens(BigDecimal totalItens) {
		this.totalItens = totalItens;
	}

	public BigDecimal getTotalLocomocao() {
		return totalLocomocao;
	}

	public void setTotalLocomocao(BigDecimal totalLocomocao) {
		this.totalLocomocao = totalLocomocao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getTroco() {
		return troco;
	}

	public void setTroco(BigDecimal troco) {
		this.troco = troco;
	}

	public BigDecimal getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(BigDecimal valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
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

	public List<ItemServico> getListaServicos() {
		return listaServicos;
	}

	public void setListaServicos(List<ItemServico> listaServicos) {
		this.listaServicos = listaServicos;
	}

	public List<FormaPagtoUtilizada> getListaFormaPagto() {
		return listaFormaPagto;
	}

	public void setListaFormaPagto(List<FormaPagtoUtilizada> listaFormaPagto) {
		this.listaFormaPagto = listaFormaPagto;
	}

	public BigDecimal getIss() {
		return iss;
	}

	public void setIss(BigDecimal iss) {
		this.iss = iss;
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
		ServicoPrestado other = (ServicoPrestado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isEmExecucao() {
		return emExecucao;
	}

	public void setEmExecucao(boolean emExecucao) {
		this.emExecucao = emExecucao;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

}
