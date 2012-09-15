package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Duplicata;
import banco.utils.DuplicataUtils;

public class DuplicataDAO extends HibernateConnection implements DuplicataUtils{

	@Override
	public void saveOrUpdate(Duplicata modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Duplicata modelo) {
		getSession().delete(modelo);
	}

	@Override
	public Duplicata find(Long id) {
		Query q = getSession().createQuery("select d from Duplicata d where d.id = :id");
		q.setParameter("id", id);
		return (Duplicata)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Duplicata> findAll() {
		Query q = getSession().createQuery("select d from Duplicata d");
		return q.list();
	}

	@Override
	public Duplicata findUltimaDuplicata() {
		Query q = getSession().createQuery("select d from Duplicata d where d.id = (" +
				"select max(dup.id) from Duplicata dup)");
		return (Duplicata) q.uniqueResult();
	}

}
