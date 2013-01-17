package aplicacao.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import aplicacao.helper.UsuarioHelper;
import aplicacao.service.ServicoPrestadoService;
import aplicacao.service.StatusServicoService;
import banco.connection.HibernateConnection;
import banco.modelo.ServicoPrestado;
import banco.modelo.StatusServico;

public class AtualizarStatusJob extends Job{

	private ServicoPrestadoService servicoService;
	private StatusServicoService service;
	
	public AtualizarStatusJob(String name) {
		super(name);
		this.servicoService = new ServicoPrestadoService();
		this.service = new StatusServicoService();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
 		List<ServicoPrestado> servicos = servicoService.findAllNaoConcluidosAutomatic();
		monitor.beginTask("Adicionando os status...", servicos.size());
		
		for(ServicoPrestado sp : servicos){
			atualizar(sp);
			monitor.worked(1);
			servicoService.setServicoPrestado(sp);
			servicoService.saveOrUpdateAutomatic();
		}
		
		//ap�s concluir as atualiza��es, ele aguarda o usuario commitar algo que deva ser commitado
		//ap�s o usuario commitar, ele commita
//		while(HibernateConnection.getSession().isDirty()){}
		HibernateConnection.autoCommit();
		
		schedule(3600000);
		return Status.OK_STATUS;
	}
	
	public void atualizar(ServicoPrestado servico){
		if(UsuarioHelper.getConfiguracaoPadrao() != null){
			//pega ultimo status do servi�o
			StatusServico statusAtual = servico.getUltimoStatus();
			
			//servi�o ainda n�o iniciado, n�o h� a necessidade de atualizar status
			if(statusAtual == null)
				return;
			
			//pega ultimo status do funcionario registrado no status do servi�o
			StatusServico statusFuncionario = service.findStatusFuncionario(statusAtual.getFuncionario());
			
			//caso o status atual do funcionario seja diferente do status atual do servi�o,
			//este servi�o nao deve ser atualizado, pois esta parado e o funcionario esta em outro servico
			if(!statusAtual.equals(statusFuncionario))
				return;
			
			if(statusAtual != null && (!statusAtual.getStatus().isPausar() 
					                  || statusAtual.getStatus().equals(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal()))){
				//calendar para saber de qual data come�ar a verificar que � a data do ultimo status adicionado
				Calendar dataUltimoStatus = Calendar.getInstance();
				dataUltimoStatus.setTime(statusAtual.getData());
				
				//calendar para rodar o loop e adicionando os status necessarios ate o dia atual
				//hora � zerada para nao gerar problemas
				Calendar data = Calendar.getInstance();
				data.setTime(dataUltimoStatus.getTime());
				data.set(Calendar.AM_PM, Calendar.AM);
				data.set(Calendar.HOUR, 0);
				data.set(Calendar.MINUTE, 0);
				data.set(Calendar.SECOND, 0);
				
				
				do{
					//ignora domingos
					if(data.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
						Calendar dt = Calendar.getInstance();
						
						//se a data do status for menor que a data que deve ser colocada, cria um novo status para add o servi�o
						//Inicio manh� se ja nao tiver iniciado
						if(UsuarioHelper.getConfiguracaoPadrao().getDtInicioManha() != null && statusAtual.getStatus().isPausar()){
							dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtInicioManha());
							dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
							
							if(dt.compareTo(dataUltimoStatus) > 0 && dt.getTime().compareTo(new Date()) <= 0){
								StatusServico ss = new StatusServico();
								ss.setData(dt.getTime());
								ss.setFuncionario(statusAtual.getFuncionario());
								ss.setServicoPrestado(statusAtual.getServicoPrestado());
								ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusInicio());
								
								servico.getListaStatus().add(ss);
								
								//como mudou, agora o ss � o statusAtual do servi�o
								statusAtual = ss;
							}
							
						}
						
						//Final manh� se ja nao estiver parado
						if(UsuarioHelper.getConfiguracaoPadrao().getDtFinalManha() != null && !statusAtual.getStatus().isPausar()){
							dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtFinalManha());
							dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
							
							if(dt.compareTo(dataUltimoStatus) > 0 && dt.getTime().compareTo(new Date()) <= 0){
								StatusServico ss = new StatusServico();
								ss.setData(dt.getTime());
								ss.setFuncionario(statusAtual.getFuncionario());
								ss.setServicoPrestado(statusAtual.getServicoPrestado());
								ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal());
								
								servico.getListaStatus().add(ss);
								
								//como mudou, agora o ss � o statusAtual do servi�o
								statusAtual = ss;
							}
						}
						
						//Inicio tarde se ja nao tiver iniciado
						if(UsuarioHelper.getConfiguracaoPadrao().getDtInicioTarde() != null && statusAtual.getStatus().isPausar()){
							dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtInicioTarde());
							dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
							
							if(dt.compareTo(dataUltimoStatus) > 0 && dt.getTime().compareTo(new Date()) <= 0){
								StatusServico ss = new StatusServico();
								ss.setData(dt.getTime());
								ss.setFuncionario(statusAtual.getFuncionario());
								ss.setServicoPrestado(statusAtual.getServicoPrestado());
								ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusInicio());
								
								servico.getListaStatus().add(ss);
								
								//como mudou, agora o ss � o statusAtual do servi�o
								statusAtual = ss;
							}
							
						}
						
						//Final tarde se ja nao estiver parado
						if(UsuarioHelper.getConfiguracaoPadrao().getDtFinalTarde() != null && !statusAtual.getStatus().isPausar()){
							dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtFinalTarde());
							dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
							
							if(dt.compareTo(dataUltimoStatus) > 0 && dt.getTime().compareTo(new Date()) <= 0){
								StatusServico ss = new StatusServico();
								ss.setData(dt.getTime());
								ss.setFuncionario(statusAtual.getFuncionario());
								ss.setServicoPrestado(statusAtual.getServicoPrestado());
								ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal());
								
								servico.getListaStatus().add(ss);
								
								//como mudou, agora o ss � o statusAtual do servi�o
								statusAtual = ss;
							}
						}
					}
						
					//adiciona 1 dia
					data.add(Calendar.DAY_OF_MONTH, 1);
					
				}while(data.getTime().compareTo(new Date()) <= 0);
			}
		}
	}

	public ServicoPrestadoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoPrestadoService servicoService) {
		this.servicoService = servicoService;
	}

}
