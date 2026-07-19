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

   public Splash(Display parentDisplay) {
      display = parentDisplay;
      if (PreferenceLoader.loadBoolean("splashScreen")) {
         this.createContents(display);
      }
   }

   public void createContents(Display parentDisplay) {
      boolean isWin32 = VersionInfo.getSWTPlatform().equals("win32");
      shell = new Shell(parentDisplay, 278536 | (isWin32 ? 4 : 0));
      shell.setLayout(new FillLayout());
      Image image = SResources.getImage("splashScreen");
      Rectangle monitorBounds = parentDisplay.getPrimaryMonitor().getBounds();
      Rectangle imageBounds = image.getBounds();
      shell.setBounds(monitorBounds.x + (monitorBounds.width - imageBounds.width) / 2, monitorBounds.y + (monitorBounds.height - imageBounds.height) / 2, imageBounds.width, imageBounds.height);
      shell.open();
      shell.update();
   }

   public static void updateText(String key) {
      updateText(key, "");
   }

   public static void updateText(String key, String suffix) {
      updateText(key, suffix, -1);
   }

   public static void updateText(String key, String suffix, int boxIndex) {
      if (shell != null) {
         String text = SResources.getString(key) + suffix;
         Image bufferImage = new Image(shell.getDisplay(), shell.getBounds());
         GC gc = new GC(bufferImage);
         gc.drawImage(SResources.getImage("splashScreen"), 0, 0);
         gc.setForeground(shell.getDisplay().getSystemColor(1));
         gc.drawText(text + "...", 15, shell.getBounds().height - 25, true);
         if (boxIndex >= 0 && boxIndex < on.length) {
            on[boxIndex] = true;
         }

         Image highlightImage = SResources.getImage("splashHighlight");

         for (int i = 0; i < on.length; i++) {
            if (on[i]) {
               int boxWidth = boxes[i + 1] - boxes[i];
               gc.drawImage(highlightImage, boxes[i], 0, boxWidth, 57, boxes[i], 173, boxWidth, 57);
            }
         }

         GC shellGc = new GC(shell);
         shellGc.drawImage(bufferImage, 0, 0);
         shellGc.dispose();
         bufferImage.dispose();
         gc.dispose();
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

   public static void setVisible(boolean visible) {
      if (shell != null) {
         shell.setVisible(visible);
      }
   }
}
