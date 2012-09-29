package tela.dialog;

import static aplicacao.helper.MessageHelper.openWarning;
import static aplicacao.helper.ValidatorHelper.validar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import tela.componentes.MecasoftText;
import aplicacao.exception.ValidationException;
import aplicacao.helper.FormatterHelper;
import aplicacao.service.DuplicataService;
import aplicacao.service.FormaPagamentoService;
import banco.modelo.Duplicata;
import banco.modelo.FormaPagamento;
import banco.modelo.FormaPagtoUtilizada;
import banco.modelo.ServicoPrestado;

public class AdicionarFormaPagamentoDialog extends TitleAreaDialog {
	private Table tableDuplicatas;
	private ComboViewer cvForma;
	private MecasoftText txtValor;
	private MecasoftText txtValorEntrada;
	private TableViewer tvDuplicatas;
	private MecasoftText txtNumeroParcela;
	private MecasoftText txtDias;
	private MecasoftText txtDataParcela;

	private FormaPagamentoService formaPagamentoService = new FormaPagamentoService();
	private DuplicataService duplicataService = new DuplicataService();
	private List<FormaPagamento> listaFormaPagto;
	private List<Duplicata> listaDuplicatas;
	private Integer numeroParcelas;
	private Integer diasParcelas = 30;
	private BigDecimal resto;
	private FormaPagtoUtilizada formaUtilizada;
	private ServicoPrestado servico;
	
	public AdicionarFormaPagamentoDialog(Shell parentShell, ServicoPrestado servico) {
		super(parentShell);
		listaFormaPagto = formaPagamentoService.findAllAtivos();
		
		this.servico = servico;
		
		listaDuplicatas = new ArrayList<Duplicata>();
		
		formaUtilizada = new FormaPagtoUtilizada();
		formaUtilizada.setServicoPrestado(servico);
		formaUtilizada.setValor(servico.getValorTotal());
		
		//desconta do valor total caso o serviço ja tenha alguma forma pagando uma parte dele
		for(FormaPagtoUtilizada forma : servico.getListaFormaPagto())
			formaUtilizada.setValor(formaUtilizada.getValor().subtract(forma.getValor()));
		
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Adicionar forma de pagamento");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblPagamento = new Label(container, SWT.NONE);
		lblPagamento.setText("Pagamento:");
		
		cvForma = new ComboViewer(container, SWT.READ_ONLY);
		Combo cbForma = cvForma.getCombo();
		cbForma.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initDataBindings();
			}
		});
		cbForma.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblValor = new Label(container, SWT.NONE);
		lblValor.setText("Valor:");
		
		txtValor = new MecasoftText(container, SWT.NONE);
		txtValor.setOptions(MecasoftText.NUMEROS, -1);
		txtValor.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNParcelas = new Label(container, SWT.NONE);
		lblNParcelas.setText("N\u00BA parcelas:");
		
		txtNumeroParcela = new MecasoftText(container, SWT.NONE);
		txtNumeroParcela.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				atualizarValores();
			}
		});
		txtNumeroParcela.setEnabled(false);
		txtNumeroParcela.setOptions(MecasoftText.NUMEROS, -1);
		txtNumeroParcela.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblValorEntrada = new Label(container, SWT.NONE);
		lblValorEntrada.setText("Valor entrada:");
		
		txtValorEntrada = new MecasoftText(container, SWT.NONE);
		txtValorEntrada.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				atualizarValores();
			}
		});
		txtValorEntrada.setEnabled(false);
		txtValorEntrada.setOptions(MecasoftText.NUMEROS, -1);
		txtValorEntrada.addChars(FormatterHelper.MECASOFTTXTMOEDA, new Integer[]{-2}, null, null);
		txtValorEntrada.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDiasEntreParcelas = new Label(container, SWT.NONE);
		lblDiasEntreParcelas.setText("Dias entre parcelas:");
		
		txtDias = new MecasoftText(container, SWT.NONE);
		txtDias.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				atualizarValores();
			}
		});
		txtDias.setEnabled(false);
		txtDias.setOptions(MecasoftText.NUMEROS, -1);
		txtDias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtDias.setText(diasParcelas.toString());
		
		Label lblDtParcela = new Label(container, SWT.NONE);
		lblDtParcela.setText("Dt. primeira parcela:");
		
		txtDataParcela = new MecasoftText(container, SWT.NONE);
		txtDataParcela.text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				gerarDuplicata();
			}
		});
		txtDataParcela.setEnabled(false);
		txtDataParcela.setOptions(MecasoftText.NUMEROS, 10);
		txtDataParcela.addChars(FormatterHelper.MECASOFTTXTDATA, new Integer[]{2, 4}, null, null);
		txtDataParcela.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tvDuplicatas = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableDuplicatas = tvDuplicatas.getTable();
		tableDuplicatas.setEnabled(false);
		tableDuplicatas.setLinesVisible(true);
		tableDuplicatas.setHeaderVisible(true);
		GridData gd_tableDuplicatas = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_tableDuplicatas.heightHint = 95;
		tableDuplicatas.setLayoutData(gd_tableDuplicatas);
		
		TableViewerColumn tvcDuplicataNum = new TableViewerColumn(tvDuplicatas, SWT.NONE);
		tvcDuplicataNum.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Duplicata)element).getNumero();
			}
		});
		TableColumn tblclmnDuplicataN = tvcDuplicataNum.getColumn();
		tblclmnDuplicataN.setWidth(132);
		tblclmnDuplicataN.setText("Duplicata N\u00BA");
		
		TableViewerColumn tvcVencimento = new TableViewerColumn(tvDuplicatas, SWT.NONE);
		tvcVencimento.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.DATEFORMATDATA.format(((Duplicata)element).getDataVencimento());
			}
		});
		TableColumn tblclmnVencimento = tvcVencimento.getColumn();
		tblclmnVencimento.setWidth(146);
		tblclmnVencimento.setText("Vencimento");
		
		TableViewerColumn tvcValor = new TableViewerColumn(tvDuplicatas, SWT.NONE);
		tvcValor.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return FormatterHelper.getDecimalFormat().format(((Duplicata)element).getValor());
			}
		});
		TableColumn tblclmnValor = tvcValor.getColumn();
		tblclmnValor.setWidth(137);
		tblclmnValor.setText("Valor");
		
		//remove as formas de pagamento que geram duplicatas
		//caso a ordem ja tenha uma forma a vista
		if(servico.getListaFormaPagto() != null && !servico.getListaFormaPagto().isEmpty()){
			if(servico.getListaFormaPagto().get(0).getFormaPagamento().isGeraPagVista()){
				//clone da lista pq nao é possivel editala no loop
				List<FormaPagamento> listaClone = new ArrayList<FormaPagamento>();
				listaClone.addAll(listaFormaPagto);
				
				for(FormaPagamento forma : listaClone){
					if(forma.isGeraDuplicata())
						listaFormaPagto.remove(forma);
				}
			}
		}
		
		initDataBindings();

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == OK){
			try {
				validar(formaUtilizada);				
				
				if(formaUtilizada.getFormaPagamento().isGeraDuplicata())
					atualizarValores();
				
				if(servico.getValorEntrada().compareTo(servico.getValorTotal()) > 0){
					setErrorMessage("O valor de entrada não pode ser superior ao valor total");
					return;
				}
				
				servico.getListaFormaPagto().add(formaUtilizada);
			} catch (ValidationException e) {
				setErrorMessage(e.getMessage());
				return;
			}
		}
		
		super.buttonPressed(buttonId);
	}
	
	private void atualizarValores(){
		try{
			//pega o periodo entre o vencimento de uma parcela e outra
			diasParcelas = txtDias.getText().isEmpty() ? 30 : new Integer(txtDias.getText());
			Date dtPrimeiraParcela;
			
			//calcula data da primeira parcela
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, diasParcelas);
			dtPrimeiraParcela = c.getTime();
			txtDataParcela.setText(FormatterHelper.DATEFORMATDATA.format(dtPrimeiraParcela));
			
			//gera as duplicatas
			gerarDuplicata();
		}catch(Exception e){
			openWarning("Informe a data da primeira parcela corretamente.");
			e.printStackTrace();
		}
		
	}
	
	private void gerarDuplicata(){
		try{
			//pega a data da 1º parcela
			Calendar data = Calendar.getInstance();
			data.setTime(FormatterHelper.DATEFORMATDATA.parse(txtDataParcela.getText()));
			
			//pega o numero de parcelas
			numeroParcelas = txtNumeroParcela.getText().isEmpty() ? 1 : new Integer(txtNumeroParcela.getText());
			
			//valor total descontada a entrada
			BigDecimal valorTotal = formaUtilizada.getValor().subtract(servico.getValorEntrada() == null ? BigDecimal.ZERO : servico.getValorEntrada());
			
			//pega o que sobra da divisão do total pela quantidade de parcelas para jogar na ultima parcela
			//e calcula o valor de cada parcela
			resto = valorTotal.remainder(new BigDecimal(numeroParcelas));
			BigDecimal valorParcela = valorTotal.subtract(resto).divide(new BigDecimal(numeroParcelas));
			
			//pega o id da ultima duplicata registrada para usa-lo como numero
			Long numeroDuplicata = duplicataService.findUltimaDuplicata() == null ? new Long(0) : duplicataService.findUltimaDuplicata().getId();
			numeroDuplicata++;
			
			//gera as duplicatas
			listaDuplicatas.clear();
			
			//caso o valor da parcela seja maior que 0, ele gera as duplicatas
			if(valorParcela.compareTo(BigDecimal.ZERO) > 0){
				for(int c = 0; c < numeroParcelas; c++){
					Duplicata d = new Duplicata();
					d.setDataVencimento(data.getTime());
					d.setNumero(numeroDuplicata.toString());
					d.setServicoPrestado(servico);
					d.setValor(valorParcela);
					listaDuplicatas.add(d);
					
					//seta a variavel data para a data da proxima duplicata e o numeroDuplicata para o numero da proxima duplicata
					data.add(Calendar.DAY_OF_MONTH, diasParcelas);
					numeroDuplicata++;
					
				}
				//adiciona o que sobrou na ultima parcela
				valorParcela = valorParcela.add(resto);
				listaDuplicatas.get(listaDuplicatas.size()-1).setValor(valorParcela);
				
			//caso seja 0, ele gera apenas 1 duplicata com o valor total da forma	
			}else{
				Duplicata d = new Duplicata();
				d.setDataVencimento(data.getTime());
				d.setNumero(numeroDuplicata.toString());
				d.setServicoPrestado(servico);
				d.setValor(valorTotal);
				listaDuplicatas.add(d);
			}
			
			tvDuplicatas.refresh();
		}catch(Exception e){
			openWarning("Informe a data da primeira parcela corretamente.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 458);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider.getKnownElements(), FormaPagamento.class, "nome");
		cvForma.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		cvForma.setContentProvider(listContentProvider);
		//
		WritableList writableList = new WritableList(listaFormaPagto, FormaPagamento.class);
		cvForma.setInput(writableList);
		//
		IObservableValue cvFormaObserveSingleSelection = ViewersObservables.observeSingleSelection(cvForma);
		IObservableValue formaUtilizadaFormaPagamentoObserveValue = PojoObservables.observeValue(formaUtilizada, "formaPagamento");
		bindingContext.bindValue(cvFormaObserveSingleSelection, formaUtilizadaFormaPagamentoObserveValue, null, null);
		//
		IObservableValue txtValortextObserveTextObserveWidget = SWTObservables.observeText(txtValor.text, SWT.Modify);
		IObservableValue formaUtilizadaValorObserveValue = PojoObservables.observeValue(formaUtilizada, "valor");
		bindingContext.bindValue(txtValortextObserveTextObserveWidget, formaUtilizadaValorObserveValue, null, null);
		//
		IObservableValue txtValorEntradatextObserveTextObserveWidget = SWTObservables.observeText(txtValorEntrada.text, SWT.Modify);
		IObservableValue servicoValorEntradaObserveValue = PojoObservables.observeValue(servico, "valorEntrada");
		bindingContext.bindValue(txtValorEntradatextObserveTextObserveWidget, servicoValorEntradaObserveValue, null, null);
		//
		ObservableListContentProvider listContentProvider_1 = new ObservableListContentProvider();
//		IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider_1.getKnownElements(), Duplicata.class, new String[]{"numero", "dataVencimento", "valor"});
//		tvDuplicatas.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		tvDuplicatas.setContentProvider(listContentProvider_1);
		//
		WritableList writableList_1 = new WritableList(listaDuplicatas, Duplicata.class);
		tvDuplicatas.setInput(writableList_1);
		//
		IObservableValue txtNumeroParcelatextObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtNumeroParcela.text);
		IObservableValue formaUtilizadaFormaPagamentogeraDuplicataObserveValue = PojoObservables.observeValue(formaUtilizada, "formaPagamento.geraDuplicata");
		bindingContext.bindValue(txtNumeroParcelatextObserveEnabledObserveWidget, formaUtilizadaFormaPagamentogeraDuplicataObserveValue, null, null);
		//
		IObservableValue txtValorEntradatextObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtValorEntrada.text);
		bindingContext.bindValue(txtValorEntradatextObserveEnabledObserveWidget, formaUtilizadaFormaPagamentogeraDuplicataObserveValue, null, null);
		//
		IObservableValue txtDiastextObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtDias.text);
		bindingContext.bindValue(txtDiastextObserveEnabledObserveWidget, formaUtilizadaFormaPagamentogeraDuplicataObserveValue, null, null);
		//
		IObservableValue txtDataParcelatextObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtDataParcela.text);
		bindingContext.bindValue(txtDataParcelatextObserveEnabledObserveWidget, formaUtilizadaFormaPagamentogeraDuplicataObserveValue, null, null);
		//
		IObservableValue tableDuplicatasObserveEnabledObserveWidget = SWTObservables.observeEnabled(tableDuplicatas);
		bindingContext.bindValue(tableDuplicatasObserveEnabledObserveWidget, formaUtilizadaFormaPagamentogeraDuplicataObserveValue, null, null);
		//
		IObservableValue txtValortextObserveEnabledObserveWidget = SWTObservables.observeEnabled(txtValor.text);
		IObservableValue formaUtilizadaFormaPagamentogeraPagVistaObserveValue = PojoObservables.observeValue(formaUtilizada, "formaPagamento.geraPagVista");
		bindingContext.bindValue(txtValortextObserveEnabledObserveWidget, formaUtilizadaFormaPagamentogeraPagVistaObserveValue, null, null);
		//
		return bindingContext;
	}

	public List<Duplicata> getListaDuplicatas() {
		return listaDuplicatas;
	}

	public void setListaDuplicatas(List<Duplicata> listaDuplicatas) {
		this.listaDuplicatas = listaDuplicatas;
	}	
	
}
