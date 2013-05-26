package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.Papel;
import banco.utils.PapelUtils;

public class PapelDAO extends EclipseLinkConnection implements PapelUtils{

	@Override
	public void saveOrUpdate(Papel modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(Papel modelo) {
		remove(modelo);
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
