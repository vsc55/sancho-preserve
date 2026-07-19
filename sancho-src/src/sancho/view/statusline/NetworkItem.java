package sancho.view.statusline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.utility.MyObservable;
import sancho.view.StatusLine;
import sancho.view.statusline.actions.NetworkConnectMoreAction;
import sancho.view.statusline.actions.NetworkDisableAction;
import sancho.view.statusline.actions.NetworkEnableAction;
import sancho.view.utility.WidgetFactory;

public class NetworkItem implements IStatusItem {
   private Composite composite = null;
   private Composite parentComposite;
   private ToolBar toolBar;
   private List popupMenuList = new ArrayList();

   public NetworkItem(StatusLine statusLine) {
      this.parentComposite = new Composite(statusLine.getStatusline(), 0);
      this.parentComposite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      this.parentComposite.setLayoutData(new GridData(1040));
      this.setConnected(Sancho.getCoreFactory().isConnected());
   }

   private void createContent() {
      if (this.composite != null) {
         this.composite.dispose();
      }

      this.composite = new Composite(this.parentComposite, 0);
      this.composite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
      this.composite.setLayoutData(new GridData(1040));
      this.toolBar = new ToolBar(this.composite, 8388608);
      if (Sancho.hasCollectionFactory()) {
         Network[] networks = Sancho.getCore().getNetworkCollection().getNetworks();
         Arrays.sort(networks, new NetworkComparator());

         for (int i = 0; i < networks.length; i++) {
            final Network network = networks[i];
            if (!network.isVirtual()) {
               ToolItem toolItem = new ToolItem(this.toolBar, 0);
               toolItem.setData(network);
               this.deletePopupMenus();
               final MenuManager menuManager = new MenuManager();
               this.popupMenuList.add(menuManager);
               menuManager.setRemoveAllWhenShown(true);
               menuManager.addMenuListener(new NetworkMenuListener(network.getEnumNetwork()));
               toolItem.addSelectionListener(new SelectionAdapter() {
                  public void widgetSelected(SelectionEvent event) {
                     Rectangle rect = ((ToolItem)event.widget).getBounds();
                     Menu menu = menuManager.createContextMenu(NetworkItem.this.toolBar);
                     Point point = new Point(rect.x, rect.y + rect.height);
                     point = NetworkItem.this.toolBar.toDisplay(point);
                     menu.setLocation(point.x, point.y);
                     menu.setVisible(true);
                  }
               });
               toolItem.addDisposeListener(new DisposeListener() {
                  public void widgetDisposed(DisposeEvent event) {
                     if (Sancho.hasCollectionFactory()) {
                        Sancho.getCore().getNetworkCollection().deleteObserver(NetworkItem.this);
                     }
                  }
               });
               toolItem.setToolTipText(network.getToolTip());
               toolItem.setImage(network.getImage());
            }
         }

         this.parentComposite.layout();
         this.parentComposite.getParent().layout();
      }
   }

   private ToolItem getToolItemByNetwork(Network network) {
      if (this.toolBar != null && !this.toolBar.isDisposed()) {
         ToolItem[] toolItems = this.toolBar.getItems();

         for (int i = 0; i < toolItems.length; i++) {
            ToolItem toolItem = toolItems[i];
            if (toolItem != null && !toolItem.isDisposed() && toolItem.getData() == network) {
               return toolItem;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void deletePopupMenus() {
      for (int i = 0; i < this.popupMenuList.size(); i++) {
         MenuManager menuManager = (MenuManager)this.popupMenuList.get(i);
         if (menuManager != null) {
            menuManager.removeAll();
            menuManager.dispose();
         }
      }

      this.popupMenuList.clear();
   }

   public void setConnected(boolean connected) {
      if (connected && Sancho.hasCollectionFactory()) {
         this.createContent();
         Sancho.getCore().getNetworkCollection().addObserver(this);
      } else {
         if (this.composite != null) {
            this.composite.dispose();
         }

         this.composite = new Composite(this.parentComposite, 0);
         this.composite.setLayoutData(WidgetFactory.createGridData(0, 1, 1));
         this.parentComposite.layout();
         this.deletePopupMenus();
      }
   }

   public void update(MyObservable observable, Object object, int id) {
      if (object != null && object instanceof Network && this.toolBar != null && !this.toolBar.isDisposed()) {
         final Network network = (Network)object;
         this.composite.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (NetworkItem.this.toolBar != null && !NetworkItem.this.toolBar.isDisposed()) {
                  ToolItem toolItem = NetworkItem.this.getToolItemByNetwork(network);
                  if (toolItem != null && !toolItem.isDisposed()) {
                     toolItem.setImage(network.getImage());
                     toolItem.setToolTipText(network.getToolTip());
                  }
               }
            }
         });
      }
   }

   // sorts networks by name, keeping gnutella ahead of g2
   private static class NetworkComparator implements Comparator {
      public int compare(Object object1, Object object2) {
         Network network1 = (Network)object1;
         Network network2 = (Network)object2;
         if (network1.getName().equalsIgnoreCase("g2") && network2.getName().equalsIgnoreCase("gnutella")) {
            return 1;
         } else {
            return network2.getName().equalsIgnoreCase("g2") && network1.getName().equalsIgnoreCase("gnutella") ? -1 : network1.getName().compareToIgnoreCase(network2.getName());
         }
      }
   }

   // builds the per-network tool item popup menu on demand
   private static class NetworkMenuListener implements IMenuListener {
      EnumNetwork enumNetwork;

      public NetworkMenuListener(EnumNetwork enumNetwork) {
         this.enumNetwork = enumNetwork;
      }

      public void menuAboutToShow(IMenuManager menuManager) {
         if (Sancho.hasCollectionFactory()) {
            Network network = Sancho.getCore().getNetworkCollection().getByEnum(this.enumNetwork);
            if (network != null) {
               if (!network.isVirtual()) {
                  if (network.isEnabled()) {
                     menuManager.add(new NetworkDisableAction(network.getEnumNetwork()));
                  } else {
                     menuManager.add(new NetworkEnableAction(network.getEnumNetwork()));
                  }
               }

               if (network.isEnabled() && (network.hasServers() || network.hasSupernodes())) {
                  menuManager.add(new Separator());
                  menuManager.add(new NetworkConnectMoreAction(network.getEnumNetwork()));
               }
            }
         }
      }
   }
}
