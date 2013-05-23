package aplicacao.command;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.PontoDialog;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.StatusServicoService;
import banco.modelo.StatusServico;

public class PontoCommand extends AbstractHandler {

	private StatusServicoService service = new StatusServicoService();
	private PontoDialog pd;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		pd = new PontoDialog(getActiveShell());
		
		if(pd.open() == IDialogConstants.OK_ID){
			StatusServico statusFuncionario = service.findStatusFuncionario(pd.getFuncionario());
			
			//caso o status do funcionario seja nulo ou de um serviço concluido, nao deve fazer nada
			if(statusFuncionario == null || statusFuncionario.getStatus().getId().compareTo(
					UsuarioHelper.getConfiguracaoPadrao().getStatusFinalizarServico().getId()) == 0)
				return null;
			
			StatusServico ss = newStatusServico(statusFuncionario);
			service.setStatusServico(ss);
			service.saveOrUpdate();
//			HibernateConnection.commit(ss);
		}
		
		return null;
	}
	
	public StatusServico newStatusServico(StatusServico statusAnterior){
		StatusServico ss = new StatusServico();
		
		ss.setFuncionario(statusAnterior.getFuncionario());
		ss.setServicoPrestado(statusAnterior.getServicoPrestado());

		//caso esteja trabalhando em um serviço, o mesmo deve ser parado
		if(!statusAnterior.getStatus().isPausar())
			ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusFinal());
		//caso contrario, o mesmo sera iniciado, pois esta parado
		else
			ss.setStatus(UsuarioHelper.getConfiguracaoPadrao().getStatusInicio());
		
		return ss;
	}

}
