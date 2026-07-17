package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class CTabFolderTabsAction extends Action {
   String prefString;
   CTabFolder cTabFolder;

   public CTabFolderTabsAction(CTabFolder var1, String var2) {
      super(SResources.getString("mi.tabsOnTop"), 2);
      this.cTabFolder = var1;
      this.prefString = var2;
   }

   public boolean isChecked() {
      return PreferenceLoader.loadBoolean(this.prefString + "TabsOnTop");
   }

   public void run() {
      PreferenceLoader.getPreferenceStore().setValue(this.prefString + "TabsOnTop", !this.isChecked());
      this.cTabFolder.setTabPosition((this.cTabFolder.getStyle() & 1024) != 0 ? 128 : 1024);
      this.cTabFolder.layout();
   }
}
