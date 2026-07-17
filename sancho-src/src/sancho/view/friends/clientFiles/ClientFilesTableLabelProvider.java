package sancho.view.friends.clientFiles;

import sancho.model.mldonkey.Result;
import sancho.view.viewer.table.GTableLabelProvider;

public class ClientFilesTableLabelProvider extends GTableLabelProvider {
   public ClientFilesTableLabelProvider(ClientFilesTableView var1) {
      super(var1);
   }

   public String getColumnText(Object var1, int var2) {
      Result var3 = (Result)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return var3.getSizeString();
         case 2:
            return var3.getFormat();
         case 3:
            return var3.getType();
         case 4:
            return var3.getCodecTag();
         case 5:
            return var3.getBitrateTagString();
         case 6:
            return var3.getLengthTag();
         case 7:
            return var3.getMD4().toUpperCase();
         default:
            return "";
      }
   }
}
