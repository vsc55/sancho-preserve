package sancho.view.friends.clientDirectories;

import sancho.view.viewer.table.GTableLabelProvider;

public class ClientDirectoriesTableLabelProvider extends GTableLabelProvider {
   public ClientDirectoriesTableLabelProvider(ClientDirectoriesTableView var1) {
      super(var1);
   }

   public String getColumnText(Object var1, int var2) {
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            String var3 = (String)var1;
            return var3.equals("") ? "[\\]" : var3;
         default:
            return "";
      }
   }
}
