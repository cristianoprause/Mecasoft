package banco.modelo.report;

import java.math.BigDecimal;
import java.util.Date;

import aplicacao.helper.UsuarioHelper;
import banco.modelo.Pessoa;
import banco.modelo.Veiculo;

public class ServicoPrestadoAnaliticoCliente {

	public static String SERVICOEXECUTADO = "Serviço já executado";
	public static String SERVICOEMEXECUCAO = "Serviço em execução";
	
	private Pessoa empresa = UsuarioHelper.getConfiguracaoPadrao().getRepresentanteEmpresa();
	private Veiculo veiculo;
	private Date dataAbertura;
	private Date dataFechamento;
	private String status;
	private String descricao;
	private BigDecimal valorTotal = BigDecimal.ZERO;
	private BigDecimal iss = BigDecimal.ZERO;
	
	public Pessoa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Pessoa empresa) {
		this.empresa = empresa;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public void setServicoExecutado(boolean executado){
		status = executado ? SERVICOEXECUTADO : SERVICOEMEXECUCAO;
	}
	public Date getDataAbertura() {
		return dataAbertura;
	}
	public void setDataAbertura(Date dataAberutra) {
		this.dataAbertura = dataAberutra;
	}	
	public Date getDataFechamento() {
		return dataFechamento;
	}
	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
	public BigDecimal getIss() {
		return iss;
	}
	public void setIss(BigDecimal iss) {
		this.iss = iss;
	}
	
}
