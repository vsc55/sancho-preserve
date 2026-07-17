package sancho.view.viewer.tree;

import gnu.trove.TIntArrayList;
import java.util.ArrayList;
import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TreeItem;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.viewer.GView;
import sancho.view.viewer.IGContentProvider;

public abstract class GTreeContentProvider implements IGContentProvider, ITreeContentProvider, ILazyTreeContentProvider, MyObserver {
   protected static final Object[] EMPTY_ARRAY = new Object[0];
   protected GView gView;
   protected boolean needsRefresh;
   protected CustomTreeViewer treeViewer;
   protected ArrayList sfList = new ArrayList();

   public void setNeedsRefresh(boolean var1) {
      this.needsRefresh = var1;
   }

   public synchronized Object getSFElement(int var1) {
      return this.sfList.get(var1);
   }

   public synchronized int getSFIndex(Object var1) {
      return this.sfList.indexOf(var1);
   }

   public ArrayList getOldExpanded() {
      ArrayList var1 = new ArrayList();
      int[] var2 = this.treeViewer.getExpandedInts();

      for (int var3 = 0; var3 < var2.length; var3++) {
         var1.add(this.getSFElement(var2[var3]));
      }

      return var1;
   }

   public void setOldExpanded(ArrayList var1) {
      TIntArrayList var2 = new TIntArrayList();

      for (Object var4 : var1) {
         int var5 = this.getSFIndex(var4);
         if (var5 >= 0) {
            var2.add(var5);
         }
      }

      this.treeViewer.setExpandedInts(var2.toNativeArray());
   }

   public synchronized int[] getAllNumChildren() {
      Object[] var1 = this.sfList.toArray();
      int[] var2 = new int[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3] = this.getNumChildren(var1[var3]);
      }

      return var2;
   }

   public synchronized void updateSorted(boolean var1) {
      ArrayList var2 = this.getOldExpanded();
      ArrayList var3 = this.sfList;
      Object[] var4 = this.treeViewer.getSortedChildren(this.treeViewer.getInput());
      int var5 = var4.length;
      this.sfList = new ArrayList(var5);

      for (int var6 = 0; var6 < var5; var6++) {
         this.sfList.add(var4[var6]);
      }

      this.setOldExpanded(var2);
      TIntArrayList var7 = new TIntArrayList();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      for (Object var11 : var3) {
         if (var9 >= var5 || var4[var9] != var11 || var1) {
            var8.add(var11);
            var7.add(var9);
         }

         var9++;
      }

      this.treeViewer.myClear(var5, true, var7.toNativeArray(), var8.toArray());
   }

   public GTreeContentProvider(GView var1) {
      this.gView = var1;
   }

   public void dispose() {
   }

   public Object[] getChildren(Object var1) {
      return EMPTY_ARRAY;
   }

   public Object getParent(Object var1) {
      return null;
   }

   public void updateChildCount(Object var1, int var2) {
   }

   public void updateChild(Object var1) {
      int var2 = this.getSFIndex(var1);
      if (var2 >= 0) {
         if (this.treeViewer.isExpanded(var2)) {
            this.treeViewer.myClear(-1, false, new int[]{var2}, new Object[]{var1});
         } else {
            Object[] var3 = this.treeViewer.getSortedChildren(var1);
            this.treeViewer.setChildCount(var1, var3.length);
         }
      }
   }

   public synchronized int[] getIndexesWithChildren() {
      TIntArrayList var1 = new TIntArrayList();
      int var2 = 0;

      for (Object var4 : this.sfList) {
         if (this.hasChildren(var4)) {
            var1.add(var2);
         }

         var2++;
      }

      return var1.toNativeArray();
   }

   public void updateElement(Object var1, int var2) {
   }

   public void updateElement(TreeItem var1, Object var2, int var3) {
      if (var2 == this.treeViewer.getInput()) {
         Object var4 = this.getSFElement(var3);
         this.treeViewer.replace(var4, var1);
         boolean var5 = this.treeViewer.isExpanded(var3);
         if (this.hasChildren(var4)) {
            Object[] var6 = this.treeViewer.getSortedChildren(var4);
            this.treeViewer.setChildCount(var1, var6.length);
         } else {
            this.treeViewer.setChildCount(var1, 0);
         }

         this.treeViewer.replace(var4, var1);
         if (var1.getExpanded() != var5) {
            var1.setExpanded(var5);
         }
      } else if (this.hasChildren(var2)) {
         Object[] var7 = this.treeViewer.getSortedChildren(var2);
         if (var3 < var7.length) {
            int var8 = this.getSFIndex(var2);
            if (var8 >= 0) {
               Object var9 = var7[var3];
               this.treeViewer.replaceChild(var2, var9, var1);
            }
         }
      }
   }

   public boolean hasChildren(Object var1) {
      return false;
   }

   public abstract int getNumChildren(Object var1);

   public void initialize() {
      this.treeViewer = ((GTreeView)this.gView).getTreeViewer();
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      if (var1 instanceof CustomTreeViewer) {
         this.treeViewer = (CustomTreeViewer)var1;
      }
   }

   public void setActive(boolean var1) {
      if (var1 && this.needsRefresh) {
         this.gView.clearAll();
         this.needsRefresh = false;
      }
   }

   public void setVisible(boolean var1) {
      if (var1 && this.needsRefresh) {
         this.gView.clearAll();
         this.needsRefresh = false;
      }
   }

   public abstract void onUpdate(MyObservable var1, Object var2, int var3);

   public void update(MyObservable var1, Object var2, int var3) {
      if (this.gView != null && !this.gView.isDisposed()) {
         if (this.gView.isVisible() && this.gView.isActive()) {
            this.gView.getComposite().getDisplay().syncExec(new GTreeContentProvider$1(this, var1, var2, var3));
         } else {
            this.needsRefresh = true;
         }
      }
   }

   public void updateDisplay() {
   }
}
