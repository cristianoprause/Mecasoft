package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ItemServico implements Serializable{

	private static final long serialVersionUID = -1085293949054197011L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Integer quantidade;
	
	@Column
	private BigDecimal total;
	
	@Column
	private BigDecimal valorUnitario;
	
	@Column
	private String descricao;
	
	@Column
	private boolean fornecedorVisivel;
	
	@ManyToOne
	private ProdutoServico item;
	
	@ManyToOne
	private Pessoa fornecedor;
	
	@ManyToOne
	@JoinColumn(name="servicoPrestado_id")
	private ServicoPrestado servicoPrestado;
	
	//para saber a qual servico o produto pertence
	@ManyToOne
	private ServicoPrestado servicoCorrespondente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ProdutoServico getItem() {
		return item;
	}

	public void setItem(ProdutoServico item) {
		this.item = item;
	}

	public ServicoPrestado getServicoPrestado() {
		return servicoPrestado;
	}

	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}

	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	public boolean isFornecedorVisivel() {
		return fornecedorVisivel;
	}

	public void setFornecedorVisivel(boolean fornecedorVisivel) {
		this.fornecedorVisivel = fornecedorVisivel;
	}

	public ServicoPrestado getServicoCorrespondente() {
		return servicoCorrespondente;
	}

	public void setServicoCorrespondente(ServicoPrestado servicoCorrespondente) {
		this.servicoCorrespondente = servicoCorrespondente;
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
		ItemServico other = (ItemServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
