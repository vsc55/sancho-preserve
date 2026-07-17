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

   public NetworkStatsTableLabelProvider(NetworkStatsTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      switch (this.cViewer.getColumnIDs()[var2]) {
         default:
            return null;
      }
   }

   public Font getFont(Object var1, int var2) {
      return var1 instanceof NetworkStatTotal ? this.boldFont : null;
   }

   public String getColumnText(Object var1, int var2) {
      NetworkStat var3 = (NetworkStat)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getNameLong();
         case 1:
            return var3.getSeenString();
         case 2:
            return var3.getSeenPercentString();
         case 3:
            return var3.getBannedString();
         case 4:
            return var3.getBannedPercentString();
         case 5:
            return var3.getRequestString();
         case 6:
            return var3.getRequestPercentString();
         case 7:
            return var3.getUploadString();
         case 8:
            return var3.getUploadPercentString();
         case 9:
            return var3.getUploadRateString();
         case 10:
            return var3.getDownloadString();
         case 11:
            return var3.getDownloadPercentString();
         case 12:
            return var3.getDownloadRateString();
         case 13:
            return var3.getRatioString();
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
      Font var1 = PreferenceLoader.loadFont("tableFontData");
      FontData[] var2 = var1.getFontData();

      for (int var3 = 0; var3 < var2.length; var3++) {
         var2[var3].setStyle(1);
      }

      this.boldFont = new Font(null, var2);
   }

   public void dispose() {
      super.dispose();
      this.boldFont.dispose();
      this.boldFont = null;
   }
}
