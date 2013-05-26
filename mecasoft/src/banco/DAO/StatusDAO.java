package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.Status;
import banco.utils.StatusUtils;

public class StatusDAO extends EclipseLinkConnection implements StatusUtils{

	@Override
	public void saveOrUpdate(Status modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(Status modelo) {
		remove(modelo);
	}

	@Override
	public Status find(Long id) {
		Query q = getEntityManager().createQuery("select s from Status s where s.id = :id");
		q.setParameter("id", id);
		return (Status)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Status> findAll() {
		Query q = getEntityManager().createQuery("select s from Status s");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Status> findAllByStatusAndFuncao(boolean status, Boolean pausar) {
		Query q = getEntityManager().createQuery("select s from Status s where s.ativo = :status and " +
												"(s.pausar = :pausar or :pausar is null)");
		q.setParameter("status", status);
		q.setParameter("pausar", pausar);
		return q.getResultList();
	}

}
