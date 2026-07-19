package sancho.view.transfer.downloads;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import sancho.core.Sancho;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumExtension;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.ChunkCanvas;
import sancho.view.transfer.ChunkImageData;
import sancho.view.transfer.clients.ClientTableView;
import sancho.view.utility.WidgetFactory;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.GView;
import sancho.view.viewer.tree.GTreeView;

public class DownloadTreeView extends GTreeView implements ICellModifier {
   public static final String BASIC_COLUMNS = "ABCDFIK";
   public static final String NAME_COLUMN = "C";
   public static final String RATE_COLUMN = "I";
   public static final String CHUNK_COLUMN = "J";
   public static final int ID = 0;
   public static final int NETWORK = 1;
   public static final int NAME = 2;
   public static final int SIZE = 3;
   public static final int DOWNLOADED = 4;
   public static final int PERCENT = 5;
   public static final int SOURCES = 6;
   public static final int AVAIL = 7;
   public static final int RATE = 8;
   public static final int CHUNKS = 9;
   public static final int ETA = 10;
   public static final int PRIORITY = 11;
   public static final int LAST = 12;
   public static final int AGE = 13;
   public static final int ETA2 = 14;
   public static final int NUMCLIENTS = 15;
   public static final int NUMSOURCES = 16;
   public static final int REMAINING = 17;
   public static final int COMMENTS = 18;
   public static final int USER = 19;
   public static final int GROUP = 20;
   private CellEditor[] cellEditors = null;
   private CustomTreeViewer treeViewer;
   private GView clientView;

   public DownloadTreeView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "download";
      this.columnLabels = new String[]{
         "download.id",
         "download.network",
         "download.name",
         "download.size",
         "download.downloaded",
         "download.percent",
         "download.sources",
         "download.availability",
         "download.rate",
         "download.chunks",
         "download.eta",
         "download.priority",
         "download.last",
         "download.age",
         "download.eta2",
         "download.numClients",
         "download.numSources",
         "download.remaining",
         "download.comments",
         "download.user",
         "download.group"
      };
      this.columnAlignment = new int[]{
         16384,
         16384,
         16384,
         131072,
         131072,
         131072,
         131072,
         131072,
         131072,
         16384,
         131072,
         16384,
         131072,
         131072,
         131072,
         131072,
         131072,
         131072,
         131072,
         16384,
         16384
      };
      this.columnDefaultWidths = new int[]{50, 50, 250, 75, 75, 50, 50, 50, 50, 75, 75, 50, 75, 75, 75, 75, 75, 75, 75, 100, 100};
      this.validExtensions = new AbstractEnum[]{
         EnumExtension.AUDIO,
         EnumExtension.VIDEO,
         EnumExtension.ARCHIVE,
         EnumExtension.CDIMAGE,
         EnumExtension.IMAGE,
         EnumExtension.DOCUMENT,
         EnumExtension.PROGRAM,
         EnumExtension.EMULECOLLECTION
      };
      this.gSorter = new DownloadTreeSorter(this);
      this.treeContentProvider = new DownloadTreeContentProvider(this);
      this.tableLabelProvider = new DownloadTreeLabelProvider(this);
      this.tableMenuListener = new DownloadTreeMenuListener(this);
      this.saveExclusionStateFilters = true;
      StringBuffer columns = new StringBuffer();
      StringBuffer columnsOff = new StringBuffer();
      columns.append(PreferenceLoader.loadString(this.preferenceString + "TableColumns"));
      if (columns.indexOf("O") == -1) {
         columnsOff.append(PreferenceLoader.loadString(this.preferenceString + "TableColumnsOff"));
         if (columnsOff.indexOf("O") == -1) {
            columnsOff.append("O");
         }

         PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "TableColumnsOff", columnsOff.toString());
      }

      this.createContents(viewFrame.getChildComposite());
      ((DownloadTreeLabelProvider)this.tableLabelProvider).setUpOwnerDraw();
   }

   public boolean canModify(Object element, String property) {
      return element instanceof File;
   }

   public boolean clientsDisplayed() {
      return this.clientView != null ? ((DownloadViewFrame)this.viewFrame).getParentSashForm(false).getMaximizedControl() == null : false;
   }

   public void createColumns() {
      super.createColumns();
      this.treeViewer = this.getTreeViewer();
      this.treeViewer.setColumnProperties(this.columnLabels);
      if (this.cellEditors != null) {
         for (int i = 0; i < this.cellEditors.length; i++) {
            if (this.cellEditors[i] != null) {
               this.cellEditors[i].dispose();
            }
         }

         this.cellEditors = null;
      }

      if (this.columnIDs.indexOf("C") > 0) {
         this.cellEditors = new CellEditor[this.columnIDs.length()];
         this.cellEditors[this.columnIDs.indexOf("C")] = new TextCellEditor(this.getComposite());
      }
   }

   public void createContents(Composite parent) {
      super.createContents(parent);
      this.addMenuListener();
      this.treeViewer.addDoubleClickListener((DownloadTreeMenuListener)this.tableMenuListener);
      this.treeViewer.addSelectionChangedListener((DownloadTreeMenuListener)this.tableMenuListener);
   }

   public Object getValue(Object element, String property) {
      File file = (File)element;
      return file.getName();
   }

   public ViewFrame getViewFrame() {
      return this.viewFrame;
   }

   public void modify(Object element, String property, Object value) {
      TreeItem item = (TreeItem)element;
      File file = (File)item.getData();
      String newName = ((String)value).trim();
      if (newName.length() > 0) {
         file.rename(newName);
      }
   }

   public void setClientTableView(ClientTableView clientTableView) {
      this.clientView = clientTableView;
      ((DownloadTreeMenuListener)this.tableMenuListener).setClientView(clientTableView);
   }

   public void setInput() {
      if (Sancho.hasCollectionFactory()) {
         this.sViewer.setInput(this.getCore().getFileCollection());
      }
   }

   public void setPreferences() {
      if (PreferenceLoader.loadBoolean("tableCellEditors")) {
         this.treeViewer.setCellEditors(this.cellEditors);
         this.treeViewer.setCellModifier(this);
      } else {
         this.treeViewer.setCellEditors(null);
         this.treeViewer.setCellModifier(null);
      }

      this.setChunkGraphs();
   }

   public void setChunkGraphs() {
      boolean displayChunkGraphs = PreferenceLoader.loadBoolean("displayChunkGraphs");
      this.treeViewer.setChunksColumn(this.columnIDs.indexOf("J"));
      this.treeViewer.setEditors(displayChunkGraphs);
      ChunkCanvas.loadColors();
      ChunkImageData.loadColors();
      this.treeViewer.clearAll();
      this.getTree().clearAll(true);
   }

   public void toggleClientsTable() {
      if (this.clientView != null) {
         DownloadViewFrame downloadViewFrame = (DownloadViewFrame)this.viewFrame;
         boolean maximized = WidgetFactory.toggleMaximizedSashFormControl(downloadViewFrame.getParentSashForm(false), downloadViewFrame.getViewForm());
         this.updateClientsTable(!maximized);
      }
   }

   public void unsetInput() {
      super.unsetInput();
   }

   public void updateClientsTable(boolean show) {
      ((DownloadTreeMenuListener)this.tableMenuListener).updateClientsTable(show);
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.setPreferences();
      this.tableLabelProvider.updateDisplay();
      this.treeContentProvider.updateDisplay();
      this.gSorter.updateDisplay();
      if (this.treeViewer.getInput() != null) {
         this.treeViewer.refresh();
      }
   }
}
