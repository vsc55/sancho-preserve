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

   public AboutDialog(Shell shell) {
      super(shell);
      Window.setDefaultImage(VersionInfo.getProgramIcon());
      boolean isWin32 = VersionInfo.getSWTPlatform().equals("win32");
      this.setShellStyle(278536 | (isWin32 ? 4 : 0));
      this.cursorOver = new Cursor(shell.getDisplay(), 21);
   }

   public boolean close() {
      if (this.cursorOver != null) {
         this.cursorOver.dispose();
      }

      return super.close();
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(VersionInfo.getName() + " " + VersionInfo.getVersion());
   }

   public Control createContents(Composite parent) {
      final Canvas canvas = new Canvas(parent, 536870912);
      final Image image = SResources.getImage("about");
      GridData gridData = new GridData();
      gridData.widthHint = image.getBounds().width;
      gridData.heightHint = image.getBounds().height;
      canvas.setLayoutData(gridData);
      canvas.setLayout(new FillLayout());
      canvas.addPaintListener(new PaintListener() {
         public void paintControl(PaintEvent event) {
            int y = 315;
            y -= 47;
            byte lineHeight = 20;
            event.gc.drawImage(image, event.x, event.y, event.width, event.height, event.x, event.y, event.width, event.height);
            event.gc.setForeground(canvas.getDisplay().getSystemColor(2));
            AboutDialog.this.drawTextAt(event.gc, "Version:", VersionInfo.getVersion(), "System:", System.getProperty("os.name"), y);
            y += lineHeight;
            AboutDialog.this
               .drawTextAt(
                  event.gc,
                  "SWT:",
                  VersionInfo.getSWTPlatform() + "-" + SWT.getVersion(),
                  "Processors:",
                  String.valueOf(Runtime.getRuntime().availableProcessors()),
                  y
               );
            y += lineHeight;
            AboutDialog.this.drawTextAt(event.gc, "", "", "Uptime:", Sancho.getUptime(), y);
            y += lineHeight;
            AboutDialog.this.drawTextAt(event.gc, "", "", "Total Memory:", String.valueOf(Runtime.getRuntime().totalMemory()), y);
            AboutDialog.this.drawTextAt(event.gc, 2, "Code:", "Rutger Ovidius", y);
            y += lineHeight;
            AboutDialog.this.drawTextAt(event.gc, "", "", "Free Memory:", String.valueOf(Runtime.getRuntime().freeMemory()), y);
            AboutDialog.this.drawTextAt(event.gc, 2, "Graphics:", "Bruce Thomas", y);
         }
      });
      canvas.addMouseMoveListener(new MouseMoveListener() {
         public void mouseMove(MouseEvent event) {
            Canvas canvas = (Canvas)event.widget;
            if (AboutDialog.this.btRect.contains(event.x, event.y) && !AboutDialog.this.mOver) {
               canvas.getShell().setCursor(AboutDialog.this.cursorOver);
               GC gc = new GC(canvas);
               AboutDialog.this.drawTextAt(gc, 1, "Graphics:", "Bruce Thomas", 348);
               gc.dispose();
               AboutDialog.this.mOver = true;
            } else if (AboutDialog.this.urlRect.contains(event.x, event.y) && !AboutDialog.this.mOver) {
               canvas.getShell().setCursor(AboutDialog.this.cursorOver);
               GC gc = new GC(canvas);
               gc.dispose();
               AboutDialog.this.mOver = true;
            } else if (AboutDialog.this.roRect.contains(event.x, event.y) && !AboutDialog.this.mOver) {
               canvas.getShell().setCursor(AboutDialog.this.cursorOver);
               GC gc = new GC(canvas);
               AboutDialog.this.drawTextAt(gc, 1, "Code:", "Rutger Ovidius", 328);
               gc.dispose();
               AboutDialog.this.mOver = true;
            } else if (AboutDialog.this.mOver
               && !AboutDialog.this.roRect.contains(event.x, event.y)
               && !AboutDialog.this.btRect.contains(event.x, event.y)
               && !AboutDialog.this.urlRect.contains(event.x, event.y)) {
               canvas.redraw();
               canvas.getShell().setCursor(null);
               AboutDialog.this.mOver = false;
            }
         }
      });
      canvas.addMouseListener(new MouseAdapter() {
         public void mouseDown(MouseEvent event) {
            if (AboutDialog.this.btRect.contains(event.x, event.y)) {
               WebLauncher.openLink(VersionInfo.getBruceHomePage());
            } else if (AboutDialog.this.urlRect.contains(event.x, event.y)) {
               WebLauncher.openLink(VersionInfo.getHomePage2());
            } else if (AboutDialog.this.roRect.contains(event.x, event.y)) {
               WebLauncher.openLink(VersionInfo.getHomePage2());
            }
         }

         public void mouseUp(MouseEvent event) {
            AboutDialog.this.close();
         }
      });
      canvas.update();
      return parent;
   }

   public void drawTextAt(GC gc, String text1, String text2, String text3, String text4, int y) {
      boolean transparent = true;
      gc.drawText(text1, 40, y, transparent);
      gc.drawText(text2, 100, y, transparent);
      gc.drawText(text3, 230, y, transparent);
      gc.drawText(text4, 330, y, transparent);
   }

   public void drawTextAt(GC gc, int colorId, String text1, String text2, int y) {
      gc.setForeground(this.getShell().getDisplay().getSystemColor(colorId));
      boolean transparent = true;
      gc.drawText(text1, 40, y, transparent);
      gc.drawText(text2, 100, y, transparent);
   }
}
