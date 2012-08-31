package aplicacao.service;

import java.util.List;

import banco.modelo.ProdutoServico;
import banco.utils.ProdutoServicoUtils;

public class ProdutoServicoService extends MecasoftService<ProdutoServico>{

	private ProdutoServico produtoServico;
	
	@Override
	public ProdutoServicoUtils getDAO() {
		return getInjector().getInstance(ProdutoServicoUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(produtoServico);
	}

	@Override
	public void delete() {
		getDAO().delete(produtoServico);
	}

	@Override
	public ProdutoServico find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<ProdutoServico> findAll() {
		return getDAO().findAll();
	}
	
	public List<ProdutoServico> findAllProdutos(){
		return getDAO().findAllByTipoAndStatus(ProdutoServico.TIPOPRODUTO, null);
	}
	
	public List<ProdutoServico> findAllServicos(){
		return getDAO().findAllByTipoAndStatus(ProdutoServico.TIPOSERVICO, null);
	}
	
	public List<ProdutoServico> findAllProdutosAtivos(){
		return getDAO().findAllByTipoAndStatus(ProdutoServico.TIPOPRODUTO, true);
	}
	
	public List<ProdutoServico> findAllServicosAtivos(){
		return getDAO().findAllByTipoAndStatus(ProdutoServico.TIPOSERVICO, true);
	}
	
	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}
	
}
