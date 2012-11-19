package aplicacao.service;

import java.util.List;

import banco.modelo.MovimentacaoEstoque;
import banco.utils.MovimentacaoEstoqueUtils;

public class MovimentacaoEstoqueService extends MecasoftService<MovimentacaoEstoque>{

	private MovimentacaoEstoque movimentacao;
	
	@Override
	public MovimentacaoEstoqueUtils getDAO() {
		return getInjector().getInstance(MovimentacaoEstoqueUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(movimentacao);
	}

	@Override
	public void delete() {
		getDAO().delete(movimentacao);
	}

	@Override
	public MovimentacaoEstoque find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<MovimentacaoEstoque> findAll() {
		return getDAO().findAll();
	}

	public MovimentacaoEstoque getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(MovimentacaoEstoque movimentacao) {
		this.movimentacao = movimentacao;
	}

}
