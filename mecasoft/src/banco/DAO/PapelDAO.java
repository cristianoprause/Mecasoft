package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Papel;
import banco.utils.PapelUtils;

public class PapelDAO extends HibernateConnection implements PapelUtils{

	@Override
	public void saveOrUpdate(Papel modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Papel modelo) {
		getSession().delete(modelo);
	}

	@Override
	public Papel find(Long id) {
		Query q = createQuery("select p from Papel p where p.id = :id");
		q.setParameter("id", id);
		return (Papel)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Papel> findAll() {
		Query q = createQuery("select p from Papel p");
		List<Papel> lista = q.list();
		return lista;
	}

}
