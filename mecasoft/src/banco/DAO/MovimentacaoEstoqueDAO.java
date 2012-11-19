package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.MovimentacaoEstoque;
import banco.utils.MovimentacaoEstoqueUtils;

public class MovimentacaoEstoqueDAO extends HibernateConnection implements MovimentacaoEstoqueUtils{

	@Override
	public void saveOrUpdate(MovimentacaoEstoque modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(MovimentacaoEstoque modelo) {
		getSession().delete(modelo);
	}

	@Override
	public MovimentacaoEstoque find(Long id) {
		Query q = getSession().createQuery("select m from MovimentacaoEstoque m where m.id = :id");
		q.setParameter("id", id);
		return (MovimentacaoEstoque)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentacaoEstoque> findAll() {
		Query q = getSession().createQuery("select m from MovimentacaoEstoque m");
		return q.list();
	}

}
