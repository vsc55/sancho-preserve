package sancho.view.utility;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.custom.CTabFolder;
import sancho.view.viewer.actions.CTabFolderTabsAction;

class WidgetFactory$CTabFolderMenuListener implements IMenuListener {
   CTabFolder cTabFolder;
   String prefString;

   public WidgetFactory$CTabFolderMenuListener(CTabFolder var1, String var2) {
      this.cTabFolder = var1;
      this.prefString = var2;
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new CTabFolderTabsAction(this.cTabFolder, this.prefString));
   }
}
