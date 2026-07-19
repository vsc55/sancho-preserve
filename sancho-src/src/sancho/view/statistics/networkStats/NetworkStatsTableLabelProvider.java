package sancho.view.statistics.networkStats;

import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.utility.NetworkStat;
import sancho.model.mldonkey.utility.NetworkStatTotal;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewer.table.GTableLabelProvider;

public class NetworkStatsTableLabelProvider extends GTableLabelProvider implements ITableFontProvider {
   Font boldFont;

   public NetworkStatsTableLabelProvider(NetworkStatsTableView tableView) {
      super(tableView);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         default:
            return null;
      }
   }

   public Font getFont(Object element, int columnIndex) {
      return element instanceof NetworkStatTotal ? this.boldFont : null;
   }

   public String getColumnText(Object element, int columnIndex) {
      NetworkStat stat = (NetworkStat)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return stat.getNameLong();
         case 1:
            return stat.getSeenString();
         case 2:
            return stat.getSeenPercentString();
         case 3:
            return stat.getBannedString();
         case 4:
            return stat.getBannedPercentString();
         case 5:
            return stat.getRequestString();
         case 6:
            return stat.getRequestPercentString();
         case 7:
            return stat.getUploadString();
         case 8:
            return stat.getUploadPercentString();
         case 9:
            return stat.getUploadRateString();
         case 10:
            return stat.getDownloadString();
         case 11:
            return stat.getDownloadPercentString();
         case 12:
            return stat.getDownloadRateString();
         case 13:
            return stat.getRatioString();
         default:
            return "";
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      if (this.boldFont != null) {
         this.boldFont.dispose();
      }

      this.boldFont = null;
      Font font = PreferenceLoader.loadFont("tableFontData");
      FontData[] fontData = font.getFontData();

      for (int i = 0; i < fontData.length; i++) {
         fontData[i].setStyle(1);
      }

      this.boldFont = new Font(null, fontData);
   }

   public void dispose() {
      super.dispose();
      if (this.boldFont != null) {
         this.boldFont.dispose();
         this.boldFont = null;
      }
   }
}
