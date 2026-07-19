package sancho.view.transfer.downloads;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.FileClient;
import sancho.view.viewer.GSorter;
import sancho.view.viewer.GView;

public class DownloadTreeSorter extends GSorter {
   private ITableLabelProvider labelProvider;
   private boolean maintainSortOrder = false;

   public DownloadTreeSorter(GView gView) {
      super(gView);
   }

   public int category(Object element) {
      if (element instanceof File) {
         return 1;
      } else {
         return element instanceof FileClient ? 2 : 3;
      }
   }

   private static int fileStateRank(EnumFileState fileState) {
      if (fileState == EnumFileState.DOWNLOADED) {
         return 0;
      } else if (fileState == EnumFileState.QUEUED) {
         return 1;
      } else {
         return fileState == EnumFileState.PAUSED ? 2 : 3;
      }
   }

   protected int _compare(Viewer viewer, Object first, Object second, int columnId) {
      int firstCategory = this.category(first);
      int secondCategory = this.category(second);
      if (firstCategory != secondCategory) {
         return firstCategory - secondCategory;
      } else if (first instanceof File) {
         File firstFile = (File)first;
         File secondFile = (File)second;
         switch (columnId) {
            case 0:
               return this.compareInts(firstFile.getId(), secondFile.getId());
            case 1:
               return this.compareStrings(firstFile.getEnumNetwork().getName(), secondFile.getEnumNetwork().getName());
            case 2:
               return this.compareStrings(firstFile.getName(), secondFile.getName());
            case 3:
               return this.compareLongs(firstFile.getSize(), secondFile.getSize());
            case 4:
               return this.compareLongs(firstFile.getDownloaded(), secondFile.getDownloaded());
            case 5:
               return this.comparePercents(firstFile.getPercent(), secondFile.getPercent());
            case 6:
               return this.compareInts(firstFile.getSources(), secondFile.getSources());
            case 7:
               return this.compareInts(firstFile.getRelativeAvail(), secondFile.getRelativeAvail());
            case 8: {
               // Rank by state (Downloaded < Queued < Paused < other), then break
               // ties by rate. The old constant returns (-1/2/3) gave the same sign
               // for both a,b and b,a on same-state pairs, violating the comparator
               // contract (TimSort abort with 2+ files sharing a state).
               int firstRank = fileStateRank(firstFile.getFileStateEnum());
               int secondRank = fileStateRank(secondFile.getFileStateEnum());
               return firstRank != secondRank ? firstRank - secondRank : this.comparePercents(firstFile.getRate(), secondFile.getRate());
            }
            case 9:
               return this.compareInts(firstFile.getNumChunks(), secondFile.getNumChunks());
            case 10: {
               // Both empty ETAs must compare equal, else compare(a,b)==compare(b,a)==1
               // breaks antisymmetry (same class fixed in case 8 / compareStrings).
               this.labelProvider = (ITableLabelProvider)((TreeViewer)viewer).getLabelProvider();
               boolean firstEtaEmpty = this.labelProvider.getColumnText(first, this.columnIndex).equals("");
               boolean secondEtaEmpty = this.labelProvider.getColumnText(second, this.columnIndex).equals("");
               if (firstEtaEmpty && secondEtaEmpty) {
                  return 0;
               } else if (firstEtaEmpty) {
                  return 1;
               } else if (secondEtaEmpty) {
                  return -1;
               } else {
                  return this.compareLongs(firstFile.getETA(), secondFile.getETA());
               }
            }
            case 11:
               return this.compareInts(firstFile.getPriority(), secondFile.getPriority());
            case 12:
               return this.compareInts(firstFile.getLastSeen(), secondFile.getLastSeen());
            case 13:
               return this.compareLongs(firstFile.getAge(), secondFile.getAge());
            case 14:
               return this.compareLongs(firstFile.getETA2(), secondFile.getETA2());
            case 15:
               return this.compareInts(firstFile.getNumClients(), secondFile.getNumClients());
            case 16:
               return this.compareInts(firstFile.getNumSources(), secondFile.getNumSources());
            case 17:
               return this.compareLongs(firstFile.getRemaining(), secondFile.getRemaining());
            case 18:
               return this.compareInts(firstFile.getNumComments(), secondFile.getNumComments());
            case 19:
               return this.compareStrings(firstFile.getUser(), secondFile.getUser());
            case 20:
               return this.compareStrings(firstFile.getGroup(), secondFile.getGroup());
            default:
               return 0;
         }
      } else {
         Client firstClient = ((FileClient)first).getClient();
         Client secondClient = ((FileClient)second).getClient();
         switch (columnId) {
            case 0:
               return this.compareInts(firstClient.getId(), secondClient.getId());
            case 1:
               return this.compareStrings(firstClient.getEnumNetwork().getName(), secondClient.getEnumNetwork().getName());
            case 2:
               return this.compareStrings(firstClient.getName(), secondClient.getName());
            case 3:
            case 11:
               this.labelProvider = (ITableLabelProvider)((TreeViewer)viewer).getLabelProvider();
               return this.compareStrings(this.labelProvider.getColumnText(first, this.columnIndex), this.labelProvider.getColumnText(second, this.columnIndex));
            case 4:
               return this.compareLongs(firstClient.getDownloaded(), secondClient.getDownloaded());
            case 5:
               return this.compareInts(firstClient.getPort(), secondClient.getPort());
            case 6:
               return this.compareAddrs(firstClient.getAddr(), secondClient.getAddr());
            case 7:
            case 8:
            case 10:
            case 12:
            default:
               return 0;
            case 9:
               FileClient firstFileClient = (FileClient)first;
               FileClient secondFileClient = (FileClient)second;
               return this.compareInts(firstClient.getNumChunks(firstFileClient.getFile().getId()), secondClient.getNumChunks(secondFileClient.getFile().getId()));
            case 13:
               return this.compareInts(firstClient.getConnectedTime(), secondClient.getConnectedTime());
         }
      }
   }

   public boolean isSorterProperty(Object element, String property) {
      if (this.maintainSortOrder && element instanceof File) {
         File file = (File)element;
         switch (this.cViewer.getColumnIDs()[this.columnIndex]) {
            case 4:
            case 17:
               return file.hasChangedBit(4);
            case 5:
               return file.hasChangedBit(32);
            case 6:
               return file.hasChangedBit(512);
            case 7:
               return file.hasChangedBit(2);
            case 8:
               return file.hasChangedBit(64) || file.hasChangedBit(128);
            case 9:
            case 11:
            case 13:
            case 14:
            case 15:
            case 16:
            default:
               return false;
            case 10:
               return file.hasChangedBit(8);
            case 12:
               return file.hasChangedBit(16);
            case 18:
               return file.hasChangedBit(2048);
         }
      } else {
         return false;
      }
   }

   public boolean sortOrder(int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
         case 1:
         case 2:
         case 10:
         case 12:
         case 14:
         case 17:
         case 19:
         case 20:
            return true;
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 11:
         case 13:
         case 15:
         case 16:
         case 18:
         default:
            return false;
      }
   }

   public void updateDisplay() {
      this.maintainSortOrder = PreferenceLoader.loadBoolean("maintainSortOrder");
   }
}
