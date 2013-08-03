package banco.DAO;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import banco.connection.EclipseLinkConnection;
import banco.modelo.Pessoa;
import banco.modelo.TipoFuncionario;
import banco.utils.PessoaUtils;

import com.google.inject.persist.Transactional;

public class PessoaDAO extends EclipseLinkConnection implements PessoaUtils{

	@Override
	@Transactional
	public void saveOrUpdate(Pessoa modelo) {
		if(modelo.getId() != null)
			merge(modelo);
		else
			persist(modelo);
	}

	@Override
	@Transactional
	public void delete(Pessoa modelo) {}

	@Override
	public Pessoa find(Long id) {
		Query q = createQueryNoCache("select p from Pessoa p where p.id = :id");
		q.setParameter("id", id);
		return (Pessoa)q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAll() {
		Query q = getEntityManager().createQuery("select p from Pessoa p");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAllByTipoAndStatus(String tipo, Boolean status) {
		
		boolean cliente = false;
		boolean fornecedor = false;
		boolean funcionario = false;
		
		if(tipo != null){
			cliente = tipo.equals(Pessoa.CLIENTE);
			fornecedor = tipo.equals(Pessoa.FORNECEDOR);
			funcionario = tipo.equals(Pessoa.FUNCIONARIO);
		}
		
		Query q = getEntityManager().createQuery("select p from Pessoa p where (p.ativo = cast(:status as boolean) or :status is null) " +
			"and ((p.tipoCliente = cast(:cliente as boolean) and cast(:cliente as boolean) is true) " +
			       "or (p.tipoFornecedor = cast(:fornecedor as boolean) and cast(:fornecedor as boolean) is true) " +
			       "or (p.tipoFuncionario = cast(:funcionario as boolean) and cast(:funcionario as boolean) is true) " +
			       "or (:tipo is null))");
		q.setParameter("status", status)
		.setParameter("cliente", cliente)
		.setParameter("fornecedor", fornecedor)
		.setParameter("funcionario", funcionario)
		.setParameter("tipo", tipo);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAllByTipoFuncionario(TipoFuncionario tipo) {
		Query q = getEntityManager().createQuery("select p from Pessoa p where p.tipo = :tipo");
		q.setParameter("tipo", tipo);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAllFuncionarioAPagar() {
		
		Calendar primeiroDiaMes = Calendar.getInstance();
		primeiroDiaMes.set(Calendar.DAY_OF_MONTH, 1);
		
		Query q = getEntityManager().createQuery("select p from Pessoa p where p.ativo is true and p.tipoFuncionario is true "+
				"and (p.dataUltimoPagto < :primeiroDiaMes or p.dataUltimoPagto is null)");
		q.setParameter("primeiroDiaMes", primeiroDiaMes.getTime());
		
		return q.getResultList();
	}

}
