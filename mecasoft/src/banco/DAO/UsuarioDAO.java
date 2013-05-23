package banco.DAO;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Usuario;
import banco.utils.UsuarioUtils;

public class UsuarioDAO extends HibernateConnection implements UsuarioUtils{

	@Override
	public void saveOrUpdate(Usuario modelo) {
		if(modelo.getId() != null)
			getEntityManager().merge(modelo);
		else
			getEntityManager().persist(modelo);
	}

	@Override
	public void delete(Usuario modelo) {
		getEntityManager().remove(modelo);
	}

	@Override
	public Usuario find(Long id) {
		Query q = getEntityManager().createQuery("select u from Usuario u where u.id = :id");
		q.setParameter("id", id);
		return (Usuario)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAll() {
		Query q = getEntityManager().createQuery("select u from Usuario u");
		List<Usuario> lista = q.getResultList();
		return lista;
	}

	@Override
	public Usuario findByLoginSenhaStatus(String login, String senha, Boolean status) {
		try{
			Query q = getEntityManager().createQuery("select u from Usuario u " +
														"where (u.login like :login or :login is null) " +
														"and (u.senha like :senha or :senha is null) " +
														"and (u.ativo = :status or :status is null)");
			q.setParameter("login", login)
			.setParameter("senha", senha)
			.setParameter("status", status);
			return (Usuario)q.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAllByStatus(boolean status) {
		Query q = getEntityManager().createQuery("select u from Usuario u where u.ativo is :status");
		q.setParameter("status", status);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> findAllByLoginAndStatus(String login, Boolean status) {
		Query q = getEntityManager().createQuery("select u from Usuario u where (u.login like :login or :login is null) " +
																	"and (u.ativo is :status or :status is null) ");
		q.setParameter("login", login)
		.setParameter("status", status);
		return q.getResultList();
	}

}
