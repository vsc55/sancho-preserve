package sancho.view.viewFrame;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class TabbedViewFrame$3 implements Listener {
   boolean drag;
   CTabItem dragItem;
   boolean exitDrag;
   // $VF: synthetic field
   private final TabbedViewFrame this$0;

   TabbedViewFrame$3(TabbedViewFrame var1) {
      this.this$0 = var1;
      this.drag = false;
      this.exitDrag = false;
   }

   public void handleEvent(Event var1) {
      Point var2 = this.this$0.cTabFolder.toControl(this.this$0.cTabFolder.getDisplay().getCursorLocation());
      switch (var1.type) {
         case 4:
            if (!this.drag) {
               return;
            }

            this.this$0.cTabFolder.setInsertMark(null, false);
            CTabItem var7 = this.this$0.cTabFolder.getItem(var2);
            if (var7 == this.dragItem) {
               return;
            }

            if (var7 != null) {
               int var8 = this.this$0.cTabFolder.indexOf(var7);
               CTabItem var5 = new CTabItem(this.this$0.cTabFolder, 0, var8);
               var5.setText(this.dragItem.getText());
               var5.setData("filterString", this.dragItem.getData("filterString"));
               this.this$0.onCTabDispose(var5);
               this.dragItem.setControl(null);
               this.dragItem.dispose();
               this.this$0.switchToTab(var5);
            }

            this.drag = false;
            this.exitDrag = false;
            this.dragItem = null;
            break;
         case 5:
            if (!this.drag) {
               return;
            }

            CTabItem var6 = this.this$0.cTabFolder.getItem(var2);
            if (var6 == null) {
               this.this$0.cTabFolder.setInsertMark(null, false);
               return;
            }

            this.this$0.cTabFolder.setInsertMark(var6, false);
            break;
         case 6:
            if (this.exitDrag) {
               this.exitDrag = false;
               this.drag = var1.button != 0;
            }
            break;
         case 7:
            if (this.drag) {
               this.this$0.cTabFolder.setInsertMark(null, false);
               this.exitDrag = true;
               this.drag = false;
            }
            break;
         case 29:
            CTabItem var3 = this.this$0.cTabFolder.getItem(var2);
            CTabItem var4 = this.this$0.cTabFolder.getSelection();
            if (var3 == null || var3 != var4) {
               return;
            }

            this.drag = true;
            this.exitDrag = false;
            this.dragItem = var3;
      }
   }
}
