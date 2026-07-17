package sancho.view;

import org.eclipse.swt.widgets.Composite;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

class WebBrowserTab$WebBrowserViewFrame extends ViewFrame {
   private int numToolItems;
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   public WebBrowserTab$WebBrowserViewFrame(WebBrowserTab var1, Composite var2, String var3, String var4, AbstractTab var5) {
      super(var2, var3, var4, var5);
      this.this$0 = var1;
      this.createViewToolBar();
      this.createViewListener(new WebBrowserTab$WebBrowserViewListener(var1, this));
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.numToolItems = PreferenceLoader.loadInt("webBrowserToolItems");

      for (int var1 = 1; var1 < this.numToolItems + 1; var1++) {
         this.addToolItem(PreferenceLoader.loadString("webBrowserToolItem" + var1), String.valueOf(var1), new WebBrowserTab$10(this, var1));
      }

      this.addToolItem("ti.web.sancho", "ProgramIcon", new WebBrowserTab$11(this));
      this.addToolSeparator();
      this.addToolItem("ti.web.stop", "page-stop", new WebBrowserTab$12(this));
      this.addToolItem("ti.web.refresh", "page-refresh", new WebBrowserTab$13(this));
      this.addToolSeparator();
      this.addToolItem("ti.web.back", "page-back", new WebBrowserTab$14(this));
      this.addToolItem("ti.web.forward", "page-forward", new WebBrowserTab$15(this));
   }

   public void updateDisplay() {
      super.updateDisplay();
      if (this.numToolItems != PreferenceLoader.loadInt("webBrowserToolItems") && this.toolBar != null) {
         for (int var2 = this.toolBar.getItemCount() - 1; var2 >= 0; var2--) {
            this.toolBar.getItems()[var2].dispose();
         }

         this.toolBar.dispose();
         this.createViewToolBar();
         this.toolBar.getParent().layout();
      } else if (this.toolBar != null) {
         for (int var1 = 1; var1 <= this.numToolItems; var1++) {
            this.toolBar.getItems()[var1 - 1].setToolTipText(PreferenceLoader.loadString("webBrowserToolItem" + var1));
         }
      }
   }

   // $VF: synthetic method
   static WebBrowserTab access$000(WebBrowserTab$WebBrowserViewFrame var0) {
      return var0.this$0;
   }
}
