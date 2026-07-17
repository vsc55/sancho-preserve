package sancho.view.utility;

import org.eclipse.jface.action.Action;
import sancho.view.preferences.PreferenceLoader;

class LinkRipper$ToggleShowAllAction extends Action {
   public LinkRipper$ToggleShowAllAction() {
      super(SResources.getString("mi.showAll"), 2);
   }

   public boolean isChecked() {
      return PreferenceLoader.loadBoolean("linkRipperShowAll");
   }

   public void run() {
      PreferenceLoader.getPreferenceStore().setValue("linkRipperShowAll", !this.isChecked());
   }
}
