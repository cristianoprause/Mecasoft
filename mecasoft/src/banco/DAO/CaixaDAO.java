package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Caixa;
import banco.utils.CaixaUtils;

public class CaixaDAO extends HibernateConnection implements CaixaUtils{

	@Override
	public void saveOrUpdate(Caixa modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Caixa modelo) {
		getSession().delete(modelo);
	}

	@Override
	public Caixa find(Long id) {
		Query q = createQuery("select c from Caixa c where c.id = :id");
		q.setParameter("id", id);
		return (Caixa)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Caixa> findAll() {
		Query q = createQuery("select c from Caixa c");
		return q.list();
	}

	@Override
	public Caixa findUltimoCaixaByStatus(Boolean status) {
		Query q = createQuery("select c from Caixa c where c.id = (select max(ca.id) from Caixa ca " +
										"where (:status is null) or (ca.dataFechamento is null and :status is true) " +
										"or (ca.dataFechamento is not null and :status is false))");
		q.setParameter("status", status);
		return (Caixa) q.uniqueResult();
	}

}
