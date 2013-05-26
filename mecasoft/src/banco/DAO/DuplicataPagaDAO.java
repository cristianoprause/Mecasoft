package banco.DAO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.DuplicataPaga;
import banco.utils.DuplicataPagaUtils;

public class DuplicataPagaDAO extends EclipseLinkConnection implements DuplicataPagaUtils{

	@Override
	public void saveOrUpdate(DuplicataPaga modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(DuplicataPaga modelo) {
		remove(modelo);
	}

	@Override
	public DuplicataPaga find(Long id) {
		Query q = getEntityManager().createQuery("select d from DuplicataPaga d where d.id = :id");
		q.setParameter("id", id);
		return (DuplicataPaga)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DuplicataPaga> findAll() {
		Query q = getEntityManager().createQuery("select d from DuplicataPaga d");
		return q.getResultList();
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
		
		Query q = getEntityManager().createQuery("select d from DuplicataPaga d where d.dataPagamento between :dtInicial and :dtFinal");
		q.setParameter("dtInicial", dtInicial)
		.setParameter("dtFinal", dtFinal);
		
		return q.getResultList();
	}

}
