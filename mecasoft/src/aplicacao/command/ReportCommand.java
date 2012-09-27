package aplicacao.command;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.postgresql.Driver;

import tela.view.ReportView;

public abstract class ReportCommand extends AbstractHandler{

	public ReportCommand() {
	}
	
	public JasperPrint getReport(String caminhoRelatorio, Map<String, Object> parametros){

		try {
			String urlBanco = "jdbc:postgresql://localhost:5432/mecasoft";
			
			DriverManager.registerDriver(new Driver());
			
			Connection con = DriverManager.getConnection(urlBanco, "postgres", "admin");
			
			return JasperFillManager.fillReport(caminhoRelatorio, parametros, con);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public ReportView getView(){
		
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			ReportView reportView = (ReportView) page.showView(ReportView.ID);
			page.bringToTop(reportView);
			
			return reportView;
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public abstract Map<String, Object> getParametros();
	
}
