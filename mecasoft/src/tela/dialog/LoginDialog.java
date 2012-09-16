package tela.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aplicacao.helper.UsuarioHelper;
import aplicacao.service.UsuarioService;
import banco.modelo.Usuario;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class LoginDialog extends TitleAreaDialog {
	private Text txtUsuario;
	private Text txtSenha;
	private UsuarioService service;
	private Usuario usuario;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LoginDialog(Shell parentShell) {
		super(parentShell);
		service = new UsuarioService();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Digite o usu\u00E1rio e a senha");
		setTitle("Login");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblUsuario = new Label(container, SWT.NONE);
		lblUsuario.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsuario.setText("Usu\u00E1rio");
		
		txtUsuario = new Text(container, SWT.BORDER);
		txtUsuario.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSenha = new Label(container, SWT.NONE);
		lblSenha.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSenha.setText("Senha");
		
		txtSenha = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtSenha.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtSenha.selectAll();
			}
		});
		txtSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 215);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == OK){
			
			if(txtUsuario.getText().isEmpty()){
				setErrorMessage("Usuário não informado");
				txtUsuario.setFocus();
				return;
			}
			
			usuario = service.login(txtUsuario.getText(), txtSenha.getText());
			
			if(usuario == null){
				setErrorMessage("Usuário não encontrado ou a senha esta incorreta. Verifique");
				txtUsuario.setFocus();
				txtUsuario.selectAll();
				return;
			}
			UsuarioHelper.setUsuario(usuario);
			
		}
		super.buttonPressed(buttonId);
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
