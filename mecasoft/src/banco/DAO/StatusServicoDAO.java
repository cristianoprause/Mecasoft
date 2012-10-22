package banco.DAO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Pessoa;
import banco.modelo.ServicoPrestado;
import banco.modelo.StatusServico;
import banco.utils.StatusServicoUtils;

public class StatusServicoDAO extends HibernateConnection implements StatusServicoUtils{

	@Override
	public void saveOrUpdate(StatusServico modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(StatusServico modelo) {
		getSession().delete(modelo);
	}

	@Override
	public StatusServico find(Long id) {
		Query q = getSession().createQuery("select s from ServicoPrestado s where s.id = :id");
		q.setParameter("id", id);
		return (StatusServico)q.uniqueResult();
	}

	@Override
	public StatusServico findStatusFuncionario(Pessoa funcionario) {
		Query q = getSession().createQuery("select s from StatusServico s where s.id = (select max(ss.id) " +
				"from StatusServico ss where ss.funcionario = :funcionario)");
		q.setParameter("funcionario", funcionario);
		return (StatusServico) q.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StatusServico> findAll() {
		Query q = getSession().createQuery("select s from StatusServico s");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StatusServico> findAllByFuncionarioAndPeriodoAndServico(Pessoa funcionario, Date dtInicial, Date dtFinal, ServicoPrestado servico) {
		
		//seta com a primeira hora do dia para buscar todos os status daquele dia
		Calendar c = Calendar.getInstance();
		if(dtInicial != null){
			c.setTime(dtInicial);
			c.set(Calendar.AM_PM, Calendar.AM);
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			dtInicial = c.getTime();
		}
		
		//seta com a ultima hora do dia para buscar todos os status daquele dia
		if(dtFinal != null){
			c.setTime(dtFinal);
			c.set(Calendar.HOUR, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			dtFinal = c.getTime();
		}
		
		Query q = getSession().createQuery("select s from StatusServico s " +
				"where (s.data >= :dtInicial or cast(:dtInicial as date) is null) " +
				"  and (s.data <= :dtFinal or cast(:dtFinal as date) is null) " +
				"  and (s.funcionario = :funcionario or :funcionario is null) " +
				"  and (s.servicoPrestado.id = :servicoPrestadoId or :servicoPrestadoId is null) " +
				"order by (case when s.funcionario.razaoSocial is null " +
				"                    then s.funcionario.nomeFantasia " +
				"                    else s.funcionario.razaoSocial " +
				"          end), " +
				"      s.servicoPrestado.id, " +
				"      s.data");
		q.setParameter("dtInicial", dtInicial)
		.setParameter("dtFinal", dtFinal)
		.setParameter("funcionario", funcionario)
		.setParameter("servicoPrestadoId", servico == null ? null : servico.getId());
		
		return q.list();
	}

}
