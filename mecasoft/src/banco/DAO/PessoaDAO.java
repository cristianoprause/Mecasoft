package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Pessoa;
import banco.utils.PessoaUtils;

public class PessoaDAO extends HibernateConnection implements PessoaUtils{

	@Override
	public void saveOrUpdate(Pessoa modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Pessoa modelo) {}

	@Override
	public Pessoa find(Long id) {
		Query q = getSession().createQuery("select p from Pessoa p where p.id = :id");
		q.setParameter("id", id);
		return (Pessoa)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAll() {
		Query q = getSession().createQuery("select p from Pessoa p");
		return q.list();
	}

}
