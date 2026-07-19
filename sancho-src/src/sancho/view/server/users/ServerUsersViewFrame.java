package sancho.view.server.users;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class ServerUsersViewFrame extends SashViewFrame {
   public ServerUsersViewFrame(SashForm sashForm, String name, String text, AbstractTab tab) {
      super(sashForm, name, text, tab);
      this.gView = new ServerUsersTableView(this);
      this.createViewListener(new ServerUsersViewListener(this));
   }
}
