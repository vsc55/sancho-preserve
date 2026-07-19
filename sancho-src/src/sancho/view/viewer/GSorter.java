package sancho.view.viewer;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.model.mldonkey.utility.Addr;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewer.table.GTableLabelProvider;

public abstract class GSorter extends ViewerSorter implements DisposeListener {
   protected int columnIndex;
   protected ICustomViewer cViewer;
   protected GView gView;
   protected int lastColumnIndex;
   protected boolean direction;
   protected boolean prevDirection;
   protected int prevColumnIndex;
   protected PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
   protected boolean workingDirection;
   protected static final boolean UP = true;
   protected static final boolean DOWN = false;

   public GSorter(GView gView) {
      this.gView = gView;
   }

   public int compare(Viewer viewer, Object object1, Object object2) {
      this.workingDirection = this.direction;
      int result = this._compare(viewer, object1, object2, this.cViewer.getColumnIDs()[this.columnIndex]);
      if (result == 0) {
         this.workingDirection = this.prevDirection;
         result = this._compare(viewer, object1, object2, this.cViewer.getColumnIDs()[this.prevColumnIndex]);
      }

      return result;
   }

   // The comparators read live model data (rate, %, downloaded, scores, …) that the
   // core thread mutates while a sort runs, so a comparator can be momentarily
   // inconsistent (an element's value changes between comparisons). Arrays.sort /
   // TimSort rejects that with "Comparison method violates its general contract"; a
   // stable merge sort tolerates it. Doing it here keeps that tolerance local to our
   // tables instead of needing the global -Djava.util.Arrays.useLegacyMergeSort flag.
   @Override
   public void sort(Viewer viewer, Object[] elements) {
      if (elements != null && elements.length > 1) {
         this.mergeSort(viewer, (Object[])elements.clone(), elements, 0, elements.length);
      }
   }

   private void mergeSort(Viewer viewer, Object[] src, Object[] dest, int low, int high) {
      int length = high - low;
      if (length < 7) {
         for (int i = low; i < high; i++) {
            for (int j = i; j > low && this.compare(viewer, dest[j - 1], dest[j]) > 0; j--) {
               Object temp = dest[j];
               dest[j] = dest[j - 1];
               dest[j - 1] = temp;
            }
         }
      } else {
         int mid = low + high >>> 1;
         this.mergeSort(viewer, dest, src, low, mid);
         this.mergeSort(viewer, dest, src, mid, high);
         if (this.compare(viewer, src[mid - 1], src[mid]) <= 0) {
            System.arraycopy(src, low, dest, low, length);
         } else {
            int i = low;
            int p = low;
            for (int q = mid; i < high; i++) {
               if (q >= high || p < mid && this.compare(viewer, src[p], src[q]) <= 0) {
                  dest[i] = src[p++];
               } else {
                  dest[i] = src[q++];
               }
            }
         }
      }
   }

   protected int _compare(Viewer viewer, Object object1, Object object2, int columnId) {
      return 0;
   }

   protected int compareAddrs(Addr addr1, Addr addr2) {
      return this.workingDirection ? addr1.compareTo(addr2) : addr2.compareTo(addr1);
   }

   protected int compareBooleans(boolean first, boolean second) {
      return this.workingDirection ? (first ? (second ? 0 : 1) : (second ? -1 : 0)) : (second ? (first ? 0 : 1) : (first ? -1 : 0));
   }

   protected int compareClientStates(Client client1, Client client2) {
      boolean downloading1 = client1.getStateEnum() == EnumHostState.CONNECTED_DOWNLOADING;
      boolean downloading2 = client2.getStateEnum() == EnumHostState.CONNECTED_DOWNLOADING;
      // Only when exactly one is downloading does it sort first. When both are
      // downloading, returning -1 (as before) for both a,b and b,a broke
      // antisymmetry (TimSort "violates its general contract"); fall through to
      // the rank/activity tie-break instead.
      if (downloading1 != downloading2) {
         return downloading1 ? -1 : 1;
      } else {
         int rank1 = client1.getState().getRank();
         int rank2 = client2.getState().getRank();
         return rank1 <= 0 && rank2 <= 0 ? this.compareStrings(client1.getDetailedClientActivity(), client2.getDetailedClientActivity()) : this.compareInts(rank1, rank2);
      }
   }

   protected int compareDefault(TableViewer tableViewer, int columnIndex, Object object1, Object object2) {
      GTableLabelProvider labelProvider = (GTableLabelProvider)tableViewer.getLabelProvider();
      String text1 = labelProvider.getColumnText(object1, columnIndex);
      String text2 = labelProvider.getColumnText(object2, columnIndex);
      return this.compareStrings(text1, text2);
   }

   protected int comparePercents(float percent1, float percent2) {
      if (percent1 == percent2) {
         return 0;
      } else {
         percent1 *= 100000.0F;
         percent2 *= 100000.0F;
         float difference = this.workingDirection ? percent1 - percent2 : percent2 - percent1;
         int result = (int)difference;
         if (difference >= 2.1474836E9F) {
            result = Integer.MAX_VALUE;
         }

         if (difference <= -2.1474836E9F) {
            result = Integer.MIN_VALUE;
         }

         return result;
      }
   }

   protected int compareInts(int first, int second) {
      // Integer.compare avoids the overflow of first - second when the operands
      // straddle a range > 2^31 (e.g. a negative score vs a large positive one),
      // which would violate the comparator contract.
      return this.workingDirection ? Integer.compare(first, second) : Integer.compare(second, first);
   }

   protected int compareLongs(long first, long second) {
      if (first == second) {
         return 0;
      } else {
         long difference = this.workingDirection ? first - second : second - first;
         int result = (int)difference;
         if (difference >= 2147483647L) {
            result = Integer.MAX_VALUE;
         }

         if (difference <= -2147483648L) {
            result = Integer.MIN_VALUE;
         }

         return result;
      }
   }

   protected int compareStrings(String text1, String text2) {
      if (text1.equals(text2)) {
         return 0;
      } else if (text1.equals("")) {
         return 1;
      } else if (text2.equals("")) {
         return -1;
      } else {
         return this.workingDirection ? text1.compareToIgnoreCase(text2) : text2.compareToIgnoreCase(text1);
      }
   }

   public int getLastColumnIndex() {
      return this.lastColumnIndex;
   }

   public boolean getDirection() {
      return this.direction;
   }

   public void initialize() {
      this.cViewer = (ICustomViewer)this.gView.getViewer();
      this.gView.getComposite().addDisposeListener(this);
      String lastColumn = PreferenceLoader.loadString(this.gView.getPreferenceString() + "LastSortColumn");
      String prevColumn = PreferenceLoader.loadString(this.gView.getPreferenceString() + "PrevSortColumn");
      if (!lastColumn.equals("") && this.gView.getColumnIDs().indexOf(lastColumn) != -1) {
         this.setColumnIndex(this.gView.getColumnIDs().indexOf(lastColumn));
         this.setDirection(PreferenceLoader.loadBoolean(this.gView.getPreferenceString() + "LastSortOrder"));
      }

      if (!prevColumn.equals("") && this.gView.getColumnIDs().indexOf(prevColumn) != -1) {
         this.prevColumnIndex = this.gView.getColumnIDs().indexOf(prevColumn);
         this.prevDirection = PreferenceLoader.loadBoolean(this.gView.getPreferenceString() + "PrevSortOrder");
      }
   }

   public boolean isSorterProperty(Object object, String property) {
      return true;
   }

   public void setColumnIndex(int columnIndex) {
      if (columnIndex != this.lastColumnIndex) {
         this.prevDirection = this.direction;
         this.prevColumnIndex = this.columnIndex;
      } else {
         this.prevDirection = !this.prevDirection;
      }

      this.columnIndex = columnIndex;
      this.direction = this.columnIndex == this.lastColumnIndex ? !this.direction : this.sortOrder(this.columnIndex);
      this.lastColumnIndex = this.columnIndex;
   }

   public void setDirection(boolean direction) {
      this.direction = direction;
   }

   public boolean sortOrder(int columnIndex) {
      return true;
   }

   public void updateDisplay() {
   }

   public void widgetDisposed(DisposeEvent event) {
      String preferenceString = this.gView.getPreferenceString();
      this.preferenceStore.setValue(preferenceString + "LastSortColumn", String.valueOf(this.gView.getColumnIDs().charAt(this.columnIndex)));
      this.preferenceStore.setDefault(preferenceString + "LastSortOrder", true);
      this.preferenceStore.setValue(preferenceString + "LastSortOrder", this.direction);
      this.preferenceStore.setValue(preferenceString + "PrevSortColumn", String.valueOf(this.gView.getColumnIDs().charAt(this.prevColumnIndex)));
      this.preferenceStore.setDefault(preferenceString + "PrevSortOrder", true);
      this.preferenceStore.setValue(preferenceString + "PrevSortOrder", this.prevDirection);
   }
}
