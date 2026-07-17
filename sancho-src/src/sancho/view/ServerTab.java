package sancho.view;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import sancho.view.server.ServerTableView;
import sancho.view.server.ServerViewFrame;
import sancho.view.server.users.ServerUsersTableView;
import sancho.view.server.users.ServerUsersViewFrame;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.WidgetFactory;

public class ServerTab extends AbstractTab {
   public ServerTab(MainWindow var1, String var2) {
      super(var1, var2);
   }

   protected void createContents(Composite var1) {
      String var2 = "serversSash";
      SashForm var3 = WidgetFactory.createSashForm(var1, var2);
      ServerViewFrame var4 = new ServerViewFrame(var3, "tab.servers", "tab.servers.buttonSmall", this);
      ServerUsersViewFrame var5 = new ServerUsersViewFrame(var3, "l.serverUsers", "tab.servers.buttonSmall", this);
      ((ServerTableView)var4.getGView()).setServerUsersTableView((ServerUsersTableView)var5.getGView());
      this.addViewFrame(var4);
      this.addViewFrame(var5);
      WidgetFactory.loadSashForm(var3, var2);
   }
}
