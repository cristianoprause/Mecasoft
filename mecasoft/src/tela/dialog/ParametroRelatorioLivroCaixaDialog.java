package tela.dialog;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import tela.componentes.MecasoftText;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;

import banco.modelo.MovimentacaoCaixa;

import aplicacao.helper.FormatterHelper;

public class ParametroRelatorioLivroCaixaDialog extends TitleAreaDialog {

	private MecasoftText txtDataInicial;
	private MecasoftText txtDataFinal;
	private ComboViewer cvTipo;
	private MecasoftText txtNumeroCaixa;
	private MecasoftText txtValorAberturaInicial;
	private MecasoftText txtValorAberturaFinal;
	private MecasoftText txtValorFechamentoInicial;
	private MecasoftText txtValorFechamentoFinal;
	private Combo cbTipo;
	
	private Date dtInicial;
	private Date dtFinal;
	private Character tipo;
	private Long numeroCaixa;
	private BigDecimal valorAberturaInicial;
	private BigDecimal valorAberturaFinal;
	private BigDecimal valorFechamentoInicial;
	private BigDecimal valorFechamentoFinal;

	public ParametroRelatorioLivroCaixaDialog(Shell parentShell) {
		super(parentShell);
		dtInicial = null;
		dtFinal = null;
		tipo = null;
		numeroCaixa = null;
		valorAberturaInicial = null;
		valorAberturaFinal = null;
		valorFechamentoInicial = null;
		valorFechamentoFinal = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe os campos para filtrar os resultados ou deixe-os vazios para trazer todos os resultados");
		setTitle("Relat\u00F3rio de livro de caixa");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblPerodoDe = new Label(container, SWT.NONE);
		lblPerodoDe.setText("Per\u00EDodo de");
		
		txtDataInicial = new MecasoftText(container, SWT.NONE);
		txtDataInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDataInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAt = new Label(container, SWT.NONE);
		lblAt.setText("at\u00E9");
		
		txtDataFinal = new MecasoftText(container, SWT.NONE);
		txtDataFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTipo = new Label(container, SWT.NONE);
		lblTipo.setText("Tipo:");
		
		cvTipo = new ComboViewer(container, SWT.READ_ONLY);
		cbTipo = cvTipo.getCombo();
		cbTipo.setItems(new String[] {"Entrada", "Sa\u00EDda"});
		cbTipo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblCaixaN = new Label(container, SWT.NONE);
		lblCaixaN.setText("Caixa N\u00BA:");
		
		txtNumeroCaixa = new MecasoftText(container, SWT.NONE);
		txtNumeroCaixa.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroCaixa.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblValorAbertudaEntre = new Label(container, SWT.NONE);
		lblValorAbertudaEntre.setText("Valor abertuda entre");
		
		txtValorAberturaInicial = new MecasoftText(container, SWT.NONE);
		txtValorAberturaInicial.setOptions(MecasoftText.NUMEROS, -1);
		txtValorAberturaInicial.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorAberturaInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblE = new Label(container, SWT.NONE);
		lblE.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblE.setText("e");
		
		txtValorAberturaFinal = new MecasoftText(container, SWT.NONE);
		txtValorAberturaFinal.setOptions(MecasoftText.NUMEROS, -1);
		txtValorAberturaFinal.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorAberturaFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblValorFechamentoEntre = new Label(container, SWT.NONE);
		lblValorFechamentoEntre.setText("Valor fechamento entre");
		
		txtValorFechamentoInicial = new MecasoftText(container, SWT.NONE);
		txtValorFechamentoInicial.setOptions(MecasoftText.NUMEROS, -1);
		txtValorFechamentoInicial.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorFechamentoInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblE_1 = new Label(container, SWT.NONE);
		lblE_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblE_1.setText("e");
		
		txtValorFechamentoFinal = new MecasoftText(container, SWT.NONE);
		txtValorFechamentoFinal.setOptions(MecasoftText.NUMEROS, -1);
		txtValorFechamentoFinal.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorFechamentoFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		return area;
	}
	
	@Override
	protected void okPressed() {
		try{
			
			if(!txtDataInicial.getText().isEmpty())
				dtInicial = FormatterHelper.getDateFormatData().parse(txtDataInicial.getText());
			
			if(!txtDataFinal.getText().isEmpty()){
				dtFinal = FormatterHelper.getDateFormatData().parse(txtDataFinal.getText());
				
				Calendar c = Calendar.getInstance();
				c.setTime(dtFinal);
				c.add(Calendar.DAY_OF_MONTH, 1);
				dtFinal = c.getTime();
			}
			
		}catch(Exception e){
			setErrorMessage("Informe as datas corretamente");
			return;
		}
		
		if(cbTipo.getSelectionIndex() == 0)
			tipo = MovimentacaoCaixa.TIPOENTRADA;
		else if(cbTipo.getSelectionIndex() == 1)
			tipo = MovimentacaoCaixa.TIPOSAIDA;
		
		if(!txtNumeroCaixa.getText().isEmpty())
			numeroCaixa = new Long(txtNumeroCaixa.getText());
		
		if(!txtValorAberturaInicial.getText().isEmpty())
			valorAberturaInicial = new BigDecimal(txtValorAberturaInicial.getText().replace(",", "."));
		if(!txtValorAberturaFinal.getText().isEmpty())
			valorAberturaFinal = new BigDecimal(txtValorAberturaFinal.getText().replace(",", "."));
		
		if(!txtValorFechamentoInicial.getText().isEmpty())
			valorFechamentoInicial = new BigDecimal(txtValorFechamentoInicial.getText().replace(",", "."));
		if(!txtValorFechamentoFinal.getText().isEmpty())
			valorFechamentoFinal = new BigDecimal(txtValorFechamentoFinal.getText().replace(",", "."));
		
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
		return new Point(450, 286);
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public Character getTipo() {
		return tipo;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public Long getNumeroCaixa() {
		return numeroCaixa;
	}

	public void setNumeroCaixa(Long numeroCaixa) {
		this.numeroCaixa = numeroCaixa;
	}

	public BigDecimal getValorAberturaInicial() {
		return valorAberturaInicial;
	}

	public void setValorAberturaInicial(BigDecimal valorAberturaInicial) {
		this.valorAberturaInicial = valorAberturaInicial;
	}

	public BigDecimal getValorAberturaFinal() {
		return valorAberturaFinal;
	}

	public void setValorAberturaFinal(BigDecimal valorAberturaFinal) {
		this.valorAberturaFinal = valorAberturaFinal;
	}

	public BigDecimal getValorFechamentoInicial() {
		return valorFechamentoInicial;
	}

	public void setValorFechamentoInicial(BigDecimal valorFechamentoInicial) {
		this.valorFechamentoInicial = valorFechamentoInicial;
	}

	public BigDecimal getValorFechamentoFinal() {
		return valorFechamentoFinal;
	}

	public void setValorFechamentoFinal(BigDecimal valorFechamentoFinal) {
		this.valorFechamentoFinal = valorFechamentoFinal;
	}

}
