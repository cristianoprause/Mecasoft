package mecasoft;

import static aplicacao.helper.MessageHelper.openQuestion;
import static aplicacao.helper.MessageHelper.openWarning;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.handlers.IHandlerService;

import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.CaixaService;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private CaixaService caixaService = new CaixaService();
	private Logger log = Logger.getLogger(getClass());
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1000, 800));
		configurer.setShowCoolBar(false);
		configurer.setShowProgressIndicator(true);
		configurer.setShowStatusLine(true);
		configurer.setTitle("MECASOFT");
	}
	
	private void verificacaoMenus() {
		// Chamada ao command que vai habilitar os menus
		IHandlerService handlerService = (IHandlerService) Activator.getDefault().getWorkbench().getService(IHandlerService.class);
		try {
			handlerService.executeCommand("mecasoft.permissaoUsuarioCommand", null);
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	@Override
	public boolean preWindowShellClose() {
		return openQuestion("Tem certeza que deseja finalizar o sistema?");
	}
	
	public void verificacaoCaixa(){
		UsuarioHelper.setCaixa(caixaService.findCaixaAtual());
		
		if(UsuarioHelper.getCaixa() != null){
			Calendar dtOpenCaixa = Calendar.getInstance();
			dtOpenCaixa.setTime(UsuarioHelper.getCaixa().getDataAbertura());
			
			Calendar dtAtual = Calendar.getInstance();
			
			//verifica se o caixa foi aberto hoje
			boolean isAbertoHoje = false;
			if(dtOpenCaixa.get(Calendar.YEAR) == dtAtual.get(Calendar.YEAR))
				if(dtOpenCaixa.get(Calendar.MONTH) == dtAtual.get(Calendar.MONTH))
					if(dtOpenCaixa.get(Calendar.DAY_OF_MONTH) == dtAtual.get(Calendar.DAY_OF_MONTH))
						isAbertoHoje = true;
			
			if(!isAbertoHoje)
				openWarning("O caixa encontra-se aberto desde o dia " + FormatterHelper.getDateFormatData().format(dtOpenCaixa.getTime()));
		}	
	}
	
	@Override
	public void postWindowOpen() {
		verificacaoMenus();
		verificacaoCaixa();
	}
	
}
