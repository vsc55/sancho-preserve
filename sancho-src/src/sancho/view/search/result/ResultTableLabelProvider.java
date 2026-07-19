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

   public ResultTableLabelProvider(ResultTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      Result result = (Result)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            if (result.isDownloading()) {
               return SResources.getImage("down_arrow_green");
            }

            return result.getNetworkImage();
         case 1:
            return result.getFileTypeImage();
         case 8:
            return result.getRating().getImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      Result result = (Result)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return result.getNetworkName();
         case 1:
            return result.getName();
         case 2:
            return result.getSizeString();
         case 3:
            return result.getFormat();
         case 4:
            return result.getType();
         case 5:
            return result.getCodecTag();
         case 6:
            return result.getBitrateTagString();
         case 7:
            return result.getLengthTag();
         case 8:
            return result.getRatingString();
         case 9:
            return result.getCompleteSourcesString();
         default:
            return "";
      }
   }

   public Color getForeground(Object element, int columnIndex) {
      Result result = (Result)element;
      if (result.downloaded()) {
         return this.alreadyDownloadedColor;
      } else {
         return result.containsFake() ? this.containsFakeColor : this.defaultColor;
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.defaultColor = PreferenceLoader.loadColor("resultDefaultColor");
      this.alreadyDownloadedColor = PreferenceLoader.loadColor("resultAlreadyDownloadedColor");
      this.containsFakeColor = PreferenceLoader.loadColor("resultFakeColor");
   }
}
