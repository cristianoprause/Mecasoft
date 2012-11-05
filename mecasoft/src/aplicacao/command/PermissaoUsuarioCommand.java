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
		
		Papel papel = UsuarioHelper.getUsuarioLogado().getPapel();
		
		IWorkbenchActivitySupport activitySupport = PlatformUI.getWorkbench().getActivitySupport();
		IActivityManager activityManager = activitySupport.getActivityManager();
		Set<String> enabledActivities = new HashSet<String>();
		
		if(papel.getCadUsuario()){
			permissoes.add("tela.view.activityUsuario");
			permissoes.add("tela.view.activityPapel");
		}
		
		if(papel.getCadPessoa()){
			permissoes.add("tela.view.activityPessoa");
			permissoes.add("tela.view.activityTipoFuncionario");
		}
		
		if(papel.getCadVeiculo()){
			permissoes.add("tela.view.activityVeiculo");
			permissoes.add("tela.view.activityTipoVeiculo");
		}
		
		if(papel.getCadProduto())
			permissoes.add("tela.view.activityProduto");
		
		if(papel.getCadServico())
			permissoes.add("tela.view.activityServico");
		
		if(papel.getGerServico())
			permissoes.add("tela.view.activityStatus");

		for(String permissao : permissoes)
			if (activityManager.getActivity(permissao).isDefined())
				enabledActivities.add(permissao);
		
		activitySupport.setEnabledActivityIds(enabledActivities);
			
		return null;
	}

	
}
