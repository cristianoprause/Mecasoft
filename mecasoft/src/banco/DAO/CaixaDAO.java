package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.Caixa;
import banco.utils.CaixaUtils;

public class CaixaDAO extends EclipseLinkConnection implements CaixaUtils{

	@Override
	public void saveOrUpdate(Caixa modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(Caixa modelo) {
		remove(modelo);
	}

	@Override
	public Caixa find(Long id) {
		Query q = getEntityManager().createQuery("select c from Caixa c where c.id = :id");
		q.setParameter("id", id);
		return (Caixa)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Caixa> findAll() {
		Query q = getEntityManager().createQuery("select c from Caixa c");
		return q.getResultList();
	}

	@Override
	public Caixa findUltimoCaixaByStatus(Boolean status) {
		try{
			Query q = getEntityManager().createQuery("select c from Caixa c where c.id = (select max(ca.id) from Caixa ca " +
											"where (:status is null) or (ca.dataFechamento is null and :status is true) " +
											"or (ca.dataFechamento is not null and :status is false))");
			q.setParameter("status", status);
			return (Caixa) q.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

}
