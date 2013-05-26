package banco.DAO;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.Duplicata;
import banco.utils.DuplicataUtils;

public class DuplicataDAO extends EclipseLinkConnection implements DuplicataUtils{

	@Override
	public void saveOrUpdate(Duplicata modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(Duplicata modelo) {
		remove(modelo);
	}

	@Override
	public Duplicata find(Long id) {
		Query q = getEntityManager().createQuery("select d from Duplicata d where d.id = :id");
		q.setParameter("id", id);
		return (Duplicata)q.getSingleResult();
	}

	@Override
	public Duplicata findUltimaDuplicata() {
		Query q = getEntityManager().createQuery("select d from Duplicata d where d.id = (" +
				"select max(dup.id) from Duplicata dup)");
		return (Duplicata) q.getSingleResult();
	}
	
	@Override
	public Duplicata findByNumero(String numero) {
		Query q = getEntityManager().createQuery("Select d from Duplicata d where d.numero like :numero and d.pago is false");
		q.setParameter("numero", numero);
		return (Duplicata) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Duplicata> findAll() {
		Query q = getEntityManager().createQuery("select d from Duplicata d");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Duplicata> findAllByPagamento(boolean pago) {
		Query q = getEntityManager().createQuery("select d from Duplicata d where d.pago = :pago");
		q.setParameter("pago", pago);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Duplicata> findAllByPeriodoAndPagamento(Date dtInicial, Date dtFinal, Boolean pago) {
		Query q = getEntityManager().createQuery("select d from Duplicata d where (d.dataVencimento between :dtInicial and :dtFinal) " +
											"and (d.pago = :pago or :pago is null)");
		q.setParameter("dtInicial", dtInicial)
		.setParameter("dtFinal", dtFinal)
		.setParameter("pago", pago);
		return q.getResultList();
	}

}
