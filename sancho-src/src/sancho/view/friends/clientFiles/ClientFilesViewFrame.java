package sancho.view.friends.clientFiles;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class ClientFilesViewFrame extends SashViewFrame {
   public ClientFilesViewFrame(SashForm sashForm, String name, String text, AbstractTab tab) {
      super(sashForm, name, text, tab);
      this.gView = new ClientFilesTableView(this);
      this.createViewListener(new ClientFilesViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
