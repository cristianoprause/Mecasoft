package aplicacao.guice;

import javax.persistence.EntityManager;

import banco.DAO.CaixaDAO;
import banco.DAO.ConfiguracaoDAO;
import banco.DAO.DuplicataDAO;
import banco.DAO.DuplicataPagaDAO;
import banco.DAO.FormaPagamentoDAO;
import banco.DAO.ItemServicoDAO;
import banco.DAO.MovimentacaoCaixaDAO;
import banco.DAO.OrcamentoDAO;
import banco.DAO.PapelDAO;
import banco.DAO.ProdutoServicoDAO;
import banco.DAO.ServicoPrestadoDAO;
import banco.DAO.TipoFuncionarioDAO;
import banco.DAO.UsuarioDAO;
import banco.DAO.VeiculoDAO;
import banco.connection.MecasoftEntityManager;
import banco.utils.CaixaUtils;
import banco.utils.ConfiguracaoUtils;
import banco.utils.DuplicataPagaUtils;
import banco.utils.DuplicataUtils;
import banco.utils.FormaPagamentoUtils;
import banco.utils.ItemServicoUtils;
import banco.utils.MovimentacaoCaixaUtils;
import banco.utils.OrcamentoUtils;
import banco.utils.PapelUtils;
import banco.utils.ProdutoServicoUtils;
import banco.utils.ServicoPrestadoUtils;
import banco.utils.TipoFuncionarioUtils;
import banco.utils.UsuarioUtils;
import banco.utils.VeiculoUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class MecasoftModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(PapelUtils.class).to(PapelDAO.class);
		bind(UsuarioUtils.class).to(UsuarioDAO.class);
//		bind(PessoaUtils.class).to(PessoaDAO.class);
		bind(VeiculoUtils.class).to(VeiculoDAO.class);
		bind(TipoFuncionarioUtils.class).to(TipoFuncionarioDAO.class);
		bind(ProdutoServicoUtils.class).to(ProdutoServicoDAO.class);
		bind(ServicoPrestadoUtils.class).to(ServicoPrestadoDAO.class);
		bind(FormaPagamentoUtils.class).to(FormaPagamentoDAO.class);
		bind(DuplicataUtils.class).to(DuplicataDAO.class);
		bind(ConfiguracaoUtils.class).to(ConfiguracaoDAO.class);
		bind(DuplicataPagaUtils.class).to(DuplicataPagaDAO.class);
		bind(CaixaUtils.class).to(CaixaDAO.class);
		bind(MovimentacaoCaixaUtils.class).to(MovimentacaoCaixaDAO.class);
		bind(ItemServicoUtils.class).to(ItemServicoDAO.class);
		bind(OrcamentoUtils.class).to(OrcamentoDAO.class);
	}
	
	@Provides
	public EntityManager getEntityManager(){
		return MecasoftEntityManager.getEntityManager();
	}

}
