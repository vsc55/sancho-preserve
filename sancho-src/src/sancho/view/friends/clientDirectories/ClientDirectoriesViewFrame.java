package sancho.view.friends.clientDirectories;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class ClientDirectoriesViewFrame extends SashViewFrame {
   public ClientDirectoriesViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new ClientDirectoriesTableView(this);
      this.createViewListener(new ClientDirectoriesViewListener(this));
   }
}
