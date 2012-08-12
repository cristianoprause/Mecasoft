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

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAllByTipoAndStatus(String tipo, boolean status) {
		
		boolean cliente = false;
		boolean fornecedor = false;
		boolean funcionario = false;
		
		if(tipo.equals(Pessoa.CLIENTE))
			cliente = true;
		else if(tipo.equals(Pessoa.FORNECEDOR))
			fornecedor = true;
		else if(tipo.equals(Pessoa.FUNCIONARIO))
			funcionario = true;
		
		Query q = getSession().createQuery("select p from Pessoa p where p.ativo is :status and (p.tipoCliente is :cliente " +
				"or p.tipoFornecedor is :fornecedor or p.tipoFuncionario is :funcionario)");
		q.setParameter("status", status)
		.setParameter("cliente", cliente)
		.setParameter("fornecedor", fornecedor)
		.setParameter("funcionario", funcionario);
		return q.list();
	}

}
