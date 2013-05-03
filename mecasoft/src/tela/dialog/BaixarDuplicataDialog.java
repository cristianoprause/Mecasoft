package tela.dialog;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.MessageHelper.openWarning;

import java.math.BigDecimal;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.DuplicataPagaService;
import aplicacao.service.DuplicataService;
import aplicacao.service.MovimentacaoCaixaService;
import banco.connection.HibernateConnection;
import banco.modelo.Duplicata;
import banco.modelo.DuplicataPaga;
import banco.modelo.MovimentacaoCaixa;

public class BaixarDuplicataDialog extends TitleAreaDialog {
	private Text txtValor;
	private Text txtTroco;
	private MecasoftText txtNumeroDuplicata;
	private MecasoftText txtJuros;
	private MecasoftText txtDesconto;
	private MecasoftText txtValorRecebido;
	private Label lblTotal;
	
	private Duplicata duplicata;
	private DuplicataPagaService service = new DuplicataPagaService();
	private DuplicataService duplicataService = new DuplicataService();
	private MovimentacaoCaixaService movimentacaoService = new MovimentacaoCaixaService();
	private BigDecimal troco;

	public BaixarDuplicataDialog(Shell parentShell, Duplicata duplicata) {
		super(parentShell);
		this.duplicata = duplicata;
		
		service.setDuplicataPaga(new DuplicataPaga());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Informe o número da duplicata e demais informações para baixa-la.");
		setTitle("Baixar duplicata");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblDuplicataN = new Label(container, SWT.NONE);
		lblDuplicataN.setText("Duplicata N\u00BA:");
		
		txtNumeroDuplicata = new MecasoftText(container, SWT.NONE);
		txtNumeroDuplicata.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				duplicata = duplicataService.findByNumero(txtNumeroDuplicata.getText());
				
				if(duplicata == null)
					duplicata = new Duplicata();
				
				txtValor.setText(FormatterHelper.getDecimalFormat().format(duplicata.getValor()));
				calcularValores();
			}
		});
		txtNumeroDuplicata.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroDuplicata.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtNumeroDuplicata.setText(duplicata.getNumero() == null ? "" : duplicata.getNumero());
		
		Label lblValor = new Label(container, SWT.NONE);
		lblValor.setText("Valor:");
		
		txtValor = new Text(container, SWT.BORDER);
		txtValor.setEnabled(false);
		txtValor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtValor.setText(FormatterHelper.getDecimalFormat().format(duplicata.getValor()));
		
		Label lblJuros = new Label(container, SWT.NONE);
		lblJuros.setText("Juros:");
		
		txtJuros = new MecasoftText(container, SWT.NONE);
		txtJuros.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularValores();
			}
		});
		txtJuros.setOptions(MecasoftText.NUMEROS, -1);
		txtJuros.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtJuros.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblDesconto = new Label(container, SWT.NONE);
		lblDesconto.setText("Desconto:");
		
		txtDesconto = new MecasoftText(container, SWT.NONE);
		txtDesconto.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularValores();
			}
		});
		txtDesconto.setOptions(MecasoftText.NUMEROS, -1);
		txtDesconto.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtDesconto.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblValorRecebido = new Label(container, SWT.NONE);
		lblValorRecebido.setText("Valor recebido:");
		
		txtValorRecebido = new MecasoftText(container, SWT.NONE);
		txtValorRecebido.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calcularValores();
			}
		});
		txtValorRecebido.setOptions(MecasoftText.NUMEROS, -1);
		txtValorRecebido.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorRecebido.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblTroco = new Label(container, SWT.NONE);
		lblTroco.setText("Troco:");
		
		txtTroco = new Text(container, SWT.BORDER);
		txtTroco.setEnabled(false);
		txtTroco.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblValorTotal = new Label(container, SWT.NONE);
		lblValorTotal.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblValorTotal.setText("Valor total:");
		
		lblTotal = new Label(container, SWT.NONE);
		lblTotal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblTotal.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblTotal.setText("R$ ".concat(FormatterHelper.getDecimalFormat().format(duplicata.getValor())));

		initDataBindings();
		
		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	public void calcularValores(){
		
		if(duplicata != null && duplicata.getId() != null){
			BigDecimal valor = duplicata.getValor() == null ? BigDecimal.ZERO : duplicata.getValor();
			BigDecimal juro = service.getDuplicataPaga().getValorJuros() == null ? BigDecimal.ZERO : service.getDuplicataPaga().getValorJuros();
			BigDecimal desconto = service.getDuplicataPaga().getValorDesconto() == null ? BigDecimal.ZERO : service.getDuplicataPaga().getValorDesconto();
			BigDecimal valorRecebido = service.getDuplicataPaga().getValorRecebido() == null ? BigDecimal.ZERO : service.getDuplicataPaga().getValorRecebido();
			BigDecimal valorTotal;
			
			valorTotal = valor.add(juro).subtract(desconto);
			troco = valorRecebido.subtract(valorTotal);
			
			service.getDuplicataPaga().setValorTotal(valorTotal);
			lblTotal.setText("R$ ".concat(FormatterHelper.getDecimalFormat().format(valorTotal)));
			
			if(troco.compareTo(BigDecimal.ZERO) > 0)
				service.getDuplicataPaga().setTroco(troco);
			else
				service.getDuplicataPaga().setTroco(BigDecimal.ZERO);
			
			initDataBindings();
			
		}else
			lblTotal.setText("R$ ".concat(FormatterHelper.getDecimalFormat().format(BigDecimal.ZERO)));
		
	}
	
	@Override
	protected void okPressed() {
		
		if(duplicata.getId() == null){
			setErrorMessage("Duplicata não encontrada.");
			return;
		}
		
		if(service.getDuplicataPaga().getValorTotal().compareTo(
				service.getDuplicataPaga().getValorRecebido() == null ? BigDecimal.ZERO : service.getDuplicataPaga().getValorRecebido()) > 0){
			setErrorMessage("O valor recebido não pode ser inferior ao valor total da duplicata.");
			return;
		}
		
		if((service.getDuplicataPaga().getValorDesconto() == null ? BigDecimal.ZERO : service.getDuplicataPaga().getValorDesconto())
				.compareTo(service.getDuplicataPaga().getValorTotal()) > 0){
			setErrorMessage("O valor do desconto não pode ser superior ao valor total da duplciata.");
			return;
		}
		
		BigDecimal valorCaixa = movimentacaoService.getTotalCaixa(UsuarioHelper.getCaixa());
		if(troco.compareTo(valorCaixa) > 0)
			openWarning("Atenção, o caixa não possui dinheiro suficiente para o troco.");
		
		//gera a movimentação no caixa
		MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
		movimentacao.setDuplicataPaga(service.getDuplicataPaga());
		movimentacao.setMotivo("Baixa da duplicata número: " + duplicata.getNumero());
		movimentacao.setStatus(MovimentacaoCaixa.STATUSDUPLICATA);
		movimentacao.setTipo(MovimentacaoCaixa.TIPOENTRADA);
		movimentacao.setValor(service.getDuplicataPaga().getValorTotal());
		
		movimentacaoService.setMovimentacao(movimentacao);
		movimentacaoService.saveOrUpdate();
		
		duplicata.setPago(true);
		duplicataService.setDuplicata(duplicata);
		duplicataService.saveOrUpdate();
		
		service.getDuplicataPaga().setDuplicata(duplicata);
		service.saveOrUpdate();
		
		HibernateConnection.commit(duplicata);
		
		openInformation("Duplicata baixa com sucesso!");
		
		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(338, 338);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtJurostextObserveTextObserveWidget = SWTObservables.observeText(txtJuros.text, SWT.Modify);
		IObservableValue servicegetDuplicataPagaValorJurosObserveValue = PojoObservables.observeValue(service.getDuplicataPaga(), "valorJuros");
		bindingContext.bindValue(txtJurostextObserveTextObserveWidget, servicegetDuplicataPagaValorJurosObserveValue, null, null);
		//
		IObservableValue txtDescontotextObserveTextObserveWidget = SWTObservables.observeText(txtDesconto.text, SWT.Modify);
		IObservableValue servicegetDuplicataPagaValorDescontoObserveValue = PojoObservables.observeValue(service.getDuplicataPaga(), "valorDesconto");
		bindingContext.bindValue(txtDescontotextObserveTextObserveWidget, servicegetDuplicataPagaValorDescontoObserveValue, null, null);
		//
		IObservableValue txtValorRecebidotextObserveTextObserveWidget = SWTObservables.observeText(txtValorRecebido.text, SWT.Modify);
		IObservableValue servicegetDuplicataPagaValorRecebidoObserveValue = PojoObservables.observeValue(service.getDuplicataPaga(), "valorRecebido");
		bindingContext.bindValue(txtValorRecebidotextObserveTextObserveWidget, servicegetDuplicataPagaValorRecebidoObserveValue, null, null);
		//
		IObservableValue txtTrocoObserveTextObserveWidget = SWTObservables.observeText(txtTroco, SWT.Modify);
		IObservableValue servicegetDuplicataPagaTrocoObserveValue = PojoObservables.observeValue(service.getDuplicataPaga(), "troco");
		bindingContext.bindValue(txtTrocoObserveTextObserveWidget, servicegetDuplicataPagaTrocoObserveValue, null, null);
		//
		return bindingContext;
	}
}
