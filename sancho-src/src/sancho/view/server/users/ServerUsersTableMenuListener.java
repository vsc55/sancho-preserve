package sancho.view.server.users;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.viewer.table.GTableMenuListener;

public class ServerUsersTableMenuListener extends GTableMenuListener implements ISelectionChangedListener, IMenuListener {
   public ServerUsersTableMenuListener(ServerUsersTableView view) {
      super(view);
   }

   public void selectionChanged(SelectionChangedEvent event) {
   }

   public void menuAboutToShow(IMenuManager menuManager) {
   }
}
