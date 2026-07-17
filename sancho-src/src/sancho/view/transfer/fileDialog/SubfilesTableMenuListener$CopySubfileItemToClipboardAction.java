package sancho.view.transfer.fileDialog;

import org.eclipse.jface.action.Action;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class SubfilesTableMenuListener$CopySubfileItemToClipboardAction extends Action {
   // $VF: synthetic field
   private final SubfilesTableMenuListener this$0;

   public SubfilesTableMenuListener$CopySubfileItemToClipboardAction(SubfilesTableMenuListener var1) {
      super(SResources.getString("mi.copy"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("copy"));
   }

   public void run() {
      StringBuffer var1 = new StringBuffer(50);
      String var2 = System.getProperty("line.separator");

      for (int var3 = 0; var3 < SubfilesTableMenuListener.access$000(this.this$0).size(); var3++) {
         SubfileItem var4 = (SubfileItem)SubfilesTableMenuListener.access$100(this.this$0).get(var3);
         if (var3 > 0) {
            var1.append(var2);
         }

         var1.append(var4.toString());
      }

      MainWindow.copyToClipboard(var1.toString());
   }
}
