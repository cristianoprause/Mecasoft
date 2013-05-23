package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Papel;
import banco.utils.PapelUtils;

public class PapelDAO extends HibernateConnection implements PapelUtils{

	@Override
	public void saveOrUpdate(Papel modelo) {
		if(modelo.getId() != null)
			getEntityManager().merge(modelo);
		else
			getEntityManager().persist(modelo);
	}

	@Override
	public void delete(Papel modelo) {
		getEntityManager().remove(modelo);
	}

	@Override
	public Papel find(Long id) {
		Query q = getEntityManager().createQuery("select p from Papel p where p.id = :id");
		q.setParameter("id", id);
		return (Papel)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Papel> findAll() {
		Query q = getEntityManager().createQuery("select p from Papel p");
		return q.getResultList();
	}

}
