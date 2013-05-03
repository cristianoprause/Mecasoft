package banco.DAO;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;

import banco.connection.HibernateConnection;
import banco.modelo.Pessoa;
import banco.modelo.TipoFuncionario;
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
		Query q = createQuery("select p from Pessoa p where p.id = :id");
		q.setParameter("id", id);
		return (Pessoa)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAll() {
		Query q = createQuery("select p from Pessoa p");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAllByTipoAndStatus(String tipo, Boolean status) {
		
		boolean cliente = false;
		boolean fornecedor = false;
		boolean funcionario = false;
		
		if(tipo.equals(Pessoa.CLIENTE))
			cliente = true;
		else if(tipo.equals(Pessoa.FORNECEDOR))
			fornecedor = true;
		else if(tipo.equals(Pessoa.FUNCIONARIO))
			funcionario = true;
		
		Query q = createQuery("select p from Pessoa p where (p.ativo is :status or :status is null) " +
			"and ((p.tipoCliente is :cliente and :cliente is true) " +
			       "or (p.tipoFornecedor is :fornecedor and :fornecedor is true) " +
			       "or (p.tipoFuncionario is :funcionario and :funcionario is true) " +
			       "or (:tipo like ''))");
		q.setParameter("status", status)
		.setParameter("cliente", cliente)
		.setParameter("fornecedor", fornecedor)
		.setParameter("funcionario", funcionario)
		.setParameter("tipo", tipo);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAllByTipoFuncionario(TipoFuncionario tipo) {
		Query q = createQuery("select p from Pessoa p where p.tipo = :tipo");
		q.setParameter("tipo", tipo);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAllFuncionarioAPagar() {
		
		Calendar primeiroDiaMes = Calendar.getInstance();
		primeiroDiaMes.set(Calendar.DAY_OF_MONTH, 1);
		
		Query q = createQuery("select p from Pessoa p where p.ativo is true and p.tipoFuncionario is true "+
				"and (p.dataUltimoPagto < :primeiroDiaMes or p.dataUltimoPagto is null)");
		q.setParameter("primeiroDiaMes", primeiroDiaMes.getTime());
		
		return q.list();
	}

}
