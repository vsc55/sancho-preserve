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

   public CToolBar(MainWindow mainWindow, boolean smallButtons) {
      this.toolbarSmallButtons = smallButtons;
      this.coolbarLocked = true;
      this.mainWindow = mainWindow;
      this.mainToolButtons = new ArrayList();
      this.createContent(mainWindow.getMainComposite());
   }

   private void createContent(Composite parent) {
      this.composite = new Composite(parent, 0);
      GridLayout gridLayout = WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false);
      this.composite.setLayout(gridLayout);
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
      for (int i = 0; i < this.coolbar.getItems().length; i++) {
         this.coolbar.getItems()[i].dispose();
      }

      for (int i = 0; i < 1; i++) {
         new CoolItem(this.coolbar, 0);
      }

      CoolItem[] items = this.coolbar.getItems();
      CoolItem item = items[0];
      item.setControl(this.mainTools);
   }

   public MenuItem createMenuItem(Menu menu, int style, boolean selected, String textKey, SelectionAdapter listener) {
      MenuItem menuItem = new MenuItem(menu, style);
      menuItem.setText(SResources.getString(textKey));
      if (selected) {
         menuItem.setSelection(selected);
      }

      menuItem.addSelectionListener(listener);
      return menuItem;
   }

   private Menu createToolBarRMMenu() {
      Menu menu = new Menu(this.mainWindow.getShell(), 8);
      this.createMenuItem(menu, 32, this.toolbarSmallButtons, "mi.cb.small", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            CToolBar.this.toggleSmallButtons();
         }
      });
      this.createMenuItem(menu, 0, false, "mi.cb.tabSelector", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            CToolBar.this.mainWindow.configureTabs();
         }
      });
      return menu;
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
      for (int i = 0; i < this.coolbar.getItemCount(); i++) {
         CoolItem coolItem = this.coolbar.getItem(i);
         ToolBar toolBar = (ToolBar)coolItem.getControl();
         Point point = toolBar.computeSize(-1, -1);
         point = coolItem.computeSize(point.x, point.y);
         coolItem.setSize(point);
         coolItem.setMinimumSize(point);
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
      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
      preferenceStore.setValue("toolbarSmallButtons", this.isToolbarSmallButtons());
   }

   public void setToolbarSmallButtons(boolean smallButtons) {
      this.toolbarSmallButtons = smallButtons;
   }

   private void toggleSmallButtons() {
      this.toolbarSmallButtons = !this.toolbarSmallButtons;
      this.coolbar.dispose();
      this.createCoolBar();
      this.createToolBars();
      this.createCoolItems();

      for (Object buttonObject : this.mainToolButtons) { ToolButton button = (ToolButton)buttonObject;
         button.useSmallButtons(this.toolbarSmallButtons);
         button.resetItem(this.mainTools);
      }

      this.layoutCoolBar();
      this.composite.getParent().layout();
   }

   public void widgetDisposed(DisposeEvent disposeEvent) {
      this.savePreferences();
   }
}
