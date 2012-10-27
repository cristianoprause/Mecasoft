package tela.editingSupport;

import static aplicacao.helper.MessageHelper.openWarning;

import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import aplicacao.helper.FormatterHelper;
import banco.modelo.StatusServico;

public class DataStatusServicoEditinfSupport extends EditingSupport{

	private TableViewer viewer;
	
	public DataStatusServicoEditinfSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		TextCellEditor tce = new TextCellEditor(viewer.getTable());
		return tce;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		StatusServico ss = (StatusServico) element;
		
		if(ss != null)
			return FormatterHelper.getDateFormatData("dd/MM/yyyy HH:mm").format(ss.getData());
		
		return "";
	}

	@Override
	protected void setValue(Object element, Object value) {
		StatusServico ss = (StatusServico) element;
		String v = (String)value;
		
		if(!v.isEmpty()){
			try{
				Date data = FormatterHelper.getDateFormatData("dd/MM/yyyy HH:mm").parse(v);
				ss.setData(data);
				viewer.refresh();
			}catch(Exception e){
				openWarning("Informe a data e hora corretamente no formato: dd/mm/aaaa hh:mm");
			}
		}
		
	}

}
