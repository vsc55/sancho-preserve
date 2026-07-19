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

   public ChunkImageData(Display display, Client client, File file, Network network, boolean limitLength, int neededWidth, int neededHeight) {
      this.display = display;
      this.client = client;
      this.file = file;
      this.network = network;
      this.limitLength = limitLength || forceLimit;
      this.neededWidth = neededWidth;
      this.neededHeight = neededHeight - 2;
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
      int availLength = 0;
      if (this.avail != null) {
         availLength = this.avail.length();
      }

      if (availLength != 0 && this.chunks.length() == availLength) {
         Color systemColor = this.display.getSystemColor(2);
         Object color = null;
         int step = 1;
         if (this.limitLength && availLength > MAX_LENGTH) {
            step = availLength / MAX_LENGTH;
            availLength = MAX_LENGTH;
         }

         Image image = new Image(this.display, availLength, 14);
         GC gc = new GC(image);
         int charIndex = 0;
         int i = 0;
         Color runColor = null;
         int runStart = 0;

         for (int runWidth = 0; i < availLength; i++) {
            if (this.chunks.charAt(charIndex) == '2') {
               color = clientCColor2;
            } else {
               char availChar = this.avail.charAt(charIndex);
               if (availChar == '0') {
                  color = clientAColor0;
               } else if (availChar == '1') {
                  color = clientAColor1;
               } else if (availChar == '2') {
                  color = clientAColor2;
               } else {
                  color = clientAColor1;
               }
            }

            if (i == 0) {
               runColor = (Color)color;
            } else {
               runWidth++;
            }

            if (!runColor.equals(color)) {
               this.drawGradient(gc, systemColor, runColor, runStart, runWidth);
               runColor = (Color)color;
               runStart = i;
               runWidth = 0;
            }

            charIndex += step;
         }

         if (runStart < i) {
            int runWidth = i - runStart;
            this.drawGradient(gc, systemColor, runColor, runStart, runWidth);
         }

         this.imageData = this.mirrorImageData(image.getImageData());
         gc.dispose();
         image.dispose();
         if (this.resizedImageData == null) {
            this.resizedImageData = this.imageData;
         }

         this.resizeImage(true);
      } else {
         if (availLength != 0) {
            Sancho.pDebug("CCI [" + this.client.getId() + "]" + availLength + "!=" + this.chunks.length());
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

      int availLength = 0;
      if (this.avail != null) {
         availLength = this.avail.length();
      }

      if (availLength != 0 && availLength == this.chunks.length()) {
         char maxAvail = 0;

         for (int i = 0; i < availLength; i++) {
            char availChar = this.avail.charAt(i);
            if (availChar > maxAvail) {
               maxAvail = availChar;
            }
         }

         int step = 1;
         if (this.limitLength && availLength > MAX_LENGTH) {
            step = availLength / MAX_LENGTH;
            availLength = MAX_LENGTH;
         }

         Color systemColor = this.display.getSystemColor(2);
         Color color = null;
         Image image = new Image(this.display, availLength, 14);
         GC gc = new GC(image);
         ArrayList colors = new ArrayList();
         Color runColor = null;
         int charIndex = 0;
         int i = 0;
         int runStart = 0;

         for (int runWidth = 0; i < availLength; i++) {
            if (this.chunks.charAt(charIndex) == '2') {
               color = fileCColor2;
            } else if (this.chunks.charAt(charIndex) == '3') {
               color = fileCColor3;
            } else {
               char availChar = this.avail.charAt(charIndex);
               if (availChar == 0) {
                  color = fileAColor0;
               } else {
                  int intensity = (int)((float)availChar / (float)maxAvail * 10.0F);
                  intensity = 10 - intensity;
                  intensity = -intensity * 10;
                  Color gradientColor = WidgetFactory.changeColor(fileIRGB, intensity);
                  if (!gradientColor.equals(color)) {
                     colors.add(gradientColor);
                     color = gradientColor;
                  } else {
                     gradientColor.dispose();
                  }
               }
            }

            if (i == 0) {
               runColor = color;
            } else {
               runWidth++;
            }

            if (!runColor.equals(color)) {
               this.drawGradient(gc, systemColor, runColor, runStart, runWidth);
               runColor = color;
               runStart = i;
               runWidth = 0;
            }

            charIndex += step;
         }

         if (runStart < i) {
            int runWidth = i - runStart;
            this.drawGradient(gc, systemColor, runColor, runStart, runWidth);
         }

         for (int j = 0; j < colors.size(); j++) {
            Color listColor = (Color)colors.get(j);
            if (listColor != null && !listColor.isDisposed()) {
               listColor.dispose();
            }
         }

         this.imageData = this.mirrorImageData(image.getImageData());
         gc.dispose();
         image.dispose();
         image = new Image(this.display, this.imageData);
         gc = new GC(image);
         charIndex = 0;
         i = 0;
         gc.setLineWidth(1);
         gc.setForeground(fileCColor1);

         for (int height = image.getBounds().height; i < availLength; i++) {
            if (this.chunks.charAt(charIndex) == '1') {
               gc.drawRectangle(i, height - 2, 0, 2);
            }

            charIndex += step;
         }

         this.imageData = image.getImageData();
         gc.dispose();
         image.dispose();
         if (this.resizedImageData == null) {
            this.resizedImageData = this.imageData;
         }

         this.resizeImage(true);
      } else {
         if (availLength != 0) {
            Sancho.pDebug("CFI: " + availLength + "!=" + this.chunks.length());
         }
      }
   }

   private void drawGradient(GC gc, Color foreground, Color background, int x, int width) {
      gc.setBackground(background);
      gc.setForeground(foreground);
      gc.fillGradientRectangle(x, 0, width, 7, true);
   }

   private ImageData mirrorImageData(ImageData imageData) {
      int height = imageData.height;
      int srcOffset = 0;
      int destOffset = (height - 1) * imageData.bytesPerLine;

      for (int i = 0; i < height / 2; i++) {
         System.arraycopy(imageData.data, srcOffset, imageData.data, destOffset, imageData.bytesPerLine);
         srcOffset += imageData.bytesPerLine;
         destOffset -= imageData.bytesPerLine;
      }

      return imageData;
   }

   private void createProgressBar(GC gc) {
      int width = (int)((double)this.file.getPercent() / 100.0 * (double)(this.neededWidth - 1));
      gc.setBackground(progressColor2);
      gc.setForeground(progressColor1);
      gc.fillGradientRectangle(0, 0, width, 4, false);
   }

   private void percentText(GC gc) {
      if (this.neededWidth > 30) {
         gc.setFont(tableFont);
         Point fullExtent = gc.textExtent(SwissArmy.percentToString(100.0F));
         String percentString = this.file.getPercentString();
         Point textExtent = gc.textExtent(percentString);
         int x = this.neededWidth / 2 + fullExtent.x / 2;
         x -= textExtent.x;
         int y = (this.neededHeight - fullExtent.y) / 2;
         y = Math.max(y, 0);
         gc.setForeground(textOverlayColor);
         gc.drawString(percentString, x, y, true);
      }
   }

   private void roundCorners(GC gc, Color color) {
      int width = this.neededWidth;
      int height = this.neededHeight;
      gc.setForeground(color);
      byte offset = 0;
      gc.drawPoint(0, offset);
      gc.drawPoint(0, height - 1 - offset);
      gc.drawPoint(width - 1, offset);
      gc.drawPoint(width - 1, height - offset - 1);
   }

   protected synchronized void resizeImage(boolean force) {
      if (this.imageData != null) {
         int width = this.neededWidth;
         int height = this.neededHeight;
         int currentWidth = this.resizedImageData.width;
         int currentHeight = this.resizedImageData.height;
         if (width > 0 && height > 0 && (force || width != currentWidth || height != currentHeight)) {
            this.resizedImageData = this.imageData.scaledTo(width, height);
         }
      }
   }

   public synchronized void drawTo(int width, Event event) {
      this.neededWidth = width;
      if (this.neededWidth > 0) {
         if (this.hasChanged()) {
            this.createImage();
         }

         if (this.resizedImageData != null) {
            this.resizeImage(false);
            Image image = new Image(this.display, this.resizedImageData);
            GC gc = new GC(image);
            if (this.client == null) {
               this.createProgressBar(gc);
            }

            this.roundCorners(gc, event.gc.getBackground());
            if (chunkGraphPercent && this.client == null) {
               this.percentText(gc);
            }

            gc.dispose();

            try {
               event.gc.drawImage(image, event.x, event.y + 1);
            } catch (Exception exception) {
               Sancho.pDebug("e.width: " + event.width + " e.height: " + event.height + " bw: " + image.getBounds().width + " bh: " + image.getBounds().height);
               exception.printStackTrace();
            }

            image.dispose();
         } else {
            Image image = new Image(this.display, this.neededWidth, this.neededHeight);
            GC gc = new GC(image);
            int halfHeight = this.neededHeight / 2;
            int otherHalf = halfHeight;
            if (this.neededHeight % 2 != 0) {
               otherHalf = halfHeight + 1;
            }

            Color systemColor = this.display.getSystemColor(2);
            Color color = new Color(this.display, 62, 62, 62);
            gc.setBackground(color);
            gc.setForeground(systemColor);
            gc.fillGradientRectangle(0, 0, this.neededWidth, halfHeight, true);
            gc.setBackground(systemColor);
            gc.setForeground(color);
            gc.fillGradientRectangle(0, halfHeight, this.neededWidth, otherHalf, true);
            this.roundCorners(gc, event.gc.getBackground());
            if (chunkGraphPercent && this.client == null) {
               this.percentText(gc);
            }

            try {
               event.gc.drawImage(image, event.x, event.y + 1);
            } catch (Exception exception) {
               Sancho.pDebug("e.width: " + event.width + " e.height: " + event.height + " bw: " + image.getBounds().width + " bh: " + image.getBounds().height);
               exception.printStackTrace();
            }

            color.dispose();
            gc.dispose();
            image.dispose();
         }
      }
   }

   private boolean hasChanged() {
      boolean changed = false;
      if (this.client == null) {
         boolean chunksChanged = this.chunks.hashCode() != this.file.getChunks().hashCode();
         if (chunksChanged) {
            return true;
         }

         boolean availChanged = this.avail.hashCode() != this.file.getAvail().hashCode();
         changed = availChanged;
      } else {
         String availability = this.client.getFileAvailability(this.file.getId());
         if (this.avail == null && availability != null) {
            changed = true;
         } else if (this.avail != null && availability != null) {
            changed = availability.hashCode() != this.avail.hashCode();
         }
      }

      return changed;
   }
}
