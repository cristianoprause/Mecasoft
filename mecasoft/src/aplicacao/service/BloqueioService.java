package aplicacao.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BloqueioService {

	public static boolean verificar(){
		try {
			URL url = new URL("http://www.cmp389.xpg.com.br/permissao.html");
			URLConnection con = url.openConnection();
			BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			while(read.ready()){
				if(read.readLine().equals("<b>1</b>"))
					return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
