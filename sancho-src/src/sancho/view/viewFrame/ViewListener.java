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

   public ViewListener(ViewFrame viewFrame) {
      this.viewFrame = viewFrame;
      this.control = viewFrame.getControl();
      this.gView = viewFrame.getGView();
   }

   protected void createNetworkWithServersFilterSubMenu(MenuManager menuManager) {
      if (Sancho.hasCollectionFactory()) {
         Network[] networks = this.viewFrame.getCore().getNetworkCollection().getNetworks();

         for (int i = 0; i < networks.length; i++) {
            Network network = networks[i];
            if (network.isEnabled() && (network.hasServers() || network.hasSupernodes())) {
               menuManager.add(new NetworkFilterAction(this.viewFrame.getGView(), network));
            }
         }
      }
   }

   protected void createEnabledNetworkFilterSubMenu(MenuManager menuManager) {
      if (Sancho.hasCollectionFactory()) {
         Network[] networks = this.viewFrame.getCore().getNetworkCollection().getNetworks();

         for (int i = 0; i < networks.length; i++) {
            Network network = networks[i];
            if (network.isEnabled() && !network.isVirtual()) {
               menuManager.add(new NetworkFilterAction(this.viewFrame.getGView(), network));
            }
         }
      }
   }

   protected void createStateFilterMenuItems(MenuManager menuManager) {
      if (this.gView.getValidStates() != null) {
         for (int i = 0; i < this.gView.getValidStates().length; i++) {
            menuManager.add(new StateFilterAction(this.gView.getValidStates()[i].getName(), this.gView, this.gView.getValidStates()[i]));
         }
      }
   }

   protected void createExtensionFilterMenuItems(MenuManager menuManager) {
      if (this.gView.getValidExtensions() != null) {
         for (int i = 0; i < this.gView.getValidExtensions().length; i++) {
            menuManager.add(new ExtensionFilterAction(this.gView.getValidExtensions()[i].getName(), this.gView, this.gView.getValidExtensions()[i]));
         }
      }
   }

   protected void createSortByColumnSubMenu(IMenuManager menuManager) {
      if (this.viewFrame.getGView() != null) {
         MyMenuManager subMenu = new MyMenuManager(SResources.getString("mi.sort"));
         subMenu.setImageString("sort");

         for (int i = 0; i < this.viewFrame.getGView().getColumnCount(); i++) {
            subMenu.add(new SortByColumnAction(this.viewFrame.getGView(), i));
         }

         menuManager.add(subMenu);
      }
   }

   protected void createDynamicColumnSubMenu(IMenuManager menuManager) {
      if (this.viewFrame.getGView() != null && !SWT.getPlatform().equals("gtk")) {
         MyMenuManager subMenu = new MyMenuManager(SResources.getString("mi.dynamicColumn"));
         subMenu.setImageString("dynamic");

         for (int i = 0; i < this.viewFrame.getGView().getColumnCount(); i++) {
            subMenu.add(new SetDynamicColumnAction(this.viewFrame.getGView(), i));
         }

         subMenu.add(new Separator());
         subMenu.add(new SetMinDynamicColumnWidthAction(this.viewFrame.getGView()));
         menuManager.add(subMenu);
      }
   }
}
