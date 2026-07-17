package sancho.view.statusline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.utility.MyObservable;
import sancho.view.StatusLine;
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
         Arrays.sort(var1, new NetworkItem$NetworkComparator());

         for (int var3 = 0; var3 < var1.length; var3++) {
            Network var4 = var1[var3];
            if (!var4.isVirtual()) {
               ToolItem var2 = new ToolItem(this.toolBar, 0);
               var2.setData(var4);
               this.deletePopupMenus();
               MenuManager var5 = new MenuManager();
               this.popupMenuList.add(var5);
               var5.setRemoveAllWhenShown(true);
               var5.addMenuListener(new NetworkItem$NetworkMenuListener(var4.getEnumNetwork()));
               var2.addSelectionListener(new NetworkItem$1(this, var5));
               var2.addDisposeListener(new NetworkItem$2(this));
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
         Network var4 = (Network)var2;
         this.composite.getDisplay().asyncExec(new NetworkItem$3(this, var4));
      }
   }

   // $VF: synthetic method
   static ToolBar access$000(NetworkItem var0) {
      return var0.toolBar;
   }

   // $VF: synthetic method
   static ToolItem access$100(NetworkItem var0, Network var1) {
      return var0.getToolItemByNetwork(var1);
   }
}
