package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Caixa;
import banco.modelo.MovimentacaoCaixa;
import banco.utils.MovimentacaoCaixaUtils;

public class MovimentacaoCaixaDAO extends HibernateConnection implements MovimentacaoCaixaUtils{

	@Override
	public void saveOrUpdate(MovimentacaoCaixa modelo) {
		if(modelo.getId() != null)
			getEntityManager().merge(modelo);
		else
			getEntityManager().persist(modelo);
	}

	@Override
	public void delete(MovimentacaoCaixa modelo) {
		getEntityManager().remove(modelo);
	}

	@Override
	public MovimentacaoCaixa find(Long id) {
		Query q = getEntityManager().createQuery("select m from MovimentacaoCaixa m where m.id = :id");
		q.setParameter("id", id);
		return (MovimentacaoCaixa)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentacaoCaixa> findAll() {
		Query q = getEntityManager().createQuery("select m from MovimentacaoCaixa m");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentacaoCaixa> findAllByCaixaAndTipo(Caixa caixa, Character tipo) {
		javax.persistence.Query q = getEntityManager().createQuery("select m from MovimentacaoCaixa m where m.caixa = :caixa " +
																			"and (m.tipo = :tipo or :tipo is null)");
		q.setParameter("caixa", caixa)
		.setParameter("tipo", tipo);
		return q.getResultList();
	}

}
