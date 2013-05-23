package tela.dialog;

import static aplicacao.helper.MessageHelper.openInformation;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import tela.componentes.MecasoftText;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.helper.UsuarioHelper;
import aplicacao.service.MovimentacaoCaixaService;
import banco.modelo.Caixa;
import banco.modelo.MovimentacaoCaixa;

public class SangriaCaixaDialog extends TitleAreaDialog {
	private Text txtMotivo;
	private MecasoftText txtValor;
	
	private Caixa caixa;
	private BigDecimal totalCaixa;
	private MovimentacaoCaixaService service = new MovimentacaoCaixaService();

	public SangriaCaixaDialog(Shell parentShell) {
		super(parentShell);
		caixa = UsuarioHelper.getCaixa();
		totalCaixa = service.getTotalCaixa(caixa);
		service.setMovimentacao(new MovimentacaoCaixa());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Sangria de caixa");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblTotalEmCaixa = new Label(container, SWT.NONE);
		lblTotalEmCaixa.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblTotalEmCaixa.setText("Total em caixa:");
		
		Label lblTotalCaixa = new Label(container, SWT.NONE);
		lblTotalCaixa.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblTotalCaixa.setText("R$ " + FormatterHelper.getDecimalFormat().format(totalCaixa));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblValor = new Label(container, SWT.NONE);
		lblValor.setText("Valor:");
		
		txtValor = new MecasoftText(container, SWT.NONE);
		txtValor.setOptions(MecasoftText.NUMEROS, -1);
		txtValor.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMotivo = new Label(container, SWT.NONE);
		lblMotivo.setText("Motivo:");
		
		txtMotivo = new Text(container, SWT.BORDER);
		txtMotivo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		initDataBindings();

		return area;
	}
	
	@Override
	protected void okPressed() {
		try {
			validar(service.getMovimentacao());
			
			if(service.getMovimentacao().getValor().compareTo(totalCaixa) > 0)
				throw new ValidationException("O caixa não possui dinheiro suficiente para realizar esta sangria.");
			
			//demais informações
			service.getMovimentacao().setStatus(MovimentacaoCaixa.STATUSSANGRIA);
			service.getMovimentacao().setTipo(MovimentacaoCaixa.TIPOSAIDA);
			
			service.saveOrUpdate();
//			HibernateConnection.commit(service.getMovimentacao());
			
			openInformation("Sangria realizada com sucesso");
			
			super.okPressed();
		} catch (ValidationException e) {
			setErrorMessage(e.getMessage());
		}
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
		return new Point(450, 262);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue txtValortextObserveTextObserveWidget = SWTObservables.observeText(txtValor.text, SWT.Modify);
		IObservableValue servicegetMovimentacaoValorObserveValue = PojoObservables.observeValue(service.getMovimentacao(), "valor");
		bindingContext.bindValue(txtValortextObserveTextObserveWidget, servicegetMovimentacaoValorObserveValue, null, null);
		//
		IObservableValue txtMotivoObserveTextObserveWidget = SWTObservables.observeText(txtMotivo, SWT.Modify);
		IObservableValue servicegetMovimentacaoMotivoObserveValue = PojoObservables.observeValue(service.getMovimentacao(), "motivo");
		bindingContext.bindValue(txtMotivoObserveTextObserveWidget, servicegetMovimentacaoMotivoObserveValue, null, null);
		//
		return bindingContext;
	}
}
