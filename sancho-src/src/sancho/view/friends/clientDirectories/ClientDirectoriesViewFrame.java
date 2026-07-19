package sancho.view.friends.clientDirectories;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class ClientDirectoriesViewFrame extends SashViewFrame {
   public ClientDirectoriesViewFrame(SashForm sashForm, String name, String text, AbstractTab tab) {
      super(sashForm, name, text, tab);
      this.gView = new ClientDirectoriesTableView(this);
      this.createViewListener(new ClientDirectoriesViewListener(this));
   }
}
