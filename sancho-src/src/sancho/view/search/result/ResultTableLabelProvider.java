package sancho.view.search.result;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Result;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableLabelProvider;

public class ResultTableLabelProvider extends GTableLabelProvider {
   private Color alreadyDownloadedColor;
   private Color containsFakeColor;
   private Color defaultColor;

   public ResultTableLabelProvider(ResultTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      Result var3 = (Result)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            if (var3.isDownloading()) {
               return SResources.getImage("down_arrow_green");
            }

            return var3.getNetworkImage();
         case 1:
            return var3.getFileTypeImage();
         case 8:
            return var3.getRating().getImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      Result var3 = (Result)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getNetworkName();
         case 1:
            return var3.getName();
         case 2:
            return var3.getSizeString();
         case 3:
            return var3.getFormat();
         case 4:
            return var3.getType();
         case 5:
            return var3.getCodecTag();
         case 6:
            return var3.getBitrateTagString();
         case 7:
            return var3.getLengthTag();
         case 8:
            return var3.getRatingString();
         case 9:
            return var3.getCompleteSourcesString();
         default:
            return "";
      }
   }

   public Color getForeground(Object var1, int var2) {
      Result var3 = (Result)var1;
      if (var3.downloaded()) {
         return this.alreadyDownloadedColor;
      } else {
         return var3.containsFake() ? this.containsFakeColor : this.defaultColor;
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.defaultColor = PreferenceLoader.loadColor("resultDefaultColor");
      this.alreadyDownloadedColor = PreferenceLoader.loadColor("resultAlreadyDownloadedColor");
      this.containsFakeColor = PreferenceLoader.loadColor("resultFakeColor");
   }
}
