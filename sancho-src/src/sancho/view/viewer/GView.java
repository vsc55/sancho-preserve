package sancho.view.viewer;

import java.util.StringTokenizer;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.IDSelector;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.filters.AbstractViewerFilter;
import sancho.view.viewer.filters.ExclusionStateViewerFilter;
import sancho.view.viewer.filters.FileExtensionViewerFilter;
import sancho.view.viewer.filters.NetworkViewerFilter;
import sancho.view.viewer.filters.RefineFilter;
import sancho.view.viewer.filters.StateViewerFilter;
import sancho.view.viewer.table.GTableLabelProvider;
import sancho.view.viewer.table.GTableMenuListener;

public abstract class GView implements DisposeListener {
   public static final String S_GVIEW = "gView";
   public static final int STATE_FILTER = 1;
   public static final int EXCLUSION_STATE_FILTER = 2;
   public static final int NETWORK_FILTER = 3;
   public static final int FILE_EXTENSION_FILTER = 4;
   public static final int REFINE_FILTER = 5;
   protected boolean active;
   protected String allColumns;
   protected int[] columnAlignment;
   protected int[] columnDefaultWidths;
   protected String columnIDs;
   protected String[] columnLabels;
   protected ControlAdapter controlAdapter;
   protected String dynamicColumn = "";
   protected boolean forceRedraw = SWT.getPlatform().equals("win32") || SWT.getPlatform().equals("gtk");
   protected GSorter gSorter;
   protected int minDynamicColumnWidth = 100;
   protected boolean oldTableScrollBar;
   protected int oldTableWidth;
   protected String preferenceString;
   protected RefineFilter refineFilter;
   protected String refineString = "";
   protected boolean saveExclusionStateFilters;
   protected boolean saveNetworkFilters;
   protected boolean saveStateFilters;
   protected StructuredViewer sViewer;
   protected GTableLabelProvider tableLabelProvider;
   protected AbstractEnum[] validExtensions;
   protected AbstractEnum[] validStates;
   protected ViewFrame viewFrame;
   protected boolean visible;
   protected MenuManager popupMenu;
   protected Object input;
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$StateViewerFilter;
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$ExclusionStateViewerFilter;
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$NetworkViewerFilter;

   public void setInput(Object var1) {
      this.getViewer().setInput(var1);
   }

   public void addFilter(ViewerFilter var1) {
      this.setRedraw(false);
      this.sViewer.addFilter(var1);
      this.setRedraw(true);
   }

   protected void addMenuListener() {
      Menu var1 = this.getComposite().getMenu();
      if (var1 != null) {
         var1.addMenuListener(new MenuAdapter() {
            public void menuShown(MenuEvent var1) {
               Menu var2 = GView.this.getComposite().getMenu();
               if (!GView.this.getViewer().getSelection().isEmpty() && var2.getItemCount() > 0) {
                  var2.setDefaultItem(var2.getItem(0));
               }
            }
         });
      }
   }

   public abstract int getItemCount();

   public abstract String getColumnText(int var1);

   public abstract int getColumnCount();

   public abstract void deselectAll();

   public abstract void selectAll();

   public abstract Item getItemAt(int var1, int var2);

   protected abstract void disposeAllColumns();

   protected abstract void createColumns();

   public abstract IGContentProvider getContentProvider();

   protected void createContents() {
      this.sViewer.setUseHashlookup(true);
      this.allColumns = IDSelector.createIDString(this.columnLabels);
      Composite var1 = this.getComposite();
      var1.setRedraw(false);
      this.createColumns();
      this.getContentProvider().initialize();
      this.getTableLabelProvider().initialize();
      this.gSorter.initialize();
      this.getTableMenuListener().initialize();
      this.sViewer.setContentProvider(this.getContentProvider());
      this.sViewer.setLabelProvider(this.getTableLabelProvider());
      this.updateDisplay();
      this.createMenu();
      this.sViewer.setSorter(this.gSorter);
      this.loadDynamicColumn();
      this.setSortIndicator();
      this.getComposite().addDisposeListener(this);
      this.loadFilters();
      this.setInput();
      var1.setRedraw(true);
   }

   public String getAllColumnIDs() {
      return this.allColumns;
   }

   public String getColumnIDs() {
      return this.columnIDs;
   }

   public String[] getColumnLabels() {
      return this.columnLabels;
   }

   public int[] getColumnAlignment() {
      return this.columnAlignment;
   }

   public ICore getCore() {
      return Sancho.getCoreFactory().getCore();
   }

   public int getDynamicColumn() {
      return this.dynamicColumn.equals("") ? -1 : this.columnIDs.indexOf(this.dynamicColumn);
   }

   public AbstractViewerFilter getFilter(Class var1) {
      for (int var2 = 0; var2 < this.getFilters().length; var2++) {
         if (var1.isInstance(this.getFilters()[var2])) {
            return (AbstractViewerFilter)this.getFilters()[var2];
         }
      }

      return null;
   }

   public ViewerFilter[] getFilters() {
      return this.sViewer.getFilters();
   }

   public int getMinDynamicColumnWidth() {
      return this.minDynamicColumnWidth;
   }

   public String getPreferenceString() {
      return this.preferenceString;
   }

   public String getRefineString() {
      return this.refineString;
   }

   public Shell getShell() {
      return this.getComposite().getShell();
   }

   public int getSortColumn() {
      return this.gSorter.getLastColumnIndex();
   }

   public abstract Composite getComposite();

   public GTableLabelProvider getTableLabelProvider() {
      return this.tableLabelProvider;
   }

   public abstract GTableMenuListener getTableMenuListener();

   public AbstractEnum[] getValidExtensions() {
      return this.validExtensions;
   }

   public AbstractEnum[] getValidStates() {
      return this.validStates;
   }

   public StructuredViewer getViewer() {
      return this.sViewer;
   }

   public ViewFrame getViewFrame() {
      return this.viewFrame;
   }

   public boolean isActive() {
      return this.active;
   }

   public boolean isDisposed() {
      return this.getViewer() == null || this.getComposite() == null || this.getComposite().isDisposed();
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void loadDynamicColumn() {
      String var1 = PreferenceLoader.loadString(this.preferenceString + "DynamicColumn");
      if (!var1.equals("")) {
         this.setDynamicColumn(var1);
      }

      int var2 = PreferenceLoader.loadInt(this.preferenceString + "MinDynamicColumnWidth");
      if (var2 > 0) {
         this.setMinDynamicColumnWidth(var2);
      }
   }

   public void loadExclusionStateFilters() {
      int var1 = PreferenceLoader.loadInt(this.preferenceString + "ExclusionStateFilters");
      if (var1 > 0) {
         ExclusionStateViewerFilter var2 = new ExclusionStateViewerFilter(this);
         var2.setFiltered(var1);
         this.addFilter(var2);
      }
   }

   public void swapFilters(String var1) {
      this.setRedraw(false);
      this.resetFilters();
      if (var1 != null) {
         this.addFilters(var1);
      }

      this.setRedraw(true);
   }

   public String filtersToString() {
      String var1 = "";
      ViewerFilter[] var2 = this.getFilters();

      for (int var3 = 0; var3 < var2.length; var3++) {
         var1 = var1 + var2[var3].toString();
      }

      return var1;
   }

   public void addFilters(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, ",");

      while (var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         String var4;
         if (var2.hasMoreTokens()) {
            var4 = var2.nextToken();
         } else {
            var4 = "";
         }

         int var5;
         try {
            var5 = Integer.parseInt(var3);
         } catch (NumberFormatException var7) {
            var5 = 0;
         }

         this.loadFilter(var5, var4);
      }
   }

   public static int filterToInt(ViewerFilter var0) {
      if (var0 instanceof StateViewerFilter) {
         return 1;
      } else if (var0 instanceof ExclusionStateViewerFilter) {
         return 2;
      } else if (var0 instanceof NetworkViewerFilter) {
         return 3;
      } else if (var0 instanceof RefineFilter) {
         return 5;
      } else {
         return var0 instanceof FileExtensionViewerFilter ? 4 : 0;
      }
   }

   public void loadFilter(int var1, String var2) {
      int var3 = 0;

      try {
         var3 = Integer.parseInt(var2);
      } catch (NumberFormatException var8) {
      }

      switch (var1) {
         case 1:
            StateViewerFilter var4 = new StateViewerFilter(this);
            var4.setFiltered(var3);
            this.addFilter(var4);
            break;
         case 2:
            ExclusionStateViewerFilter var5 = new ExclusionStateViewerFilter(this);
            var5.setFiltered(var3);
            this.addFilter(var5);
            break;
         case 3:
            NetworkViewerFilter var6 = new NetworkViewerFilter(this);
            var6.setFiltered(var3);
            this.addFilter(var6);
            break;
         case 4:
            FileExtensionViewerFilter var7 = new FileExtensionViewerFilter(this);
            var7.setFiltered(var3);
            this.addFilter(var7);
            break;
         case 5:
            this.refineString = var2;
            this.viewFrame.setRefineText(var2);
            this.updateRefineFilter();
      }
   }

   public void loadFilters() {
      if (this.saveStateFilters) {
         this.loadStateFilters();
      }

      if (this.saveExclusionStateFilters) {
         this.loadExclusionStateFilters();
      }

      if (this.saveNetworkFilters) {
         this.loadNetworkFilters();
      }
   }

   public void loadNetworkFilters() {
      int var1 = PreferenceLoader.loadInt(this.preferenceString + "NetworkFilters");
      if (var1 > 0) {
         NetworkViewerFilter var2 = new NetworkViewerFilter(this);
         var2.setFiltered(var1);
         this.addFilter(var2);
      }
   }

   public void loadStateFilters() {
      int var1 = PreferenceLoader.loadInt(this.preferenceString + "StateFilters");
      if (var1 > 0) {
         StateViewerFilter var2 = new StateViewerFilter(this);
         var2.setFiltered(var1);
         this.addFilter(var2);
      }
   }

   public void setRedraw(boolean var1) {
      if (this.forceRedraw) {
         this.getComposite().setRedraw(var1);
      }
   }

   public void refresh() {
      this.sViewer.refresh();
   }

   public void refresh(boolean var1) {
      if (var1) {
         this.setRedraw(false);
      }

      this.refresh();
      if (var1) {
         this.setRedraw(true);
      }
   }

   public void removeDynamicColumn() {
      this.dynamicColumn = "";
      if (this.controlAdapter != null) {
         this.getComposite().removeControlListener(this.controlAdapter);
         this.controlAdapter = null;
      }
   }

   public void removeFilter(ViewerFilter var1) {
      this.setRedraw(false);
      this.sViewer.removeFilter(var1);
      this.setRedraw(true);
   }

   public void clearAll() {
      ICustomViewer var1 = (ICustomViewer)this.getViewer();
      var1.clearAll();
   }

   public void resetColumns() {
      ICustomViewer var1 = (ICustomViewer)this.getViewer();
      Object var2 = var1.getInput();
      var1.setInput(null);
      var1.setEditors(false);
      this.gSorter.setColumnIndex(0);
      this.createColumns();
      var1.setInput(var2);
      this.resetDynamicColumn();
      this.setSortIndicator();
      this.updateDisplay();
   }

   public abstract void setSortIndicator();

   public void resetFilters() {
      this.setRedraw(false);
      this.sViewer.resetFilters();
      this.refineFilter = null;
      this.viewFrame.setRefineText("");
      this.setRedraw(true);
   }

   public abstract int getColumnWidthsExcept(int var1);

   public abstract void setColumnWidth(int var1, int var2);

   public void resizeColumns() {
      Composite var1 = this.getComposite();
      if (var1 != null && !var1.isDisposed() && this.dynamicColumn != null && !this.dynamicColumn.equals("")) {
         int var2 = var1.getBounds().width;
         ScrollBar var3 = var1.getVerticalBar();
         boolean var4 = var3 != null ? var3.getThumb() < var3.getMaximum() : false;
         if (this.oldTableWidth != var2 || var4 != this.oldTableScrollBar) {
            this.oldTableWidth = var2;
            this.oldTableScrollBar = var4;
            ICustomViewer var5 = (ICustomViewer)this.getViewer();
            int var6 = 0;
            int var7 = this.dynamicColumn.charAt(0) - 'A';
            int[] var8 = var5.getColumnIDs();
            int var9 = -1;

            for (int var10 = 0; var10 < var8.length; var10++) {
               if (var7 == var8[var10]) {
                  var9 = var10;
               }
            }

            if (this.getColumnCount() == var8.length && var9 != -1) {
               var6 = this.getColumnWidthsExcept(var9);
               if (var1 instanceof Tree) {
                  var6++;
               }

               if (var4) {
                  var6 += var3.getSize().x;
               }

               try {
                  this.setColumnWidth(var9, Math.max(this.minDynamicColumnWidth, var2 - var6));
               } catch (Exception var12) {
                  var12.printStackTrace();
               }
            }
         }
      }
   }

   public void saveEmptyGViewerFilter(String var1) {
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + var1, 0);
   }

   public void saveFilters() {
      if (this.saveStateFilters) {
         this.saveFilters(
            "StateFilters",
            class$sancho$view$viewer$filters$StateViewerFilter == null
               ? (class$sancho$view$viewer$filters$StateViewerFilter = class$("sancho.view.viewer.filters.StateViewerFilter"))
               : class$sancho$view$viewer$filters$StateViewerFilter
         );
      }

      if (this.saveExclusionStateFilters) {
         this.saveFilters(
            "ExclusionStateFilters",
            class$sancho$view$viewer$filters$ExclusionStateViewerFilter == null
               ? (class$sancho$view$viewer$filters$ExclusionStateViewerFilter = class$("sancho.view.viewer.filters.ExclusionStateViewerFilter"))
               : class$sancho$view$viewer$filters$ExclusionStateViewerFilter
         );
      }

      if (this.saveNetworkFilters) {
         this.saveFilters(
            "NetworkFilters",
            class$sancho$view$viewer$filters$NetworkViewerFilter == null
               ? (class$sancho$view$viewer$filters$NetworkViewerFilter = class$("sancho.view.viewer.filters.NetworkViewerFilter"))
               : class$sancho$view$viewer$filters$NetworkViewerFilter
         );
      }
   }

   public void saveFilters(String var1, Class var2) {
      boolean var3 = false;

      for (int var4 = 0; var4 < this.getFilters().length; var4++) {
         if (var2.isInstance(this.getFilters()[var4])) {
            this.saveGViewerFilter((AbstractViewerFilter)this.getFilters()[var4], var1);
            var3 = true;
         }
      }

      if (!var3) {
         this.saveEmptyGViewerFilter(var1);
      }
   }

   public void saveGViewerFilter(AbstractViewerFilter var1, String var2) {
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + var2, var1.getFiltered());
   }

   public void setActive(boolean var1) {
      this.active = var1;
      this.getContentProvider().setActive(var1);
   }

   public void resetDynamicColumn() {
      String var1 = this.dynamicColumn;
      this.dynamicColumn = "";
      this.setDynamicColumn(var1);
   }

   public void setDynamicColumn(int var1) {
      this.setDynamicColumn(String.valueOf(this.columnIDs.charAt(var1)));
   }

   public void setDynamicColumn(String var1) {
      if (!"gtk".equals(SWT.getPlatform()) && var1 != null && var1.length() <= 1) {
         if (this.columnIDs.indexOf(var1) != -1 && !this.dynamicColumn.equals(var1)) {
            this.dynamicColumn = var1;
            if (this.controlAdapter == null) {
               this.controlAdapter = new ControlAdapter() {
                  public void controlResized(ControlEvent var1) {
                     GView.this.resizeColumns();
                  }
               };
               this.getComposite().addControlListener(this.controlAdapter);
               this.resizeColumns();
            }
         } else {
            this.removeDynamicColumn();
         }
      }
   }

   public abstract void setInput();

   public void setMinDynamicColumnWidth(int var1) {
      this.minDynamicColumnWidth = var1;
   }

   public void setRefineString(String var1) {
      this.refineString = var1;
      this.updateRefineFilter();
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
      this.getContentProvider().setVisible(var1);
   }

   public boolean sortByColumn(int var1) {
      this.gSorter.setColumnIndex(var1);
      this.refresh();
      return this.gSorter.getDirection();
   }

   public void unsetInput() {
      this.sViewer.setInput(null);
   }

   public abstract void setLinesVisible(boolean var1);

   public abstract void setFont(Font var1);

   public void updateDisplay() {
      ((ICustomViewer)this.getViewer()).updateDisplay();
      this.setLinesVisible(PreferenceLoader.loadBoolean("displayGridLines"));
      this.setFont(PreferenceLoader.loadFont("tableFontData"));
      this.getTableLabelProvider().updateDisplay();
   }

   public void updateRefineFilter() {
      if (this.refineString.equals("")) {
         if (this.refineFilter != null) {
            this.removeFilter(this.refineFilter);
            this.refineFilter = null;
         }
      } else if (this.refineFilter == null) {
         this.refineFilter = new RefineFilter(this);
         this.addFilter(this.refineFilter);
      } else {
         this.refineFilter.update();
         this.refresh(true);
      }
   }

   public void onDisconnect() {
      this.getComposite().setMenu(null);
      if (this.popupMenu != null) {
         IContributionItem[] var1 = this.popupMenu.getItems();

         for (int var2 = 0; var2 < var1.length; var2++) {
            var1[var2].dispose();
         }

         this.popupMenu.removeAll();
         this.popupMenu.dispose();
         this.popupMenu = null;
      }
   }

   public void createMenu() {
      Composite var1 = this.getComposite();
      if (var1.getMenu() == null) {
         this.popupMenu = new MenuManager("");
         this.popupMenu.setRemoveAllWhenShown(true);
         this.popupMenu.addMenuListener(this.getTableMenuListener());
         var1.setMenu(this.popupMenu.createContextMenu(var1));
      }
   }

   public void onConnect() {
      this.createMenu();
   }

   public void widgetDisposed(DisposeEvent var1) {
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "DynamicColumn", this.dynamicColumn);
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "MinDynamicColumnWidth", this.minDynamicColumnWidth);
      this.saveFilters();
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
