package sancho.view.utility;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;

public class Splash {
   private static Shell shell = null;
   private static Display display;
   public static boolean[] on = new boolean[10];
   public static int[] boxes = new int[]{13, 66, 107, 162, 212, 259, 314, 363, 398, 441, 492};

   public Splash(Display var1) {
      display = var1;
      if (PreferenceLoader.loadBoolean("splashScreen")) {
         this.createContents(display);
      }
   }

   public void createContents(Display var1) {
      boolean var2 = VersionInfo.getSWTPlatform().equals("win32");
      shell = new Shell(var1, 278536 | (var2 ? 4 : 0));
      shell.setLayout(new FillLayout());
      Image var3 = SResources.getImage("splashScreen");
      Rectangle var4 = var1.getPrimaryMonitor().getBounds();
      Rectangle var5 = var3.getBounds();
      shell.setBounds(var4.x + (var4.width - var5.width) / 2, var4.y + (var4.height - var5.height) / 2, var5.width, var5.height);
      shell.open();
      shell.update();
   }

   public static void updateText(String var0) {
      updateText(var0, "");
   }

   public static void updateText(String var0, String var1) {
      updateText(var0, var1, -1);
   }

   public static void updateText(String var0, String var1, int var2) {
      if (shell != null) {
         String var3 = SResources.getString(var0) + var1;
         Image var4 = new Image(shell.getDisplay(), shell.getBounds());
         GC var5 = new GC(var4);
         var5.drawImage(SResources.getImage("splashScreen"), 0, 0);
         var5.setForeground(shell.getDisplay().getSystemColor(1));
         var5.drawText(var3 + "...", 15, shell.getBounds().height - 25, true);
         if (var2 >= 0) {
            on[var2] = true;
         }

         Image var6 = SResources.getImage("splashHighlight");

         for (int var7 = 0; var7 < on.length; var7++) {
            if (on[var7]) {
               int var8 = boxes[var7 + 1] - boxes[var7];
               var5.drawImage(var6, boxes[var7], 0, var8, 57, boxes[var7], 173, var8, 57);
            }
         }

         GC var9 = new GC(shell);
         var9.drawImage(var4, 0, 0);
         var9.dispose();
         var4.dispose();
         var5.dispose();
         display.readAndDispatch();
      }
   }

   public static void dispose() {
      if (shell != null) {
         shell.dispose();
         shell = null;
         display = null;
      }
   }

   public static void setVisible(boolean var0) {
      if (shell != null) {
         shell.setVisible(var0);
      }
   }
}
