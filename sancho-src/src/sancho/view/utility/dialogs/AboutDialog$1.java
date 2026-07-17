package sancho.view.utility.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;

class AboutDialog$1 implements PaintListener {
   // $VF: synthetic field
   private final Image val$image;
   // $VF: synthetic field
   private final Canvas val$canvas;
   // $VF: synthetic field
   private final AboutDialog this$0;

   AboutDialog$1(AboutDialog var1, Image var2, Canvas var3) {
      this.this$0 = var1;
      this.val$image = var2;
      this.val$canvas = var3;
   }

   public void paintControl(PaintEvent var1) {
      int var2 = 315;
      var2 -= 47;
      byte var3 = 20;
      var1.gc.drawImage(this.val$image, var1.x, var1.y, var1.width, var1.height, var1.x, var1.y, var1.width, var1.height);
      var1.gc.setForeground(this.val$canvas.getDisplay().getSystemColor(2));
      this.this$0.drawTextAt(var1.gc, "Version:", VersionInfo.getVersion(), "System:", System.getProperty("os.name"), var2);
      var2 += var3;
      this.this$0
         .drawTextAt(
            var1.gc,
            "SWT:",
            VersionInfo.getSWTPlatform() + "-" + SWT.getVersion(),
            "Processors:",
            String.valueOf(Runtime.getRuntime().availableProcessors()),
            var2
         );
      var2 += var3;
      this.this$0.drawTextAt(var1.gc, "", "", "Uptime:", Sancho.getUptime(), var2);
      var2 += var3;
      this.this$0.drawTextAt(var1.gc, "", "", "Total Memory:", String.valueOf(Runtime.getRuntime().totalMemory()), var2);
      this.this$0.drawTextAt(var1.gc, 2, "Code:", "Rutger Ovidius", var2);
      var2 += var3;
      this.this$0.drawTextAt(var1.gc, "", "", "Free Memory:", String.valueOf(Runtime.getRuntime().freeMemory()), var2);
      this.this$0.drawTextAt(var1.gc, 2, "Graphics:", "Bruce Thomas", var2);
   }
}
