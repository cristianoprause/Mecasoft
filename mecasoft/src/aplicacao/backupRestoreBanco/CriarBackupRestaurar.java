package aplicacao.backupRestoreBanco;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aplicacao.service.CaixaService;
import aplicacao.service.MecasoftService;

@SuppressWarnings("rawtypes")
public class CriarBackupRestaurar {

	private List<MecasoftService> listaService;
	private List<BackupModelo> listaBackup = new ArrayList<BackupModelo>();

	@SuppressWarnings("unchecked")
	public void backup(String caminhoBackup) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(caminhoBackup));
		listaBackup.clear();
		carregarLista();

		for (MecasoftService service : listaService) {
			listaBackup.add(new BackupModelo(fos, service.findAll()));
		}

		for (BackupModelo backup : listaBackup) {
			backup.gravarLinhas();
		}
		
		fos.close();
	}

	private void carregarLista() {
		listaService = new ArrayList<MecasoftService>();

		listaService.add(new CaixaService());
	}

}
