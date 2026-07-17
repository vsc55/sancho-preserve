package sancho.view.statusline;

import sancho.view.StatusLine;
import sancho.view.WebBrowserTab_win32;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.LinkRipper;

public class LinkEntryItem_win32 extends LinkEntryItem {
   public LinkEntryItem_win32(StatusLine var1, StatusConsole var2) {
      super(var1, var2);
   }

   public void setupLinkRipper(LinkRipper var1) {
      AbstractTab var2 = this.statusLine.getMainWindow().getActiveTab();
      if (var2 instanceof WebBrowserTab_win32) {
         var1.setInputURL(((WebBrowserTab_win32)var2).getInputText());
         var1.setCurrentLinks(((WebBrowserTab_win32)var2).getCurrentLinks());
      }
   }
}
