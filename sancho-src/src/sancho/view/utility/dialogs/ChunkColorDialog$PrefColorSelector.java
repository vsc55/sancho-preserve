package sancho.view.utility.dialogs;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.widgets.Composite;
import sancho.view.preferences.PreferenceLoader;

class ChunkColorDialog$PrefColorSelector {
   ColorSelector cs;
   String prefString;

   public ChunkColorDialog$PrefColorSelector(Composite var1, String var2) {
      this.cs = new ColorSelector(var1);
      this.cs.setColorValue(PreferenceLoader.loadRGB(var2));
      this.prefString = var2;
   }

   public void save() {
      PreferenceLoader.setRGB(this.prefString, this.cs.getColorValue());
   }
}
