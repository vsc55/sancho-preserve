package sancho.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

public class WebBrowserTab_win32$URLFile extends Action {
   String urlFile;
   // $VF: synthetic field
   private final WebBrowserTab_win32 this$0;

   public WebBrowserTab_win32$URLFile(WebBrowserTab_win32 var1, File var2) {
      this.this$0 = var1;
      String var3 = var2.getName();
      this.urlFile = var2.toString();
      int var4 = var3.lastIndexOf(".");
      var3 = var4 == -1 ? var3 : var3.substring(0, var4);
      this.setText(var1.formatTitle(var3));
      this.setImageDescriptor(SResources.getImageDescriptor("web-link"));
   }

   public void run() {
      String var1 = null;

      try {
         BufferedReader var2 = new BufferedReader(new FileReader(this.urlFile));

         String var3;
         while ((var3 = var2.readLine()) != null) {
            if (var3.startsWith("URL=")) {
               var1 = var3.substring(4, var3.length());
            }
         }

         var2.close();
      } catch (Exception var4) {
      }

      if (var1 != null) {
         this.this$0.navigate(this.this$0.getSelectedBrowser(), var1);
      }
   }
}
