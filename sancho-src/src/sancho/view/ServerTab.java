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
   public ServerTab(MainWindow mainWindow, String name) {
      super(mainWindow, name);
   }

   protected void createContents(Composite composite) {
      String sashName = "serversSash";
      SashForm sashForm = WidgetFactory.createSashForm(composite, sashName);
      ServerViewFrame serverViewFrame = new ServerViewFrame(sashForm, "tab.servers", "tab.servers.buttonSmall", this);
      ServerUsersViewFrame serverUsersViewFrame = new ServerUsersViewFrame(sashForm, "l.serverUsers", "tab.servers.buttonSmall", this);
      ((ServerTableView)serverViewFrame.getGView()).setServerUsersTableView((ServerUsersTableView)serverUsersViewFrame.getGView());
      this.addViewFrame(serverViewFrame);
      this.addViewFrame(serverUsersViewFrame);
      WidgetFactory.loadSashForm(sashForm, sashName);
   }
}
