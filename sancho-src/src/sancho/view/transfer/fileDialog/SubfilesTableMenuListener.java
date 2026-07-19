package sancho.view.transfer.fileDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.IPreview;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableMenuListenerClient;

public class SubfilesTableMenuListener extends GTableMenuListenerClient implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$view$transfer$fileDialog$SubfileItem;

   public SubfilesTableMenuListener(SubfilesTableView view) {
      super(view);
   }

   protected void onDeleteKey() {
   }

   protected void sendToStatusline(String text) {
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$view$transfer$fileDialog$SubfileItem == null
            ? (class$sancho$view$transfer$fileDialog$SubfileItem = class$("sancho.view.transfer.fileDialog.SubfileItem"))
            : class$sancho$view$transfer$fileDialog$SubfileItem
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         menuManager.add(new CopySubfileItemToClipboardAction());
         int[] nums = new int[this.selectedObjects.size()];

         for (int i = 0; i < this.selectedObjects.size(); i++) {
            SubfileItem subfile = (SubfileItem)this.selectedObjects.get(i);
            nums[i] = subfile.getNum();
         }

         IPreview[] previews = new IPreview[]{((SubfilesTableView)this.gView).getIPreview()};
         this.addPreview(menuManager, previews, nums);
      }
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }

   // Menu action that copies the selected subfile items to the clipboard.
   private class CopySubfileItemToClipboardAction extends Action {
      public CopySubfileItemToClipboardAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         StringBuffer buffer = new StringBuffer(50);
         String separator = System.getProperty("line.separator");

         for (int i = 0; i < SubfilesTableMenuListener.this.selectedObjects.size(); i++) {
            SubfileItem subfile = (SubfileItem)SubfilesTableMenuListener.this.selectedObjects.get(i);
            if (i > 0) {
               buffer.append(separator);
            }

            buffer.append(subfile.toString());
         }

         MainWindow.copyToClipboard(buffer.toString());
      }
   }
}
