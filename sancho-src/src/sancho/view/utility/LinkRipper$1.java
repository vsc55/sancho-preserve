package sancho.view.utility;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Text;
import sancho.view.transfer.UniformResourceLocator;

class LinkRipper$1 extends DropTargetAdapter {
   // $VF: synthetic field
   private final UniformResourceLocator val$uRL;
   // $VF: synthetic field
   private final Text val$linkEntryText;
   // $VF: synthetic field
   private final LinkRipper this$0;

   LinkRipper$1(LinkRipper var1, UniformResourceLocator var2, Text var3) {
      this.this$0 = var1;
      this.val$uRL = var2;
      this.val$linkEntryText = var3;
   }

   public void dragEnter(DropTargetEvent var1) {
      // Request DROP_LINK only if the source offers it, else COPY, else NONE — forcing
      // detail=4 made SWT reject a COPY-only drag so the drop was never delivered.
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
         this.val$linkEntryText.append((String)var1.data);
      }
   }
}
