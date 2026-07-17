package sancho.view.transfer.downloads;

import org.eclipse.jface.viewers.Viewer;
import sancho.core.Sancho;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.FileCollection;
import sancho.utility.MyObservable;
import sancho.view.transfer.FileClient;
import sancho.view.utility.SResources;
import sancho.view.viewer.tree.GTreeContentProvider;

public class DownloadTreeContentProvider extends GTreeContentProvider {
   public DownloadTreeContentProvider(DownloadTreeView var1) {
      super(var1);
   }

   public Object[] getChildren(Object var1) {
      return var1 instanceof File ? ((File)var1).getFileClientSetArray() : GTreeContentProvider.EMPTY_ARRAY;
   }

   public Object[] getElements(Object var1) {
      return var1 instanceof FileCollection ? ((FileCollection)var1).getAllInteresting() : GTreeContentProvider.EMPTY_ARRAY;
   }

   public Object getParent(Object var1) {
      if (var1 instanceof FileClient) {
         return ((FileClient)var1).getFile();
      } else {
         return var1 instanceof File ? this.treeViewer.getInput() : null;
      }
   }

   public boolean hasChildren(Object var1) {
      return var1 instanceof File ? ((File)var1).getFileClientSetSize() > 0 : false;
   }

   public int getNumChildren(Object var1) {
      return var1 instanceof File ? ((File)var1).getFileClientSetSize() : 0;
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      if (var3 != null) {
         ((MyObservable)var3).addObserver(this);
         this.updateHeaderLabel();
      }
   }

   public void onUpdate(MyObservable var1, Object var2, int var3) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (var1 instanceof FileCollection) {
            FileCollection var4 = (FileCollection)var1;
            if (var2 instanceof FileClient) {
               FileClient var5 = (FileClient)var2;
               switch (var3) {
                  case 1:
                     this.treeViewer.addChild(var5.getFile(), var5);
                     break;
                  case 2:
                     this.treeViewer.removeChild(var5.getFile(), var5);
               }
            }

            if (var4.requiresRefresh()) {
               this.treeViewer.refresh();
               this.updateHeaderLabel();
               return;
            }

            if (var4.added()) {
               synchronized (var4) {
                  Object[] var6 = var4.getAndClearAdded();
                  this.treeViewer.add(this.treeViewer.getInput(), var6);

                  for (int var7 = 0; var7 < var6.length; var7++) {
                     ((File)var6[var7]).clearChangedBits();
                  }
               }
            }

            if (var4.updated()) {
               synchronized (var4) {
                  Object[] var14 = var4.getAndClearUpdated();
                  this.treeViewer.update(var14, SResources.SA_Z);

                  for (int var16 = 0; var16 < var14.length; var16++) {
                     ((File)var14[var16]).clearChangedBits();
                  }
               }
            }

            if (var4.removed()) {
               synchronized (var4) {
                  Object[] var15 = var4.getAndClearRemoved();
                  this.treeViewer.remove(var15);

                  for (int var17 = 0; var17 < var15.length; var17++) {
                     ((File)var15[var17]).clearChangedBits();
                  }
               }
            }

            this.updateHeaderLabel();
         }
      }
   }

   public void updateHeaderLabel() {
      if (Sancho.hasCollectionFactory()) {
         String var1 = this.gView.getCore().getFileCollection().getHeaderText();
         this.gView.getViewFrame().updateCLabelText(var1);
      }
   }
}
