package aplicacao.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import banco.modelo.ItemServico;
import banco.modelo.ProdutoServico;
import banco.modelo.ServicoPrestado;
import banco.utils.ServicoPrestadoUtils;

public class ServicoPrestadoService extends MecasoftService<ServicoPrestado>{

	private ServicoPrestado servicoPrestado;
	
	@Override
	public ServicoPrestadoUtils getDAO() {
		return getInjector().getInstance(ServicoPrestadoUtils.class);
	}

	@Override
	public void saveOrUpdate() {
		getDAO().saveOrUpdate(servicoPrestado);
	}

	@Override
	public void delete() {
		getDAO().delete(servicoPrestado);
	}

	@Override
	public ServicoPrestado find(Long id) {
		return getDAO().find(id);
	}

	@Override
	public List<ServicoPrestado> findAll() {
		return getDAO().findAll();
	}
	
	public List<ServicoPrestado> findAllAtivosByPeriodo(Date dataInicial, Date dataFinal){
		return getDAO().findAllByPeriodoAndStatusAndConclusao(dataInicial, dataFinal, true, null);
	}
	
	public List<ServicoPrestado> findAllNaoConcluidos(){
		return getDAO().findAllByStatusAndConclusao(true, true);
	}
	
	public void organizarListas() {

		// isso deve ser feito porque não é possivel editar a lista dentro do
		// loop (java.util.ConcurrentModificationException)
		List<ItemServico> lista = new ArrayList<ItemServico>();
		lista.addAll(servicoPrestado.getListaProdutos());

		for (ItemServico is : lista) {
			if (is.getItem().getTipo().equals(ProdutoServico.TIPOSERVICO))
				servicoPrestado.getListaProdutos().remove(is);
			else if (is.getItem().getTipo().equals(ProdutoServico.TIPOPRODUTO))
				servicoPrestado.getListaServicos().remove(is);
		}
	}
	
	public ServicoPrestado getServicoPrestado() {
		return servicoPrestado;
	}
	
	public void setServicoPrestado(ServicoPrestado servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
	}

}
