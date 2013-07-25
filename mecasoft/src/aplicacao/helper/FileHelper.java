package aplicacao.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import mecasoft.Activator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;

public class FileHelper {

	public static File FILE_PROPERTIES = new File(propertiesPath() + File.separator + "mecasoft.properties");
	
	public static String directoryPath(String pasta) {
		try {
			URL confURL = Activator.getDefault().getBundle().getEntry(pasta + "/");
			return FileLocator.toFileURL(confURL).getFile();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String logoPath(){
		String imagem = UsuarioHelper.getConfiguracaoPadrao() == null ? null : UsuarioHelper.getConfiguracaoPadrao().getLogoEmpresa();
		
		return imagem == null || imagem.isEmpty() ? "" : directoryPath("logo") + imagem;
	}
	
	public static String propertiesPath(){
		return userPath() + File.separator + ".mecasoft";
	}
	
	public static String userPath(){
		return System.getProperty("user.home");
	}
	
	public static Map<String, String> getProperties() throws IOException{
		Map<String, String> properties = new HashMap<String, String>();
		
		BufferedReader read = new BufferedReader(new FileReader(FILE_PROPERTIES));
		
		String linha = null;
		while(read.ready()){
			linha = read.readLine();
			properties.put(StringUtils.substringBefore(linha, "="), StringUtils.substringAfter(linha, "="));
		}
		
		return properties;
	}
	
}
