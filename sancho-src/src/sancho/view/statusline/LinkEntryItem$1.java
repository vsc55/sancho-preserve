package sancho.view.statusline;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolBar;
import sancho.core.Sancho;
import sancho.view.utility.SResources;

class LinkEntryItem$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final ToolBar val$toolBar;
   // $VF: synthetic field
   private final LinkEntryItem this$0;

   LinkEntryItem$1(LinkEntryItem var1, ToolBar var2) {
      this.this$0 = var1;
      this.val$toolBar = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      InputDialog var2 = new InputDialog(this.val$toolBar.getShell(), SResources.getString("sl.http.title"), SResources.getString("sl.http.linkTo"), "", null);
      var2.open();
      String var3 = var2.getValue();
      if (var3 != null) {
         Sancho.send((short)29, "http " + var3);
      }
   }
}
