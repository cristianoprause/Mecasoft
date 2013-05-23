package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Orcamento;
import banco.utils.OrcamentoUtils;

public class OrcamentoDAO extends HibernateConnection implements OrcamentoUtils{

	@Override
	public void saveOrUpdate(Orcamento modelo) {
		if(modelo.getId() != null)
			getEntityManager().merge(modelo);
		else
			getEntityManager().persist(modelo);
	}

	@Override
	public void delete(Orcamento modelo) {
		getEntityManager().remove(modelo);
	}

	@Override
	public Orcamento find(Long id) {
		Query q = getEntityManager().createQuery("select o from Orcamento o where o.id = :id");
		q.setParameter("id", id);
		return (Orcamento)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Orcamento> findAll() {
		Query q = getEntityManager().createQuery("select o from Orcamento o");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Orcamento> findAllByStatus(String status) {
		boolean sts = status.equals(Orcamento.PENDENTE);
		Query q = getEntityManager().createQuery("select o from Orcamento o where o.pendente is :sts");
		q.setParameter("sts", sts);
		return q.getResultList();
	}

}
