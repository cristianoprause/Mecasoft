package tela.editor;

import org.eclipse.core.runtime.IProgressMonitor;
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
import aplicacao.helper.LayoutHelper;
import aplicacao.helper.MessageHelper;
import banco.connection.HibernateConnection;

public abstract class MecasoftEditor extends EditorPart implements ISaveablePart2{
	private Boolean showExcluir = true;
	protected Composite compositeConteudo;
	private Composite compositeBotoes;

	public MecasoftEditor() {
	}
	
	public abstract void salvarRegistro();
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
		compositeBotoes.setLayout(new GridLayout(2, false));
		
		Button btnSalvar = new Button(compositeBotoes, SWT.NONE);
		btnSalvar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				salvarRegistro();
			}
		});
		btnSalvar.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/save32.png"));
		btnSalvar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnSalvar.setText("Salvar");
		
		Button btnExcluir = new Button(compositeBotoes, SWT.NONE);
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
			btnExcluir.dispose();

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
	
	public void closeThisEditor(){
		HibernateConnection.commit();
		getEditorSite().getPart().getSite().getWorkbenchWindow().getActivePage().closeEditor(this, false);
	}
	
	@Override
	public void setFocus() {
	}
	
	@Override
	public int promptToSaveOnClose() {
		if(MessageHelper.openQuestion("Os dados foram alterados, deseja salvar antes de sair?")){
			salvarRegistro();
			return YES;
			
		}
		else{
			HibernateConnection.rollBack();
			getSite().getWorkbenchWindow().getActivePage().closeAllEditors(false);
			return NO;
		}
		
//		return CANCEL;
	}
	
	public Button createNewButton(){
		return new Button(compositeBotoes, SWT.NONE);
	}
	
	public void showComponentes(Boolean possuiId){}
	
	public void setErroMessage(String erro){
		new ErroDialog(LayoutHelper.getActiveShell(), erro).open();
	}
}
