package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Pessoa;
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

}
