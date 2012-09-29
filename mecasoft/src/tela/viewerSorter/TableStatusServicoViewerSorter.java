package tela.viewerSorter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import banco.modelo.StatusServico;

public class TableStatusServicoViewerSorter extends ViewerSorter{

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		StatusServico sp1 = (StatusServico)e1;
		StatusServico sp2 = (StatusServico)e2;
		
		return sp2.getData().compareTo(sp1.getData());
	}
	
}
