package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.FormaPagamento;
import banco.utils.FormaPagamentoUtils;

public class FormaPagamentoDAO extends EclipseLinkConnection implements FormaPagamentoUtils{

	@Override
	public void saveOrUpdate(FormaPagamento modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(FormaPagamento modelo) {
		remove(modelo);
	}

	@Override
	public FormaPagamento find(Long id) {
		Query q = getEntityManager().createQuery("select f from FormaPagamento f where f.id = :id");
		q.setParameter("id", id);
		return (FormaPagamento)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormaPagamento> findAll() {
		Query q = getEntityManager().createQuery("select f from FormaPagamento f");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormaPagamento> findAllByStatus(Boolean status) {
		Query q = getEntityManager().createQuery("select f from FormaPagamento f where f.ativo = :status");
		q.setParameter("status", status);
		return q.getResultList();
	}

}
