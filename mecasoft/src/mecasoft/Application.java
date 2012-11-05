package mecasoft;

import java.util.List;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import tela.dialog.LoginDialog;
import aplicacao.helper.UsuarioHelper;
import aplicacao.job.AtualizarStatusJob;
import aplicacao.service.ConfiguracaoService;
import banco.connection.HibernateConnection;
import banco.modelo.Configuracao;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private ConfiguracaoService configuracaoService = new ConfiguracaoService();
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		try {
			new HibernateConnection().initSystem();
			HibernateConnection.openConnection();
			LoginDialog ld = new LoginDialog(display.getActiveShell());
			if(ld.open() == IDialogConstants.OK_ID){
				verificacaoConfiguracoes();
				new AtualizarStatusJob("Atualizando status dos serviços...").schedule();
				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				if (returnCode == PlatformUI.RETURN_RESTART) {
					return IApplication.EXIT_RESTART;
				}
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
	public void verificacaoConfiguracoes(){
		List<Configuracao> configuracoes = configuracaoService.findAll();
		
		if(!configuracoes.isEmpty())
			UsuarioHelper.setConfiguracaoPadrao(configuracoes.get(0));
	}
}
