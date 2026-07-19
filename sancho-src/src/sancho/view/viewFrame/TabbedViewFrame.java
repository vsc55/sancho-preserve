package sancho.view.viewFrame;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.actions.AddTabAction;
import sancho.view.viewFrame.actions.RemoveTabAction;
import sancho.view.viewFrame.actions.TabsOnTopAction;

public class TabbedViewFrame extends ViewFrame {
   protected Composite cTabChildComposite;
   protected CTabFolder cTabFolder;
   protected int defTabHeight = -1;
   protected CTabItem oldSelectionItem;
   protected MenuManager popupMenu;
   protected String tabPrefString;

   public TabbedViewFrame(Composite var1, String var2, String var3, AbstractTab var4, String var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public TabbedViewFrame(Composite var1, String var2, String var3, AbstractTab var4, String var5, boolean var6) {
      super(var1, var2, var3, var4, var6);
      this.tabPrefString = var5;
      this.createPopupMenu();
      this.cTabFolder = WidgetFactory.createCTabFolder(this.childComposite, PreferenceLoader.loadBoolean(var5 + "TabsOnTop") ? 128 : 1024);
      this.cTabFolder.setBorderVisible(false);
      int var7 = PreferenceLoader.loadInt(var5 + "Tabs");
      if (var7 < 1) {
         var7 = 1;
      }

      for (int var9 = 0; var9 < var7; var9++) {
         CTabItem var8 = new CTabItem(this.cTabFolder, 0);
         var8.setText(PreferenceLoader.loadString(var5 + "Tab_" + var9 + "_Name"));
         var8.setData("filterString", PreferenceLoader.loadString(var5 + "Tab_" + var9 + "_Filters"));
         this.onCTabDispose(var8);
      }

      if (SWT.getPlatform().equals("fox")) {
         this.defTabHeight = this.cTabFolder.getTabHeight();
      }

      if (!PreferenceLoader.loadBoolean(var5 + "ShowTabs")) {
         this.cTabFolder.setTabHeight(0);
      }

      this.cTabChildComposite = new Composite(this.cTabFolder, 0);
      this.cTabChildComposite.setLayout(new FillLayout());
      this.cTabFolder.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            CTabItem var2 = (CTabItem)var1.item;
            oldSelectionItem.setData("filterString", gView.filtersToString());
            oldSelectionItem.setControl(null);
            switchToTab(var2, false);
         }
      });
      int var10 = SWT.getPlatform().equals("fox") ? 3 : 35;
      this.cTabFolder.addListener(var10, new Listener() {
         public void handleEvent(Event var1) {
            if (!SWT.getPlatform().equals("fox") || var1.button == 3) {
               Point var2 = cTabFolder.getDisplay().getCursorLocation();
               var2 = cTabFolder.toControl(var2);
               CTabItem var3 = cTabFolder.getItem(var2);
               if (var3 != null) {
                  Menu var4 = popupMenu.createContextMenu(cTabFolder);
                  var4.setVisible(true);
               }
            }
         }
      });
      Listener dragListener = new Listener() {
         boolean drag = false;
         CTabItem dragItem;
         boolean exitDrag = false;

         public void handleEvent(Event var1) {
            Point var2 = cTabFolder.toControl(cTabFolder.getDisplay().getCursorLocation());
            switch (var1.type) {
               case 4:
                  if (!this.drag) {
                     return;
                  }

                  cTabFolder.setInsertMark(null, false);
                  CTabItem var7 = cTabFolder.getItem(var2);
                  if (var7 == this.dragItem) {
                     return;
                  }

                  if (var7 != null) {
                     int var8 = cTabFolder.indexOf(var7);
                     CTabItem var5 = new CTabItem(cTabFolder, 0, var8);
                     var5.setText(this.dragItem.getText());
                     var5.setData("filterString", this.dragItem.getData("filterString"));
                     onCTabDispose(var5);
                     this.dragItem.setControl(null);
                     this.dragItem.dispose();
                     switchToTab(var5);
                  }

                  this.drag = false;
                  this.exitDrag = false;
                  this.dragItem = null;
                  break;
               case 5:
                  if (!this.drag) {
                     return;
                  }

                  CTabItem var6 = cTabFolder.getItem(var2);
                  if (var6 == null) {
                     cTabFolder.setInsertMark(null, false);
                     return;
                  }

                  cTabFolder.setInsertMark(var6, false);
                  break;
               case 6:
                  if (this.exitDrag) {
                     this.exitDrag = false;
                     this.drag = var1.button != 0;
                  }
                  break;
               case 7:
                  if (this.drag) {
                     cTabFolder.setInsertMark(null, false);
                     this.exitDrag = true;
                     this.drag = false;
                  }
                  break;
               case 29:
                  CTabItem var3 = cTabFolder.getItem(var2);
                  CTabItem var4 = cTabFolder.getSelection();
                  if (var3 == null || var3 != var4) {
                     return;
                  }

                  this.drag = true;
                  this.exitDrag = false;
                  this.dragItem = var3;
            }
         }
      };
      this.cTabFolder.addListener(29, dragListener);
      this.cTabFolder.addListener(4, dragListener);
      this.cTabFolder.addListener(5, dragListener);
      this.cTabFolder.addListener(7, dragListener);
      this.cTabFolder.addListener(6, dragListener);
   }

   public void createItem(String var1) {
      CTabItem var2 = new CTabItem(this.cTabFolder, 0);
      var2.setText(var1);
      var2.setData("filterString", "");
      this.cTabFolder.setSelection(var2);
      this.onCTabDispose(var2);
      this.switchToTab(var2);
   }

   public void createPopupMenu() {
      this.popupMenu = new MenuManager();
      this.popupMenu.setRemoveAllWhenShown(true);
      this.popupMenu.addMenuListener(new TabMenuListener(this, this.tabPrefString));
   }

   public Composite getChildComposite() {
      return this.cTabChildComposite;
   }

   public CTabFolder getCTabFolder() {
      return this.cTabFolder;
   }

   public void onCTabDispose(CTabItem var1) {
      var1.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent var1) {
            CTabItem var2 = (CTabItem)var1.widget;
            PreferenceStore var3 = PreferenceLoader.getPreferenceStore();
            var3.setValue(tabPrefString + "Tabs", cTabFolder.getItemCount());
            int var4 = cTabFolder.indexOf(var2);
            var3.setValue(tabPrefString + "Tab_" + var4 + "_Name", var2.getText());
            if (cTabFolder.getSelection() == var2) {
               var3.setValue(tabPrefString + "Tab_" + var4 + "_Filters", gView.filtersToString());
            } else {
               var3.setValue(tabPrefString + "Tab_" + var4 + "_Filters", (String)var2.getData("filterString"));
            }
         }
      });
   }

   public void switchToTab(CTabItem var1) {
      this.switchToTab(var1, true);
   }

   public void switchToTab(CTabItem var1, boolean var2) {
      if (var2) {
         this.cTabFolder.setSelection(var1);
      }

      this.gView.swapFilters((String)var1.getData("filterString"));
      var1.setControl(this.getChildComposite());
      this.oldSelectionItem = var1;
   }

   public void dispose() {
      if (this.cTabFolder != null) {
         this.cTabFolder.dispose();
         this.cTabFolder = null;
      }

      super.dispose();
   }

   public void toggleTabs() {
      this.cTabFolder.setTabHeight(this.cTabFolder.getTabHeight() == 0 ? this.defTabHeight : 0);
      PreferenceLoader.getPreferenceStore().setValue(this.tabPrefString + "ShowTabs", this.cTabFolder.getTabHeight() != 0);
      this.cTabFolder.layout();
   }

   public void toggleTabPosition() {
      this.cTabFolder.setTabPosition((this.cTabFolder.getStyle() & 1024) != 0 ? 128 : 1024);
      this.cTabFolder.layout();
   }

   // Menu listener that populates the tab context menu (add/remove tab, tabs-on-top).
   private static class TabMenuListener implements IMenuListener {
      String tabPrefString;
      TabbedViewFrame viewFrame;

      public TabMenuListener(TabbedViewFrame var1, String var2) {
         this.viewFrame = var1;
         this.tabPrefString = var2;
      }

      public void menuAboutToShow(IMenuManager var1) {
         var1.add(new AddTabAction(this.viewFrame));
         if (this.viewFrame.getCTabFolder().getItemCount() > 1) {
            var1.add(new RemoveTabAction(this.viewFrame));
         }

         var1.add(new Separator());
         var1.add(new TabsOnTopAction(this.tabPrefString, this.viewFrame));
      }
   }
}
