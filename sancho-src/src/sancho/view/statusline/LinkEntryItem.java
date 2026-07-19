package sancho.view.statusline;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.Sancho;
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
      final ToolBar var2 = new ToolBar(var1, 8388608);
      ToolItem var3 = new ToolItem(var2, 8388608);
      var3.setImage(SResources.getImage("http-add"));
      var3.setToolTipText(SResources.getString("sl.httpAdd"));
      var3.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            InputDialog dialog = new InputDialog(var2.getShell(), SResources.getString("sl.http.title"), SResources.getString("sl.http.linkTo"), "", null);
            dialog.open();
            String result = dialog.getValue();
            if (result != null) {
               Sancho.send((short)29, "http " + result);
            }
         }
      });
      ToolItem var4 = new ToolItem(var2, 0);
      var4.setImage(SResources.getImage("web-link-12"));
      var4.setToolTipText(SResources.getString("sl.rip"));
      var4.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            LinkRipper linkRipper = LinkEntryItem.this.statusLine.getMainWindow().getLinkRipper();
            if (linkRipper != null) {
               linkRipper.setFocus();
            } else {
               linkRipper = LinkEntryItem.this.statusLine.getMainWindow().openLinkRipper();
               LinkEntryItem.this.setupLinkRipper(linkRipper);
               linkRipper.open();
            }
         }
      });
      final ToolItem var5 = new ToolItem(var2, 32);
      var5.setImage(SResources.getImage("up_arrow_green"));
      var5.setToolTipText(SResources.getString("sl.linkEntry"));
      var5.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            GridData gridData = new GridData(768);
            if (LinkEntryItem.this.linkEntryToggle) {
               var5.setSelection(false);
               gridData.heightHint = 0;
            } else {
               var5.setSelection(true);
               gridData.heightHint = 75;
            }

            LinkEntryItem.this.linkEntryToggle = !LinkEntryItem.this.linkEntryToggle;
            LinkEntryItem.this.linkEntryComposite.setLayoutData(gridData);
            LinkEntryItem.this.statusLine.getMainWindow().getMainComposite().layout();
         }
      });
      final ToolItem var6 = new ToolItem(var2, 32);
      var6.setImage(SResources.getImage("console-12"));
      var6.setToolTipText(SResources.getString("tab.console.tooltip"));
      if (this.statusConsole.isVisible()) {
         var6.setSelection(true);
      }

      var6.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            LinkEntryItem.this.statusConsole.toggleVisible();
            var6.setSelection(LinkEntryItem.this.statusConsole.isVisible());
         }
      });
   }

   public void setupLinkRipper(LinkRipper var1) {
   }
}
