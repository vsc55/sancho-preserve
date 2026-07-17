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

   public DownloadTreeLabelProvider(GView var1) {
      super(var1);
   }

   private void drawName(Event var1) {
      TreeItem var2 = (TreeItem)var1.item;
      Object var3 = var2.getData();
      if (var3 != null) {
         if (var3 instanceof File) {
            File var4 = (File)var3;
            Image var5 = var4.getAvgRatingImage();
            if (var5 != null) {
               Rectangle var6 = var2.getBounds(var1.index);
               Rectangle var7 = var5.getBounds();
               int var8 = var6.x + var6.width - 2 - var7.width;
               int var9 = var1.y + 1;
               var1.gc.drawImage(var5, var8, var9);
               var1.gc.drawText("is the time", var1.x + var1.width, var1.y + 1);
            }
         }
      }
   }

   private void drawChunks(Event var1) {
      ICustomViewer var2 = (ICustomViewer)this.gView.getViewer();
      if (var2.getEditors()) {
         TreeItem var3 = (TreeItem)var1.item;
         Rectangle var4 = var3.getBounds(var1.index);
         Object var5 = var3.getData();
         if (var5 != null) {
            ChunkImageData var6 = (ChunkImageData)this.chunkImageDataCache.get(var5);
            if (var6 == null) {
               File var7 = null;
               Client var8 = null;
               if (var5 instanceof File) {
                  var7 = (File)var5;
               } else if (var5 instanceof FileClient) {
                  FileClient var9 = (FileClient)var5;
                  var7 = var9.getFile();
                  var8 = var9.getClient();
               }

               var6 = new ChunkImageData(this.gView.getComposite().getDisplay(), var8, var7, null, true, var4.width, var1.height);
               this.chunkImageDataCache.put(var5, var6);
            }

            var6.drawTo(var4.width - this.margin, var1);
         }
      }
   }

   public void setUpOwnerDraw() {
      DownloadTreeLabelProvider$1 var1 = new DownloadTreeLabelProvider$1(this);
      this.gView.getViewer().getControl().addListener(42, var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      if (var1 instanceof File) {
         File var4 = (File)var1;
         switch (this.cViewer.getColumnIDs()[var2]) {
            case 1:
               return var4.getEnumNetwork().getImage();
            case 2:
               return var4.getFileTypeImage();
            case 11:
               return var4.getPriorityImage();
            case 18:
               return var4.getCommentImage();
            default:
               return null;
         }
      } else if (var1 instanceof FileClient) {
         Client var3 = ((FileClient)var1).getClient();
         switch (this.cViewer.getColumnIDs()[var2]) {
            case 0:
               return var3.getEnumNetwork().getImage();
            case 1:
               return var3.getEnumNetwork().getImage();
            case 2:
               return var3.getNameImage();
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
               return var3.getAddr().getImage();
            case 11:
               return var3.getSoftwareImage();
         }
      } else {
         return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      if (var1 instanceof File) {
         File var4 = (File)var1;
         switch (this.cViewer.getColumnIDs()[var2]) {
            case 0:
               return String.valueOf(var4.getId()).intern();
            case 1:
               return var4.getEnumNetwork().getName();
            case 2:
               return var4.getName();
            case 3:
               return var4.getSizeString();
            case 4:
               return var4.getDownloadedString();
            case 5:
               return var4.getPercentString();
            case 6:
               return var4.getSourcesString();
            case 7:
               return var4.getRelativeAvailString();
            case 8:
               return var4.getRateString();
            case 9:
               return String.valueOf(var4.getNumChunks()).intern();
            case 10:
               return var4.getEtaString();
            case 11:
               return var4.getPriorityString();
            case 12:
               return var4.getLastSeenString();
            case 13:
               return var4.getAgeString();
            case 14:
               return var4.getEta2String();
            case 15:
               return String.valueOf(var4.getNumClients()).intern();
            case 16:
               return String.valueOf(var4.getNumSources()).intern();
            case 17:
               return var4.getRemainingString();
            case 18:
               return var4.getNumCommentsString();
            case 19:
               return var4.getUser();
            case 20:
               return var4.getGroup();
            default:
               return "";
         }
      } else if (var1 instanceof FileClient) {
         Client var3 = ((FileClient)var1).getClient();
         switch (this.cViewer.getColumnIDs()[var2]) {
            case 0:
               return String.valueOf(var3.getId()).intern();
            case 1:
               return var3.getEnumNetwork().getName();
            case 2:
               return var3.getName();
            case 3:
               return var3.getModeString();
            case 4:
               return var3.getDownloadedString();
            case 5:
               return String.valueOf(var3.getPort()).intern();
            case 6:
               return var3.getAddr().toString();
            case 7:
            case 8:
            case 10:
            case 12:
            default:
               return "";
            case 9:
               return String.valueOf(var3.getNumChunks(((FileClient)var1).getFile().getId())).intern();
            case 11:
               return var3.getSoftware();
            case 13:
               return var3.getConnectedTimeString();
         }
      } else {
         return "";
      }
   }

   public Font getFont(Object var1, int var2) {
      if (var1 instanceof File) {
         File var3 = (File)var1;
         if (var3.getFileStateEnum() == EnumFileState.QUEUED) {
            return this.queuedFont;
         } else if (var3.getFileStateEnum() == EnumFileState.PAUSED) {
            return this.pausedFont;
         } else if (var3.getFileStateEnum() == EnumFileState.DOWNLOADED) {
            return this.downloadedFont;
         } else if (var3.getRate() / 1000.0F > 20.0F) {
            return this.rateAbove20Font;
         } else if (var3.getRate() / 1000.0F > 10.0F) {
            return this.rateAbove10Font;
         } else {
            return var3.getRate() / 1000.0F > 0.0F ? this.rateAbove0Font : null;
         }
      } else {
         return null;
      }
   }

   public Color getForeground(Object var1, int var2) {
      if (!this.displayColors) {
         return null;
      } else if (var1 instanceof File) {
         File var4 = (File)var1;
         if (this.indicateFakes && var4.containsFake()) {
            return this.containsFakeColor;
         } else if (var4.getFileStateEnum() == EnumFileState.QUEUED) {
            return this.queuedFileColor;
         } else if (var4.getFileStateEnum() == EnumFileState.PAUSED) {
            return this.pausedFileColor;
         } else if (var4.getFileStateEnum() == EnumFileState.DOWNLOADED) {
            return this.downloadedFileColor;
         } else if (var4.getRate() / 1000.0F > 20.0F) {
            return this.rateAbove20Color;
         } else if (var4.getRate() / 1000.0F > 10.0F) {
            return this.rateAbove10Color;
         } else if (var4.getRate() / 1000.0F > 0.0F) {
            return this.rateAbove0Color;
         } else {
            return var4.getRelativeAvail() == 0 ? this.unAvailableFileColor : this.availableFileColor;
         }
      } else if (var1 instanceof FileClient) {
         Client var3 = ((FileClient)var1).getClient();
         return var3.getStateEnum() == EnumHostState.CONNECTED_DOWNLOADING ? this.rateAbove0Color : null;
      } else {
         return null;
      }
   }

   public Color getBackground(Object var1, int var2) {
      if (this.alternateColors) {
         IGContentProvider var3 = (IGContentProvider)this.gView.getViewer().getContentProvider();
         if (var3 instanceof GTreeContentProvider) {
            Object var4 = var1;
            if (var1 instanceof FileClient) {
               var4 = ((FileClient)var1).getFile();
            }

            if (((GTreeContentProvider)var3).getSFIndex(var4) % 2 != 0) {
               return this.alternateColor;
            }
         }
      }

      return null;
   }

   public boolean isLabelProperty(Object var1, String var2) {
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

   // $VF: synthetic method
   static ICustomViewer access$000(DownloadTreeLabelProvider var0) {
      return var0.cViewer;
   }

   // $VF: synthetic method
   static void access$100(DownloadTreeLabelProvider var0, Event var1) {
      var0.drawChunks(var1);
   }
}
