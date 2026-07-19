package sancho.view.transfer;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import sancho.core.Sancho;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.Network;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.WidgetFactory;
import sancho.view.utility.dialogs.ChunkColorDialog;

public class ChunkCanvas extends Canvas implements MyObserver, DisposeListener, PaintListener, Runnable {
   private static final int INITIAL_HEIGHT = 18;
   private static final int HALF_INITIAL_HEIGHT = 9;
   private static int MAX_LENGTH = 200;
   private static final boolean forceLimit = !SWT.getPlatform().equals("win32");
   private static List chunkList = new ArrayList();
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
   private String avail;
   private String chunks;
   private Client client;
   private File file;
   private Network network;
   private ImageData imageData;
   private ImageData resizedImageData;
   private boolean limitLength;
   private Rectangle rectangle;

   public static void loadColors() {
      clientCColor2 = PreferenceLoader.loadColor("chunkClientCColor2");
      clientAColor0 = PreferenceLoader.loadColor("chunkClientAColor0");
      clientAColor1 = PreferenceLoader.loadColor("chunkClientAColor1");
      clientAColor2 = PreferenceLoader.loadColor("chunkClientAColor2");
      fileCColor2 = PreferenceLoader.loadColor("chunkFileCColor2");
      fileCColor3 = PreferenceLoader.loadColor("chunkFileCColor3");
      fileAColor0 = PreferenceLoader.loadColor("chunkFileAColor0");
      fileCColor1 = PreferenceLoader.loadColor("chunkFileCColor1");
      fileIRGB = PreferenceLoader.loadRGB("chunkFileIRGB");
      progressColor1 = PreferenceLoader.loadColor("chunkProgress1");
      progressColor2 = PreferenceLoader.loadColor("chunkProgress2");
      MAX_LENGTH = PreferenceLoader.loadInt("maxChunkGraphLength");
   }

   public static void refreshAll() {
      synchronized (chunkList) {
         for (int i = 0; i < chunkList.size(); i++) {
            ((ChunkCanvas)chunkList.get(i)).refresh(true);
         }
      }
   }

   public ChunkCanvas(Composite composite, int style, Client client, File file, Network network, boolean limitLength) {
      super(composite, style);
      this.client = client;
      this.file = file;
      this.network = network;
      this.limitLength = limitLength || forceLimit;
      if (client != null) {
         client.addObserver(this);
      } else if (file != null) {
         file.addObserver(this);
      }

      this.createImage();
      this.addDisposeListener(this);
      this.addPaintListener(this);
      final Composite parentComposite = composite;
      this.addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick(MouseEvent event) {
            ChunkColorDialog dialog = new ChunkColorDialog(parentComposite.getShell());
            if (dialog.open() == 0) {
               ChunkCanvas.refreshAll();
            }
         }
      });
      this.addControlListener(new ControlAdapter() {
         public void controlResized(ControlEvent event) {
            ChunkCanvas.this.resizeImage(event, false);
         }
      });
      synchronized (chunkList) {
         chunkList.add(this);
      }
   }

   private void createClientImage() {
      this.avail = this.client.getFileAvailability(this.file.getId());
      this.chunks = this.file.getChunks();
      int length = 0;
      if (this.avail != null) {
         length = this.avail.length();
      }

      if (length != 0 && this.chunks.length() == length) {
         Display display = this.getDisplay();
         Color black = display.getSystemColor(2);
         Object color = null;
         int step = 1;
         if (this.limitLength && length > MAX_LENGTH) {
            step = length / MAX_LENGTH;
            length = MAX_LENGTH;
         }

         Image image = new Image(display, length, 18);
         GC gc = new GC(image);
         int charIndex = 0;
         int i = 0;
         Color runColor = null;
         int runStart = 0;

         for (int runLength = 0; i < length; i++) {
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
               runLength++;
            }

            if (!runColor.equals(color)) {
               this.drawGradient(gc, black, runColor, runStart, runLength);
               runColor = (Color)color;
               runStart = i;
               runLength = 0;
            }

            charIndex += step;
         }

         if (runStart < i) {
            int runLength = i - runStart;
            this.drawGradient(gc, black, runColor, runStart, runLength);
         }

         this.imageData = this.mirrorImageData(image.getImageData());
         gc.dispose();
         image.dispose();
         if (this.resizedImageData == null) {
            this.resizedImageData = this.imageData;
         }

         this.resizeImage(null, true);
      } else {
         if (length != 0) {
            Sancho.pDebug("CCI [" + this.client.getId() + "]" + length + "!=" + this.chunks.length());
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

      int length = 0;
      if (this.avail != null) {
         length = this.avail.length();
      }

      if (length != 0 && length == this.chunks.length()) {
         char maxAvail = 0;

         for (int i = 0; i < length; i++) {
            char availChar = this.avail.charAt(i);
            if (availChar > maxAvail) {
               maxAvail = availChar;
            }
         }

         int step = 1;
         if (this.limitLength && length > MAX_LENGTH) {
            step = length / MAX_LENGTH;
            length = MAX_LENGTH;
         }

         Display display = this.getDisplay();
         Color black = display.getSystemColor(2);
         Color color = null;
         Image image = new Image(display, length, 18);
         GC gc = new GC(image);
         ArrayList colors = new ArrayList();
         Color runColor = null;
         int charIndex = 0;
         int i = 0;
         int runStart = 0;

         for (int runLength = 0; i < length; i++) {
            if (this.chunks.charAt(charIndex) == '2') {
               color = fileCColor2;
            } else if (this.chunks.charAt(charIndex) == '3') {
               color = fileCColor3;
            } else {
               char availChar = this.avail.charAt(charIndex);
               if (availChar == 0) {
                  color = fileAColor0;
               } else {
                  int shade = (int)((float)availChar / (float)maxAvail * 10.0F);
                  shade = 10 - shade;
                  shade = -shade * 10;
                  Color shadeColor = WidgetFactory.changeColor(fileIRGB, shade);
                  if (!shadeColor.equals(color)) {
                     colors.add(shadeColor);
                     color = shadeColor;
                  } else {
                     shadeColor.dispose();
                  }
               }
            }

            if (i == 0) {
               runColor = color;
            } else {
               runLength++;
            }

            if (!runColor.equals(color)) {
               this.drawGradient(gc, black, runColor, runStart, runLength);
               runColor = color;
               runStart = i;
               runLength = 0;
            }

            charIndex += step;
         }

         if (runStart < i) {
            int runLength = i - runStart;
            this.drawGradient(gc, black, runColor, runStart, runLength);
         }

         for (int j = 0; j < colors.size(); j++) {
            Color allocatedColor = (Color)colors.get(j);
            if (allocatedColor != null && !allocatedColor.isDisposed()) {
               allocatedColor.dispose();
            }
         }

         this.imageData = this.mirrorImageData(image.getImageData());
         gc.dispose();
         image.dispose();
         image = new Image(display, this.imageData);
         gc = new GC(image);
         charIndex = 0;
         i = 0;
         gc.setLineWidth(1);
         gc.setForeground(fileCColor1);

         for (int height = image.getBounds().height; i < length; i++) {
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

         this.resizeImage(null, true);
      } else {
         if (length != 0) {
            Sancho.pDebug("CFI: " + length + "!=" + this.chunks.length());
         }
      }
   }

   private synchronized void createImage() {
      if (this.client != null) {
         this.createClientImage();
      } else {
         this.createFileImage();
      }
   }

   private void createProgressBar(int width, GC gc) {
      int progressWidth = (int)((double)this.file.getPercent() / 100.0 * (double)(width - 1));
      gc.setBackground(progressColor2);
      gc.setForeground(progressColor1);
      gc.fillGradientRectangle(0, 0, progressWidth, 4, false);
   }

   private ImageData mirrorImageData(ImageData imageData) {
      int height = imageData.height;
      int topOffset = 0;
      int bottomOffset = (height - 1) * imageData.bytesPerLine;

      for (int i = 0; i < height / 2; i++) {
         System.arraycopy(imageData.data, topOffset, imageData.data, bottomOffset, imageData.bytesPerLine);
         topOffset += imageData.bytesPerLine;
         bottomOffset -= imageData.bytesPerLine;
      }

      return imageData;
   }

   private void drawGradient(GC gc, Color foreground, Color background, int x, int width) {
      gc.setBackground(background);
      gc.setForeground(foreground);
      gc.fillGradientRectangle(x, 0, width, 9, true);
   }

   private boolean hasChanged() {
      boolean changed = false;
      if (this.client == null) {
         boolean chunksChanged = this.chunks.hashCode() != this.file.getChunks().hashCode();
         boolean availChanged = this.avail.hashCode() != this.file.getAvail().hashCode();
         changed = chunksChanged || availChanged;
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

   public synchronized void paintControl(PaintEvent event) {
      if (this.resizedImageData != null) {
         Image image = new Image(this.getDisplay(), this.resizedImageData);
         GC gc = new GC(image);
         if (this.client == null) {
            this.createProgressBar(this.resizedImageData.width, gc);
         }

         this.roundCorners(this.resizedImageData.width, this.resizedImageData.height, gc);
         gc.dispose();
         boolean inBounds = event.x + event.width <= image.getBounds().width && event.y + event.height <= image.getBounds().height;

         try {
            if (inBounds) {
               event.gc.drawImage(image, event.x, event.y, event.width, event.height, event.x, event.y, event.width, event.height);
            }
         } catch (Exception exception) {
            Sancho.pDebug("e.width: " + event.width + " e.height: " + event.height + " bw: " + image.getBounds().width + " bh: " + image.getBounds().height);
            exception.printStackTrace();
         }

         image.dispose();
      } else if (this.rectangle != null) {
         Image image = new Image(this.getDisplay(), this.rectangle);
         GC gc = new GC(image);
         int halfHeight = this.rectangle.height / 2;
         int bottomHalf = halfHeight;
         if (this.rectangle.height % 2 != 0) {
            bottomHalf = halfHeight + 1;
         }

         Color black = this.getDisplay().getSystemColor(2);
         Color grey = new Color(this.getDisplay(), 62, 62, 62);
         gc.setBackground(grey);
         gc.setForeground(black);
         gc.fillGradientRectangle(0, 0, this.rectangle.width, halfHeight, true);
         gc.setBackground(black);
         gc.setForeground(grey);
         gc.fillGradientRectangle(0, halfHeight, this.rectangle.width, bottomHalf, true);
         this.roundCorners(this.rectangle.width, this.rectangle.height, gc);
         boolean inBounds = event.x + event.width <= image.getBounds().width && event.y + event.height <= image.getBounds().height;

         try {
            if (inBounds) {
               event.gc.drawImage(image, event.x, event.y, event.width, event.height, event.x, event.y, event.width, event.height);
            }
         } catch (Exception exception) {
            Sancho.pDebug("e.width: " + event.width + " e.height: " + event.height + " bw: " + image.getBounds().width + " bh: " + image.getBounds().height);
            exception.printStackTrace();
         }

         grey.dispose();
         gc.dispose();
         image.dispose();
      } else {
         event.gc.setBackground(this.getBackground());
         event.gc.fillRectangle(event.x, event.y, event.width, event.height);
      }
   }

   private void refresh(boolean force) {
      if (force || this.hasChanged()) {
         this.createImage();
         this.redraw();
      }
   }

   private void refresh() {
      this.refresh(false);
   }

   protected synchronized void resizeImage(ControlEvent event, boolean force) {
      if (this.imageData != null) {
         int width = this.getClientArea().width;
         int height = this.getClientArea().height;
         int currentWidth = this.resizedImageData.width;
         int currentHeight = this.resizedImageData.height;
         if (width > 0 && height > 0 && (force || width != currentWidth || height != currentHeight)) {
            this.resizedImageData = this.imageData.scaledTo(width, height);
            this.redraw();
         }
      } else {
         this.rectangle = this.getClientArea();
      }
   }

   private void roundCorners(int width, int height, GC gc) {
      gc.setForeground(this.getBackground());
      byte offset = 0;
      gc.drawPoint(0, offset);
      gc.drawPoint(0, height - 1 - offset);
      gc.drawPoint(width - 1, offset);
      gc.drawPoint(width - 1, height - offset - 1);
   }

   public void run() {
      if (!this.isDisposed() && this.isVisible()) {
         this.refresh();
      }
   }

   public void update(MyObservable observable, Object arg, int id) {
      if (!this.isDisposed()) {
         boolean changed = false;
         if (observable instanceof Client && arg == null) {
            changed = id == 16;
         } else if (observable instanceof File) {
            changed = ((File)observable).hasChangedBit(1024);
         }

         if (changed) {
            this.getDisplay().syncExec(this);
         }
      }
   }

   public synchronized void widgetDisposed(DisposeEvent event) {
      if (this.file != null) {
         this.file.deleteObserver(this);
      }

      if (this.client != null) {
         this.client.deleteObserver(this);
      }

      synchronized (chunkList) {
         chunkList.remove(this);
      }
   }
}
