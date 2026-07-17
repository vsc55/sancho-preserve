package sancho.view.viewFrame;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewer.GView;
import sancho.view.viewer.actions.ExtensionFilterAction;
import sancho.view.viewer.actions.NetworkFilterAction;
import sancho.view.viewer.actions.SetDynamicColumnAction;
import sancho.view.viewer.actions.SetMinDynamicColumnWidthAction;
import sancho.view.viewer.actions.SortByColumnAction;
import sancho.view.viewer.actions.StateFilterAction;

public abstract class ViewListener implements IMenuListener {
   protected ViewFrame viewFrame;
   protected Control control;
   protected GView gView;

   public ViewListener(ViewFrame var1) {
      this.viewFrame = var1;
      this.control = var1.getControl();
      this.gView = var1.getGView();
   }

   protected void createNetworkWithServersFilterSubMenu(MenuManager var1) {
      if (Sancho.hasCollectionFactory()) {
         Network[] var2 = this.viewFrame.getCore().getNetworkCollection().getNetworks();

         for (int var3 = 0; var3 < var2.length; var3++) {
            Network var4 = var2[var3];
            if (var4.isEnabled() && (var4.hasServers() || var4.hasSupernodes())) {
               var1.add(new NetworkFilterAction(this.viewFrame.getGView(), var4));
            }
         }
      }
   }

   protected void createEnabledNetworkFilterSubMenu(MenuManager var1) {
      if (Sancho.hasCollectionFactory()) {
         Network[] var2 = this.viewFrame.getCore().getNetworkCollection().getNetworks();

         for (int var3 = 0; var3 < var2.length; var3++) {
            Network var4 = var2[var3];
            if (var4.isEnabled() && !var4.isVirtual()) {
               var1.add(new NetworkFilterAction(this.viewFrame.getGView(), var4));
            }
         }
      }
   }

   protected void createStateFilterMenuItems(MenuManager var1) {
      if (this.gView.getValidStates() != null) {
         for (int var2 = 0; var2 < this.gView.getValidStates().length; var2++) {
            var1.add(new StateFilterAction(this.gView.getValidStates()[var2].getName(), this.gView, this.gView.getValidStates()[var2]));
         }
      }
   }

   protected void createExtensionFilterMenuItems(MenuManager var1) {
      if (this.gView.getValidExtensions() != null) {
         for (int var2 = 0; var2 < this.gView.getValidExtensions().length; var2++) {
            var1.add(new ExtensionFilterAction(this.gView.getValidExtensions()[var2].getName(), this.gView, this.gView.getValidExtensions()[var2]));
         }
      }
   }

   protected void createSortByColumnSubMenu(IMenuManager var1) {
      if (this.viewFrame.getGView() != null) {
         MyMenuManager var2 = new MyMenuManager(SResources.getString("mi.sort"));
         var2.setImageString("sort");

         for (int var3 = 0; var3 < this.viewFrame.getGView().getColumnCount(); var3++) {
            var2.add(new SortByColumnAction(this.viewFrame.getGView(), var3));
         }

         var1.add(var2);
      }
   }

   protected void createDynamicColumnSubMenu(IMenuManager var1) {
      if (this.viewFrame.getGView() != null && !SWT.getPlatform().equals("gtk")) {
         MyMenuManager var2 = new MyMenuManager(SResources.getString("mi.dynamicColumn"));
         var2.setImageString("dynamic");

         for (int var3 = 0; var3 < this.viewFrame.getGView().getColumnCount(); var3++) {
            var2.add(new SetDynamicColumnAction(this.viewFrame.getGView(), var3));
         }

         var2.add(new Separator());
         var2.add(new SetMinDynamicColumnWidthAction(this.viewFrame.getGView()));
         var1.add(var2);
      }
   }
}
