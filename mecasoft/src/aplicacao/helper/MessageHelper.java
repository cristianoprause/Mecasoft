package aplicacao.helper;

import org.eclipse.jface.dialogs.MessageDialog;



public class MessageHelper {
	
	public static void openInformation(String inf){
		MessageDialog.openInformation(LayoutHelper.getActiveShell(), "Informação", inf);
	}
	
	public static boolean openQuestion(String inf){
		return MessageDialog.openQuestion(LayoutHelper.getActiveShell(), "Confirmação", inf);
	}
	
	public static void openWarning(String inf){
		MessageDialog.openWarning(LayoutHelper.getActiveShell(), "Aviso", inf);
	}
	
	public static void openError(String inf){
		MessageDialog.openError(LayoutHelper.getActiveShell(), "Erro", inf);
	}
	
}
