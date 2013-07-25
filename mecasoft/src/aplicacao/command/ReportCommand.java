package aplicacao.command;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.postgresql.Driver;

import tela.view.ReportView;
import aplicacao.helper.FileHelper;

public abstract class ReportCommand extends AbstractHandler{

	public ReportCommand() {
	}
	
	public JasperPrint getReport(String caminhoRelatorio){

		try {
			String urlBanco = "jdbc:postgresql://localhost:5432/mecasoft";
			
			DriverManager.registerDriver(new Driver());
			
			Connection con = DriverManager.getConnection(urlBanco, "postgres", "admin");
			
			return JasperFillManager.fillReport(FileHelper.directoryPath("reports").concat(caminhoRelatorio), getParametros(), con);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public JasperPrint getReport(String caminhoRelatorio, List<?> listaObjetos){

		try {
			JRDataSource jrds = new JRBeanCollectionDataSource(listaObjetos);
			return JasperFillManager.fillReport(FileHelper.directoryPath("reports").concat(caminhoRelatorio), getParametros(), jrds);
			
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
	
	public Map<String, Object> getParametros() {
		return new HashMap<String, Object>();
	}
	
}
