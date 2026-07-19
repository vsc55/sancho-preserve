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
   public DownloadViewListener(TabbedSashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new ColumnSelectorAction(this.gView));
      menuManager.add(new Separator());
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
      MyMenuManager showMenu = new MyMenuManager(SResources.getString("mi.show"));
      showMenu.setImageString("target");
      showMenu.add(new RemoveAllFiltersAction(this.gView));
      showMenu.add(new Separator());
      this.createEnabledNetworkFilterSubMenu(showMenu);
      showMenu.add(new Separator());
      showMenu.add(new ExclusionStateFilterAction(EnumFileState.PAUSED.getName(), this.gView, EnumFileState.PAUSED));
      showMenu.add(new ExclusionStateFilterAction(EnumFileState.QUEUED.getName(), this.gView, EnumFileState.QUEUED));
      showMenu.add(new Separator());
      this.createExtensionFilterMenuItems(showMenu);
      menuManager.add(showMenu);
      menuManager.add(new ToggleTabsAction((TabbedSashViewFrame)this.viewFrame));
      menuManager.add(new DisplayChunkGraphsAction((DownloadTreeView)this.gView));
      showMenu.add(new Separator());
      this.createSashActions(menuManager, "l.uploaders");
   }

   // Checkbox menu action that toggles the "display chunk graphs" preference and refreshes the tree.
   private static class DisplayChunkGraphsAction extends Action {
      protected DownloadTreeView gView;

      public DisplayChunkGraphsAction(DownloadTreeView view) {
         super(SResources.getString("p.r.downloads.displayChunkGraphs"));
         if (this.isChecked()) {
            this.setImageDescriptor(SResources.getImageDescriptor("checkmark"));
         }

         this.gView = view;
      }

      public boolean isChecked() {
         return PreferenceLoader.loadBoolean("displayChunkGraphs");
      }

      public void run() {
         boolean checked = this.isChecked();
         PreferenceLoader.getPreferenceStore().setValue("displayChunkGraphs", !checked);
         PreferenceLoader.saveStore();
         this.gView.setChunkGraphs();
      }
   }
}
