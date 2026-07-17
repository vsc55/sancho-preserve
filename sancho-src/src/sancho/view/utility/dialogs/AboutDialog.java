package sancho.view.utility.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
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
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;

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
      Canvas var2 = new Canvas(var1, 536870912);
      Image var3 = SResources.getImage("about");
      GridData var4 = new GridData();
      var4.widthHint = var3.getBounds().width;
      var4.heightHint = var3.getBounds().height;
      var2.setLayoutData(var4);
      var2.setLayout(new FillLayout());
      var2.addPaintListener(new AboutDialog$1(this, var3, var2));
      var2.addMouseMoveListener(new AboutDialog$2(this));
      var2.addMouseListener(new AboutDialog$3(this));
      var2.update();
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
