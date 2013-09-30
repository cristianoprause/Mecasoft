package tela.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import aplicacao.helper.LayoutHelper;
import aplicacao.service.OrcamentoService;
import banco.modelo.Orcamento;

public class ParametroRelatorioOrcamentoDialog extends TitleAreaDialog {
	
	private Orcamento orcamento;
	
	private Text txtOrcamento;

	public ParametroRelatorioOrcamentoDialog(Shell parentShell) {
		super(parentShell);
		this.orcamento = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Relat\u00F3rio de or\u00E7amento");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblOrcamento = new Label(container, SWT.NONE);
		lblOrcamento.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrcamento.setText("Or\u00E7amento:");
		
		txtOrcamento = new Text(container, SWT.BORDER);
		txtOrcamento.setEditable(false);
		txtOrcamento.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionar = new Button(container, SWT.NONE);
		btnSelecionar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				orcamento = selecionarOrcamento();
				
				if(orcamento != null)
					txtOrcamento.setText(orcamento.getNumero());
				else
					txtOrcamento.setText("");
			}
		});
		btnSelecionar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionar.setText("Selecionar");

		return area;
	}
	
	@Override
	protected void okPressed() {
		if(orcamento == null){
			setErrorMessage("Selecione o orçamento.");
			return;
		}
		
		super.okPressed();
	}
	
	private Orcamento selecionarOrcamento(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Orcamento)element).getNumero() + " - "  + ((Orcamento)element).getStatusOrcamento();
			}
		});
		
		sid.setElements(new OrcamentoService().findAll().toArray());
		
		return (Orcamento) sid.getElementoSelecionado();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 183);
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

}
