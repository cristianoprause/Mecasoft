package tela.view;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.jasperassistant.designer.viewer.ViewerComposite;

public class ReportView extends ViewPart {

	public static final String ID = "tela.view.ReportView"; //$NON-NLS-1$
	private ViewerComposite view;

	public ReportView() {
		createActions();
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		{
			view = new ViewerComposite(container, SWT.NONE);
		}

	}
	
	public void setReport(JasperPrint jPrint, String title){
		view.getReportViewer().setDocument(jPrint);
		setPartName(title);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
	}

	@Override
	public void setFocus() {
	}

}
