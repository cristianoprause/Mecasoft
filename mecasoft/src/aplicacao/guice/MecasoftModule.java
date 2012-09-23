package aplicacao.guice;

import banco.DAO.ConfiguracaoDAO;
import banco.DAO.DuplicataDAO;
import banco.DAO.FormaPagamentoDAO;
import banco.DAO.PapelDAO;
import banco.DAO.PessoaDAO;
import banco.DAO.ProdutoServicoDAO;
import banco.DAO.ServicoPrestadoDAO;
import banco.DAO.StatusDAO;
import banco.DAO.StatusServicoDAO;
import banco.DAO.TipoFuncionarioDAO;
import banco.DAO.TipoVeiculoDAO;
import banco.DAO.UsuarioDAO;
import banco.DAO.VeiculoDAO;
import banco.utils.ConfiguracaoUtils;
import banco.utils.DuplicataUtils;
import banco.utils.FormaPagamentoUtils;
import banco.utils.PapelUtils;
import banco.utils.PessoaUtils;
import banco.utils.ProdutoServicoUtils;
import banco.utils.ServicoPrestadoUtils;
import banco.utils.StatusServicoUtils;
import banco.utils.StatusUtils;
import banco.utils.TipoFuncionarioUtils;
import banco.utils.TipoVeiculoUtils;
import banco.utils.UsuarioUtils;
import banco.utils.VeiculoUtils;

import com.google.inject.AbstractModule;

public class MecasoftModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(PapelUtils.class).to(PapelDAO.class);
		bind(UsuarioUtils.class).to(UsuarioDAO.class);
		bind(PessoaUtils.class).to(PessoaDAO.class);
		bind(VeiculoUtils.class).to(VeiculoDAO.class);
		bind(TipoVeiculoUtils.class).to(TipoVeiculoDAO.class);
		bind(TipoFuncionarioUtils.class).to(TipoFuncionarioDAO.class);
		bind(ProdutoServicoUtils.class).to(ProdutoServicoDAO.class);
		bind(StatusUtils.class).to(StatusDAO.class);
		bind(ServicoPrestadoUtils.class).to(ServicoPrestadoDAO.class);
		bind(FormaPagamentoUtils.class).to(FormaPagamentoDAO.class);
		bind(DuplicataUtils.class).to(DuplicataDAO.class);
		bind(ConfiguracaoUtils.class).to(ConfiguracaoDAO.class);
		bind(StatusServicoUtils.class).to(StatusServicoDAO.class);
	}

}
