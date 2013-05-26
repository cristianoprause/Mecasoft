package banco.DAO;

import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.Configuracao;
import banco.utils.ConfiguracaoUtils;

public class ConfiguracaoDAO extends EclipseLinkConnection implements ConfiguracaoUtils{

	@Override
	public void saveOrUpdate(Configuracao modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	public void delete(Configuracao modelo) {
		remove(modelo);
	}

	@Override
	public Configuracao find(Long id) {
		Query q = getEntityManager().createQuery("select c from Configuracao c where c.id = :id");
		q.setParameter("id", id);
		return (Configuracao)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Configuracao> findAll() {
		Query q = getEntityManager().createQuery("select c from Configuracao c");
		return q.getResultList();
	}

}
