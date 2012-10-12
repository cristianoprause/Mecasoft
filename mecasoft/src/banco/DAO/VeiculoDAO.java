package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection2;
import banco.modelo.TipoVeiculo;
import banco.modelo.Veiculo;
import banco.utils.VeiculoUtils;

public class VeiculoDAO extends HibernateConnection2 implements VeiculoUtils{

	@Override
	public void saveOrUpdate(Veiculo modelo) {
		if(modelo.getId() == null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Veiculo modelo) {}

	@Override
	public Veiculo find(Long id) {
		Query q = getSession().createQuery("select v from Veiculo v where v.id = :id");
		q.setParameter("id", id);
		return (Veiculo)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Veiculo> findAll() {
		Query q = getSession().createQuery("select v from Veiculo v");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Veiculo> findAllByTipo(TipoVeiculo tipo) {
		Query q = getSession().createQuery("select v from Veiculo v where v.tipo = :tipo");
		q.setParameter("tipo", tipo);
		return q.list();
	}

}
