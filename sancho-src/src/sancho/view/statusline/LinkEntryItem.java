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

   public LinkEntryItem(StatusLine statusLine, StatusConsole statusConsole) {
      this.statusLine = statusLine;
      this.statusConsole = statusConsole;
      this.linkEntryComposite = statusLine.getLinkEntryComposite();
      this.composite = statusLine.getStatusline();
      this.createContents();
   }

   public void createContents() {
      Composite composite = new Composite(this.composite, 0);
      composite.setLayoutData(new GridData(1104));
      composite.setLayout(new FillLayout());
      final ToolBar toolBar = new ToolBar(composite, 8388608);
      ToolItem httpAddItem = new ToolItem(toolBar, 8388608);
      httpAddItem.setImage(SResources.getImage("http-add"));
      httpAddItem.setToolTipText(SResources.getString("sl.httpAdd"));
      httpAddItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            InputDialog dialog = new InputDialog(toolBar.getShell(), SResources.getString("sl.http.title"), SResources.getString("sl.http.linkTo"), "", null);
            dialog.open();
            String result = dialog.getValue();
            if (result != null) {
               Sancho.send((short)29, "http " + result);
            }
         }
      });
      ToolItem ripItem = new ToolItem(toolBar, 0);
      ripItem.setImage(SResources.getImage("web-link-12"));
      ripItem.setToolTipText(SResources.getString("sl.rip"));
      ripItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
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
      final ToolItem linkToolItem = new ToolItem(toolBar, 32);
      linkToolItem.setImage(SResources.getImage("up_arrow_green"));
      linkToolItem.setToolTipText(SResources.getString("sl.linkEntry"));
      linkToolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            GridData gridData = new GridData(768);
            if (LinkEntryItem.this.linkEntryToggle) {
               linkToolItem.setSelection(false);
               gridData.heightHint = 0;
            } else {
               linkToolItem.setSelection(true);
               gridData.heightHint = 75;
            }

            LinkEntryItem.this.linkEntryToggle = !LinkEntryItem.this.linkEntryToggle;
            LinkEntryItem.this.linkEntryComposite.setLayoutData(gridData);
            LinkEntryItem.this.statusLine.getMainWindow().getMainComposite().layout();
         }
      });
      final ToolItem consoleItem = new ToolItem(toolBar, 32);
      consoleItem.setImage(SResources.getImage("console-12"));
      consoleItem.setToolTipText(SResources.getString("tab.console.tooltip"));
      if (this.statusConsole.isVisible()) {
         consoleItem.setSelection(true);
      }

      consoleItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            LinkEntryItem.this.statusConsole.toggleVisible();
            consoleItem.setSelection(LinkEntryItem.this.statusConsole.isVisible());
         }
      });
   }

   public void setupLinkRipper(LinkRipper linkRipper) {
   }
}
