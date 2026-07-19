package sancho.view.utility.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;
import sancho.view.utility.WebLauncher;

public class AboutDialog extends Dialog {
   private static final String S_URL = "URL:";
   private static final String S_HP = "sancho-gui.sf.net";
   private static final String S_CODE = "Code:";
   private static final String S_RO = "Rutger Ovidius";
   private static final String S_GRAPHICS = "Graphics:";
   private static final String S_BT = "Bruce Thomas";
   private static final int _X1 = 40;
   private static final int _X2 = 100;
   private static final int _X3 = 230;
   private static final int _X4 = 330;
   Rectangle btRect = new Rectangle(38, 348, 162, 17);
   Rectangle roRect = new Rectangle(38, 328, 162, 17);
   Rectangle urlRect = new Rectangle(38, 309, 162, 17);
   Cursor cursorOver;
   boolean mOver;

   public AboutDialog(Shell var1) {
      super(var1);
      Window.setDefaultImage(VersionInfo.getProgramIcon());
      boolean var2 = VersionInfo.getSWTPlatform().equals("win32");
      this.setShellStyle(278536 | (var2 ? 4 : 0));
      this.cursorOver = new Cursor(var1.getDisplay(), 21);
   }

   public boolean close() {
      if (this.cursorOver != null) {
         this.cursorOver.dispose();
      }

      return super.close();
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(VersionInfo.getName() + " " + VersionInfo.getVersion());
   }

   public Control createContents(Composite var1) {
      final Canvas canvas = new Canvas(var1, 536870912);
      final Image image = SResources.getImage("about");
      GridData var4 = new GridData();
      var4.widthHint = image.getBounds().width;
      var4.heightHint = image.getBounds().height;
      canvas.setLayoutData(var4);
      canvas.setLayout(new FillLayout());
      canvas.addPaintListener(new PaintListener() {
         public void paintControl(PaintEvent var1) {
            int var2 = 315;
            var2 -= 47;
            byte var3 = 20;
            var1.gc.drawImage(image, var1.x, var1.y, var1.width, var1.height, var1.x, var1.y, var1.width, var1.height);
            var1.gc.setForeground(canvas.getDisplay().getSystemColor(2));
            AboutDialog.this.drawTextAt(var1.gc, "Version:", VersionInfo.getVersion(), "System:", System.getProperty("os.name"), var2);
            var2 += var3;
            AboutDialog.this
               .drawTextAt(
                  var1.gc,
                  "SWT:",
                  VersionInfo.getSWTPlatform() + "-" + SWT.getVersion(),
                  "Processors:",
                  String.valueOf(Runtime.getRuntime().availableProcessors()),
                  var2
               );
            var2 += var3;
            AboutDialog.this.drawTextAt(var1.gc, "", "", "Uptime:", Sancho.getUptime(), var2);
            var2 += var3;
            AboutDialog.this.drawTextAt(var1.gc, "", "", "Total Memory:", String.valueOf(Runtime.getRuntime().totalMemory()), var2);
            AboutDialog.this.drawTextAt(var1.gc, 2, "Code:", "Rutger Ovidius", var2);
            var2 += var3;
            AboutDialog.this.drawTextAt(var1.gc, "", "", "Free Memory:", String.valueOf(Runtime.getRuntime().freeMemory()), var2);
            AboutDialog.this.drawTextAt(var1.gc, 2, "Graphics:", "Bruce Thomas", var2);
         }
      });
      canvas.addMouseMoveListener(new MouseMoveListener() {
         public void mouseMove(MouseEvent var1) {
            Canvas var2 = (Canvas)var1.widget;
            if (AboutDialog.this.btRect.contains(var1.x, var1.y) && !AboutDialog.this.mOver) {
               var2.getShell().setCursor(AboutDialog.this.cursorOver);
               GC var5 = new GC(var2);
               AboutDialog.this.drawTextAt(var5, 1, "Graphics:", "Bruce Thomas", 348);
               var5.dispose();
               AboutDialog.this.mOver = true;
            } else if (AboutDialog.this.urlRect.contains(var1.x, var1.y) && !AboutDialog.this.mOver) {
               var2.getShell().setCursor(AboutDialog.this.cursorOver);
               GC var4 = new GC(var2);
               var4.dispose();
               AboutDialog.this.mOver = true;
            } else if (AboutDialog.this.roRect.contains(var1.x, var1.y) && !AboutDialog.this.mOver) {
               var2.getShell().setCursor(AboutDialog.this.cursorOver);
               GC var3 = new GC(var2);
               AboutDialog.this.drawTextAt(var3, 1, "Code:", "Rutger Ovidius", 328);
               var3.dispose();
               AboutDialog.this.mOver = true;
            } else if (AboutDialog.this.mOver
               && !AboutDialog.this.roRect.contains(var1.x, var1.y)
               && !AboutDialog.this.btRect.contains(var1.x, var1.y)
               && !AboutDialog.this.urlRect.contains(var1.x, var1.y)) {
               var2.redraw();
               var2.getShell().setCursor(null);
               AboutDialog.this.mOver = false;
            }
         }
      });
      canvas.addMouseListener(new MouseAdapter() {
         public void mouseDown(MouseEvent var1) {
            if (AboutDialog.this.btRect.contains(var1.x, var1.y)) {
               WebLauncher.openLink(VersionInfo.getBruceHomePage());
            } else if (AboutDialog.this.urlRect.contains(var1.x, var1.y)) {
               WebLauncher.openLink(VersionInfo.getHomePage2());
            } else if (AboutDialog.this.roRect.contains(var1.x, var1.y)) {
               WebLauncher.openLink(VersionInfo.getHomePage2());
            }
         }

         public void mouseUp(MouseEvent var1) {
            AboutDialog.this.close();
         }
      });
      canvas.update();
      return var1;
   }

   public void drawTextAt(GC var1, String var2, String var3, String var4, String var5, int var6) {
      boolean var7 = true;
      var1.drawText(var2, 40, var6, var7);
      var1.drawText(var3, 100, var6, var7);
      var1.drawText(var4, 230, var6, var7);
      var1.drawText(var5, 330, var6, var7);
   }

   public void drawTextAt(GC var1, int var2, String var3, String var4, int var5) {
      var1.setForeground(this.getShell().getDisplay().getSystemColor(var2));
      boolean var6 = true;
      var1.drawText(var3, 40, var5, var6);
      var1.drawText(var4, 100, var5, var6);
   }
}
