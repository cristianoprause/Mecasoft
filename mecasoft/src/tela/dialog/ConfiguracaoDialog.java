package tela.dialog;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.ValidatorHelper.validar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import aplicacao.exception.ValidationException;
import aplicacao.helper.FileHelper;
import aplicacao.helper.LayoutHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.ConfiguracaoService;
import aplicacao.service.PessoaService;
import aplicacao.service.UsuarioService;
import banco.modelo.Configuracao;
import banco.modelo.Papel;
import banco.modelo.Pessoa;
import banco.modelo.Usuario;

public class ConfiguracaoDialog extends TitleAreaDialog {
	private Logger log = Logger.getLogger(getClass());
	private ConfiguracaoService service = new ConfiguracaoService();
	private UsuarioService usuarioService = new UsuarioService();
	private Usuario user;
	
	private Text txtEmpresa;
	private Text txtSenhaAtual;
	private Text txtNovaSenha;
	private Text txtConfirmarSenha;
	private Text txtLogoEmpresa;
	private Label lblStatusLogo;
	private Label lblIconStatusLogo;

	public ConfiguracaoDialog(Shell parentShell) {
		super(parentShell);
		
		//pega as configurações caso o usuário ja tenha salvo
		if(UsuarioHelper.getConfiguracaoPadrao() == null)
			service.setConfiguracao(new Configuracao());
		else
			service.setConfiguracao(service.find(UsuarioHelper.getConfiguracaoPadrao().getId()));
		
		//permissao do usuário
		if(UsuarioHelper.getUsuarioLogado() == null){
			Papel p = new Papel();
			p.setGerServico(false);
			user = new Usuario();
			user.setPapel(p);
		}else
			user = UsuarioHelper.getUsuarioLogado();
		
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Selecione a pessoa cadastrada que representa a empresa e informe os horarios de espediente");
		setTitle("Configura\u00E7\u00F5es");
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));
		
		Group grpEmpresa = new Group(area, SWT.NONE);
		grpEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpEmpresa.setLayout(new GridLayout(6, false));
		grpEmpresa.setText("Empresa");
		
		Label lblEmpresa = new Label(grpEmpresa, SWT.NONE);
		lblEmpresa.setText("Empresa:");
		
		txtEmpresa = new Text(grpEmpresa, SWT.BORDER);
		txtEmpresa.setEnabled(false);
		GridData gd_txtEmpresa = new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1);
		gd_txtEmpresa.widthHint = 254;
		txtEmpresa.setLayoutData(gd_txtEmpresa);
		
		Button btnSelecionarEmpresa = new Button(grpEmpresa, SWT.NONE);
		btnSelecionarEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSelecionarEmpresa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Pessoa p = selecionarPessoa();
				if(p != null){
					service.getConfiguracao().setRepresentanteEmpresa(p);
					txtEmpresa.setText(p.getNomeFantasia());
				}
			}
		});
		btnSelecionarEmpresa.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarEmpresa.setText("Selecionar");
		
		Label lblLogoDaEmpresa = new Label(grpEmpresa, SWT.NONE);
		lblLogoDaEmpresa.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLogoDaEmpresa.setText("Logo da empresa:");
		
		txtLogoEmpresa = new Text(grpEmpresa, SWT.BORDER);
		txtLogoEmpresa.setEnabled(false);
		txtLogoEmpresa.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Button btnSelecionarLogo = new Button(grpEmpresa, SWT.NONE);
		btnSelecionarLogo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//pega o arquivo
				FileDialog fd = new FileDialog(getActiveShell(), SWT.SINGLE);
				fd.setFilterExtensions(new String[]{"*.png", "*.jpg"});
				String caminho = fd.open();
				
				//adiciona o nome do arquivo as configuracoes
				service.getConfiguracao().setLogoEmpresa(fd.getFileName());
				initDataBindings();
				
				//verifica se o usuário selecionou uma imagem
				if(caminho == null){
					verificaLogo();
					return;
				}
				
				//se o arquivo existir mesmo, move para a pasta do sistema
				File file = new File(caminho);
				if(!file.exists())
					return;
				
				moveLogo(file);
				verificaLogo();
			}
		});
		btnSelecionarLogo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/find16.png"));
		btnSelecionarLogo.setText("Selecionar");
		
		lblIconStatusLogo = new Label(grpEmpresa, SWT.NONE);
		lblIconStatusLogo.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true, 1, 1));
		
		lblStatusLogo = new Label(grpEmpresa, SWT.NONE);
		lblStatusLogo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		new Label(grpEmpresa, SWT.NONE);
		verificaLogo();
		
		Group grpSenha = new Group(area, SWT.NONE);
		grpSenha.setLayout(new GridLayout(2, false));
		grpSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpSenha.setText("Senha");
		
		Label lblSenhaAtual = new Label(grpSenha, SWT.NONE);
		lblSenhaAtual.setText("Senha atual:");
		
		txtSenhaAtual = new Text(grpSenha, SWT.BORDER | SWT.PASSWORD);
		txtSenhaAtual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNovaSenha = new Label(grpSenha, SWT.NONE);
		lblNovaSenha.setText("Nova senha:");
		
		txtNovaSenha = new Text(grpSenha, SWT.BORDER | SWT.PASSWORD);
		txtNovaSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblConfirmarSenha = new Label(grpSenha, SWT.NONE);
		lblConfirmarSenha.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmarSenha.setText("Confirmar senha:");
		
		txtConfirmarSenha = new Text(grpSenha, SWT.BORDER | SWT.PASSWORD);
		txtConfirmarSenha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		initDataBindings();

		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void okPressed() {
		
		try{
			validar(service.getConfiguracao());
			
			//verificar senha do usuario
			if(!txtSenhaAtual.getText().isEmpty() || !txtNovaSenha.getText().isEmpty()){
				if(!txtSenhaAtual.getText().equals(UsuarioHelper.getUsuarioLogado().getSenha())){
					setErrorMessage("Senha incorreta. Informe a senha atual corretamente para alterar a senha ou apague o campo \"Senha atual\".");
					return;
				}
				
				if(!txtNovaSenha.getText().equals(txtConfirmarSenha.getText())){
					setErrorMessage("A senha não coincide com a confirmação.");
					return;
				}
				
				Usuario user = UsuarioHelper.getUsuarioLogado();
				user.setSenha(txtNovaSenha.getText());
				usuarioService.setUsuario(user);
				usuarioService.saveOrUpdate();
			}
			
			service.saveOrUpdate();
			
			UsuarioHelper.setConfiguracaoPadrao(service.getConfiguracao());
			openInformation("Configurações salvas com sucesso!");
//			HibernateConnection.commit(service.getConfiguracao());
			super.okPressed();
			
		}catch (ValidationException e) {
			setErrorMessage(e.getMessage());
		}catch(Exception e){
			setErrorMessage("Informe os horários corretamente");
		}
		
	}
	
	private Pessoa selecionarPessoa(){
		SelecionarItemDialog sid = new SelecionarItemDialog(LayoutHelper.getActiveShell(), new LabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Pessoa)element).getNomeFantasia();
			}
		});
		
		sid.setElements(new PessoaService().findAllAtivos().toArray());
		
		return (Pessoa)sid.getElementoSelecionado();
	}
	
	private void verificaLogo(){
		if (service.getConfiguracao().getLogoEmpresa() == null || service.getConfiguracao().getLogoEmpresa().isEmpty()){
			lblIconStatusLogo.setImage(null);
			lblStatusLogo.setText("");
			return;
		}

		File file = new File(FileHelper.logoPath());

		if (file.exists()) {
			lblIconStatusLogo.setImage(ResourceManager.getPluginImage(
					"mecasoft", "assents/funcoes/ativo16.png"));

			lblStatusLogo.setText("Imagem encontrada");
			lblStatusLogo.setForeground(ResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		}else{
			lblIconStatusLogo.setImage(ResourceManager.getPluginImage("mecasoft", "assents/funcoes/inativo16.png"));
			
			lblStatusLogo.setText("Imagem não encontrada");
			lblStatusLogo.setForeground(ResourceManager.getColor(SWT.COLOR_RED));
		}
	}
	
	private void moveLogo(File file){
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(new File(Configuracao.pastaLogo + service.getConfiguracao().getLogoEmpresa()));
			byte array[] = new byte[10000];
			
			while(fis.read(array) > -1)
				fos.write(array);
			
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(488, 399);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtEmpresaObserveTextObserveWidget = SWTObservables.observeText(txtEmpresa, SWT.Modify);
		IObservableValue servicegetConfiguracaoRepresentanteEmpresanomeFantasiaObserveValue = PojoObservables.observeValue(service.getConfiguracao(), "representanteEmpresa.nomeFantasia");
		bindingContext.bindValue(txtEmpresaObserveTextObserveWidget, servicegetConfiguracaoRepresentanteEmpresanomeFantasiaObserveValue, null, null);
		//
		IObservableValue observeTextTxtLogoEmpresaObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtLogoEmpresa);
		IObservableValue logoEmpresaServicegetConfiguracaoObserveValue = PojoProperties.value("logoEmpresa").observe(service.getConfiguracao());
		bindingContext.bindValue(observeTextTxtLogoEmpresaObserveWidget, logoEmpresaServicegetConfiguracaoObserveValue, null, null);
		//
		return bindingContext;
	}
}
