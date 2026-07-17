package sancho.view.transfer.fileDialog;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.IPreview;
import sancho.view.viewer.table.GTableMenuListenerClient;

public class SubfilesTableMenuListener extends GTableMenuListenerClient implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$view$transfer$fileDialog$SubfileItem;

   public SubfilesTableMenuListener(SubfilesTableView var1) {
      super(var1);
   }

   protected void onDeleteKey() {
   }

   protected void sendToStatusline(String var1) {
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$view$transfer$fileDialog$SubfileItem == null
            ? (class$sancho$view$transfer$fileDialog$SubfileItem = class$("sancho.view.transfer.fileDialog.SubfileItem"))
            : class$sancho$view$transfer$fileDialog$SubfileItem
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new SubfilesTableMenuListener$CopySubfileItemToClipboardAction(this));
         int[] var2 = new int[this.selectedObjects.size()];

         for (int var3 = 0; var3 < this.selectedObjects.size(); var3++) {
            SubfileItem var4 = (SubfileItem)this.selectedObjects.get(var3);
            var2[var3] = var4.getNum();
         }

         IPreview[] var5 = new IPreview[]{((SubfilesTableView)this.gView).getIPreview()};
         this.addPreview(var1, var5, var2);
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
   static List access$000(SubfilesTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$100(SubfilesTableMenuListener var0) {
      return var0.selectedObjects;
   }
}
