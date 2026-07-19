package sancho.view.friends.clientFiles;

import sancho.model.mldonkey.Result;
import sancho.view.viewer.table.GTableLabelProvider;

public class ClientFilesTableLabelProvider extends GTableLabelProvider {
   public ClientFilesTableLabelProvider(ClientFilesTableView view) {
      super(view);
   }

   public String getColumnText(Object element, int columnIndex) {
      Result result = (Result)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return result.getName();
         case 1:
            return result.getSizeString();
         case 2:
            return result.getFormat();
         case 3:
            return result.getType();
         case 4:
            return result.getCodecTag();
         case 5:
            return result.getBitrateTagString();
         case 6:
            return result.getLengthTag();
         case 7:
            return result.getMD4().toUpperCase();
         default:
            return "";
      }
   }
}
