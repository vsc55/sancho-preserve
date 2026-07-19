package sancho.view.downloadComplete;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Composite;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class DownloadCompleteTableView extends GTableView {
   public static final int NAME = 0;
   public static final int SIZE = 1;
   public static final int HASH = 2;
   public static final int DATE = 3;
   List itemList = new ArrayList();

   public DownloadCompleteTableView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "downloadComplete";
      this.columnLabels = new String[]{"downloadComplete.name", "downloadComplete.size", "downloadComplete.hash", "downloadComplete.date"};
      this.columnAlignment = new int[]{16384, 131072, 16384, 131072};
      this.columnDefaultWidths = new int[]{150, 75, 250, 200};
      this.gSorter = new DownloadCompleteTableSorter(this);
      this.tableContentProvider = new DownloadCompleteTableContentProvider(this);
      this.tableLabelProvider = new DownloadCompleteTableLabelProvider(this);
      this.tableMenuListener = new DownloadCompleteTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      this.parseList();
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((DownloadCompleteTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
      this.updateHeader();
   }

   public void setInput() {
      this.sViewer.setInput(this.itemList);
   }

   public void updateHeader() {
      this.getViewFrame().updateCLabelText(SResources.getString("l.downloadCompleteTitle") + ": " + this.getTable().getItemCount());
   }

   protected void parseList() {
      try {
         BufferedReader reader = new BufferedReader(new FileReader(VersionInfo.getDownloadLogFile()));

         String line;
         while ((line = reader.readLine()) != null) {
            DownloadCompleteItem item = new DownloadCompleteItem();
            if (item.parseLine(line)) {
               this.itemList.add(item);
            }
         }

         reader.close();
      } catch (FileNotFoundException fileNotFoundException) {
      } catch (IOException ioException) {
      }
   }
}
