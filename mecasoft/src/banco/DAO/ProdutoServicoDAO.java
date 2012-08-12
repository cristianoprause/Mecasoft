package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.ProdutoServico;
import banco.utils.ProdutoServicoUtils;

public class ProdutoServicoDAO extends HibernateConnection implements ProdutoServicoUtils{

	@Override
	public void saveOrUpdate(ProdutoServico modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(ProdutoServico modelo) {
		getSession().delete(modelo);
	}

	@Override
	public ProdutoServico find(Long id) {
		Query q = getSession().createQuery("select p from ProdutoServico p where p.id = :id");
		q.setParameter("id", id);
		return (ProdutoServico)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoServico> findAll() {
		Query q = getSession().createQuery("select p from ProdutoServico p");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoServico> findAllByTipo(String tipo) {
		Query q = getSession().createQuery("select p from ProdutoServico p where p.tipo like :tipo");
		q.setParameter("tipo", tipo);
		return q.list();
	}

}
