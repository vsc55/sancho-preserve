package sancho.view.transfer.downloads;

import java.util.Hashtable;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.ChunkImageData;
import sancho.view.transfer.FileClient;
import sancho.view.viewer.GView;
import sancho.view.viewer.IGContentProvider;
import sancho.view.viewer.table.GTableLabelProvider;
import sancho.view.viewer.tree.GTreeContentProvider;

public class DownloadTreeLabelProvider extends GTableLabelProvider implements ITableFontProvider {
   private Color availableFileColor;
   private boolean displayColors = true;
   private Color downloadedFileColor;
   private Font downloadedFont;
   private Color pausedFileColor;
   private Font pausedFont;
   private Color queuedFileColor;
   private Font queuedFont;
   private Color rateAbove0Color;
   private Font rateAbove0Font;
   private Color rateAbove10Color;
   private Font rateAbove10Font;
   private Color rateAbove20Color;
   private Font rateAbove20Font;
   private Color unAvailableFileColor;
   private Color containsFakeColor;
   private boolean indicateFakes;
   private Hashtable chunkImageDataCache = new Hashtable();
   private int margin = SWT.getPlatform().equals("win32") ? 1 : 4;

   public DownloadTreeLabelProvider(GView gView) {
      super(gView);
   }

   private void drawName(Event event) {
      TreeItem item = (TreeItem)event.item;
      Object data = item.getData();
      if (data != null) {
         if (data instanceof File) {
            File file = (File)data;
            Image image = file.getAvgRatingImage();
            if (image != null) {
               Rectangle itemBounds = item.getBounds(event.index);
               Rectangle imageBounds = image.getBounds();
               int x = itemBounds.x + itemBounds.width - 2 - imageBounds.width;
               int y = event.y + 1;
               event.gc.drawImage(image, x, y);
            }
         }
      }
   }

   // Evict a stale element's cached chunk image. Called by the viewer when a download
   // is removed/replaced/cleared; without it this map grew unbounded (the viewer used to
   // prune a different, never-populated field).
   public void removeFromCache(Object element) {
      this.chunkImageDataCache.remove(element);
   }

   private void drawChunks(Event event) {
      ICustomViewer viewer = (ICustomViewer)this.gView.getViewer();
      if (viewer.getEditors()) {
         TreeItem item = (TreeItem)event.item;
         Rectangle bounds = item.getBounds(event.index);
         Object data = item.getData();
         if (data != null) {
            ChunkImageData chunkImageData = (ChunkImageData)this.chunkImageDataCache.get(data);
            if (chunkImageData == null) {
               File file = null;
               Client client = null;
               if (data instanceof File) {
                  file = (File)data;
               } else if (data instanceof FileClient) {
                  FileClient fileClient = (FileClient)data;
                  file = fileClient.getFile();
                  client = fileClient.getClient();
               }

               chunkImageData = new ChunkImageData(this.gView.getComposite().getDisplay(), client, file, null, true, bounds.width, event.height);
               this.chunkImageDataCache.put(data, chunkImageData);
            }

            chunkImageData.drawTo(bounds.width - this.margin, event);
         }
      }
   }

   public void setUpOwnerDraw() {
      this.gView.getViewer().getControl().addListener(42, new Listener() {
         public void handleEvent(Event event) {
            switch (DownloadTreeLabelProvider.this.cViewer.getColumnIDs()[event.index]) {
               case 9:
                  DownloadTreeLabelProvider.this.drawChunks(event);
            }
         }
      });
   }

   public Image getColumnImage(Object element, int columnIndex) {
      if (element instanceof File) {
         File file = (File)element;
         switch (this.cViewer.getColumnIDs()[columnIndex]) {
            case 1:
               return file.getEnumNetwork().getImage();
            case 2:
               return file.getFileTypeImage();
            case 11:
               return file.getPriorityImage();
            case 18:
               return file.getCommentImage();
            default:
               return null;
         }
      } else if (element instanceof FileClient) {
         Client client = ((FileClient)element).getClient();
         switch (this.cViewer.getColumnIDs()[columnIndex]) {
            case 0:
               return client.getEnumNetwork().getImage();
            case 1:
               return client.getEnumNetwork().getImage();
            case 2:
               return client.getNameImage();
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            default:
               return null;
            case 6:
               return client.getAddr().getImage();
            case 11:
               return client.getSoftwareImage();
         }
      } else {
         return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      if (element instanceof File) {
         File file = (File)element;
         switch (this.cViewer.getColumnIDs()[columnIndex]) {
            case 0:
               return String.valueOf(file.getId());
            case 1:
               return file.getEnumNetwork().getName();
            case 2:
               return file.getName();
            case 3:
               return file.getSizeString();
            case 4:
               return file.getDownloadedString();
            case 5:
               return file.getPercentString();
            case 6:
               return file.getSourcesString();
            case 7:
               return file.getRelativeAvailString();
            case 8:
               return file.getRateString();
            case 9:
               return String.valueOf(file.getNumChunks());
            case 10:
               return file.getEtaString();
            case 11:
               return file.getPriorityString();
            case 12:
               return file.getLastSeenString();
            case 13:
               return file.getAgeString();
            case 14:
               return file.getEta2String();
            case 15:
               return String.valueOf(file.getNumClients());
            case 16:
               return String.valueOf(file.getNumSources());
            case 17:
               return file.getRemainingString();
            case 18:
               return file.getNumCommentsString();
            case 19:
               return file.getUser();
            case 20:
               return file.getGroup();
            default:
               return "";
         }
      } else if (element instanceof FileClient) {
         Client client = ((FileClient)element).getClient();
         switch (this.cViewer.getColumnIDs()[columnIndex]) {
            case 0:
               return String.valueOf(client.getId());
            case 1:
               return client.getEnumNetwork().getName();
            case 2:
               return client.getName();
            case 3:
               return client.getModeString();
            case 4:
               return client.getDownloadedString();
            case 5:
               return String.valueOf(client.getPort());
            case 6:
               return client.getAddr().toString();
            case 7:
            case 8:
            case 10:
            case 12:
            default:
               return "";
            case 9:
               return String.valueOf(client.getNumChunks(((FileClient)element).getFile().getId()));
            case 11:
               return client.getSoftware();
            case 13:
               return client.getConnectedTimeString();
         }
      } else {
         return "";
      }
   }

   public Font getFont(Object element, int columnIndex) {
      if (element instanceof File) {
         File file = (File)element;
         if (file.getFileStateEnum() == EnumFileState.QUEUED) {
            return this.queuedFont;
         } else if (file.getFileStateEnum() == EnumFileState.PAUSED) {
            return this.pausedFont;
         } else if (file.getFileStateEnum() == EnumFileState.DOWNLOADED) {
            return this.downloadedFont;
         } else if (file.getRate() / 1000.0F > 20.0F) {
            return this.rateAbove20Font;
         } else if (file.getRate() / 1000.0F > 10.0F) {
            return this.rateAbove10Font;
         } else {
            return file.getRate() / 1000.0F > 0.0F ? this.rateAbove0Font : null;
         }
      } else {
         return null;
      }
   }

   public Color getForeground(Object element, int columnIndex) {
      if (!this.displayColors) {
         return null;
      } else if (element instanceof File) {
         File file = (File)element;
         if (this.indicateFakes && file.containsFake()) {
            return this.containsFakeColor;
         } else if (file.getFileStateEnum() == EnumFileState.QUEUED) {
            return this.queuedFileColor;
         } else if (file.getFileStateEnum() == EnumFileState.PAUSED) {
            return this.pausedFileColor;
         } else if (file.getFileStateEnum() == EnumFileState.DOWNLOADED) {
            return this.downloadedFileColor;
         } else if (file.getRate() / 1000.0F > 20.0F) {
            return this.rateAbove20Color;
         } else if (file.getRate() / 1000.0F > 10.0F) {
            return this.rateAbove10Color;
         } else if (file.getRate() / 1000.0F > 0.0F) {
            return this.rateAbove0Color;
         } else {
            return file.getRelativeAvail() == 0 ? this.unAvailableFileColor : this.availableFileColor;
         }
      } else if (element instanceof FileClient) {
         Client client = ((FileClient)element).getClient();
         return client.getStateEnum() == EnumHostState.CONNECTED_DOWNLOADING ? this.rateAbove0Color : null;
      } else {
         return null;
      }
   }

   public Color getBackground(Object element, int columnIndex) {
      if (this.alternateColors) {
         IGContentProvider contentProvider = (IGContentProvider)this.gView.getViewer().getContentProvider();
         if (contentProvider instanceof GTreeContentProvider) {
            Object file = element;
            if (element instanceof FileClient) {
               file = ((FileClient)element).getFile();
            }

            if (((GTreeContentProvider)contentProvider).getSFIndex(file) % 2 != 0) {
               return this.alternateColor;
            }
         }
      }

      return null;
   }

   public boolean isLabelProperty(Object element, String property) {
      return true;
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.displayColors = PreferenceLoader.loadBoolean("displayTableColors");
      this.unAvailableFileColor = PreferenceLoader.loadColor("downloadsUnAvailableFileColor");
      this.downloadedFileColor = PreferenceLoader.loadColor("downloadsDownloadedFileColor");
      this.queuedFileColor = PreferenceLoader.loadColor("downloadsQueuedFileColor");
      this.pausedFileColor = PreferenceLoader.loadColor("downloadsPausedFileColor");
      this.availableFileColor = PreferenceLoader.loadColor("downloadsAvailableFileColor");
      this.rateAbove0Color = PreferenceLoader.loadColor("downloadsRateAbove0FileColor");
      this.rateAbove10Color = PreferenceLoader.loadColor("downloadsRateAbove10FileColor");
      this.rateAbove20Color = PreferenceLoader.loadColor("downloadsRateAbove20FileColor");
      this.indicateFakes = PreferenceLoader.loadBoolean("dlIndicateFakes");
      this.containsFakeColor = PreferenceLoader.loadColor("downloadsContainsFakeColor");
      this.rateAbove20Font = PreferenceLoader.loadFont("downloadsRateAbove20FontData");
      this.rateAbove10Font = PreferenceLoader.loadFont("downloadsRateAbove10FontData");
      this.rateAbove0Font = PreferenceLoader.loadFont("downloadsRateAbove0FontData");
      this.pausedFont = PreferenceLoader.loadFont("downloadsPausedFontData");
      this.queuedFont = PreferenceLoader.loadFont("downloadsQueuedFontData");
      this.downloadedFont = PreferenceLoader.loadFont("downloadsDownloadedFontData");
   }
}
