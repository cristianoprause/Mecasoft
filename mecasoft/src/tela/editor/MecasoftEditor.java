package tela.editor;

import static aplicacao.helper.LayoutHelper.getActiveShell;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.ResourceManager;

import tela.dialog.ErroDialog;
import tela.dialog.SimNaoCancelarDialog;
import aplicacao.exception.ValidationException;
import aplicacao.helper.LayoutHelper;
import banco.connection.HibernateConnection;

public abstract class MecasoftEditor extends EditorPart implements ISaveablePart2{
	
	protected Composite compositeConteudo;
	private Composite compositeBotoes;
	private Boolean showExcluir = true;
	private Boolean showSalvar = true;
	private Button btnExcluir;
	private Button btnSalvar;

	public MecasoftEditor() {
	}
	
	public abstract void salvarRegistro() throws ValidationException;
	public abstract void excluirRegistro();
	public abstract void addComponentes(Composite compositeConteudo);
	
	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		compositeConteudo = new Composite(composite, SWT.BORDER);
		compositeConteudo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		compositeBotoes = new Composite(composite, SWT.BORDER);
		compositeBotoes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeBotoes.setLayout(new GridLayout(3, false));
		
		btnSalvar = new Button(compositeBotoes, SWT.NONE);
		btnSalvar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					salvarRegistro();
					closeThisEditor();
				} catch (ValidationException e4) {
					setErroMessage(e4.getMessage());
					return;
				}
			}
		});
		btnSalvar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/save32.png"));
		btnSalvar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnSalvar.setText("Salvar");
		
		btnExcluir = new Button(compositeBotoes, SWT.NONE);
		btnExcluir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				excluirRegistro();
			}
		});
		btnExcluir.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/delete32.png"));
		btnExcluir.setText("Excluir");
		
		addComponentes(compositeConteudo);
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.getVerticalBar().setPageIncrement(15);
		scrolledComposite.getVerticalBar().setIncrement(15);
		
		if(!showExcluir)
			disposeExcluir();
		if(!showSalvar)
			disposeSalvar();

	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public void setShowExcluir(Boolean showExcluir){
		this.showExcluir = showExcluir;
	}
	
	public Boolean getShowExcluir() {
		return showExcluir;
	}

	public void setShowSalvar(Boolean showSalvar){
		this.showSalvar = showSalvar;
	}
	
	public Boolean getShowSalvar() {
		return showSalvar;
	}
	
	public void disposeSalvar(){
		btnSalvar.dispose();
	}
	
	public void disposeExcluir(){
		btnExcluir.dispose();
	}
	
	public void closeThisEditor(){
		HibernateConnection.commit();
		getEditorSite().getPart().getSite().getWorkbenchWindow().getActivePage().closeEditor(this, false);
	}
	
	@Override
	public int promptToSaveOnClose() {
		SimNaoCancelarDialog sncd = new SimNaoCancelarDialog(getActiveShell(), "Os dados foram alterados, deseja salvar antes de sair?");
		try {
			sncd.open();
			
			if(sncd.getId() == IDialogConstants.OK_ID){
				salvarRegistro();
				closeThisEditor();
				return YES;
			}
			
			if(sncd.getId() == IDialogConstants.CANCEL_ID){
				HibernateConnection.rollBack();
				getSite().getWorkbenchWindow().getActivePage().closeAllEditors(false);
				return NO;
			}
		
		} catch (ValidationException e) {
			setErroMessage(e.getMessage());
		}
		
		return CANCEL;
	}
	
	public Button createNewButton(String text){
		Button btn =  new Button(compositeBotoes, SWT.NONE);
		btn.setText(text);
		return btn;
	}
	
	public Button createNewButton(){
		return createNewButton("");
	}
	
	public void showComponentes(Boolean possuiId){}
	
	public void setErroMessage(String erro){
		new ErroDialog(LayoutHelper.getActiveShell(), erro).open();
	}
	
}
