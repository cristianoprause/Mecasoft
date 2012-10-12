package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection2;
import banco.modelo.TipoVeiculo;
import banco.utils.TipoVeiculoUtils;

public class TipoVeiculoDAO extends HibernateConnection2 implements TipoVeiculoUtils{

	@Override
	public void saveOrUpdate(TipoVeiculo modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(TipoVeiculo modelo) {
		getSession().delete(modelo);
	}

	@Override
	public TipoVeiculo find(Long id) {
		Query q = getSession().createQuery("select t from TipoVeiculo t where t.id = :id");
		q.setParameter("id", id);
		return (TipoVeiculo)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoVeiculo> findAll() {
		Query q = getSession().createQuery("select t from TipoVeiculo t");
		return q.list();
	}

}
