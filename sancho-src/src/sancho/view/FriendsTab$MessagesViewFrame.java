package sancho.view;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

class FriendsTab$MessagesViewFrame extends SashViewFrame {
   // $VF: synthetic field
   private final FriendsTab this$0;

   public FriendsTab$MessagesViewFrame(FriendsTab var1, SashForm var2, String var3, String var4, AbstractTab var5) {
      super(var2, var3, var4, var5);
      this.this$0 = var1;
      this.createViewListener(new FriendsTab$MessagesViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("ti.f.closeAllTabs", "x", new FriendsTab$5(this));
   }

   // $VF: synthetic method
   static FriendsTab access$100(FriendsTab$MessagesViewFrame var0) {
      return var0.this$0;
   }
}
