package sancho.view.friends.clientDirectories;

import sancho.view.viewer.table.GTableLabelProvider;

public class ClientDirectoriesTableLabelProvider extends GTableLabelProvider {
   public ClientDirectoriesTableLabelProvider(ClientDirectoriesTableView view) {
      super(view);
   }

   public String getColumnText(Object element, int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            String directory = (String)element;
            return directory.equals("") ? "[\\]" : directory;
         default:
            return "";
      }
   }
}
