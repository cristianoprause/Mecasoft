package aplicacao.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;

import aplicacao.helper.UsuarioHelper;
import banco.modelo.Papel;

public class PermissaoUsuarioCommand extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<String> permissoes = new ArrayList<String>();
		
		Papel papel = null;
		papel = UsuarioHelper.getUsuarioLogado().getPapel();
		
		IWorkbenchActivitySupport activitySupport = PlatformUI.getWorkbench().getActivitySupport();
		IActivityManager activityManager = activitySupport.getActivityManager();
		Set<String> enabledActivities = new HashSet<String>();
		
		if(papel == null || papel.getCadUsuario()){
			permissoes.add("tela.view.activityUsuario");
			permissoes.add("tela.view.activityPapel");
		}
		
		if(papel == null || papel.getCadPessoa()){
			permissoes.add("tela.view.activityPessoa");
			permissoes.add("tela.view.activityTipoFuncionario");
		}
		
		if(papel == null || papel.getCadVeiculo())
			permissoes.add("tela.view.activityVeiculo");
		
		if(papel == null || papel.getCadProduto())
			permissoes.add("tela.view.activityProduto");
		
		if(papel == null || papel.getCadServico())
			permissoes.add("tela.view.activityServico");
		
		if(papel == null || papel.getCadFormaPagto())
			permissoes.add("tela.view.activityFormaPagto");
		
		if(papel == null || papel.getGerServico()){
			permissoes.add("tela.view.activityStatus");
			permissoes.add("tela.view.activityServicoPrestado");
		}
		
		if(papel == null || papel.getGerDuplicata())
			permissoes.add("tela.view.activityGerarDuplicata");
		
		if(papel == null || papel.getGerCaixa())
			permissoes.add("tela.view.activityCaixa");
		
		if(papel == null || papel.getGerarRelatorio())
			permissoes.add("tela.view.activityRelatorio");

		for(String permissao : permissoes)
			if (activityManager.getActivity(permissao).isDefined())
				enabledActivities.add(permissao);
		
		activitySupport.setEnabledActivityIds(enabledActivities);
			
		return null;
	}

	
}
