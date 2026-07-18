package org.eclipse.jface.viewers;

import gnu.trove.TIntArrayList;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.downloads.DownloadTreeLabelProvider;
import sancho.view.viewer.table.GTableLabelProvider;
import sancho.view.viewer.tree.GTreeContentProvider;

public class CustomTreeViewer extends TreeViewer implements ICustomViewer {
   private Hashtable chunkImageDataCache = new Hashtable();
   private Hashtable parentToItemMap = new Hashtable();
   private Hashtable parentToChildrenMap = new Hashtable();
   private TIntArrayList expandedIndex = new TIntArrayList();
   private boolean activeEditors;
   private int chunksColumn = -1;
   private int[] columnIDs;
   private boolean followSelection;

   protected Object[] getRawChildren(Object var1) {
      GTreeContentProvider var2 = (GTreeContentProvider)this.getContentProvider();
      if (this.equals(var1, this.getRoot())) {
         Object[] var4 = null;
         if (var1 != null && var2 != null) {
            var4 = var2.getElements(var1);
         }

         return var4 != null ? var4 : new Object[0];
      } else {
         Object[] var3 = var2.getChildren(var1);
         return var3 != null ? var3 : new Object[0];
      }
   }

   public CustomTreeViewer(Composite var1, int var2) {
      super(var1, var2);
      Tree var3 = this.getTree();
      CustomTreeViewer$1 var4 = new CustomTreeViewer$1(this);
      var3.addTreeListener(var4);
   }

   protected void myHandleOpen(SelectionEvent var1) {
      super.handleOpen(var1);
   }

   protected void hookControl(Control var1) {
      var1.addDisposeListener(new CustomTreeViewer$2(this));
      OpenStrategy var2 = new OpenStrategy(var1);
      var2.addSelectionListener(new CustomTreeViewer$3(this));
      var2.addPostSelectionListener(new CustomTreeViewer$4(this));
      var2.addOpenListener(new CustomTreeViewer$5(this));
      this.addTreeListener(var1, new CustomTreeViewer$6(this));
      Tree var3 = (Tree)var1;
      var3.addMouseListener(new CustomTreeViewer$7(this));
      if ((var3.getStyle() & 268435456) != 0) {
         var3.addDisposeListener(new CustomTreeViewer$8(this));
         var3.addListener(36, new CustomTreeViewer$9(this));
      }
   }

   public boolean isExpanded(int var1) {
      return this.expandedIndex.contains(var1);
   }

   public void addExpanded(int var1) {
      if (!this.expandedIndex.contains(var1)) {
         this.expandedIndex.add(var1);
      }
   }

   public void removeExpanded(int var1) {
      int var2 = this.expandedIndex.indexOf(var1);
      if (var2 >= 0) {
         this.expandedIndex.remove(var2);
      }
   }

   public int[] getExpandedInts() {
      return this.expandedIndex.toNativeArray();
   }

   public void setExpandedInts(int[] var1) {
      this.expandedIndex.clear();
      this.expandedIndex.add(var1);
   }

   public void expandAll() {
      this.getTree().setRedraw(false);
      GTreeContentProvider var1 = (GTreeContentProvider)this.getContentProvider();
      int[] var2 = var1.getIndexesWithChildren();

      for (int var3 = 0; var3 < var2.length; var3++) {
         this.expand(var2[var3]);
      }

      this.getTree().setRedraw(true);
   }

   public void collapseAll() {
      this.getTree().setRedraw(false);
      Tree var1 = this.getTree();
      int var2 = var1.getItemCount();

      for (int var3 = 0; var3 < var2; var3++) {
         this.collapse(var3);
      }

      this.getTree().setRedraw(true);
   }

   public boolean getExpandedState(Object var1) {
      GTreeContentProvider var2 = (GTreeContentProvider)this.getContentProvider();
      int var3 = var2.getSFIndex(var1);
      return this.isExpanded(var3);
   }

   public void collapseToLevel(Object var1, int var2) {
      GTreeContentProvider var3 = (GTreeContentProvider)this.getContentProvider();
      int var4 = var3.getSFIndex(var1);
      if (var4 >= 0) {
         Tree var5 = this.getTree();
         int var6 = var5.getItemCount();
         if (var4 < var6) {
            TreeItem var7 = var5.getItem(var4);
            var7.setExpanded(false);
         }

         this.removeExpanded(var4);
      }
   }

   public void expand(int var1) {
      Tree var2 = this.getTree();
      int var3 = var2.getItemCount();
      if (var1 < var3) {
         TreeItem var4 = var2.getItem(var1);
         var4.setExpanded(true);
         this.addExpanded(var1);
      }
   }

   public void collapse(int var1) {
      if (this.isExpanded(var1)) {
         Tree var2 = this.getTree();
         int var3 = var2.getItemCount();
         if (var1 < var3) {
            TreeItem var4 = var2.getItem(var1);
            var4.setExpanded(false);
            this.removeExpanded(var1);
         }
      }
   }

   public void expandToLevel(Object var1, int var2) {
      GTreeContentProvider var3 = (GTreeContentProvider)this.getContentProvider();
      int var4 = var3.getSFIndex(var1);
      if (var4 >= 0 && var3.hasChildren(var1)) {
         this.expand(var4);
      }
   }

   public void refresh(Object var1) {
      this.preservingSelection(new CustomTreeViewer$10(this));
   }

   public void add(Object var1, Object[] var2) {
      this.preservingSelection(new CustomTreeViewer$11(this));
   }

   public void remove(Object[] var1) {
      this.removeFromCache(var1);
      this.preservingSelection(new CustomTreeViewer$12(this));
   }

   public void update(Object[] var1, String[] var2) {
      boolean var3 = false;

      for (int var4 = 0; var4 < var1.length; var4++) {
         if (this.myUpdate(var1[var4], var2)) {
            var3 = true;
         }
      }

      if (var3) {
         this.preservingSelection(new CustomTreeViewer$13(this));
      }
   }

   public void update(Object var1, String[] var2) {
   }

   public boolean myUpdate(Object var1, String[] var2) {
      TreeItem var3 = (TreeItem)this.parentToItemMap.get(var1);
      if (var3 == null) {
         return this.passesFilters(var1);
      } else {
         return this.failsFilters(var1) ? true : this.internalUpdate(var3, var1, var2);
      }
   }

   protected boolean internalUpdate(Item var1, Object var2, String[] var3) {
      boolean var4 = false;
      if (var3 != null) {
         for (int var5 = 0; var5 < var3.length; var5++) {
            var4 = this.needsRefilter(var2, var3[var5]);
            if (var4) {
               break;
            }
         }
      }

      boolean var8;
      if (var3 == null) {
         var8 = true;
      } else {
         var8 = false;
         IBaseLabelProvider var6 = this.getLabelProvider();

         for (int var7 = 0; var7 < var3.length; var7++) {
            var8 = var6.isLabelProperty(var2, var3[var7]);
            if (var8) {
               break;
            }
         }
      }

      if (var8) {
         this.doUpdateItem(var1, var2);
      }

      return var4;
   }

   public ISelection getSelection() {
      Control var1 = this.getControl();
      if (var1 != null && !var1.isDisposed()) {
         Item[] var2 = this.getSelection(var1);
         ArrayList var3 = new ArrayList(var2.length);
         TreeItem[] var4 = this.getTree().getItems();
         GTreeContentProvider var5 = (GTreeContentProvider)this.getContentProvider();
         int var6 = 0;

         for (int var7 = 0; var7 < var2.length; var7++) {
            TreeItem var8 = (TreeItem)var2[var7];
            if (var8.getParentItem() == null) {
               Object var11 = var8.getData();
               if (var11 != null) {
                  var3.add(var11);
               } else {
                  for (int var10 = var6; var10 < var4.length; var10++) {
                     if (var8 == var4[var10]) {
                        var3.add(var5.getSFElement(var10));
                        var6 = var10;
                        break;
                     }
                  }
               }
            } else {
               Object var9 = var8.getData();
               if (var9 != null) {
                  var3.add(var9);
               }
            }
         }

         return new StructuredSelection((List)var3);
      } else {
         return TreeSelection.EMPTY;
      }
   }

   protected void setSelectionToWidget(List var1, boolean var2) {
      if (var1 == null) {
         this.getTree().deselectAll();
      } else {
         this.virtualSetSelectionToWidget(var1, var2);
      }
   }

   protected void virtualSetSelectionToWidget(List var1, boolean var2) {
      int var3 = var1.size();
      if (var3 != 0) {
         Tree var4 = this.getTree();
         Object var5 = this.getRoot();
         ArrayList var6 = new ArrayList(var3);
         GTreeContentProvider var7 = (GTreeContentProvider)this.getContentProvider();
         TreeItem var8 = null;

         for (Object var10 : var1) {
            TreeItem var11 = (TreeItem)this.parentToItemMap.get(var10);
            if (var11 != null) {
               var6.add(var11);
            } else {
               int var12 = var7.getSFIndex(var10);
               if (var12 >= 0) {
                  var11 = var4.getItem(var12);
                  var6.add(var11);
                  if (var11.getData() == null) {
                     var7.updateElement(var11, var5, var12);
                  }
               }
            }

            if (var8 == null && var11 != null) {
               var8 = var11;
            }
         }

         if (var6.size() > 0) {
            TreeItem[] var13 = new TreeItem[var6.size()];
            var6.toArray(var13);
            if (this.followSelection) {
               var4.setSelection(var13);
            } else {
               var4.deselectAll();

               for (int var14 = 0; var14 < var13.length; var14++) {
                  var4.select(var13[var14]);
               }
            }
         } else {
            var4.deselectAll();
         }

         if (var2 && var8 != null) {
            var4.showItem(var8);
         }
      }
   }

   protected void preservingSelection(Runnable var1) {
      // See CustomTableViewer: delegate to the equivalent modern JFace method.
      super.preservingSelection(var1);
   }

   public synchronized void removeFromCache(Object[] var1) {
      for (int var2 = 0; var2 < var1.length; var2++) {
         this.removeFromCache(var1[var2]);
      }
   }

   public synchronized void removeFromCache(Object element) {
      this.chunkImageDataCache.remove(element);
      // The download tree caches its chunk images in the label provider, not here;
      // prune that (the real) map too so it doesn't grow without bound.
      IBaseLabelProvider labelProvider = this.getLabelProvider();
      if (labelProvider instanceof DownloadTreeLabelProvider) {
         ((DownloadTreeLabelProvider)labelProvider).removeFromCache(element);
      }
   }

   public void replace(Object var1, TreeItem var2) {
      TreeItem var3 = (TreeItem)this.parentToItemMap.get(var1);
      if (var3 != null && var3 != var2) {
         this.removeFromCache(var1);
         if (!var3.isDisposed()) {
            var3.setData(null);
            var3.setItemCount(0);
            Tree var4 = this.getTree();
            int var5 = var4.indexOf(var3);
            if (var5 != -1) {
               var4.clear(var5, true);
            }
         }
      }

      this.parentToItemMap.put(var1, var2);
      var2.setData(var1);
      this.doUpdateItem(var2, var1);
   }

   public void replaceChild(Object var1, Object var2, TreeItem var3) {
      Hashtable var4 = (Hashtable)this.parentToChildrenMap.get(var1);
      if (var4 == null) {
         var4 = new Hashtable();
         this.parentToChildrenMap.put(var1, var4);
      }

      TreeItem var5 = (TreeItem)var4.get(var2);
      if (var5 != null) {
         TreeItem var6 = var3.getParentItem();
         int var7 = var6.indexOf(var3);
         if (!var5.isDisposed()) {
            var6 = var5.getParentItem();
            var7 = var6.indexOf(var5);
         }

         if (var5 != var3 && !var5.isDisposed()) {
            var5.setData(null);
            var6 = var5.getParentItem();
            var7 = var6.indexOf(var5);
            if (var7 != -1) {
               var6.clear(var7, true);
            }
         }
      }

      var4.put(var2, var3);
      var3.setData(var2);
      this.doUpdateItem(var3, var2);
   }

   public void addChild(Object var1, Object var2) {
      GTreeContentProvider var3 = (GTreeContentProvider)this.getContentProvider();
      TreeItem var4 = (TreeItem)this.parentToItemMap.get(var1);
      if (var4 != null) {
         Hashtable var5 = (Hashtable)this.parentToChildrenMap.get(var1);
         if (var5 == null) {
            Object[] var6 = this.getSortedChildren(var1);
            var4.setItemCount(var6.length);
         } else {
            int var13 = var4.getItemCount();
            Object[] var7 = this.getSortedChildren(var1);
            int var8 = var7.length;
            if (var8 > var13) {
               int var9 = -1;

               for (int var10 = 0; var10 < var8; var10++) {
                  if (var7[var10] == var2) {
                     var9 = var10;
                     break;
                  }
               }

               if (var9 > -1 && var9 <= var13) {
                  new TreeItem(var4, var4.getStyle(), var9);
               } else {
                  var3 = (GTreeContentProvider)this.getContentProvider();
                  int var11 = var3.getSFIndex(var1);
                  if (var11 >= 0) {
                     this.myClear(-1, false, new int[]{var11}, new Object[]{var1});
                  }
               }
            }
         }
      }
   }

   public void removeChild(Object var1, Object var2) {
      Hashtable var3 = (Hashtable)this.parentToChildrenMap.get(var1);
      if (var3 != null) {
         TreeItem var4 = (TreeItem)var3.remove(var2);
         if (var4 != null) {
            if (var3.size() == 0) {
               this.parentToChildrenMap.remove(var1);
            }

            this.removeFromCache(var2);
            if (!var4.isDisposed()) {
               var4.setData(null);
               var4.dispose();
            }
         } else {
            GTreeContentProvider var5 = (GTreeContentProvider)this.getContentProvider();
            int var6 = var5.getSFIndex(var1);
            if (var6 >= 0) {
               this.myClear(-1, false, new int[]{var6}, new Object[]{var1});
            }
         }
      } else {
         GTreeContentProvider var7 = (GTreeContentProvider)this.getContentProvider();
         int var8 = var7.getSFIndex(var1);
         if (var8 >= 0) {
            this.myClear(-1, false, new int[]{var8}, new Object[]{var1});
         }
      }
   }

   public void setChildCount(TreeItem var1, int var2) {
      var1.setItemCount(var2);
   }

   public void setChildCount(Object var1, int var2) {
      TreeItem var3 = (TreeItem)this.parentToItemMap.get(var1);
      if (var3 != null) {
         var3.setItemCount(var2);
      }
   }

   protected boolean passesFilters(Object var1) {
      ViewerFilter[] var2 = this.getFilters();
      if (var2.length == 0) {
         return true;
      } else {
         Object var3 = this.getRoot();

         for (int var4 = 0; var4 < var2.length; var4++) {
            if (var2[var4].select(this, var3, var1)) {
               return true;
            }
         }

         return false;
      }
   }

   protected boolean failsFilters(Object var1) {
      ViewerFilter[] var2 = this.getFilters();
      Object var3 = this.getRoot();

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (!var2[var4].select(this, var3, var1)) {
            return true;
         }
      }

      return false;
   }

   protected void inputChanged(Object var1, Object var2) {
      this.refresh();
   }

   public void clearAll() {
      this.preservingSelection(new CustomTreeViewer$14(this));
   }

   public void myClear(int var1, boolean var2, int[] var3, Object[] var4) {
      Tree var5 = this.getTree();
      int var6 = var5.getItemCount();
      if (var1 < 0) {
         var1 = var6;
      }

      ArrayList var7 = new ArrayList();
      TIntArrayList var8 = new TIntArrayList();

      for (int var9 = 0; var9 < var4.length; var9++) {
         Object var10 = var4[var9];
         TreeItem var11 = (TreeItem)this.parentToItemMap.remove(var10);
         if (var11 != null) {
            this.removeFromCache(var10);
            var8.add(var3[var9]);
            var7.add(var11);
            var11.setData(null);
         } else if (!var2) {
            var8.add(var3[var9]);
         }

         Hashtable var12 = (Hashtable)this.parentToChildrenMap.remove(var4[var9]);
         if (var12 != null) {
            Object[] var13 = SwissArmy.toArray(var12.keySet());

            for (int var14 = 0; var14 < var13.length; var14++) {
               this.removeFromCache(var13[var14]);
               TreeItem var15 = (TreeItem)var12.get(var13[var14]);
               var15.setData(null);
            }
         }
      }

      if (var1 >= 0 && var6 != var1) {
         var5.setItemCount(var1);
      }

      int[] var16 = var8.toNativeArray();

      for (int var17 = 0; var17 < var16.length; var17++) {
         int var18 = var16[var17];
         if (var18 >= var1) {
            break;
         }

         if (var2) {
            TreeItem var19 = (TreeItem)var7.get(var17);
            var19.setExpanded(false);
            var19.setItemCount(0);
         }

         if (var18 < var1) {
            var5.clear(var18, true);
         }
      }

      // Explicitly render the visible top-level rows from the freshly-sorted content
      // provider, mirroring CustomTableViewer.myClear. The positional-diff clear above
      // relies on SWT lazy SetData re-firing, which the modernized SWT/JFace does not
      // do reliably, so top rows could otherwise render stale ("stuck") after a column
      // sort or filter switch. Bounded by the screen height (child rows keep using the
      // expand SetData path).
      GTreeContentProvider var20 = (GTreeContentProvider)this.getContentProvider();
      Object var21 = this.getInput();
      if (var21 != null) {
         int var22 = var5.getItemHeight();
         TreeItem var26 = var5.getTopItem();
         int var23 = var26 != null ? var5.indexOf(var26) : 0;
         if (var23 < 0) {
            var23 = 0;
         }

         int var24 = var22 > 0 ? var5.getClientArea().height / var22 + 2 : var1;

         for (int var25 = var23; var25 < var1 && var25 < var23 + var24; var25++) {
            var20.updateElement(var5.getItem(var25), var21, var25);
         }
      }
   }

   protected void myInternalVirtualRefreshAll() {
      ((GTreeContentProvider)this.getContentProvider()).updateSorted(true);
   }

   protected void myInternalVirtualRefreshSome() {
      ((GTreeContentProvider)this.getContentProvider()).updateSorted(false);
   }

   public void updateSelection(ISelection var1) {
      super.updateSelection(var1);
   }

   public void updateDisplay() {
      this.followSelection = PreferenceLoader.loadBoolean("followSelection");
   }

   public Object[] getSortedChildren(Object var1) {
      Object[] var2 = this.getFilteredChildren(var1);
      ViewerSorter var3 = this.getSorter();
      if (var3 != null) {
         var2 = (Object[])var2.clone();
         var3.sort(this, var2);
      }

      return var2;
   }

   public void setChunksColumn(int var1) {
      if (this.chunksColumn != var1) {
         this.chunksColumn = var1;
      }
   }

   public int getChunksColumn() {
      return this.chunksColumn;
   }

   public void setEditors(boolean var1) {
      this.activeEditors = var1;
   }

   public boolean getEditors() {
      return this.activeEditors;
   }

   public void setColumnIDs(String var1) {
      this.columnIDs = new int[var1.length()];

      for (int var2 = 0; var2 < var1.length(); var2++) {
         this.columnIDs[var2] = var1.charAt(var2) - 'A';
      }
   }

   public int[] getColumnIDs() {
      return this.columnIDs;
   }

   protected void doUpdateItem(Item var1, Object var2) {
      TreeItem var3 = (TreeItem)var1;
      GTableLabelProvider var4 = (GTableLabelProvider)this.getLabelProvider();
      int var5 = this.getTree().getColumnCount();
      boolean var6 = var4 instanceof ITableFontProvider;

      for (int var7 = 0; var7 < var5; var7++) {
         var3.setBackground(var7, var4.getBackground(var2, var7));
         var3.setForeground(var7, var4.getForeground(var2, var7));
         if (!this.activeEditors || var7 != this.chunksColumn) {
            if (var6) {
               var3.setFont(var7, ((ITableFontProvider)var4).getFont(var2, var7));
            }

            var3.setText(var7, var4.getColumnText(var2, var7));
            var3.setImage(var7, var4.getColumnImage(var2, var7));
         }
      }
   }
}
