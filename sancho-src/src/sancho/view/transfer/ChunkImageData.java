package sancho.view.transfer;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import sancho.core.Sancho;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Network;
import sancho.utility.SwissArmy;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.WidgetFactory;

public class ChunkImageData {
   private static final int INITIAL_HEIGHT = 14;
   private static final int HALF_INITIAL_HEIGHT = 7;
   private static int MAX_LENGTH = 200;
   private static boolean chunkGraphPercent;
   private static final boolean forceLimit = !SWT.getPlatform().equals("win32");
   Display display;
   private String avail;
   private String chunks;
   private Client client;
   private File file;
   private Network network;
   private ImageData imageData;
   private ImageData resizedImageData;
   private boolean limitLength;
   private static Color clientAColor0;
   private static Color clientAColor1;
   private static Color clientAColor2;
   private static Color clientCColor2;
   private static Color fileCColor2;
   private static Color fileCColor3;
   private static Color fileAColor0;
   private static Color fileCColor1;
   private static RGB fileIRGB;
   private static Color progressColor1;
   private static Color progressColor2;
   private static Color textOverlayColor;
   private static Font tableFont;
   int neededWidth;
   int neededHeight;

   public static void loadColors() {
      clientCColor2 = PreferenceLoader.loadColor("chunkClientCColor2");
      clientAColor0 = PreferenceLoader.loadColor("chunkClientAColor0");
      clientAColor1 = PreferenceLoader.loadColor("chunkClientAColor1");
      clientAColor2 = PreferenceLoader.loadColor("chunkClientAColor2");
      fileCColor1 = PreferenceLoader.loadColor("chunkFileCColor1");
      fileCColor2 = PreferenceLoader.loadColor("chunkFileCColor2");
      fileCColor3 = PreferenceLoader.loadColor("chunkFileCColor3");
      fileAColor0 = PreferenceLoader.loadColor("chunkFileAColor0");
      fileIRGB = PreferenceLoader.loadRGB("chunkFileIRGB");
      progressColor1 = PreferenceLoader.loadColor("chunkProgress1");
      progressColor2 = PreferenceLoader.loadColor("chunkProgress2");
      textOverlayColor = PreferenceLoader.loadColor("chunkText");
      tableFont = PreferenceLoader.loadFont("tableFontData");
      MAX_LENGTH = PreferenceLoader.loadInt("maxChunkGraphLength");
      chunkGraphPercent = PreferenceLoader.loadBoolean("displayChunkGraphPercent");
   }

   public ChunkImageData(Display var1, Client var2, File var3, Network var4, boolean var5, int var6, int var7) {
      this.display = var1;
      this.client = var2;
      this.file = var3;
      this.network = var4;
      this.limitLength = var5 || forceLimit;
      this.neededWidth = var6;
      this.neededHeight = var7 - 2;
      this.createImage();
   }

   private synchronized void createImage() {
      if (this.client != null) {
         this.createClientImage();
      } else {
         this.createFileImage();
      }
   }

   private void createClientImage() {
      this.avail = this.client.getFileAvailability(this.file.getId());
      this.chunks = this.file.getChunks();
      int var1 = 0;
      if (this.avail != null) {
         var1 = this.avail.length();
      }

      if (var1 != 0 && this.chunks.length() == var1) {
         Color var2 = this.display.getSystemColor(2);
         Object var3 = null;
         int var4 = 1;
         if (this.limitLength && var1 > MAX_LENGTH) {
            var4 = var1 / MAX_LENGTH;
            var1 = MAX_LENGTH;
         }

         Image var5 = new Image(this.display, var1, 14);
         GC var6 = new GC(var5);
         int var7 = 0;
         int var8 = 0;
         Color var9 = null;
         int var10 = 0;

         for (int var11 = 0; var8 < var1; var8++) {
            if (this.chunks.charAt(var7) == '2') {
               var3 = clientCColor2;
            } else {
               char var12 = this.avail.charAt(var7);
               if (var12 == '0') {
                  var3 = clientAColor0;
               } else if (var12 == '1') {
                  var3 = clientAColor1;
               } else if (var12 == '2') {
                  var3 = clientAColor2;
               } else {
                  var3 = clientAColor1;
               }
            }

            if (var8 == 0) {
               var9 = (Color)var3;
            } else {
               var11++;
            }

            if (!var9.equals(var3)) {
               this.drawGradient(var6, var2, var9, var10, var11);
               var9 = (Color)var3;
               var10 = var8;
               var11 = 0;
            }

            var7 += var4;
         }

         if (var10 < var8) {
            int var14 = var8 - var10;
            this.drawGradient(var6, var2, var9, var10, var14);
         }

         this.imageData = this.mirrorImageData(var5.getImageData());
         var6.dispose();
         var5.dispose();
         if (this.resizedImageData == null) {
            this.resizedImageData = this.imageData;
         }

         this.resizeImage(true);
      } else {
         if (var1 != 0) {
            Sancho.pDebug("CCI [" + this.client.getId() + "]" + var1 + "!=" + this.chunks.length());
         }
      }
   }

   private void createFileImage() {
      this.chunks = this.file.getChunks();
      if (this.network != null) {
         if (this.file.hasAvails()) {
            this.avail = this.file.getAvails(this.network);
         } else {
            this.avail = this.file.getAvail();
         }
      } else {
         this.avail = this.file.getAvail();
      }

      int var1 = 0;
      if (this.avail != null) {
         var1 = this.avail.length();
      }

      if (var1 != 0 && var1 == this.chunks.length()) {
         char var3 = 0;

         for (int var4 = 0; var4 < var1; var4++) {
            char var2 = this.avail.charAt(var4);
            if (var2 > var3) {
               var3 = var2;
            }
         }

         int var5 = 1;
         if (this.limitLength && var1 > MAX_LENGTH) {
            var5 = var1 / MAX_LENGTH;
            var1 = MAX_LENGTH;
         }

         Color var6 = this.display.getSystemColor(2);
         Color var7 = null;
         Image var8 = new Image(this.display, var1, 14);
         GC var9 = new GC(var8);
         ArrayList var10 = new ArrayList();
         Color var11 = null;
         int var12 = 0;
         int var13 = 0;
         int var14 = 0;

         for (int var15 = 0; var13 < var1; var13++) {
            if (this.chunks.charAt(var12) == '2') {
               var7 = fileCColor2;
            } else if (this.chunks.charAt(var12) == '3') {
               var7 = fileCColor3;
            } else {
               char var18 = this.avail.charAt(var12);
               if (var18 == 0) {
                  var7 = fileAColor0;
               } else {
                  int var16 = (int)((float)var18 / (float)var3 * 10.0F);
                  var16 = 10 - var16;
                  var16 = -var16 * 10;
                  Color var17 = WidgetFactory.changeColor(fileIRGB, var16);
                  if (!var17.equals(var7)) {
                     var10.add(var17);
                     var7 = var17;
                  } else {
                     var17.dispose();
                  }
               }
            }

            if (var13 == 0) {
               var11 = var7;
            } else {
               var15++;
            }

            if (!var11.equals(var7)) {
               this.drawGradient(var9, var6, var11, var14, var15);
               var11 = var7;
               var14 = var13;
               var15 = 0;
            }

            var12 += var5;
         }

         if (var14 < var13) {
            int var23 = var13 - var14;
            this.drawGradient(var9, var6, var11, var14, var23);
         }

         for (int var26 = 0; var26 < var10.size(); var26++) {
            Color var27 = (Color)var10.get(var26);
            if (var27 != null && !var27.isDisposed()) {
               var27.dispose();
            }
         }

         this.imageData = this.mirrorImageData(var8.getImageData());
         var9.dispose();
         var8.dispose();
         var8 = new Image(this.display, this.imageData);
         var9 = new GC(var8);
         var12 = 0;
         var13 = 0;
         var9.setLineWidth(1);
         var9.setForeground(fileCColor1);

         for (int var28 = var8.getBounds().height; var13 < var1; var13++) {
            if (this.chunks.charAt(var12) == '1') {
               var9.drawRectangle(var13, var28 - 2, 0, 2);
            }

            var12 += var5;
         }

         this.imageData = var8.getImageData();
         var9.dispose();
         var8.dispose();
         if (this.resizedImageData == null) {
            this.resizedImageData = this.imageData;
         }

         this.resizeImage(true);
      } else {
         if (var1 != 0) {
            Sancho.pDebug("CFI: " + var1 + "!=" + this.chunks.length());
         }
      }
   }

   private void drawGradient(GC var1, Color var2, Color var3, int var4, int var5) {
      var1.setBackground(var3);
      var1.setForeground(var2);
      var1.fillGradientRectangle(var4, 0, var5, 7, true);
   }

   private ImageData mirrorImageData(ImageData var1) {
      int var2 = var1.height;
      int var3 = 0;
      int var4 = (var2 - 1) * var1.bytesPerLine;

      for (int var5 = 0; var5 < var2 / 2; var5++) {
         System.arraycopy(var1.data, var3, var1.data, var4, var1.bytesPerLine);
         var3 += var1.bytesPerLine;
         var4 -= var1.bytesPerLine;
      }

      return var1;
   }

   private void createProgressBar(GC var1) {
      int var2 = (int)((double)this.file.getPercent() / 100.0 * (double)(this.neededWidth - 1));
      var1.setBackground(progressColor2);
      var1.setForeground(progressColor1);
      var1.fillGradientRectangle(0, 0, var2, 4, false);
   }

   private void percentText(GC var1) {
      if (this.neededWidth > 30) {
         var1.setFont(tableFont);
         Point var2 = var1.textExtent(SwissArmy.percentToString(100.0F));
         String var3 = this.file.getPercentString();
         Point var4 = var1.textExtent(var3);
         int var5 = this.neededWidth / 2 + var2.x / 2;
         var5 -= var4.x;
         int var6 = (this.neededHeight - var2.y) / 2;
         var6 = Math.max(var6, 0);
         var1.setForeground(textOverlayColor);
         var1.drawString(var3, var5, var6, true);
      }
   }

   private void roundCorners(GC var1, Color var2) {
      int var3 = this.neededWidth;
      int var4 = this.neededHeight;
      var1.setForeground(var2);
      byte var5 = 0;
      var1.drawPoint(0, var5);
      var1.drawPoint(0, var4 - 1 - var5);
      var1.drawPoint(var3 - 1, var5);
      var1.drawPoint(var3 - 1, var4 - var5 - 1);
   }

   protected synchronized void resizeImage(boolean var1) {
      if (this.imageData != null) {
         int var2 = this.neededWidth;
         int var3 = this.neededHeight;
         int var4 = this.resizedImageData.width;
         int var5 = this.resizedImageData.height;
         if (var2 > 0 && var3 > 0 && (var1 || var2 != var4 || var3 != var5)) {
            this.resizedImageData = this.imageData.scaledTo(var2, var3);
         }
      }
   }

   public synchronized void drawTo(int var1, Event var2) {
      this.neededWidth = var1;
      if (this.neededWidth > 0) {
         if (this.hasChanged()) {
            this.createImage();
         }

         if (this.resizedImageData != null) {
            this.resizeImage(false);
            Image var3 = new Image(this.display, this.resizedImageData);
            GC var4 = new GC(var3);
            if (this.client == null) {
               this.createProgressBar(var4);
            }

            this.roundCorners(var4, var2.gc.getBackground());
            if (chunkGraphPercent && this.client == null) {
               this.percentText(var4);
            }

            var4.dispose();

            try {
               var2.gc.drawImage(var3, var2.x, var2.y + 1);
            } catch (Exception var11) {
               Sancho.pDebug("e.width: " + var2.width + " e.height: " + var2.height + " bw: " + var3.getBounds().width + " bh: " + var3.getBounds().height);
               var11.printStackTrace();
            }

            var3.dispose();
         } else {
            Image var12 = new Image(this.display, this.neededWidth, this.neededHeight);
            GC var13 = new GC(var12);
            int var5 = this.neededHeight / 2;
            int var6 = var5;
            if (this.neededHeight % 2 != 0) {
               var6 = var5 + 1;
            }

            Color var7 = this.display.getSystemColor(2);
            Color var8 = new Color(this.display, 62, 62, 62);
            var13.setBackground(var8);
            var13.setForeground(var7);
            var13.fillGradientRectangle(0, 0, this.neededWidth, var5, true);
            var13.setBackground(var7);
            var13.setForeground(var8);
            var13.fillGradientRectangle(0, var5, this.neededWidth, var6, true);
            this.roundCorners(var13, var2.gc.getBackground());
            if (chunkGraphPercent && this.client == null) {
               this.percentText(var13);
            }

            try {
               var2.gc.drawImage(var12, var2.x, var2.y + 1);
            } catch (Exception var10) {
               Sancho.pDebug("e.width: " + var2.width + " e.height: " + var2.height + " bw: " + var12.getBounds().width + " bh: " + var12.getBounds().height);
               var10.printStackTrace();
            }

            var8.dispose();
            var13.dispose();
            var12.dispose();
         }
      }
   }

   private boolean hasChanged() {
      boolean var1 = false;
      if (this.client == null) {
         boolean var2 = this.chunks.hashCode() != this.file.getChunks().hashCode();
         if (var2) {
            return true;
         }

         boolean var3 = this.avail.hashCode() != this.file.getAvail().hashCode();
         var1 = var3;
      } else {
         String var4 = this.client.getFileAvailability(this.file.getId());
         if (this.avail == null && var4 != null) {
            var1 = true;
         } else if (this.avail != null && var4 != null) {
            var1 = var4.hashCode() != this.avail.hashCode();
         }
      }

      return var1;
   }
}
