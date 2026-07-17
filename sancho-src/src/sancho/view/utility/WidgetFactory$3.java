package sancho.view.utility;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.view.transfer.UniformResourceLocator;

class WidgetFactory$3 extends DropTargetAdapter {
   // $VF: synthetic field
   private final UniformResourceLocator val$uRL;

   WidgetFactory$3(UniformResourceLocator var1) {
      this.val$uRL = var1;
   }

   public void dragEnter(DropTargetEvent var1) {
      var1.detail = 1;

      for (int var2 = 0; var2 < var1.dataTypes.length; var2++) {
         if (this.val$uRL.isSupportedType(var1.dataTypes[var2])) {
            var1.detail = 4;
            break;
         }
      }
   }

   public void drop(DropTargetEvent var1) {
      if (var1.data != null) {
         SwissArmy.sendLink(Sancho.getCore(), (String)var1.data);
      }
   }
}
