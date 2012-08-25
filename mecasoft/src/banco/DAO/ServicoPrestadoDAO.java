package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.ServicoPrestado;
import banco.utils.ServicoPrestadoUtils;

public class ServicoPrestadoDAO extends HibernateConnection implements ServicoPrestadoUtils{

	@Override
	public void saveOrUpdate(ServicoPrestado modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(ServicoPrestado modelo) {
		getSession().delete(modelo);
	}

	@Override
	public ServicoPrestado find(Long id) {
		Query q = getSession().createQuery("select s from ServicoPrestado s where s.id = :id");
		q.setParameter("id", id);
		return (ServicoPrestado)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServicoPrestado> findAll() {
		Query q = getSession().createQuery("select s from ServicoPrestado s");
		return q.list();
	}

}
