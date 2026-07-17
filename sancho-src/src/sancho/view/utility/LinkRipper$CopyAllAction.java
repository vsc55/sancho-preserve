package sancho.view.utility;

import org.eclipse.jface.action.Action;

class LinkRipper$CopyAllAction extends Action {
   // $VF: synthetic field
   private final LinkRipper this$0;

   public LinkRipper$CopyAllAction(LinkRipper var1) {
      super(SResources.getString("mi.copyAll"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("plus"));
   }

   public void run() {
      String var1 = "";

      for (int var2 = 0; var2 < this.this$0.urlList.getItems().length; var2++) {
         var1 = var1 + this.this$0.urlList.getItems()[var2] + "\n";
      }

      if (!var1.equals("")) {
         this.this$0.addToClipBoard(var1);
      }
   }
}
