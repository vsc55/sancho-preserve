package sancho.view.transfer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.utility.SResources;

class FileDetailDialog$8 extends SelectionAdapter {
   // $VF: synthetic field
   private final FileDetailDialog this$0;

   FileDetailDialog$8(FileDetailDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      MessageBox var2 = new MessageBox(FileDetailDialog.access$300(this.this$0).getShell(), 196);
      var2.setMessage(SResources.getString("dd.f.reallyCancel"));
      if (var2.open() == 64) {
         FileDetailDialog.access$400(this.this$0).setState(EnumFileState.CANCELLED);
         FileDetailDialog.access$300(this.this$0).setEnabled(false);
         FileDetailDialog.access$500(this.this$0).setEnabled(false);
      }
   }
}
