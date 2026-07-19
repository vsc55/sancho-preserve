package sancho.view.mainWindow;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.ToolButton;
import sancho.view.utility.WidgetFactory;

public class CToolBar implements DisposeListener {
   private Composite composite;
   private CoolBar coolbar;
   private boolean coolbarLocked;
   private List mainToolButtons;
   private ToolBar mainTools;
   private MainWindow mainWindow;
   private boolean toolbarSmallButtons;

   public CToolBar(MainWindow var1, boolean var2) {
      this.toolbarSmallButtons = var2;
      this.coolbarLocked = true;
      this.mainWindow = var1;
      this.mainToolButtons = new ArrayList();
      this.createContent(var1.getMainComposite());
   }

   private void createContent(Composite var1) {
      this.composite = new Composite(var1, 0);
      GridLayout var2 = WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false);
      this.composite.setLayout(var2);
      this.composite.setLayoutData(new GridData(768));
      this.createCoolBar();
      this.createToolBars();
      this.createCoolItems();
   }

   private void createCoolBar() {
      this.coolbar = new CoolBar(this.composite, 8388608);
      this.coolbar.addDisposeListener(this);
      this.coolbar.setLayoutData(new GridData(768));
   }

   public void createCoolItems() {
      for (int var1 = 0; var1 < this.coolbar.getItems().length; var1++) {
         this.coolbar.getItems()[var1].dispose();
      }

      for (int var2 = 0; var2 < 1; var2++) {
         new CoolItem(this.coolbar, 0);
      }

      CoolItem[] var3 = this.coolbar.getItems();
      CoolItem var4 = var3[0];
      var4.setControl(this.mainTools);
   }

   public MenuItem createMenuItem(Menu var1, int var2, boolean var3, String var4, SelectionAdapter var5) {
      MenuItem var6 = new MenuItem(var1, var2);
      var6.setText(SResources.getString(var4));
      if (var3) {
         var6.setSelection(var3);
      }

      var6.addSelectionListener(var5);
      return var6;
   }

   private Menu createToolBarRMMenu() {
      Menu var1 = new Menu(this.mainWindow.getShell(), 8);
      this.createMenuItem(var1, 32, this.toolbarSmallButtons, "mi.cb.small", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            CToolBar.this.toggleSmallButtons();
         }
      });
      this.createMenuItem(var1, 0, false, "mi.cb.tabSelector", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            CToolBar.this.mainWindow.configureTabs();
         }
      });
      return var1;
   }

   public void createToolBars() {
      if (this.mainTools != null && !this.mainTools.isDisposed()) {
         this.mainTools.getMenu().dispose();
         this.mainTools.dispose();
      }

      this.mainTools = new ToolBar(this.coolbar, (this.toolbarSmallButtons ? 131072 : 0) | 8388608);
      this.mainTools.setMenu(this.createToolBarRMMenu());
   }

   public List getMainToolButtons() {
      return this.mainToolButtons;
   }

   public ToolBar getToolBar() {
      return this.mainTools;
   }

   public boolean isToolbarSmallButtons() {
      return this.toolbarSmallButtons;
   }

   public void layoutCoolBar() {
      for (int var1 = 0; var1 < this.coolbar.getItemCount(); var1++) {
         CoolItem var2 = this.coolbar.getItem(var1);
         ToolBar var3 = (ToolBar)var2.getControl();
         Point var4 = var3.computeSize(-1, -1);
         var4 = var2.computeSize(var4.x, var4.y);
         var2.setSize(var4);
         var2.setMinimumSize(var4);
      }

      this.coolbar.getParent().layout();
      this.coolbar.setLocked(this.coolbarLocked);
   }

   public void reset() {
      this.mainToolButtons.clear();
      this.createToolBars();
      this.createCoolItems();
   }

   public void savePreferences() {
      PreferenceStore var1 = PreferenceLoader.getPreferenceStore();
      var1.setValue("toolbarSmallButtons", this.isToolbarSmallButtons());
   }

   public void setToolbarSmallButtons(boolean var1) {
      this.toolbarSmallButtons = var1;
   }

   private void toggleSmallButtons() {
      this.toolbarSmallButtons = !this.toolbarSmallButtons;
      this.coolbar.dispose();
      this.createCoolBar();
      this.createToolBars();
      this.createCoolItems();

      for (Object var2o : this.mainToolButtons) { ToolButton var2 = (ToolButton)var2o;
         var2.useSmallButtons(this.toolbarSmallButtons);
         var2.resetItem(this.mainTools);
      }

      this.layoutCoolBar();
      this.composite.getParent().layout();
   }

   public void widgetDisposed(DisposeEvent var1) {
      this.savePreferences();
   }
}
