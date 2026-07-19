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
   public DownloadTreeContentProvider(DownloadTreeView downloadTreeView) {
      super(downloadTreeView);
   }

   public Object[] getChildren(Object element) {
      return element instanceof File ? ((File)element).getFileClientSetArray() : GTreeContentProvider.EMPTY_ARRAY;
   }

   public Object[] getElements(Object inputElement) {
      return inputElement instanceof FileCollection ? ((FileCollection)inputElement).getAllInteresting() : GTreeContentProvider.EMPTY_ARRAY;
   }

   public Object getParent(Object element) {
      if (element instanceof FileClient) {
         return ((FileClient)element).getFile();
      } else {
         return element instanceof File ? this.treeViewer.getInput() : null;
      }
   }

   public boolean hasChildren(Object element) {
      return element instanceof File ? ((File)element).getFileClientSetSize() > 0 : false;
   }

   public int getNumChildren(Object element) {
      return element instanceof File ? ((File)element).getFileClientSetSize() : 0;
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      if (newInput != null) {
         ((MyObservable)newInput).addObserver(this);
         this.updateHeaderLabel();
      }
   }

   public void onUpdate(MyObservable observable, Object data, int updateType) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (observable instanceof FileCollection) {
            FileCollection fileCollection = (FileCollection)observable;
            if (data instanceof FileClient) {
               FileClient fileClient = (FileClient)data;
               switch (updateType) {
                  case 1:
                     this.treeViewer.addChild(fileClient.getFile(), fileClient);
                     break;
                  case 2:
                     this.treeViewer.removeChild(fileClient.getFile(), fileClient);
               }
            }

            if (fileCollection.requiresRefresh()) {
               this.treeViewer.refresh();
               this.updateHeaderLabel();
               return;
            }

            if (fileCollection.added()) {
               synchronized (fileCollection) {
                  Object[] addedFiles = fileCollection.getAndClearAdded();
                  this.treeViewer.add(this.treeViewer.getInput(), addedFiles);

                  for (int i = 0; i < addedFiles.length; i++) {
                     ((File)addedFiles[i]).clearChangedBits();
                  }
               }
            }

            if (fileCollection.updated()) {
               synchronized (fileCollection) {
                  Object[] updatedFiles = fileCollection.getAndClearUpdated();
                  this.treeViewer.update(updatedFiles, SResources.SA_Z);

                  for (int i = 0; i < updatedFiles.length; i++) {
                     ((File)updatedFiles[i]).clearChangedBits();
                  }
               }
            }

            if (fileCollection.removed()) {
               synchronized (fileCollection) {
                  Object[] removedFiles = fileCollection.getAndClearRemoved();
                  this.treeViewer.remove(removedFiles);

                  for (int i = 0; i < removedFiles.length; i++) {
                     ((File)removedFiles[i]).clearChangedBits();
                  }
               }
            }

            this.updateHeaderLabel();
         }
      }
   }

   public void updateHeaderLabel() {
      if (Sancho.hasCollectionFactory()) {
         String headerText = this.gView.getCore().getFileCollection().getHeaderText();
         this.gView.getViewFrame().updateCLabelText(headerText);
      }
   }
}
