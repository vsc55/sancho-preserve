package sancho.view.statusline;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.Sancho;
import sancho.view.StatusLine;
import sancho.view.utility.SResources;

public class CoreConsoleItem {
   private Composite composite;

   public CoreConsoleItem(StatusLine statusLine) {
      this.composite = statusLine.getStatusline();
      this.createContents();
   }

   public void createContents() {
      Composite composite = new Composite(this.composite, 0);
      composite.setLayoutData(new GridData(1104));
      composite.setLayout(new FillLayout());
      ToolBar toolBar = new ToolBar(composite, 8388608);
      ToolItem toolItem = new ToolItem(toolBar, 0);
      toolItem.setImage(SResources.getImage("ProgramIcon-12"));
      toolItem.setToolTipText(SResources.getString("sl.coreConsole"));
      toolItem.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            Sancho.getCoreConsole().getShell().open();
         }
      });
   }
}
