package tela.dialog;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import banco.modelo.MovimentacaoCaixa;

public class ParametroRelatorioMovimentacaoCaixaDialog extends TitleAreaDialog {

	private MecasoftText txtDataInicial;
	private MecasoftText txtDataFinal;
	private MecasoftText txtNumeroCaixa;
	private MecasoftText txtNumeroMovimentacao;
	private MecasoftText txtValorInicial;
	private MecasoftText txtValorFinal;

	private Date dtInicial;
	private Date dtFinal;
	private Long numeroCaixa;
	private Long numeroMovimentacao;
	private BigDecimal valorInicial;
	private BigDecimal valorFinal;
	private String tipo;
	private Combo cbTipo;

	public ParametroRelatorioMovimentacaoCaixaDialog(Shell parentShell) {
		super(parentShell);
		dtInicial = null;
		dtFinal = null;
		numeroCaixa = null;
		numeroMovimentacao = null;
		valorInicial = null;
		valorFinal = null;
		tipo = null;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe os campos para filtrar os resultados ou deixe-os vazios para trazer todos os resultados");
		setTitle("Relat\u00F3rio de movimenta\u00E7\u00E3o de caixa");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lblPerodoDe = new Label(container, SWT.NONE);
		lblPerodoDe.setText("Per\u00EDodo de");

		txtDataInicial = new MecasoftText(container, SWT.NONE);
		txtDataInicial.setOptions(MecasoftText.NUMEROS, 10);
		txtDataInicial.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[] {
				2, 4 }, null, null);
		txtDataInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblAt = new Label(container, SWT.NONE);
		lblAt.setText("at\u00E9");

		txtDataFinal = new MecasoftText(container, SWT.NONE);
		txtDataFinal.setOptions(MecasoftText.NUMEROS, 10);
		txtDataFinal.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[] {
				2, 4 }, null, null);
		txtDataFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblCaixaN = new Label(container, SWT.NONE);
		lblCaixaN.setText("Caixa N\u00BA:");

		txtNumeroCaixa = new MecasoftText(container, SWT.NONE);
		txtNumeroCaixa.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroCaixa.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));

		Label lblMovimentaoN = new Label(container, SWT.NONE);
		lblMovimentaoN.setText("Movimenta\u00E7\u00E3o N\u00BA:");

		txtNumeroMovimentacao = new MecasoftText(container, SWT.NONE);
		txtNumeroMovimentacao.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroMovimentacao.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 3, 1));

		Label lblValorTotalEntre = new Label(container, SWT.NONE);
		lblValorTotalEntre.setText("Valor total entre");

		txtValorInicial = new MecasoftText(container, SWT.NONE);
		txtValorInicial.setOptions(MecasoftText.NUMEROS, -1);
		txtValorInicial.addChars(FormatterHelper.MECASOFTTXTMOEDA,
				new Integer[] { -2 }, null, null);
		txtValorInicial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblE = new Label(container, SWT.NONE);
		lblE.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false,
				1, 1));
		lblE.setText("e");

		txtValorFinal = new MecasoftText(container, SWT.NONE);
		txtValorFinal.setOptions(MecasoftText.NUMEROS, -1);
		txtValorFinal.addChars(FormatterHelper.MECASOFTTXTMOEDA,
				new Integer[] { -2 }, null, null);
		txtValorFinal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		Label lblTipo = new Label(container, SWT.NONE);
		lblTipo.setText("Tipo:");
		
		ComboViewer cvTipo = new ComboViewer(container, SWT.READ_ONLY);
		cbTipo = cvTipo.getCombo();
		cbTipo.setItems(new String[] {"Entrada", "Sa\u00EDda"});
		cbTipo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		return area;
	}

	@Override
	protected void okPressed() {
		try {
			if (!txtDataInicial.getText().isEmpty())
				dtInicial = FormatterHelper.getDateFormatData().parse(txtDataInicial.getText());

			if (!txtDataFinal.getText().isEmpty()) {
				dtFinal = FormatterHelper.getDateFormatData().parse(txtDataFinal.getText());

				Calendar c = Calendar.getInstance();
				c.setTime(dtFinal);
				c.add(Calendar.DAY_OF_MONTH, 1);
				dtFinal = c.getTime();
			}
		} catch (ParseException e) {
			setErrorMessage("Informe as datas corretamente.");
			return;
		}

		if (!txtNumeroCaixa.getText().isEmpty())
			numeroCaixa = new Long(txtNumeroCaixa.getText());

		if (!txtNumeroMovimentacao.getText().isEmpty())
			numeroMovimentacao = new Long(txtNumeroMovimentacao.getText());

		if (!txtValorInicial.getText().isEmpty())
			valorInicial = new BigDecimal(txtValorInicial.getText().replace(
					",", "."));

		if (!txtValorFinal.getText().isEmpty())
			valorFinal = new BigDecimal(txtValorFinal.getText().replace(",",
					"."));

		if (cbTipo.getSelectionIndex() == 0)
			tipo = MovimentacaoCaixa.TIPOENTRADA + "";
		else if (cbTipo.getSelectionIndex() == 1)
			tipo = MovimentacaoCaixa.TIPOSAIDA + "";

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
		return new Point(450, 287);
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

	public Long getNumeroCaixa() {
		return numeroCaixa;
	}

	public void setNumeroCaixa(Long numeroCaixa) {
		this.numeroCaixa = numeroCaixa;
	}

	public Long getNumeroMovimentacao() {
		return numeroMovimentacao;
	}

	public void setNumeroMovimentacao(Long numeroMovimentacao) {
		this.numeroMovimentacao = numeroMovimentacao;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
