package sancho.view.transfer.uploaders;

import org.eclipse.jface.viewers.Viewer;
import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProviderOM;

public class UploadersTableContentProvider extends GTableContentProviderOM {
   public static final String RS_UPLOADERS = SResources.getString("l.uploaders");
   public static final String RS_DISABLED = SResources.getString("l.disabled");

   public UploadersTableContentProvider(UploadersTableView view) {
      super(view);
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      if (newInput != null) {
         ObjectMap objectMap = (ObjectMap)newInput;
         objectMap.addObserver(this);
         if (!PreferenceLoader.loadBoolean("pollUploaders")) {
            this.updateHeaderLabel();
         } else {
            this.updateHeaderLabel(objectMap.size());
         }
      } else {
         this.updateHeaderLabel();
      }
   }

   protected void updateHeaderLabel(int count) {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_UPLOADERS + ": " + count);
      }
   }

   protected void updateHeaderLabel() {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_UPLOADERS + ": " + RS_DISABLED);
      }
   }
}
