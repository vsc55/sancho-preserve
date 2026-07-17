package sancho.view.statusline;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.StatusLine;
import sancho.view.utility.LinkRipper;
import sancho.view.utility.SResources;

public class LinkEntryItem {
   protected Composite linkEntryComposite;
   protected StatusLine statusLine;
   protected Composite composite;
   protected boolean linkEntryToggle = false;
   protected StatusConsole statusConsole;

   public LinkEntryItem(StatusLine var1, StatusConsole var2) {
      this.statusLine = var1;
      this.statusConsole = var2;
      this.linkEntryComposite = var1.getLinkEntryComposite();
      this.composite = var1.getStatusline();
      this.createContents();
   }

   public void createContents() {
      Composite var1 = new Composite(this.composite, 0);
      var1.setLayoutData(new GridData(1104));
      var1.setLayout(new FillLayout());
      ToolBar var2 = new ToolBar(var1, 8388608);
      ToolItem var3 = new ToolItem(var2, 8388608);
      var3.setImage(SResources.getImage("http-add"));
      var3.setToolTipText(SResources.getString("sl.httpAdd"));
      var3.addSelectionListener(new LinkEntryItem$1(this, var2));
      ToolItem var4 = new ToolItem(var2, 0);
      var4.setImage(SResources.getImage("web-link-12"));
      var4.setToolTipText(SResources.getString("sl.rip"));
      var4.addSelectionListener(new LinkEntryItem$2(this));
      ToolItem var5 = new ToolItem(var2, 32);
      var5.setImage(SResources.getImage("up_arrow_green"));
      var5.setToolTipText(SResources.getString("sl.linkEntry"));
      var5.addSelectionListener(new LinkEntryItem$3(this, var5));
      ToolItem var6 = new ToolItem(var2, 32);
      var6.setImage(SResources.getImage("console-12"));
      var6.setToolTipText(SResources.getString("tab.console.tooltip"));
      if (this.statusConsole.isVisible()) {
         var6.setSelection(true);
      }

      var6.addSelectionListener(new LinkEntryItem$4(this, var6));
   }

   public void setupLinkRipper(LinkRipper var1) {
   }
}
