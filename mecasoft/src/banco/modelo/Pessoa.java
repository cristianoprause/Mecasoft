package banco.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;

import aplicacao.helper.PadraoHelper;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Pessoa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5104439984829411483L;
	
	public static String CLIENTE = "CLIENTE";
	public static String FORNECEDOR = "FORNECEDOR";
	public static String FUNCIONARIO  = "FUNCIONARIO";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private boolean ativo = true;
	
	@Column
	@NotEmpty(message="Informe o nome (fantasia).")
	private String nomeFantasia;
	
	@Column
	private String razaoSocial;
	
	@Column
	@NotEmpty(message="Informe o CPF/CNPJ.")
	private String cpfCnpj;
	
	@Column
	private String rgInscricaoEst;
	
	@Column
	private String foneFax = "";
	
	@Column
	private String celular = "";
	
	@Column
	private String email;
	
	@Column
	private String cep;
	
	@Column
	@NotEmpty(message="Informe a cidade.")
	private String cidade;
	
	@Column
	@NotEmpty(message="Informe o bairro.")
	private String bairro;
	
	@Column
	@NotEmpty(message="Informe a rua.")
	private String rua;
	
	@Column
	@NotEmpty(message="Informe o número.")
	private String numero;
	
	@Column
	private String complemento;
	
	@Column
	private boolean tipoCliente;
	
	@Column
	private boolean tipoFornecedor;
	
	@Column
	private boolean tipoFuncionario;
	
	@OneToMany(mappedBy="cliente")
	@Cascade(value={CascadeType.ALL})
	private List<Veiculo> listaVeiculo = new ArrayList<Veiculo>();
	
	@OneToMany(mappedBy="id.pessoa",orphanRemoval=true)
    @Cascade(value= {CascadeType.ALL})
    private List<ForneceProduto> listaProduto = new ArrayList<ForneceProduto>();
	
	@Column
	private String serie = "";
	
	@Column(precision=14, scale=2)
	private BigDecimal salario;
	
	@Column
	@Temporal(value = TemporalType.DATE)
	private Date dataUltimoPagto;
	
	@Column
	private String carteiraNum = "";
	
	@ManyToOne
	private TipoFuncionario tipo;
	
	public String getStatus(){
		if(ativo)
			return PadraoHelper.ATIVO;
		else
			return PadraoHelper.DESATIVADO;
	}
	
	public String getNome(){
		return razaoSocial == null ? nomeFantasia : razaoSocial;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getRgInscricaoEst() {
		return rgInscricaoEst;
	}

	public void setRgInscricaoEst(String rgInscricaoEst) {
		this.rgInscricaoEst = rgInscricaoEst;
	}

	public String getFoneFax() {
		return foneFax;
	}

	public void setFoneFax(String foneFax) {
		this.foneFax = foneFax;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public boolean getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(boolean tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public boolean getTipoFornecedor() {
		return tipoFornecedor;
	}

	public void setTipoFornecedor(boolean tipoFornecedor) {
		this.tipoFornecedor = tipoFornecedor;
	}

	public boolean getTipoFuncionario() {
		return tipoFuncionario;
	}

	public void setTipoFuncionario(boolean tipoFuncionario) {
		this.tipoFuncionario = tipoFuncionario;
	}

	public List<Veiculo> getListaVeiculo() {
		return listaVeiculo;
	}

	public void setListaVeiculo(List<Veiculo> listaVeiculo) {
		this.listaVeiculo = listaVeiculo;
	}

	public List<ForneceProduto> getListaProduto() {
		return listaProduto;
	}

	public void setListaProduto(List<ForneceProduto> listaProduto) {
		this.listaProduto = listaProduto;
	}
	
	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public Date getDataUltimoPagto() {
		return dataUltimoPagto;
	}

	public void setDataUltimoPagto(Date dataUltimoPagto) {
		this.dataUltimoPagto = dataUltimoPagto;
	}

	public String getCarteiraNum() {
		return carteiraNum;
	}

	public void setCarteiraNum(String carteiraNum) {
		this.carteiraNum = carteiraNum;
	}

	public TipoFuncionario getTipo() {
		return tipo;
	}

	public void setTipo(TipoFuncionario tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
