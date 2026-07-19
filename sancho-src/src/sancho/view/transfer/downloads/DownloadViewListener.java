package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedSashViewFrame;
import sancho.view.viewFrame.TabbedSashViewListener;
import sancho.view.viewFrame.actions.ToggleTabsAction;
import sancho.view.viewer.actions.ColumnSelectorAction;
import sancho.view.viewer.actions.ExclusionStateFilterAction;
import sancho.view.viewer.actions.RemoveAllFiltersAction;

public class DownloadViewListener extends TabbedSashViewListener {
   public DownloadViewListener(TabbedSashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new ColumnSelectorAction(this.gView));
      var1.add(new Separator());
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
      MyMenuManager var2 = new MyMenuManager(SResources.getString("mi.show"));
      var2.setImageString("target");
      var2.add(new RemoveAllFiltersAction(this.gView));
      var2.add(new Separator());
      this.createEnabledNetworkFilterSubMenu(var2);
      var2.add(new Separator());
      var2.add(new ExclusionStateFilterAction(EnumFileState.PAUSED.getName(), this.gView, EnumFileState.PAUSED));
      var2.add(new ExclusionStateFilterAction(EnumFileState.QUEUED.getName(), this.gView, EnumFileState.QUEUED));
      var2.add(new Separator());
      this.createExtensionFilterMenuItems(var2);
      var1.add(var2);
      var1.add(new ToggleTabsAction((TabbedSashViewFrame)this.viewFrame));
      var1.add(new DisplayChunkGraphsAction((DownloadTreeView)this.gView));
      var2.add(new Separator());
      this.createSashActions(var1, "l.uploaders");
   }

   // Checkbox menu action that toggles the "display chunk graphs" preference and refreshes the tree.
   private static class DisplayChunkGraphsAction extends Action {
      protected DownloadTreeView gView;

      public DisplayChunkGraphsAction(DownloadTreeView var1) {
         super(SResources.getString("p.r.downloads.displayChunkGraphs"));
         if (this.isChecked()) {
            this.setImageDescriptor(SResources.getImageDescriptor("checkmark"));
         }

         this.gView = var1;
      }

      public boolean isChecked() {
         return PreferenceLoader.loadBoolean("displayChunkGraphs");
      }

      public void run() {
         boolean var1 = this.isChecked();
         PreferenceLoader.getPreferenceStore().setValue("displayChunkGraphs", !var1);
         PreferenceLoader.saveStore();
         this.gView.setChunkGraphs();
      }
   }
}
