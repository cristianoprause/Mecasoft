package mecasoft;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;

public class Perspective implements IPerspectiveFactory {

	/** Top folder's id. */
	public static final String FI_TOP = "mecasoft.topFolder";
	/** Top folder's id. */
	public static final String FI_BOTTOM = "mecasoft.bottomFolder";

	float ratioTop = 0.35f;
	float ratioBottom = 0.60f;

	public void createInitialLayout(IPageLayout layout) {
//		layout.setEditorAreaVisible(false);
//		layout.setFixed(true);
		IPartService partService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService();
		partService.addPartListener(new PerspectiveListenerAdapter());

		String editorAreaId = layout.getEditorArea();

		layout.createPlaceholderFolder(FI_TOP, IPageLayout.TOP, ratioTop, editorAreaId);
		layout.createPlaceholderFolder(FI_BOTTOM, IPageLayout.BOTTOM, ratioBottom, editorAreaId);

		layout.setEditorAreaVisible(false);
		
	}

}
