package aplicacao.backupRestoreBanco;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import aplicacao.backupRestoreBanco.BackupProtocolo;

public class BackupModelo {

	private FileOutputStream fos;
	private List<Object> listElement;

	// private List<Annotation> listValidAnnotation = new
	// ArrayListIterator(Id.class);

	public BackupModelo(FileOutputStream fos, List<Object> listElement) {
		this.fos = fos;
		this.listElement = listElement;
	}

	public void gravarLinhas() throws IOException {
		try {
			for (Object element : listElement) {
				// protocolo para gravar classe e o nome da classe
				fos.write(BackupProtocolo.CLASSE.getValor());
				fos.write(element.getClass().getSimpleName().getBytes());

				// grava o valor de cada metodo que contenha alguma das
				// anotacoes padroes
				for (Method metodo : element.getClass().getMethods()) {
					if (metodo.getName().startsWith("get") && !metodo.getName().equals("getClass"))
						gravarColuna(metodo.invoke(element, new Object[] {}));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void gravarColuna(Object element) throws IOException {
		fos.write(BackupProtocolo.COLUNA.getValor());
		String valor = null;

		if (element instanceof Long)
			valor = ((Long) element).toString();
		else if (element instanceof BigDecimal)
			valor = ((BigDecimal) element).toString();
		else if (element instanceof Date)
			valor = ((Date) element).toString();

		if (valor != null) 
			fos.write(valor.getBytes());
		else
			fos.write("NULL".getBytes());

	}

}
