package sancho.view.search.result;

import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.CTabFolderViewFrame;
import sancho.view.viewer.GView;
import sancho.view.viewer.table.GTableContentProvider;

public class ResultTableContentProvider extends GTableContentProvider {
   private static final String RS_RESULTS = SResources.getString("t.search.results");
   private String searchWaitingString = "";
   private ObjectMap objectMap;

   public ResultTableContentProvider(ResultTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      ObjectMap var2 = (ObjectMap)var1;
      synchronized (var2) {
         var2.clearAllLists();
         this.updateHeaderLabel();
         return var2.getKeyArray();
      }
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      this.objectMap = (ObjectMap)var3;
      if (var3 != null) {
         this.objectMap.addObserver(this);
         this.updateHeaderLabel(this.objectMap);
      }
   }

   public void dispose() {
      super.dispose();
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isVisible() && this.gView.isActive()) {
            Display var4 = this.tableViewer.getTable().getDisplay();
            var4.asyncExec(new ResultTableContentProvider$1(this, var2, var1, var3));
         } else {
            this.needsRefresh = true;
         }
      }
   }

   public void updateHeaderLabel() {
      if (this.gView != null && !this.gView.isDisposed()) {
         this.updateHeaderLabel((ObjectMap)this.gView.getViewer().getInput());
      }
   }

   private void updateHeaderLabel(ObjectMap var1) {
      if (this.gView != null && !this.gView.isDisposed() && var1 != null) {
         int var2 = var1.size();
         CTabFolderViewFrame var3 = (CTabFolderViewFrame)this.gView.getViewFrame();
         if (var3.getGView() == this.gView) {
            this.gView.getViewFrame().updateCLabelText(RS_RESULTS + ": " + var2 + this.searchWaitingString);
         }
      }
   }

   // $VF: synthetic method
   static GView access$000(ResultTableContentProvider var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(ResultTableContentProvider var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static CustomTableViewer access$200(ResultTableContentProvider var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static CustomTableViewer access$300(ResultTableContentProvider var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static CustomTableViewer access$400(ResultTableContentProvider var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static String access$502(ResultTableContentProvider var0, String var1) {
      return var0.searchWaitingString = var1;
   }
}
