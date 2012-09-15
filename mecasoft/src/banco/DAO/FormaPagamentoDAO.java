package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.FormaPagamento;
import banco.utils.FormaPagamentoUtils;

public class FormaPagamentoDAO extends HibernateConnection implements FormaPagamentoUtils{

	@Override
	public void saveOrUpdate(FormaPagamento modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(FormaPagamento modelo) {
		getSession().delete(modelo);
	}

	@Override
	public FormaPagamento find(Long id) {
		Query q = getSession().createQuery("select f from FormaPagamento f where f.id = :id");
		q.setParameter("id", id);
		return (FormaPagamento)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormaPagamento> findAll() {
		Query q = getSession().createQuery("select f from FormaPagamento f");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormaPagamento> findAllByStatus(Boolean status) {
		Query q = getSession().createQuery("select f from FormaPagamento f where f.ativo is :status");
		q.setParameter("status", status);
		return q.list();
	}

}
