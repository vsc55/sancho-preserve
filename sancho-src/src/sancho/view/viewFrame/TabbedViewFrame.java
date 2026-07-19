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

   public TabbedViewFrame(Composite parent, String prefString, String imageString, AbstractTab tab, String tabPrefString) {
      this(parent, prefString, imageString, tab, tabPrefString, false);
   }

   public TabbedViewFrame(Composite parent, String prefString, String imageString, AbstractTab tab, String tabPrefString, boolean flag) {
      super(parent, prefString, imageString, tab, flag);
      this.tabPrefString = tabPrefString;
      this.createPopupMenu();
      this.cTabFolder = WidgetFactory.createCTabFolder(this.childComposite, PreferenceLoader.loadBoolean(tabPrefString + "TabsOnTop") ? 128 : 1024);
      this.cTabFolder.setBorderVisible(false);
      int tabCount = PreferenceLoader.loadInt(tabPrefString + "Tabs");
      if (tabCount < 1) {
         tabCount = 1;
      }

      for (int i = 0; i < tabCount; i++) {
         CTabItem tabItem = new CTabItem(this.cTabFolder, 0);
         tabItem.setText(PreferenceLoader.loadString(tabPrefString + "Tab_" + i + "_Name"));
         tabItem.setData("filterString", PreferenceLoader.loadString(tabPrefString + "Tab_" + i + "_Filters"));
         this.onCTabDispose(tabItem);
      }

      if (SWT.getPlatform().equals("fox")) {
         this.defTabHeight = this.cTabFolder.getTabHeight();
      }

      if (!PreferenceLoader.loadBoolean(tabPrefString + "ShowTabs")) {
         this.cTabFolder.setTabHeight(0);
      }

      this.cTabChildComposite = new Composite(this.cTabFolder, 0);
      this.cTabChildComposite.setLayout(new FillLayout());
      this.cTabFolder.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            CTabItem tabItem = (CTabItem)event.item;
            oldSelectionItem.setData("filterString", gView.filtersToString());
            oldSelectionItem.setControl(null);
            switchToTab(tabItem, false);
         }
      });
      int eventType = SWT.getPlatform().equals("fox") ? 3 : 35;
      this.cTabFolder.addListener(eventType, new Listener() {
         public void handleEvent(Event event) {
            if (!SWT.getPlatform().equals("fox") || event.button == 3) {
               Point point = cTabFolder.getDisplay().getCursorLocation();
               point = cTabFolder.toControl(point);
               CTabItem tabItem = cTabFolder.getItem(point);
               if (tabItem != null) {
                  Menu menu = popupMenu.createContextMenu(cTabFolder);
                  menu.setVisible(true);
               }
            }
         }
      });
      Listener dragListener = new Listener() {
         boolean drag = false;
         CTabItem dragItem;
         boolean exitDrag = false;

         public void handleEvent(Event event) {
            Point point = cTabFolder.toControl(cTabFolder.getDisplay().getCursorLocation());
            switch (event.type) {
               case 4:
                  if (!this.drag) {
                     return;
                  }

                  cTabFolder.setInsertMark(null, false);
                  CTabItem targetItem = cTabFolder.getItem(point);
                  if (targetItem == this.dragItem) {
                     return;
                  }

                  if (targetItem != null) {
                     int index = cTabFolder.indexOf(targetItem);
                     CTabItem newItem = new CTabItem(cTabFolder, 0, index);
                     newItem.setText(this.dragItem.getText());
                     newItem.setData("filterString", this.dragItem.getData("filterString"));
                     onCTabDispose(newItem);
                     this.dragItem.setControl(null);
                     this.dragItem.dispose();
                     switchToTab(newItem);
                  }

                  this.drag = false;
                  this.exitDrag = false;
                  this.dragItem = null;
                  break;
               case 5:
                  if (!this.drag) {
                     return;
                  }

                  CTabItem markItem = cTabFolder.getItem(point);
                  if (markItem == null) {
                     cTabFolder.setInsertMark(null, false);
                     return;
                  }

                  cTabFolder.setInsertMark(markItem, false);
                  break;
               case 6:
                  if (this.exitDrag) {
                     this.exitDrag = false;
                     this.drag = event.button != 0;
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
                  CTabItem cursorItem = cTabFolder.getItem(point);
                  CTabItem selectedItem = cTabFolder.getSelection();
                  if (cursorItem == null || cursorItem != selectedItem) {
                     return;
                  }

                  this.drag = true;
                  this.exitDrag = false;
                  this.dragItem = cursorItem;
            }
         }
      };
      this.cTabFolder.addListener(29, dragListener);
      this.cTabFolder.addListener(4, dragListener);
      this.cTabFolder.addListener(5, dragListener);
      this.cTabFolder.addListener(7, dragListener);
      this.cTabFolder.addListener(6, dragListener);
   }

   public void createItem(String text) {
      CTabItem tabItem = new CTabItem(this.cTabFolder, 0);
      tabItem.setText(text);
      tabItem.setData("filterString", "");
      this.cTabFolder.setSelection(tabItem);
      this.onCTabDispose(tabItem);
      this.switchToTab(tabItem);
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

   public void onCTabDispose(CTabItem tabItem) {
      tabItem.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent event) {
            CTabItem tabItem = (CTabItem)event.widget;
            PreferenceStore store = PreferenceLoader.getPreferenceStore();
            store.setValue(tabPrefString + "Tabs", cTabFolder.getItemCount());
            int index = cTabFolder.indexOf(tabItem);
            store.setValue(tabPrefString + "Tab_" + index + "_Name", tabItem.getText());
            if (cTabFolder.getSelection() == tabItem) {
               store.setValue(tabPrefString + "Tab_" + index + "_Filters", gView.filtersToString());
            } else {
               store.setValue(tabPrefString + "Tab_" + index + "_Filters", (String)tabItem.getData("filterString"));
            }
         }
      });
   }

   public void switchToTab(CTabItem tabItem) {
      this.switchToTab(tabItem, true);
   }

   public void switchToTab(CTabItem tabItem, boolean select) {
      if (select) {
         this.cTabFolder.setSelection(tabItem);
      }

      this.gView.swapFilters((String)tabItem.getData("filterString"));
      tabItem.setControl(this.getChildComposite());
      this.oldSelectionItem = tabItem;
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

      public TabMenuListener(TabbedViewFrame viewFrame, String tabPrefString) {
         this.viewFrame = viewFrame;
         this.tabPrefString = tabPrefString;
      }

      public void menuAboutToShow(IMenuManager menuManager) {
         menuManager.add(new AddTabAction(this.viewFrame));
         if (this.viewFrame.getCTabFolder().getItemCount() > 1) {
            menuManager.add(new RemoveTabAction(this.viewFrame));
         }

         menuManager.add(new Separator());
         menuManager.add(new TabsOnTopAction(this.tabPrefString, this.viewFrame));
      }
   }
}
