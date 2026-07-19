package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class CTabFolderTabsAction extends Action {
   String prefString;
   CTabFolder cTabFolder;

   public CTabFolderTabsAction(CTabFolder cTabFolder, String prefString) {
      super(SResources.getString("mi.tabsOnTop"), 2);
      this.cTabFolder = cTabFolder;
      this.prefString = prefString;
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
