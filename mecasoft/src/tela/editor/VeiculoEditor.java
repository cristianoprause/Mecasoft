package tela.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.wb.swt.ResourceManager;

public class VeiculoEditor extends MecasoftEditor {

	public static final String ID = "tela.editor.VeiculoEditor"; //$NON-NLS-1$
	private Text txtMarca;
	private Text txtModelo;
	private Text txtPlaca;
	private Text txtHodometro;
	private Text txtHorimetro;
	private Text txtCliente;

	public VeiculoEditor() {
		
	}

	@Override
	public void salvarRegistro() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluirRegistro() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		compositeConteudo.setLayout(new GridLayout(4, false));
		
		Label lblMarca = new Label(compositeConteudo, SWT.NONE);
		lblMarca.setText("Marca:");
		
		txtMarca = new Text(compositeConteudo, SWT.BORDER);
		txtMarca.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblModelo = new Label(compositeConteudo, SWT.NONE);
		lblModelo.setText("Modelo:");
		
		txtModelo = new Text(compositeConteudo, SWT.BORDER);
		txtModelo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblTipo = new Label(compositeConteudo, SWT.NONE);
		lblTipo.setText("Tipo:");
		
		Combo cbTipo = new Combo(compositeConteudo, SWT.READ_ONLY);
		cbTipo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnAtivo = new Button(compositeConteudo, SWT.CHECK);
		btnAtivo.setText("Ativo");
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblPlaca = new Label(compositeConteudo, SWT.NONE);
		lblPlaca.setText("Placa:");
		
		txtPlaca = new Text(compositeConteudo, SWT.BORDER);
		GridData gd_txtPlaca = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtPlaca.widthHint = 113;
		txtPlaca.setLayoutData(gd_txtPlaca);
		new Label(compositeConteudo, SWT.NONE);
		new Label(compositeConteudo, SWT.NONE);
		
		Label lblHodometro = new Label(compositeConteudo, SWT.NONE);
		lblHodometro.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHodometro.setText("Hod\u00F4metro:");
		
		txtHodometro = new Text(compositeConteudo, SWT.BORDER);
		GridData gd_txtHodometro = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtHodometro.widthHint = 186;
		txtHodometro.setLayoutData(gd_txtHodometro);
		
		Label lblHormetro = new Label(compositeConteudo, SWT.NONE);
		lblHormetro.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHormetro.setText("Hor\u00EDmetro:");
		
		txtHorimetro = new Text(compositeConteudo, SWT.BORDER);
		txtHorimetro.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCliente = new Label(compositeConteudo, SWT.NONE);
		lblCliente.setText("Cliente:");
		
		txtCliente = new Text(compositeConteudo, SWT.BORDER);
		txtCliente.setEnabled(false);
		txtCliente.setEditable(false);
		txtCliente.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelecionarCliente = new Button(compositeConteudo, SWT.NONE);
		btnSelecionarCliente.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarCliente.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnSelecionarCliente.setText("Selecionar");
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

}
