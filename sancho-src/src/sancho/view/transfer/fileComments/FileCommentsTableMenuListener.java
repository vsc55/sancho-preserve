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

   public FileCommentsTableMenuListener(FileCommentsTableView view) {
      super(view);
   }

   protected void onDeleteKey() {
   }

   protected void sendToStatusline(String text) {
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$utility$FileComment == null
            ? (class$sancho$model$mldonkey$utility$FileComment = class$("sancho.model.mldonkey.utility.FileComment"))
            : class$sancho$model$mldonkey$utility$FileComment
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         menuManager.add(new CopyFileCommentsToClipboardAction());
      }
   }

   // $VF: synthetic method
   static Class class$(String name) {
      try {
         return Class.forName(name);
      } catch (ClassNotFoundException notFound) {
         throw new NoClassDefFoundError(notFound.getMessage());
      }
   }

   // Menu action that copies the selected file comments to the clipboard.
   private class CopyFileCommentsToClipboardAction extends Action {
      public CopyFileCommentsToClipboardAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         StringBuffer buffer = new StringBuffer(50);
         String lineSeparator = System.getProperty("line.separator");

         for (int i = 0; i < FileCommentsTableMenuListener.this.selectedObjects.size(); i++) {
            FileComment comment = (FileComment)FileCommentsTableMenuListener.this.selectedObjects.get(i);
            if (i > 0) {
               buffer.append(lineSeparator);
            }

            buffer.append(comment.toString());
         }

         MainWindow.copyToClipboard(buffer.toString());
      }
   }
}
