package sancho.view.transfer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.utility.SResources;

class FileDetailDialog$9 extends SelectionAdapter {
   // $VF: synthetic field
   private final FileDetailDialog this$0;

   FileDetailDialog$9(FileDetailDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (FileDetailDialog.access$400(this.this$0).getFileStateEnum() == EnumFileState.PAUSED) {
         FileDetailDialog.access$400(this.this$0).setState(EnumFileState.DOWNLOADING);
         FileDetailDialog.access$500(this.this$0).setText(SResources.getString("dd.f.pauseFile"));
      } else if (FileDetailDialog.access$400(this.this$0).getFileStateEnum() == EnumFileState.DOWNLOADING) {
         FileDetailDialog.access$400(this.this$0).setState(EnumFileState.PAUSED);
         FileDetailDialog.access$500(this.this$0).setText(SResources.getString("dd.f.resumeFile"));
      } else if (FileDetailDialog.access$400(this.this$0).getFileStateEnum() == EnumFileState.DOWNLOADED) {
         if (FileDetailDialog.access$100(this.this$0).getText().equals("")) {
            FileDetailDialog.access$400(this.this$0).saveFileAs(FileDetailDialog.access$400(this.this$0).getName());
         } else {
            FileDetailDialog.access$400(this.this$0).saveFileAs(FileDetailDialog.access$100(this.this$0).getText());
         }

         FileDetailDialog.access$500(this.this$0).setText(SResources.getString("b.ok"));
         FileDetailDialog.access$500(this.this$0).setEnabled(false);
      }
   }
}
