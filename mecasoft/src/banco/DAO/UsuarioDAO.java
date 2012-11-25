package banco.DAO;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Usuario;
import banco.utils.UsuarioUtils;

public class UsuarioDAO extends HibernateConnection implements UsuarioUtils{

	@Override
	public void saveOrUpdate(Usuario modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Usuario modelo) {
		getSession().delete(modelo);
	}

	@Override
	public Usuario find(Long id) {
		Query q = getSession().createQuery("select u from Usuario u where u.id = :id");
		q.setParameter("id", id);
		return (Usuario)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAll() {
		Query q = getSession().createQuery("select u from Usuario u");
		List<Usuario> lista = q.list();
		return lista;
	}

	@Override
	public Usuario findByLoginSenhaStatus(String login, String senha, Boolean status) {
		try{
			Query q = getSession().createQuery("select u from Usuario u " +
														"where (u.login like :login or :login is null) " +
														"and (u.senha like :senha or :senha is null) " +
														"and (u.ativo is :status or :status is null)");
			q.setParameter("login", login);
			q.setParameter("senha", senha)
			.setParameter("status", status);
			return (Usuario)q.uniqueResult();
		}catch(NoResultException e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAllByStatus(boolean status) {
		Query q = getSession().createQuery("select u from Usuario u where u.ativo is :status");
		q.setParameter("status", status);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAllByLoginAndStatus(String login, Boolean status) {
		Query q = getSession().createQuery("select u from Usuario u where (u.login like :login or :login is null) " +
																	"and (u.ativo is :status or :status is null) ");
		q.setParameter("login", login)
		.setParameter("status", status);
		return q.list();
	}

}
