package banco.DAO;

import java.util.Date;
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

	@Override
	public Duplicata findUltimaDuplicata() {
		Query q = getSession().createQuery("select d from Duplicata d where d.id = (" +
				"select max(dup.id) from Duplicata dup)");
		return (Duplicata) q.uniqueResult();
	}
	
	@Override
	public Duplicata findByNumero(String numero) {
		Query q = getSession().createQuery("Select d from Duplicata d where d.numero like :numero and d.pago is false");
		q.setParameter("numero", numero);
		return (Duplicata) q.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Duplicata> findAll() {
		Query q = getSession().createQuery("select d from Duplicata d");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Duplicata> findAllByPagamento(boolean pago) {
		Query q = getSession().createQuery("select d from Duplicata d where d.pago is :pago");
		q.setParameter("pago", pago);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Duplicata> findAllByPeriodoAndPagamento(Date dtInicial, Date dtFinal, Boolean pago) {
		Query q = getSession().createQuery("select d from Duplicata d where (d.dataVencimento between :dtInicial and :dtFinal) " +
											"and (d.pago is :pago or :pago is null)");
		q.setParameter("dtInicial", dtInicial)
		.setParameter("dtFinal", dtFinal)
		.setParameter("pago", pago);
		return q.list();
	}

}
