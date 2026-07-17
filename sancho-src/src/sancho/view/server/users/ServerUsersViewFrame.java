package sancho.view.server.users;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class ServerUsersViewFrame extends SashViewFrame {
   public ServerUsersViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new ServerUsersTableView(this);
      this.createViewListener(new ServerUsersViewListener(this));
   }
}
