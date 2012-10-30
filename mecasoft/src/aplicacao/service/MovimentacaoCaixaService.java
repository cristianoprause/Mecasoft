package aplicacao.service;

import java.math.BigDecimal;
import java.util.List;

import banco.modelo.Caixa;
import banco.modelo.MovimentacaoCaixa;
import banco.utils.MovimentacaoCaixaUtils;

public class MovimentacaoCaixaService extends MecasoftService<MovimentacaoCaixa>{

	private MovimentacaoCaixa movimentacao;
	
	@Override
	public MovimentacaoCaixaUtils getDAO() {
		return getInjector().getInstance(MovimentacaoCaixaUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(movimentacao);
	}

	@Override
	public void delete() {
		getDAO().delete(movimentacao);
	}
	
	public BigDecimal getTotalCaixa(Caixa caixa){
		List<MovimentacaoCaixa> listaMovimentacao = findAllByCaixa(caixa);
		BigDecimal total = caixa.getValorAbertura();
		
		for(MovimentacaoCaixa m : listaMovimentacao){
			if(m.getTipo().equals(MovimentacaoCaixa.TIPOENTRADA))
				total = total.add(m.getValor());
			else
				total = total.subtract(m.getValor());
		}
		
		return total;
	}
	
	public BigDecimal getTotalCaixaByTipo(Caixa caixa, Character tipo){
		List<MovimentacaoCaixa> listaMovimentacao = getDAO().findAllByCaixaAndTipo(caixa, tipo);
		BigDecimal total = BigDecimal.ZERO;
		
		for(MovimentacaoCaixa m : listaMovimentacao){
			total = total.add(m.getValor());
		}
		
		return total;
	}

	@Override
	public MovimentacaoCaixa find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<MovimentacaoCaixa> findAll() {
		return getDAO().findAll();
	}
	
	public List<MovimentacaoCaixa> findAllByCaixa(Caixa caixa){
		return getDAO().findAllByCaixaAndTipo(caixa, null);
	}

	public MovimentacaoCaixa getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(MovimentacaoCaixa movimentacao) {
		this.movimentacao = movimentacao;
	}

}
