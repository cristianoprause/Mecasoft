package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.HibernateConnection;
import banco.modelo.TipoFuncionario;
import banco.utils.TipoFuncionarioUtils;

public class TipoFuncionarioDAO extends HibernateConnection implements TipoFuncionarioUtils{

	@Override
	public void saveOrUpdate(TipoFuncionario modelo) {
		if(modelo.getId() != null)
			getEntityManager().merge(modelo);
		else
			getEntityManager().persist(modelo);
	}

	@Override
	public void delete(TipoFuncionario modelo) {
		getEntityManager().remove(modelo);
	}

	@Override
	public TipoFuncionario find(Long id) {
		Query q = getEntityManager().createQuery("select t from TipoFuncionario t where t.id = :id");
		q.setParameter("id", id);
		return (TipoFuncionario)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoFuncionario> findAll() {
		Query q = getEntityManager().createQuery("select t from TipoFuncionario t");
		return q.getResultList();
	}

}
