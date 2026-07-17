package sancho.view.utility;

import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

class HeaderBarMouseAdapter$1 extends MenuAdapter {
   Font oldFont;
   Font boldFont;
   // $VF: synthetic field
   private final HeaderBarMouseAdapter this$0;

   HeaderBarMouseAdapter$1(HeaderBarMouseAdapter var1) {
      this.this$0 = var1;
      this.oldFont = HeaderBarMouseAdapter.access$000(this.this$0).getFont();
   }

   public void menuShown(MenuEvent var1) {
      Menu var2 = (Menu)var1.widget;
      FontData[] var3 = this.oldFont.getFontData();

      for (int var4 = 0; var4 < var3.length; var4++) {
         var3[var4].setStyle(1);
      }

      this.boldFont = new Font(null, var3);
      HeaderBarMouseAdapter.access$000(this.this$0).setFont(this.boldFont);
      new MenuItem(var2, 2);
      this.this$0.addMenuItem(var2, "mi.copy", "copy", new HeaderBarMouseAdapter$2(this));
   }

   public void menuHidden(MenuEvent var1) {
      Menu var2 = (Menu)var1.widget;
      var2.removeMenuListener(this);
      HeaderBarMouseAdapter.access$000(this.this$0).setFont(this.oldFont);
      if (this.boldFont != null && !this.boldFont.isDisposed()) {
         this.boldFont.dispose();
      }
   }

   // $VF: synthetic method
   static HeaderBarMouseAdapter access$100(HeaderBarMouseAdapter$1 var0) {
      return var0.this$0;
   }
}
