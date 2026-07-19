package sancho.view.viewFrame;

import org.eclipse.swt.custom.CTabFolder;

public abstract class CTabFolderViewListener extends SashViewListener {
   protected CTabFolder cTabFolder;

   public CTabFolderViewListener(CTabFolderViewFrame viewFrame) {
      super(viewFrame);
      this.cTabFolder = viewFrame.getCTabFolder();
   }
}
