package sancho.view.downloadComplete;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.viewer.table.GTableMenuListener;

public class DownloadCompleteTableMenuListener extends GTableMenuListener implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$view$downloadComplete$DownloadCompleteItem;

   public DownloadCompleteTableMenuListener(DownloadCompleteTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$view$downloadComplete$DownloadCompleteItem == null
            ? (class$sancho$view$downloadComplete$DownloadCompleteItem = class$("sancho.view.downloadComplete.DownloadCompleteItem"))
            : class$sancho$view$downloadComplete$DownloadCompleteItem
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         String[] var2 = new String[this.selectedObjects.size()];

         for (int var3 = 0; var3 < this.selectedObjects.size(); var3++) {
            var2[var3] = ((DownloadCompleteItem)this.selectedObjects.get(var3)).getLink();
         }

         this.addClipboardMenu(var1);
      }
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
