package tela.dialog;

import java.math.BigDecimal;
import java.util.Date;

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
import org.eclipse.wb.swt.SWTResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.MessageHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.CaixaService;
import aplicacao.service.MovimentacaoCaixaService;
import banco.modelo.Caixa;
import banco.modelo.MovimentacaoCaixa;

public class AbrirFecharCaixaDialog extends TitleAreaDialog {

	private MecasoftText txtValorAbertura;
	private MecasoftText txtDataAbertura;
	private MecasoftText txtValorFechamento;
	private MecasoftText txtDataFechamento;

	private Caixa caixa;
	private CaixaService service = new CaixaService();
	private MovimentacaoCaixaService movimentacaoService = new MovimentacaoCaixaService();

	public AbrirFecharCaixaDialog(Shell parentShell) {
		super(parentShell);
		this.caixa = UsuarioHelper.getCaixa();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Abrir/Fechar caixa");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblStatus = new Label(container, SWT.NONE);
		lblStatus.setText("Status:");
		
		Label lblValorStatus = new Label(container, SWT.NONE);
		lblValorStatus.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblValorStatus.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		
		Label lblValorAbertura = new Label(container, SWT.NONE);
		lblValorAbertura.setText("Valor abertura:");
		
		txtValorAbertura = new MecasoftText(container, SWT.NONE);
		txtValorAbertura.setOptions(MecasoftText.NUMEROS, -1);
		txtValorAbertura.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorAbertura.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(container, SWT.NONE);
		
		Label lblDataAbertura = new Label(container, SWT.NONE);
		lblDataAbertura.setText("Data abertura:");
		
		txtDataAbertura = new MecasoftText(container, SWT.NONE);
		txtDataAbertura.setEnabled(false);
		txtDataAbertura.setOptions(MecasoftText.NUMEROS, 10);
		txtDataAbertura.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataAbertura.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblValorFechamento = new Label(container, SWT.NONE);
		lblValorFechamento.setText("Valor fechamento:");
		
		txtValorFechamento = new MecasoftText(container, SWT.NONE);
		txtValorFechamento.setOptions(MecasoftText.NUMEROS, -1);
		txtValorFechamento.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorFechamento.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label lblDataFechamento = new Label(container, SWT.NONE);
		lblDataFechamento.setText("Data fechamento:");
		
		txtDataFechamento = new MecasoftText(container, SWT.NONE);
		txtDataFechamento.setEnabled(false);
		txtDataFechamento.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFechamento.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataFechamento.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);
		
		//verifica se o caixa esta aberto
		if(caixa == null){
			Caixa ultimoCaixa = service.findUltimoCaixaAberto();
			
			lblValorStatus.setText("Caixa fechado");
			lblValorStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			
			if(ultimoCaixa != null)
				txtValorAbertura.setText(FormatterHelper.getDecimalFormat().format(ultimoCaixa.getValorFechamento()));
			
			txtDataAbertura.setText(FormatterHelper.getDateFormatData().format(new Date()));
			
			txtValorFechamento.setEnabled(false);
		}else{
			lblValorStatus.setText("Caixa aberto");
			lblValorStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
			
			txtDataAbertura.setText(FormatterHelper.getDateFormatData().format(caixa.getDataAbertura()));
			txtDataFechamento.setText(FormatterHelper.getDateFormatData().format(new Date()));
			txtValorFechamento.setText(FormatterHelper.getDecimalFormat().format(movimentacaoService.getTotalCaixa(caixa)));
			
			txtValorAbertura.setText(FormatterHelper.getDecimalFormat().format(caixa.getValorAbertura()));
			txtValorAbertura.setEnabled(false);
		}

		return area;
	}
	
	@Override
	protected void okPressed() {
		
		if(caixa == null || caixa.getId() == null){
			caixa = new Caixa();
			
			//valor e data de abertura
			try{
				caixa.setValorAbertura(new BigDecimal(txtValorAbertura.getText().replace(",", ".")));
				caixa.setDataAbertura(new Date());
			}catch(Exception e){
				setErrorMessage("Informe o valor de abertura do caixa.");
				return;
			}
			
		}else{
			//valor e data de fechamento
			try{
				caixa.setValorFechamento(new BigDecimal(txtValorFechamento.getText().replace(",", ".")));
				
				//gera um suprimento para justificar o dinheiro extra
				BigDecimal valorCaixa = movimentacaoService.getTotalCaixa(caixa);
				if(caixa.getValorFechamento().compareTo(valorCaixa) > 0){
					MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
					movimentacao.setMotivo("Suprimento gerado ao fechar o caixa");
					movimentacao.setStatus(MovimentacaoCaixa.STATUSSUPRIMENTO);
					movimentacao.setTipo(MovimentacaoCaixa.TIPOENTRADA);
					movimentacao.setValor(caixa.getValorFechamento().subtract(valorCaixa));
					movimentacaoService.setMovimentacao(movimentacao);
					movimentacaoService.saveOrUpdate();
					
				//gera uma sangria para justificar o dinheiro que esta faltando
				}else if(caixa.getValorFechamento().compareTo(valorCaixa) < 0){
					MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
					movimentacao.setMotivo("Sangria gerada ao fechar o caixa");
					movimentacao.setStatus(MovimentacaoCaixa.STATUSSANGRIA);
					movimentacao.setTipo(MovimentacaoCaixa.TIPOSAIDA);
					movimentacao.setValor(valorCaixa.subtract(caixa.getValorFechamento()));
					movimentacaoService.setMovimentacao(movimentacao);
					movimentacaoService.saveOrUpdate();
				}
				
				caixa.setDataFechamento(new Date());
			}catch(Exception e){
				setErrorMessage("Informe o valor de fechamento do caixa");
				return;
			}
		}
		
		service.setCaixa(caixa);
		service.saveOrUpdate();
//		service.commit();
		
		if(caixa.getDataFechamento() == null)
			MessageHelper.openInformation("Caixa aberto com sucesso!");
		else{
			MessageHelper.openInformation("Caixa fechado com sucesso!");
			caixa = null;
		}
		
		UsuarioHelper.setCaixa(caixa);
		
		super.okPressed();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
