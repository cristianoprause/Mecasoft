package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Status;
import banco.utils.StatusUtils;

public class StatusDAO extends HibernateConnection implements StatusUtils{

	@Override
	public void saveOrUpdate(Status modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Status modelo) {
		getSession().delete(modelo);
	}

	@Override
	public Status find(Long id) {
		Query q = createQuery("select s from Status s where s.id = :id");
		q.setParameter("id", id);
		return (Status)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Status> findAll() {
		Query q = createQuery("select s from Status s");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Status> findAllByStatusAndFuncao(boolean status, Boolean pausar) {
		Query q = createQuery("select s from Status s where s.ativo is :status and " +
												"(s.pausar is :pausar or :pausar is null)");
		q.setParameter("status", status);
		q.setParameter("pausar", pausar);
		return q.list();
	}

}
