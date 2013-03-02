package aplicacao.helper;

import java.net.URL;

import mecasoft.Activator;

import org.eclipse.core.runtime.FileLocator;

public class FileHelper {

	public static String caminhoPasta(String pasta) {
		try {
			URL confURL = Activator.getDefault().getBundle().getEntry(pasta + "/");
			return FileLocator.toFileURL(confURL).getFile();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String caminhoLogoEmpresa(){
		String imagem = UsuarioHelper.getConfiguracaoPadrao() == null ? null : UsuarioHelper.getConfiguracaoPadrao().getLogoEmpresa();
		
		return imagem == null || imagem.isEmpty() ? "" : caminhoPasta("logo") + imagem;
	}
	
}
