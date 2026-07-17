package sancho.view.viewFrame;

import org.eclipse.jface.action.Action;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

class ViewFrame$ToggleRefineAction extends Action {
   String prefString;

   public ViewFrame$ToggleRefineAction(String var1, String var2) {
      super(SResources.getString(var1), 2);
      this.prefString = var2;
   }

   public boolean isChecked() {
      return PreferenceLoader.loadBoolean(this.prefString);
   }

   public void run() {
      PreferenceLoader.getPreferenceStore().setValue(this.prefString, !this.isChecked());
      PreferenceLoader.saveStore();
   }
}
