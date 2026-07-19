package sancho.view.transfer.fileComments;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.utility.FileComment;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableMenuListenerClient;

public class FileCommentsTableMenuListener extends GTableMenuListenerClient implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$utility$FileComment;

   public FileCommentsTableMenuListener(FileCommentsTableView var1) {
      super(var1);
   }

   protected void onDeleteKey() {
   }

   protected void sendToStatusline(String var1) {
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$utility$FileComment == null
            ? (class$sancho$model$mldonkey$utility$FileComment = class$("sancho.model.mldonkey.utility.FileComment"))
            : class$sancho$model$mldonkey$utility$FileComment
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new CopyFileCommentsToClipboardAction());
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

   // Menu action that copies the selected file comments to the clipboard.
   private class CopyFileCommentsToClipboardAction extends Action {
      public CopyFileCommentsToClipboardAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         StringBuffer var1 = new StringBuffer(50);
         String var2 = System.getProperty("line.separator");

         for (int var3 = 0; var3 < FileCommentsTableMenuListener.this.selectedObjects.size(); var3++) {
            FileComment var4 = (FileComment)FileCommentsTableMenuListener.this.selectedObjects.get(var3);
            if (var3 > 0) {
               var1.append(var2);
            }

            var1.append(var4.toString());
         }

         MainWindow.copyToClipboard(var1.toString());
      }
   }
}
