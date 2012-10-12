package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Configuracao;
import banco.utils.ConfiguracaoUtils;

public class ConfiguracaoDAO extends HibernateConnection implements ConfiguracaoUtils{

	@Override
	public void saveOrUpdate(Configuracao modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Configuracao modelo) {
		getSession().delete(modelo);
	}

	@Override
	public Configuracao find(Long id) {
		Query q = getSession().createQuery("select c from Configuracao c where c.id = :id");
		q.setParameter("id", id);
		return (Configuracao)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Configuracao> findAll() {
		Query q = getSession().createQuery("select c from Configuracao c");
		return q.list();
	}

}
