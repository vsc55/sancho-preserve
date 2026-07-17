package sancho.view.friends.clientFiles;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class ClientFilesViewFrame extends SashViewFrame {
   public ClientFilesViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new ClientFilesTableView(this);
      this.createViewListener(new ClientFilesViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
