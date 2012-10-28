package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Caixa;
import banco.modelo.MovimentacaoCaixa;
import banco.utils.MovimentacaoCaixaUtils;

public class MovimentacaoCaixaDAO extends HibernateConnection implements MovimentacaoCaixaUtils{

	@Override
	public void saveOrUpdate(MovimentacaoCaixa modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(MovimentacaoCaixa modelo) {
		getSession().delete(modelo);
	}

	@Override
	public MovimentacaoCaixa find(Long id) {
		Query q = getSession().createQuery("select m from MovimentacaoCaixa m where m.id = :id");
		q.setParameter("id", id);
		return (MovimentacaoCaixa)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentacaoCaixa> findAll() {
		Query q = getSession().createQuery("select m from MovimentacaoCaixa m");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentacaoCaixa> findAllByCaixa(Caixa caixa) {
		Query q = getSession().createQuery("select m from MovimentacaoCaixa m where m.caixa = :caixa");
		q.setParameter("caixa", caixa);
		return q.list();
	}

}
