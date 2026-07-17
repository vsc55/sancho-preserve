package sancho.view.search.result;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.model.mldonkey.Result;
import sancho.utility.ObjectMap;
import sancho.view.SearchTab;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
import sancho.view.viewer.table.GTableMenuListener;
import sancho.view.viewer.table.GTableView;

public class ResultTableMenuListener extends GTableMenuListener {
   private CTabItem cTabItem;
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Result;

   public ResultTableMenuListener(GTableView var1, CTabItem var2) {
      super(var1);
      this.cTabItem = var2;
   }

   public void initialize() {
      super.initialize();
      if (PreferenceLoader.loadBoolean("searchTooltips")) {
         ResultTableMenuListener$ToolTipHandler var1 = new ResultTableMenuListener$ToolTipHandler(this, this.gView.getShell());
         var1.activateHoverHelp(this.gView.getComposite(), (ICustomViewer)this.gView.getViewer());
         this.gView.getComposite().addDisposeListener(new ResultTableMenuListener$1(this, var1));
      }
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$Result == null
            ? (class$sancho$model$mldonkey$Result = class$("sancho.model.mldonkey.Result"))
            : class$sancho$model$mldonkey$Result
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new ResultTableMenuListener$DownloadAction(this));
         var1.add(new ResultTableMenuListener$ResultDetailAction(this));
         var1.add(new ResultTableMenuListener$RemoveResultsAction(this));
         var1.add(new Separator());
         var1.add(new ResultTableMenuListener$CopyNameAction(this));
         this.addClipboardMenu(var1);
         var1.add(new Separator());
         Result var2 = (Result)this.selectedObjects.get(0);
         this.addWebServicesMenu(var1, var2.getMD4(), var2.getED2K(), var2.getSize());
         this.addSelectAllMenu(var1);
      }
   }

   public void removeSelected() {
      if (this.selectedObjects.size() != 0) {
         ObjectMap var1 = (ObjectMap)this.gView.getViewer().getInput();
         if (var1 != null) {
            var1.remove(this.selectedObjects.toArray());
         }
      }
   }

   protected void onDeleteKey() {
      this.removeSelected();
   }

   public boolean downloadResult(Result var1) {
      if (!Sancho.hasCollectionFactory()) {
         return true;
      } else {
         boolean var2 = PreferenceLoader.loadBoolean("searchForceDownload");
         if (var1.downloaded() && !var2) {
            Shell var3 = this.tableViewer.getTable().getShell();
            MessageBox var4 = new MessageBox(var3, 194);
            var4.setText(SResources.getString("s.alreadyDownloadedTitle"));
            var4.setMessage(var1.getName() + "\n" + SResources.getString("s.alreadyDownloadedText"));
            if (var4.open() == 64) {
               this.gView.getCore().getResultCollection().download(var1, true);
               this.updateItem(5000, var1);
               return true;
            } else {
               return false;
            }
         } else {
            this.gView.getCore().getResultCollection().download(var1, var2);
            this.updateItem(5000, var1);
            return true;
         }
      }
   }

   protected void updateItem(int var1, Result var2) {
      this.tableViewer.getTable().getDisplay().timerExec(var1, new ResultTableMenuListener$2(this, var2));
   }

   public void downloadSingleFile(Result var1) {
      this.downloadResult(var1);
      this.postDownloadStats(1, "");
   }

   public void downloadSelected() {
      if (Sancho.hasCollectionFactory()) {
         String var1 = "";
         int var2 = 0;

         for (int var3 = 0; var3 < this.selectedObjects.size(); var3++) {
            Result var4 = (Result)this.selectedObjects.get(var3);
            if (this.downloadResult(var4)) {
               var2++;
            } else {
               var1 = var1 + var4.getName() + "\n";
            }
         }

         this.postDownloadStats(var2, var1);
      }
   }

   public void postDownloadStats(int var1, String var2) {
      SearchTab var3 = (SearchTab)this.cTabItem.getParent().getData();
      var3.getMainWindow().getStatusline().setText(SResources.getString("s.sl.startedDownload") + var1);
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // $VF: synthetic method
   static CustomTableViewer access$000(ResultTableMenuListener var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static CustomTableViewer access$100(ResultTableMenuListener var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static CustomTableViewer access$200(ResultTableMenuListener var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static CustomTableViewer access$300(ResultTableMenuListener var0) {
      return var0.tableViewer;
   }

   // $VF: synthetic method
   static List access$400(ResultTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$500(ResultTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$600(ResultTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$700(ResultTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$2900(ResultTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$3200(ResultTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$3300(ResultTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$3400(ResultTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$3500(ResultTableMenuListener var0) {
      return var0.gView;
   }
}
