package banco.DAO;

import java.util.List;

import org.hibernate.Query;

import aplicacao.service.UsuarioService;
import banco.connection.HibernateConnection;
import banco.modelo.Funcionario;
import banco.modelo.Usuario;
import banco.utils.FuncionarioUtils;

public class FuncionarioDAO extends HibernateConnection implements FuncionarioUtils{

	@Override
	public void saveOrUpdate(Funcionario modelo) {
		if(modelo.getId() != null)
			getSession().merge(modelo);
		else
			getSession().persist(modelo);
	}

	@Override
	public void delete(Funcionario modelo) {
	}

	@Override
	public Funcionario find(Long id) {
		Query q = getSession().createQuery("select f from Funcionario f where f.id = :id");
		q.setParameter("id", id);
		return (Funcionario)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Funcionario> findAll() {
		Query q = getSession().createQuery("select f from Funcionario f");
		return q.list();
	}

	@Override
	public List<Funcionario> findAllSemUsuario() {
		List<Usuario> listaUsuarios = new UsuarioService().findAll();
		List<Funcionario> listaFuncionarios = findAll();
		
		for(Usuario u : listaUsuarios){
			listaFuncionarios.remove(u.getFuncionario());
		}
		
		return listaFuncionarios;
	}

}
