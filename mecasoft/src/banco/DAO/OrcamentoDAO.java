package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Orcamento;
import banco.utils.OrcamentoUtils;

public class OrcamentoDAO extends HibernateConnection implements OrcamentoUtils{

	@Override
	public void saveOrUpdate(Orcamento modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Orcamento modelo) {
		getSession().delete(modelo);
	}

	@Override
	public Orcamento find(Long id) {
		Query q = createQuery("select o from Orcamento o where o.id = :id");
		q.setParameter("id", id);
		return (Orcamento)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Orcamento> findAll() {
		Query q = createQuery("select o from Orcamento o");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Orcamento> findAllByStatus(String status) {
		boolean sts = status.equals(Orcamento.PENDENTE);
		Query q = createQuery("select o from Orcamento o where o.pendente is :sts");
		q.setParameter("sts", sts);
		return q.list();
	}

}
