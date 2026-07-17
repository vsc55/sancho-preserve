package sancho.view;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import sancho.model.mldonkey.Client;

class FriendsTab$1 extends MouseAdapter {
   // $VF: synthetic field
   private final FriendsTab this$0;

   FriendsTab$1(FriendsTab var1) {
      this.this$0 = var1;
   }

   public void mouseDoubleClick(MouseEvent var1) {
      if (var1.widget instanceof Table) {
         Table var2 = (Table)var1.widget;
         TableItem[] var3 = var2.getSelection();

         for (int var4 = 0; var4 < var3.length; var4++) {
            this.this$0.openTab((Client)var3[var4].getData());
         }
      }
   }
}
