package sancho.view;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewFrame.ViewListener;

public class WebBrowserTab$WebBrowserViewListener extends ViewListener {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   public WebBrowserTab$WebBrowserViewListener(WebBrowserTab var1, ViewFrame var2) {
      super(var2);
      this.this$0 = var1;
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.this$0.createFavoritesMenu(var1);
   }
}
