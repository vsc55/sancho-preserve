package sancho.view.statistics.networks;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Network;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewer.table.GTableLabelProvider;

public class NetworksTableLabelProvider extends GTableLabelProvider {
   private Color enabledColor;

   public NetworksTableLabelProvider(NetworksTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         default:
            return null;
      }
   }

   public Color getForeground(Object element, int columnIndex) {
      if (element instanceof Network) {
         Network network = (Network)element;
         if (network.isEnabled()) {
            return this.enabledColor;
         }
      }

      return null;
   }

   public String getColumnText(Object element, int columnIndex) {
      Network network = (Network)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return network.getName();
         case 1:
            return network.getUploadedString();
         case 2:
            return network.getDownloadedString();
         default:
            return "";
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.enabledColor = PreferenceLoader.loadColor("networkEnabledColor");
   }
}
