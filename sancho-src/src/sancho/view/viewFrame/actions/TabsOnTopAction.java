package sancho.view.viewFrame.actions;

import org.eclipse.jface.action.Action;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedViewFrame;

public class TabsOnTopAction extends Action {
   String tabPrefString;
   TabbedViewFrame viewFrame;

   public TabsOnTopAction(String var1, TabbedViewFrame var2) {
      super(SResources.getString("mi.tabsOnTop"), 2);
      this.tabPrefString = var1;
      this.viewFrame = var2;
   }

   public boolean isChecked() {
      return PreferenceLoader.loadBoolean(this.tabPrefString + "TabsOnTop");
   }

   public void run() {
      PreferenceLoader.getPreferenceStore().setValue(this.tabPrefString + "TabsOnTop", !this.isChecked());
      this.viewFrame.toggleTabPosition();
   }
}
