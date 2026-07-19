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

   public CoreConsoleItem(StatusLine var1) {
      this.composite = var1.getStatusline();
      this.createContents();
   }

   public void createContents() {
      Composite var1 = new Composite(this.composite, 0);
      var1.setLayoutData(new GridData(1104));
      var1.setLayout(new FillLayout());
      ToolBar var2 = new ToolBar(var1, 8388608);
      ToolItem var3 = new ToolItem(var2, 0);
      var3.setImage(SResources.getImage("ProgramIcon-12"));
      var3.setToolTipText(SResources.getString("sl.coreConsole"));
      var3.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var4) {
            Sancho.getCoreConsole().getShell().open();
         }
      });
   }
}
