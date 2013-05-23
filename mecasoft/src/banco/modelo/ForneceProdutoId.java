package banco.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import aplicacao.service.PessoaService;
import aplicacao.service.ProdutoServicoService;

@Embeddable
public class ForneceProdutoId implements Serializable{

	private static final long serialVersionUID = 7243217096674766756L;

	@Column(nullable=false)
	private Long pessoaId;
	
	@Column(nullable = false)
	private Long produtoId;

	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}
	
	public Pessoa getPessoa(){
		return new PessoaService().find(pessoaId);
	}
	
	public void setPessoa(Pessoa pessoa){
		pessoaId = pessoa.getId();
	}
	
	public ProdutoServico getProduto(){
		return new ProdutoServicoService().find(produtoId);
	}
	
	public void setProduto(ProdutoServico produto){
		this.produtoId = produto.getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pessoaId == null) ? 0 : pessoaId.hashCode());
		result = prime * result
				+ ((produtoId == null) ? 0 : produtoId.hashCode());
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
		ForneceProdutoId other = (ForneceProdutoId) obj;
		if (pessoaId == null) {
			if (other.pessoaId != null)
				return false;
		} else if (!pessoaId.equals(other.pessoaId))
			return false;
		if (produtoId == null) {
			if (other.produtoId != null)
				return false;
		} else if (!produtoId.equals(other.produtoId))
			return false;
		return true;
	}


	
}
