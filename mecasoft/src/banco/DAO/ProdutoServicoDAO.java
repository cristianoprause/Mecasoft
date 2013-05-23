package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.HibernateConnection;
import banco.modelo.ProdutoServico;
import banco.utils.ProdutoServicoUtils;

public class ProdutoServicoDAO extends HibernateConnection implements ProdutoServicoUtils{

	@Override
	public void saveOrUpdate(ProdutoServico modelo) {
		if(modelo.getId() != null)
			getEntityManager().merge(modelo);
		else
			getEntityManager().persist(modelo);
	}

	@Override
	public void delete(ProdutoServico modelo) {
		getEntityManager().remove(modelo);
	}

	@Override
	public ProdutoServico find(Long id) {
		Query q = getEntityManager().createQuery("select p from ProdutoServico p where p.id = :id");
		q.setParameter("id", id);
		return (ProdutoServico)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoServico> findAll() {
		Query q = getEntityManager().createQuery("select p from ProdutoServico p");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoServico> findAllByTipoAndStatus(String tipo, Boolean status) {
		Query q = getEntityManager().createQuery("select p from ProdutoServico p where p.tipo like :tipo and (p.ativo is :status or :status is null)");
		q.setParameter("tipo", tipo)
		.setParameter("status", status);
		return q.getResultList();
	}

}
