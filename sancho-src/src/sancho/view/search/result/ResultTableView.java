package sancho.view.search.result;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumRating;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.viewer.filters.WordViewerFilter;
import sancho.view.viewer.table.GTableMenuListener;
import sancho.view.viewer.table.GTableView;

public class ResultTableView extends GTableView implements IDoubleClickListener {
   public static final int NETWORK = 0;
   public static final int NAME = 1;
   public static final int SIZE = 2;
   public static final int FORMAT = 3;
   public static final int MEDIA = 4;
   public static final int CODEC = 5;
   public static final int BITRATE = 6;
   public static final int LENGTH = 7;
   public static final int AVAILABILITY = 8;
   public static final int COMPLETE_SOURCES = 9;

   public ResultTableView(ResultViewFrame var1, CTabItem var2, AbstractTab var3) {
      super(var1);
      var2.setData("gView", this);
      this.preferenceString = "result";
      this.columnLabels = new String[]{
         "result.network",
         "result.name",
         "result.size",
         "result.format",
         "result.media",
         "result.codec",
         "result.bitrate",
         "result.length",
         "result.availability",
         "result.completeSources"
      };
      this.columnDefaultWidths = new int[]{60, 300, 65, 50, 50, 60, 60, 60, 90, 60};
      this.columnAlignment = new int[]{16384, 16384, 131072, 16384, 16384, 131072, 131072, 131072, 16384, 131072};
      this.validStates = new AbstractEnum[]{EnumRating.EXCELLENT, EnumRating.VERY_HIGH, EnumRating.HIGH, EnumRating.NORMAL, EnumRating.LOW};
      this.tableContentProvider = new ResultTableContentProvider(this);
      this.tableLabelProvider = new ResultTableLabelProvider(this);
      this.gSorter = new ResultTableSorter(this);
      this.tableMenuListener = new ResultTableMenuListener(this, var2);
      this.createContents(var1.getCTabFolder());
   }

   public void setInput() {
   }

   public void setInput(Object var1) {
      this.sViewer.setInput(var1);
   }

   public GTableMenuListener getMenuListener() {
      return this.tableMenuListener;
   }

   protected void createContents(Composite var1) {
      super.createContents(var1);
      this.sViewer.addSelectionChangedListener((ResultTableMenuListener)this.tableMenuListener);
      if (PreferenceLoader.loadBoolean("searchFilterPornography")) {
         this.sViewer.addFilter(new WordViewerFilter(2));
      } else if (PreferenceLoader.loadBoolean("searchFilterProfanity")) {
         this.sViewer.addFilter(new WordViewerFilter(1));
      }

      this.addMenuListener();
      this.getTableViewer().addDoubleClickListener(this);
      this.getTable().setToolTipText("");
      ((ResultViewFrame)this.getViewFrame()).onCTabFolderSelection();
   }

   public void doubleClick(DoubleClickEvent var1) {
      ((ResultTableMenuListener)this.getMenuListener()).downloadSelected();
   }
}
