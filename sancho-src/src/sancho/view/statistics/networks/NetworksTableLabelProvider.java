package sancho.view.statistics.networks;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Network;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewer.table.GTableLabelProvider;

public class NetworksTableLabelProvider extends GTableLabelProvider {
   private Color enabledColor;

   public NetworksTableLabelProvider(NetworksTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      switch (this.cViewer.getColumnIDs()[var2]) {
         default:
            return null;
      }
   }

   public Color getForeground(Object var1, int var2) {
      if (var1 instanceof Network) {
         Network var3 = (Network)var1;
         if (var3.isEnabled()) {
            return this.enabledColor;
         }
      }

      return null;
   }

   public String getColumnText(Object var1, int var2) {
      Network var3 = (Network)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return var3.getUploadedString();
         case 2:
            return var3.getDownloadedString();
         default:
            return "";
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.enabledColor = PreferenceLoader.loadColor("networkEnabledColor");
   }
}
