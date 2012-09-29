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
import banco.connection.HibernateConnection;
import banco.modelo.ServicoPrestado;
import banco.modelo.StatusServico;

public class AtualizarStatusJob extends Job{

	private ServicoPrestadoService servicoService;
	
	public AtualizarStatusJob(String name) {
		super(name);
		this.servicoService = new ServicoPrestadoService();
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
		
		//após concluir as atualizações, ele commita
		HibernateConnection.autoCommit();
		
//		schedule(3600000);
		schedule(60000);
		return Status.OK_STATUS;
	}
	
	public static void atualizar(ServicoPrestado servico){
		if(UsuarioHelper.getConfiguracaoPadrao() != null){
			StatusServico statusAtual = servico.getUltimoStatus();
			
			if(statusAtual != null && (!statusAtual.getStatus().isPausar() 
					                  || statusAtual.getStatus().equals(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal()))){
				//calendar para saber de qual data começar a verificar que é a data do ultimo status adicionado
				Calendar dataUltimoStatus = Calendar.getInstance();
				dataUltimoStatus.setTime(statusAtual.getData());
				
				//calendar para rodar o loop e adicionando os status necessarios ate o dia atual
				Calendar data = Calendar.getInstance();
				data.setTime(dataUltimoStatus.getTime());
				
				do{
					Calendar dt = Calendar.getInstance();
					
					//se a data do status for menor que a data que deve ser colocada, cria um novo status para add o serviço
					//Inicio manhã
					if(UsuarioHelper.getConfiguracaoPadrao().getDtInicioManha() != null){
						dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtInicioManha());
						dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
						
						if(dt.compareTo(dataUltimoStatus) > 0){
							StatusServico ss = new StatusServico();
							ss.setData(dt.getTime());
							ss.setFuncionario(statusAtual.getFuncionario());
							ss.setServicoPrestado(statusAtual.getServicoPrestado());
							ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusInicio());
							
							servico.getListaStatus().add(ss);
						}
						
					}
					
					//Final manhã
					if(UsuarioHelper.getConfiguracaoPadrao().getDtFinalManha() != null){
						dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtFinalManha());
						dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
						
						if(dt.compareTo(dataUltimoStatus) > 0){
							StatusServico ss = new StatusServico();
							ss.setData(dt.getTime());
							ss.setFuncionario(statusAtual.getFuncionario());
							ss.setServicoPrestado(statusAtual.getServicoPrestado());
							ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal());
							
							servico.getListaStatus().add(ss);
						}
					}
					
					//Inicio tarde
					if(UsuarioHelper.getConfiguracaoPadrao().getDtInicioTarde() != null){
						dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtInicioTarde());
						dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
						
						if(dt.compareTo(dataUltimoStatus) > 0){
							StatusServico ss = new StatusServico();
							ss.setData(dt.getTime());
							ss.setFuncionario(statusAtual.getFuncionario());
							ss.setServicoPrestado(statusAtual.getServicoPrestado());
							ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusInicio());
							
							servico.getListaStatus().add(ss);
						}
						
					}
					
					//Final tarde
					if(UsuarioHelper.getConfiguracaoPadrao().getDtFinalTarde() != null){
						dt.setTime(UsuarioHelper.getConfiguracaoPadrao().getDtFinalTarde());
						dt.set(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
						
						if(dt.compareTo(dataUltimoStatus) > 0){
							StatusServico ss = new StatusServico();
							ss.setData(dt.getTime());
							ss.setFuncionario(statusAtual.getFuncionario());
							ss.setServicoPrestado(statusAtual.getServicoPrestado());
							ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal());
							
							servico.getListaStatus().add(ss);
						}
					}
					
					//adiciona 1 dia
					data.add(Calendar.DAY_OF_MONTH, 1);
					
				}while(data.getTime().compareTo(new Date()) < 0);
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
