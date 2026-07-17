package sancho.view.viewFrame;

import org.eclipse.swt.custom.CTabFolder;

public abstract class CTabFolderViewListener extends SashViewListener {
   protected CTabFolder cTabFolder;

   public CTabFolderViewListener(CTabFolderViewFrame var1) {
      super(var1);
      this.cTabFolder = var1.getCTabFolder();
   }
}
