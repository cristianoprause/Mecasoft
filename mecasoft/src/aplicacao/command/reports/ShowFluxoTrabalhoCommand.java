package aplicacao.command.reports;

import static aplicacao.helper.LayoutHelper.getActiveShell;
import static aplicacao.helper.MessageHelper.openError;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;

import tela.dialog.ParametroRelatorioFluxoTrabalhoDialog;
import aplicacao.command.ReportCommand;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.ReportHelper;
import aplicacao.service.ServicoPrestadoService;
import aplicacao.service.StatusServicoService;
import banco.modelo.ServicoPrestado;
import banco.modelo.StatusServico;
import banco.modelo.report.FluxoTrabalho;

public class ShowFluxoTrabalhoCommand extends ReportCommand{

	private ParametroRelatorioFluxoTrabalhoDialog prftd;
	private StatusServicoService statusService = new StatusServicoService();
	private ServicoPrestadoService servicoService = new ServicoPrestadoService();
	private List<StatusServico> statusPorFuncionario;
	private List<StatusServico> statusPorServico;
	private List<FluxoTrabalho> listaFluxo;
	private boolean novaLinha;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		prftd = new ParametroRelatorioFluxoTrabalhoDialog(getActiveShell());
		if(prftd.open() == IDialogConstants.OK_ID){
			
			List<FluxoTrabalho> listaFluxo = getLinesReport();
			JasperPrint jPrint = getReport(ReportHelper.FLUXO_TRABALHO, listaFluxo);
			
			if(!listaFluxo.isEmpty())
				getView().setReport(jPrint, "Fluxo de trabalho");
			else
				openError("Não há fluxo disponível para mostrar no relatório.");
			
		}
		
		return null;
	}

	private List<FluxoTrabalho> getLinesReport(){
		listaFluxo = new ArrayList<FluxoTrabalho>();
		
		//pega o servico selecionado pelo usuario
		ServicoPrestado sp = servicoService.find(prftd.getServicoNumero());
		
		//caso nao tenha encontrado o serviço informado pelo usuario
		if(sp == null && prftd.getServicoNumero() != null){
			sp = new ServicoPrestado();
			sp.setId(prftd.getServicoNumero());
		}
		
		//todos os status que devem ser mostrados no relatorio
		List<StatusServico> listaStatus = statusService.findAllByFuncionarioAndPeriodoAndServico(prftd.getFuncionario(), 
				prftd.getDtInicial(), prftd.getDtFinal(), sp);
		statusPorServico = new ArrayList<StatusServico>();
		statusPorFuncionario = new ArrayList<StatusServico>();
		
		novaLinha = false;
		for(StatusServico ss : listaStatus){
			boolean novoFuncionario = false;
			//verifica se o status pertence ao mesmo funcionario e mesmo servico da lista atual
			if(statusPorServico.isEmpty()){
				statusPorServico.add(ss);
				statusPorFuncionario.add(ss);
			}else if(ss.getFuncionario().equals(statusPorServico.get(0).getFuncionario())){
				statusPorFuncionario.add(ss);
				
				if(ss.getServicoPrestado().equals(statusPorServico.get(0).getServicoPrestado()))
					statusPorServico.add(ss);
				else
					novaLinha = true;
			//caso nao pertença, ele seta novalinha como true o que gera um novo fluxoCaixa
			}else{
				novaLinha = true;
				novoFuncionario = true;
			}
			
			if(novaLinha)
				criarNovaLinha(novoFuncionario, ss);
			
		}
		
		if(!listaStatus.isEmpty())
			criarNovaLinha(true, listaStatus.get(listaStatus.size() - 1));
		
		return listaFluxo;
		
	}
	
	private void criarNovaLinha(boolean novoFuncionario, StatusServico ss) {
		// status representante
		StatusServico ssRepresentante = statusPorServico.get(0);

		FluxoTrabalho ft = new FluxoTrabalho();
		ft.setDataServico(ssRepresentante.getServicoPrestado()
				.getDataAbertura());
		ft.setNumeroServico(ssRepresentante.getServicoPrestado().getId());
		ft.setNome(ssRepresentante.getFuncionario().getNome());
		ft.setTipo(ssRepresentante.getFuncionario().getTipo().getNome());

		// calcula o tempo gasto do funcionario no servico
		String tempoGasto = FormatterHelper.formatarTempo(FormatterHelper
				.calculaPeriodoStatusServico(statusPorServico));
		ft.setTempoGasto(tempoGasto);

		// caso seja o ultimo status
		if (novoFuncionario) {
			// calcula o tempo total trabalhado pelo funcionario
			String tempoTotal = FormatterHelper.formatarTempo(FormatterHelper
					.calculaPeriodoStatusServico(statusPorFuncionario));
			ft.setTempoTotal(tempoTotal);

			novoFuncionario = false;
			statusPorFuncionario = new ArrayList<StatusServico>();
			statusPorFuncionario.add(ss);
		}

		listaFluxo.add(ft);
		novaLinha = false;
		// zera a lista de status por servico e ja adiciona o novo status
		// por que provavelmente pertence a outro serviço e/ou outro funcionario
		statusPorServico = new ArrayList<StatusServico>();
		statusPorServico.add(ss);
		
	}

}
