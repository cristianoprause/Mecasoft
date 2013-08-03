package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.Pessoa;
import banco.modelo.Veiculo;
import banco.utils.VeiculoUtils;

public class VeiculoDAO extends EclipseLinkConnection implements VeiculoUtils{

	@Override
	public void saveOrUpdate(Veiculo modelo) {
		if(modelo.getId() == null)
			merge(modelo);
		else
			persist(modelo);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Veiculo> findAllByPessoa(Pessoa pessoa) {
		Query q = getEntityManager().createQuery("select v from Veiculo v where v.cliente = :cliente");
		q.setParameter("cliente", pessoa);
		return q.getResultList();
	}
}
