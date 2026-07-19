package sancho.view.downloadComplete;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.viewer.table.GTableMenuListener;

public class DownloadCompleteTableMenuListener extends GTableMenuListener implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$view$downloadComplete$DownloadCompleteItem;

   public DownloadCompleteTableMenuListener(DownloadCompleteTableView view) {
      super(view);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$view$downloadComplete$DownloadCompleteItem == null
            ? (class$sancho$view$downloadComplete$DownloadCompleteItem = class$("sancho.view.downloadComplete.DownloadCompleteItem"))
            : class$sancho$view$downloadComplete$DownloadCompleteItem
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         String[] links = new String[this.selectedObjects.size()];

         for (int i = 0; i < this.selectedObjects.size(); i++) {
            links[i] = ((DownloadCompleteItem)this.selectedObjects.get(i)).getLink();
         }

         this.addClipboardMenu(menuManager);
      }
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException classNotFoundException) {
         throw new NoClassDefFoundError(classNotFoundException.getMessage());
      }
   }
}
