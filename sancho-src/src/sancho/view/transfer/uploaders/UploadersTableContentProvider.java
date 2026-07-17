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

   public UploadersTableContentProvider(UploadersTableView var1) {
      super(var1);
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      if (var3 != null) {
         ObjectMap var4 = (ObjectMap)var3;
         var4.addObserver(this);
         if (!PreferenceLoader.loadBoolean("pollUploaders")) {
            this.updateHeaderLabel();
         } else {
            this.updateHeaderLabel(var4.size());
         }
      } else {
         this.updateHeaderLabel();
      }
   }

   protected void updateHeaderLabel(int var1) {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_UPLOADERS + ": " + var1);
      }
   }

   protected void updateHeaderLabel() {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.gView.getViewFrame().updateCLabelText(RS_UPLOADERS + ": " + RS_DISABLED);
      }
   }
}
