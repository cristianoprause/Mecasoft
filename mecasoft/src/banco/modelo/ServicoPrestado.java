package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ServicoPrestado implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9195363929741867859L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private BigDecimal totalServico;
	
	@Column
	private BigDecimal totalItens;
	
	@Column
	private BigDecimal totalLocomocao;
	
	@Column
	private BigDecimal totalMaoObra;
	
	@Column
	private BigDecimal valorTotal;
	
	@Column
	private BigDecimal desconto;
	
	@Column
	private BigDecimal troco;
	
	@Column
	private BigDecimal valorEntrada;
	
	@Column
	private BigDecimal juros;
	
	@Column
	private Date dataAbertura;
	
	@Column
	private Date dataFechamento;
	
	@ManyToOne
	private Pessoa cliente;
	
	@ManyToOne
	private Veiculo veiculo;
	
	@OneToMany(mappedBy="servicoPrestado")
	private List<ItemServico> listaServicos;
	
	@OneToMany(mappedBy="servicoPrestado")
	private List<ItemServico> listaProdutos;
	
	@OneToMany(mappedBy="servicoPrestado")
	private List<StatusServico> listaStatus;
	
	@OneToMany(mappedBy="servicoPrestado")
	private List<FormaPagtoUtilizada> listaFormaPagto;

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

	public BigDecimal getTotalMaoObra() {
		return totalMaoObra;
	}

	public void setTotalMaoObra(BigDecimal totalMaoObra) {
		this.totalMaoObra = totalMaoObra;
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

	public BigDecimal getJuros() {
		return juros;
	}

	public void setJuros(BigDecimal juros) {
		this.juros = juros;
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

	public List<ItemServico> getListaProdutos() {
		return listaProdutos;
	}

	public void setListaProdutos(List<ItemServico> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	public List<StatusServico> getListaStatus() {
		return listaStatus;
	}

	public void setListaStatus(List<StatusServico> listaStatus) {
		this.listaStatus = listaStatus;
	}

	public List<FormaPagtoUtilizada> getListaFormaPagto() {
		return listaFormaPagto;
	}

	public void setListaFormaPagto(List<FormaPagtoUtilizada> listaFormaPagto) {
		this.listaFormaPagto = listaFormaPagto;
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

}
