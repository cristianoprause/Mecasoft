package banco.DAO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.DuplicataPaga;
import banco.utils.DuplicataPagaUtils;

public class DuplicataPagaDAO extends HibernateConnection implements DuplicataPagaUtils{

	@Override
	public void saveOrUpdate(DuplicataPaga modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(DuplicataPaga modelo) {
		getSession().delete(modelo);
	}

	@Override
	public DuplicataPaga find(Long id) {
		Query q = getSession().createQuery("select d from DuplicataPaga d where d.id = :id");
		q.setParameter("id", id);
		return (DuplicataPaga)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DuplicataPaga> findAll() {
		Query q = getSession().createQuery("select d from DuplicataPaga d");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DuplicataPaga> findAllByPeriodo(Date dtInicial, Date dtFinal) {
		Calendar c = Calendar.getInstance();
		c.setTime(dtFinal);
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		dtFinal = c.getTime();
		
		Query q = getSession().createQuery("select d from DuplicataPaga d where d.dataPagamento between :dtInicial and :dtFinal");
		q.setParameter("dtInicial", dtInicial)
		.setParameter("dtFinal", dtFinal);
		
		return q.list();
	}

}
