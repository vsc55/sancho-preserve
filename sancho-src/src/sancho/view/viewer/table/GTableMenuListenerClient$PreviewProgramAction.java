package sancho.view.viewer.table;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.program.Program;
import sancho.model.mldonkey.IPreview;

class GTableMenuListenerClient$PreviewProgramAction extends Action {
   IPreview iPreview;
   Program program;
   int subFileNum;
   // $VF: synthetic field
   private final GTableMenuListenerClient this$0;

   public GTableMenuListenerClient$PreviewProgramAction(GTableMenuListenerClient var1, IPreview var2, Program var3, int var4) {
      super(var3.getName());
      this.this$0 = var1;
      this.setImageDescriptor(var2.getFileTypeImageDescriptor());
      this.iPreview = var2;
      this.program = var3;
      this.subFileNum = var4;
   }

   public void run() {
      this.this$0.sendToStatusline(this.iPreview.preview(this.program, this.subFileNum));
   }
}
