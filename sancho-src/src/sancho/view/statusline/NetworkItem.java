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

   public NetworkItem(StatusLine var1) {
      this.parentComposite = new Composite(var1.getStatusline(), 0);
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
         Network[] var1 = Sancho.getCore().getNetworkCollection().getNetworks();
         Arrays.sort(var1, new NetworkComparator());

         for (int var3 = 0; var3 < var1.length; var3++) {
            Network var4 = var1[var3];
            if (!var4.isVirtual()) {
               ToolItem var2 = new ToolItem(this.toolBar, 0);
               var2.setData(var4);
               this.deletePopupMenus();
               final MenuManager var5 = new MenuManager();
               this.popupMenuList.add(var5);
               var5.setRemoveAllWhenShown(true);
               var5.addMenuListener(new NetworkMenuListener(var4.getEnumNetwork()));
               var2.addSelectionListener(new SelectionAdapter() {
                  public void widgetSelected(SelectionEvent var1) {
                     Rectangle var2 = ((ToolItem)var1.widget).getBounds();
                     Menu var3 = var5.createContextMenu(NetworkItem.this.toolBar);
                     Point var4 = new Point(var2.x, var2.y + var2.height);
                     var4 = NetworkItem.this.toolBar.toDisplay(var4);
                     var3.setLocation(var4.x, var4.y);
                     var3.setVisible(true);
                  }
               });
               var2.addDisposeListener(new DisposeListener() {
                  public void widgetDisposed(DisposeEvent var1) {
                     if (Sancho.hasCollectionFactory()) {
                        Sancho.getCore().getNetworkCollection().deleteObserver(NetworkItem.this);
                     }
                  }
               });
               var2.setToolTipText(var4.getToolTip());
               var2.setImage(var4.getImage());
            }
         }

         this.parentComposite.layout();
         this.parentComposite.getParent().layout();
      }
   }

   private ToolItem getToolItemByNetwork(Network var1) {
      if (this.toolBar != null && !this.toolBar.isDisposed()) {
         ToolItem[] var2 = this.toolBar.getItems();

         for (int var4 = 0; var4 < var2.length; var4++) {
            ToolItem var3 = var2[var4];
            if (var3 != null && !var3.isDisposed() && var3.getData() == var1) {
               return var3;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void deletePopupMenus() {
      for (int var1 = 0; var1 < this.popupMenuList.size(); var1++) {
         MenuManager var2 = (MenuManager)this.popupMenuList.get(var1);
         if (var2 != null) {
            var2.removeAll();
            var2.dispose();
         }
      }

      this.popupMenuList.clear();
   }

   public void setConnected(boolean var1) {
      if (var1 && Sancho.hasCollectionFactory()) {
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

   public void update(MyObservable var1, Object var2, int var3) {
      if (var2 != null && var2 instanceof Network && this.toolBar != null && !this.toolBar.isDisposed()) {
         final Network var4 = (Network)var2;
         this.composite.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (NetworkItem.this.toolBar != null && !NetworkItem.this.toolBar.isDisposed()) {
                  ToolItem var1 = NetworkItem.this.getToolItemByNetwork(var4);
                  if (var1 != null && !var1.isDisposed()) {
                     var1.setImage(var4.getImage());
                     var1.setToolTipText(var4.getToolTip());
                  }
               }
            }
         });
      }
   }

   // sorts networks by name, keeping gnutella ahead of g2
   private static class NetworkComparator implements Comparator {
      public int compare(Object var1, Object var2) {
         Network var3 = (Network)var1;
         Network var4 = (Network)var2;
         if (var3.getName().equalsIgnoreCase("g2") && var4.getName().equalsIgnoreCase("gnutella")) {
            return 1;
         } else {
            return var4.getName().equalsIgnoreCase("g2") && var3.getName().equalsIgnoreCase("gnutella") ? -1 : var3.getName().compareToIgnoreCase(var4.getName());
         }
      }
   }

   // builds the per-network tool item popup menu on demand
   private static class NetworkMenuListener implements IMenuListener {
      EnumNetwork enumNetwork;

      public NetworkMenuListener(EnumNetwork var1) {
         this.enumNetwork = var1;
      }

      public void menuAboutToShow(IMenuManager var1) {
         if (Sancho.hasCollectionFactory()) {
            Network var2 = Sancho.getCore().getNetworkCollection().getByEnum(this.enumNetwork);
            if (var2 != null) {
               if (!var2.isVirtual()) {
                  if (var2.isEnabled()) {
                     var1.add(new NetworkDisableAction(var2.getEnumNetwork()));
                  } else {
                     var1.add(new NetworkEnableAction(var2.getEnumNetwork()));
                  }
               }

               if (var2.isEnabled() && (var2.hasServers() || var2.hasSupernodes())) {
                  var1.add(new Separator());
                  var1.add(new NetworkConnectMoreAction(var2.getEnumNetwork()));
               }
            }
         }
      }
   }
}
