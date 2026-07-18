package sancho.view;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Combo;
import sancho.view.transfer.UniformResourceLocator;

class WebBrowserTab$1 extends DropTargetAdapter {
   // $VF: synthetic field
   private final UniformResourceLocator val$uRL;
   // $VF: synthetic field
   private final Combo val$linkEntryCombo;
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$1(WebBrowserTab var1, UniformResourceLocator var2, Combo var3) {
      this.this$0 = var1;
      this.val$uRL = var2;
      this.val$linkEntryCombo = var3;
   }

   public void dragEnter(DropTargetEvent var1) {
      // Only request DROP_LINK when the source actually offers it; forcing detail=4
      // unconditionally made SWT reject a COPY-only drag (plain text/selection), so
      // the drop was silently never delivered. Fall back to COPY, else NONE.
      boolean var2 = false;

      for (int var3 = 0; var3 < var1.dataTypes.length; var3++) {
         if (this.val$uRL.isSupportedType(var1.dataTypes[var3])) {
            var2 = true;
            break;
         }
      }

      if (var2 && (var1.operations & 4) != 0) {
         var1.detail = 4;
      } else if ((var1.operations & 1) != 0) {
         var1.detail = 1;
      } else {
         var1.detail = 0;
      }
   }

   public void drop(DropTargetEvent var1) {
      if (var1.data != null) {
         this.val$linkEntryCombo.setText((String)var1.data);
      }
   }
}
