package aplicacao.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import banco.modelo.Cep;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;

@SuppressWarnings("deprecation")
public class CepService {

	@SuppressWarnings("rawtypes")
	public Cep getEndereco(String cep) {
		
		// a string da url		
		String urlString = "http://cep.republicavirtual.com.br/web_cep.php";

		// os parametros a serem enviados
		Properties parameters = new Properties();
		parameters.setProperty("cep", cep);
		parameters.setProperty("formato", "xml");

		// o iterador, para criar a URL
		Iterator i = parameters.keySet().iterator();
		// o contador
		int counter = 0;

		// enquanto ainda existir parametros
		while (i.hasNext()) {

			// pega o nome
			String name = (String) i.next();
			// pega o valor
			String value = parameters.getProperty(name);

			// adiciona com um conector (? ou &)
			// o primeiro � ?, depois s�o &
			urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;
			
		}

		try {
			// cria o objeto url
			URL url = new URL(urlString);

			// cria o objeto httpurlconnection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// seta o metodo
			connection.setRequestProperty("Request-Method", "GET");

			// seta a variavel para ler o resultado
			connection.setDoInput(true);
			connection.setDoOutput(false);

			// conecta com a url destino
			connection.connect();

			// abre a conex�o pra input
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			// le ate o final
			StringBuffer newData = new StringBuffer();
			String s = "";
			while (null != ((s = br.readLine()))) {
				newData.append(s);
			}
			br.close();

			// Controi classe a partir do XML 
			XStream xstream = new XStream(new DomDriver());
			Annotations.configureAliases(xstream, Cep.class);
			xstream.alias("webservicecep", Cep.class);
			Cep endereco = (Cep) xstream.fromXML(newData.toString());
			
			return endereco;

		} catch (Exception e) {
			return null;
		}

	}

}
