package sancho.view.transfer.fileComments;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
         var1.add(new FileCommentsTableMenuListener$CopyFileCommentsToClipboardAction(this));
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

   // $VF: synthetic method
   static List access$000(FileCommentsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$100(FileCommentsTableMenuListener var0) {
      return var0.selectedObjects;
   }
}
