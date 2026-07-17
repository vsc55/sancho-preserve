package sancho.view.viewFrame;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.WidgetFactory;

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
      this.cTabFolder.addSelectionListener(new TabbedViewFrame$1(this));
      int var10 = SWT.getPlatform().equals("fox") ? 3 : 35;
      this.cTabFolder.addListener(var10, new TabbedViewFrame$2(this));
      TabbedViewFrame$3 var11 = new TabbedViewFrame$3(this);
      this.cTabFolder.addListener(29, var11);
      this.cTabFolder.addListener(4, var11);
      this.cTabFolder.addListener(5, var11);
      this.cTabFolder.addListener(7, var11);
      this.cTabFolder.addListener(6, var11);
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
      this.popupMenu.addMenuListener(new TabbedViewFrame$TabMenuListener(this, this.tabPrefString));
   }

   public Composite getChildComposite() {
      return this.cTabChildComposite;
   }

   public CTabFolder getCTabFolder() {
      return this.cTabFolder;
   }

   public void onCTabDispose(CTabItem var1) {
      var1.addDisposeListener(new TabbedViewFrame$4(this));
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
}
