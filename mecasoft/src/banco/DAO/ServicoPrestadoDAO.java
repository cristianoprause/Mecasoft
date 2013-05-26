package banco.DAO;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.ServicoPrestado;
import banco.utils.ServicoPrestadoUtils;

import com.ibm.icu.util.Calendar;

public class ServicoPrestadoDAO extends EclipseLinkConnection implements
		ServicoPrestadoUtils {

	@Override
	public void saveOrUpdate(ServicoPrestado modelo) {
		if (modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(ServicoPrestado modelo) {
		remove(modelo);
	}

	@Override
	public ServicoPrestado find(Long id) {
		Query q = getEntityManager().createQuery(
				"select s from ServicoPrestado s where s.id = :id");
		q.setParameter("id", id);
		return (ServicoPrestado) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServicoPrestado> findAll() {
		Query q = getEntityManager().createQuery("select s from ServicoPrestado s");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServicoPrestado> findAllByPeriodoAndStatusAndConclusao(
			Date dataInicial, Date dataFinal, Boolean status, Boolean emExecucao) {

		if (dataInicial != null && dataFinal != null) {
			Calendar c = Calendar.getInstance();

			c.setTime(dataInicial);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			dataInicial = c.getTime();

			c.setTime(dataFinal);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			dataFinal = c.getTime();
		}

		Query q = getEntityManager().createQuery("select s from ServicoPrestado s where (s.dataAbertura between :dataInicial and :dataFinal) "
								+ "and (s.ativo = :status or :status is null) "
								+ "and (s.emExecucao = :emExecucao or :emExecucao is null)");
		q.setParameter("dataInicial", dataInicial);
		q.setParameter("dataFinal", dataFinal);
		q.setParameter("status", status);
		q.setParameter("emExecucao", emExecucao);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServicoPrestado> findAllByStatusAndConclusao(Boolean status,
			Boolean emExecucao) {
		Query q = getEntityManager()
				.createQuery(
						"select s from ServicoPrestado s where (s.ativo = :status or :status is null) "
								+ "and (s.emExecucao = :emExecucao or :emExecucao is null)");
		q.setParameter("status", status).setParameter("emExecucao", emExecucao);
		return q.getResultList();
	}

}
