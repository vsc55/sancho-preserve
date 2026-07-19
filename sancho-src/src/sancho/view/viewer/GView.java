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

   public void setInput(Object input) {
      this.getViewer().setInput(input);
   }

   public void addFilter(ViewerFilter filter) {
      this.setRedraw(false);
      this.sViewer.addFilter(filter);
      this.setRedraw(true);
   }

   protected void addMenuListener() {
      Menu menu = this.getComposite().getMenu();
      if (menu != null) {
         menu.addMenuListener(new MenuAdapter() {
            public void menuShown(MenuEvent event) {
               Menu menu = GView.this.getComposite().getMenu();
               if (!GView.this.getViewer().getSelection().isEmpty() && menu.getItemCount() > 0) {
                  menu.setDefaultItem(menu.getItem(0));
               }
            }
         });
      }
   }

   public abstract int getItemCount();

   public abstract String getColumnText(int columnIndex);

   public abstract int getColumnCount();

   public abstract void deselectAll();

   public abstract void selectAll();

   public abstract Item getItemAt(int x, int y);

   protected abstract void disposeAllColumns();

   protected abstract void createColumns();

   public abstract IGContentProvider getContentProvider();

   protected void createContents() {
      this.sViewer.setUseHashlookup(true);
      this.allColumns = IDSelector.createIDString(this.columnLabels);
      Composite composite = this.getComposite();
      composite.setRedraw(false);
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
      composite.setRedraw(true);
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

   public AbstractViewerFilter getFilter(Class filterClass) {
      for (int i = 0; i < this.getFilters().length; i++) {
         if (filterClass.isInstance(this.getFilters()[i])) {
            return (AbstractViewerFilter)this.getFilters()[i];
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
      String column = PreferenceLoader.loadString(this.preferenceString + "DynamicColumn");
      if (!column.equals("")) {
         this.setDynamicColumn(column);
      }

      int width = PreferenceLoader.loadInt(this.preferenceString + "MinDynamicColumnWidth");
      if (width > 0) {
         this.setMinDynamicColumnWidth(width);
      }
   }

   public void loadExclusionStateFilters() {
      int filtered = PreferenceLoader.loadInt(this.preferenceString + "ExclusionStateFilters");
      if (filtered > 0) {
         ExclusionStateViewerFilter filter = new ExclusionStateViewerFilter(this);
         filter.setFiltered(filtered);
         this.addFilter(filter);
      }
   }

   public void swapFilters(String filters) {
      this.setRedraw(false);
      this.resetFilters();
      if (filters != null) {
         this.addFilters(filters);
      }

      this.setRedraw(true);
   }

   public String filtersToString() {
      String text = "";
      ViewerFilter[] filters = this.getFilters();

      for (int i = 0; i < filters.length; i++) {
         text = text + filters[i].toString();
      }

      return text;
   }

   public void addFilters(String filters) {
      StringTokenizer tokenizer = new StringTokenizer(filters, ",");

      while (tokenizer.hasMoreTokens()) {
         String idToken = tokenizer.nextToken();
         String value;
         if (tokenizer.hasMoreTokens()) {
            value = tokenizer.nextToken();
         } else {
            value = "";
         }

         int id;
         try {
            id = Integer.parseInt(idToken);
         } catch (NumberFormatException exception) {
            id = 0;
         }

         this.loadFilter(id, value);
      }
   }

   public static int filterToInt(ViewerFilter filter) {
      if (filter instanceof StateViewerFilter) {
         return 1;
      } else if (filter instanceof ExclusionStateViewerFilter) {
         return 2;
      } else if (filter instanceof NetworkViewerFilter) {
         return 3;
      } else if (filter instanceof RefineFilter) {
         return 5;
      } else {
         return filter instanceof FileExtensionViewerFilter ? 4 : 0;
      }
   }

   public void loadFilter(int id, String value) {
      int filtered = 0;

      try {
         filtered = Integer.parseInt(value);
      } catch (NumberFormatException exception) {
      }

      switch (id) {
         case 1:
            StateViewerFilter stateFilter = new StateViewerFilter(this);
            stateFilter.setFiltered(filtered);
            this.addFilter(stateFilter);
            break;
         case 2:
            ExclusionStateViewerFilter exclusionStateFilter = new ExclusionStateViewerFilter(this);
            exclusionStateFilter.setFiltered(filtered);
            this.addFilter(exclusionStateFilter);
            break;
         case 3:
            NetworkViewerFilter networkFilter = new NetworkViewerFilter(this);
            networkFilter.setFiltered(filtered);
            this.addFilter(networkFilter);
            break;
         case 4:
            FileExtensionViewerFilter fileExtensionFilter = new FileExtensionViewerFilter(this);
            fileExtensionFilter.setFiltered(filtered);
            this.addFilter(fileExtensionFilter);
            break;
         case 5:
            this.refineString = value;
            this.viewFrame.setRefineText(value);
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
      int filtered = PreferenceLoader.loadInt(this.preferenceString + "NetworkFilters");
      if (filtered > 0) {
         NetworkViewerFilter filter = new NetworkViewerFilter(this);
         filter.setFiltered(filtered);
         this.addFilter(filter);
      }
   }

   public void loadStateFilters() {
      int filtered = PreferenceLoader.loadInt(this.preferenceString + "StateFilters");
      if (filtered > 0) {
         StateViewerFilter filter = new StateViewerFilter(this);
         filter.setFiltered(filtered);
         this.addFilter(filter);
      }
   }

   public void setRedraw(boolean redraw) {
      if (this.forceRedraw) {
         this.getComposite().setRedraw(redraw);
      }
   }

   public void refresh() {
      this.sViewer.refresh();
   }

   public void refresh(boolean redraw) {
      if (redraw) {
         this.setRedraw(false);
      }

      this.refresh();
      if (redraw) {
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

   public void removeFilter(ViewerFilter filter) {
      this.setRedraw(false);
      this.sViewer.removeFilter(filter);
      this.setRedraw(true);
   }

   public void clearAll() {
      ICustomViewer viewer = (ICustomViewer)this.getViewer();
      viewer.clearAll();
   }

   public void resetColumns() {
      ICustomViewer viewer = (ICustomViewer)this.getViewer();
      Object input = viewer.getInput();
      viewer.setInput(null);
      viewer.setEditors(false);
      this.gSorter.setColumnIndex(0);
      this.createColumns();
      viewer.setInput(input);
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

   public abstract int getColumnWidthsExcept(int columnIndex);

   public abstract void setColumnWidth(int columnIndex, int width);

   public void resizeColumns() {
      Composite composite = this.getComposite();
      if (composite != null && !composite.isDisposed() && this.dynamicColumn != null && !this.dynamicColumn.equals("")) {
         int compositeWidth = composite.getBounds().width;
         ScrollBar scrollBar = composite.getVerticalBar();
         boolean hasScrollBar = scrollBar != null ? scrollBar.getThumb() < scrollBar.getMaximum() : false;
         if (this.oldTableWidth != compositeWidth || hasScrollBar != this.oldTableScrollBar) {
            this.oldTableWidth = compositeWidth;
            this.oldTableScrollBar = hasScrollBar;
            ICustomViewer viewer = (ICustomViewer)this.getViewer();
            int totalWidth = 0;
            int dynamicColumnID = this.dynamicColumn.charAt(0) - 'A';
            int[] columnIDs = viewer.getColumnIDs();
            int dynamicColumnIndex = -1;

            for (int i = 0; i < columnIDs.length; i++) {
               if (dynamicColumnID == columnIDs[i]) {
                  dynamicColumnIndex = i;
               }
            }

            if (this.getColumnCount() == columnIDs.length && dynamicColumnIndex != -1) {
               totalWidth = this.getColumnWidthsExcept(dynamicColumnIndex);
               if (composite instanceof Tree) {
                  totalWidth++;
               }

               if (hasScrollBar) {
                  totalWidth += scrollBar.getSize().x;
               }

               try {
                  this.setColumnWidth(dynamicColumnIndex, Math.max(this.minDynamicColumnWidth, compositeWidth - totalWidth));
               } catch (Exception exception) {
                  exception.printStackTrace();
               }
            }
         }
      }
   }

   public void saveEmptyGViewerFilter(String filterName) {
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + filterName, 0);
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

   public void saveFilters(String filterName, Class filterClass) {
      boolean saved = false;

      for (int i = 0; i < this.getFilters().length; i++) {
         if (filterClass.isInstance(this.getFilters()[i])) {
            this.saveGViewerFilter((AbstractViewerFilter)this.getFilters()[i], filterName);
            saved = true;
         }
      }

      if (!saved) {
         this.saveEmptyGViewerFilter(filterName);
      }
   }

   public void saveGViewerFilter(AbstractViewerFilter filter, String filterName) {
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + filterName, filter.getFiltered());
   }

   public void setActive(boolean active) {
      this.active = active;
      this.getContentProvider().setActive(active);
   }

   public void resetDynamicColumn() {
      String column = this.dynamicColumn;
      this.dynamicColumn = "";
      this.setDynamicColumn(column);
   }

   public void setDynamicColumn(int columnIndex) {
      this.setDynamicColumn(String.valueOf(this.columnIDs.charAt(columnIndex)));
   }

   public void setDynamicColumn(String column) {
      if (!"gtk".equals(SWT.getPlatform()) && column != null && column.length() <= 1) {
         if (this.columnIDs.indexOf(column) != -1 && !this.dynamicColumn.equals(column)) {
            this.dynamicColumn = column;
            if (this.controlAdapter == null) {
               this.controlAdapter = new ControlAdapter() {
                  public void controlResized(ControlEvent event) {
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

   public void setMinDynamicColumnWidth(int width) {
      this.minDynamicColumnWidth = width;
   }

   public void setRefineString(String refineString) {
      this.refineString = refineString;
      this.updateRefineFilter();
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
      this.getContentProvider().setVisible(visible);
   }

   public boolean sortByColumn(int columnIndex) {
      this.gSorter.setColumnIndex(columnIndex);
      this.refresh();
      return this.gSorter.getDirection();
   }

   public void unsetInput() {
      this.sViewer.setInput(null);
   }

   public abstract void setLinesVisible(boolean visible);

   public abstract void setFont(Font font);

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
         IContributionItem[] items = this.popupMenu.getItems();

         for (int i = 0; i < items.length; i++) {
            items[i].dispose();
         }

         this.popupMenu.removeAll();
         this.popupMenu.dispose();
         this.popupMenu = null;
      }
   }

   public void createMenu() {
      Composite composite = this.getComposite();
      if (composite.getMenu() == null) {
         this.popupMenu = new MenuManager("");
         this.popupMenu.setRemoveAllWhenShown(true);
         this.popupMenu.addMenuListener(this.getTableMenuListener());
         composite.setMenu(this.popupMenu.createContextMenu(composite));
      }
   }

   public void onConnect() {
      this.createMenu();
   }

   public void widgetDisposed(DisposeEvent event) {
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "DynamicColumn", this.dynamicColumn);
      PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "MinDynamicColumnWidth", this.minDynamicColumnWidth);
      this.saveFilters();
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }
}
