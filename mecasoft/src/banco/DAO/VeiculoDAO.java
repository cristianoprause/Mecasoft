package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Veiculo;
import banco.utils.VeiculoUtils;

public class VeiculoDAO extends HibernateConnection implements VeiculoUtils{

	@Override
	public void saveOrUpdate(Veiculo modelo) {
		if(modelo.getId() == null)
			getEntityManager().merge(modelo);
		else
			getEntityManager().persist(modelo);
	}

	@Override
	public void delete(Veiculo modelo) {}

	@Override
	public Veiculo find(Long id) {
		Query q = getEntityManager().createQuery("select v from Veiculo v where v.id = :id");
		q.setParameter("id", id);
		return (Veiculo)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Veiculo> findAll() {
		Query q = getEntityManager().createQuery("select v from Veiculo v");
		return q.getResultList();
	}
}
