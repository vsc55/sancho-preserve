package sancho.view.transfer;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
         for (int var1 = 0; var1 < chunkList.size(); var1++) {
            ((ChunkCanvas)chunkList.get(var1)).refresh(true);
         }
      }
   }

   public ChunkCanvas(Composite var1, int var2, Client var3, File var4, Network var5, boolean var6) {
      super(var1, var2);
      this.client = var3;
      this.file = var4;
      this.network = var5;
      this.limitLength = var6 || forceLimit;
      if (var3 != null) {
         var3.addObserver(this);
      } else if (var4 != null) {
         var4.addObserver(this);
      }

      this.createImage();
      this.addDisposeListener(this);
      this.addPaintListener(this);
      this.addMouseListener(new ChunkCanvas$1(this, var1));
      this.addControlListener(new ChunkCanvas$2(this));
      synchronized (chunkList) {
         chunkList.add(this);
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
         Display var2 = this.getDisplay();
         Color var3 = var2.getSystemColor(2);
         Object var4 = null;
         int var5 = 1;
         if (this.limitLength && var1 > MAX_LENGTH) {
            var5 = var1 / MAX_LENGTH;
            var1 = MAX_LENGTH;
         }

         Image var6 = new Image(var2, var1, 18);
         GC var7 = new GC(var6);
         int var8 = 0;
         int var9 = 0;
         Color var10 = null;
         int var11 = 0;

         for (int var12 = 0; var9 < var1; var9++) {
            if (this.chunks.charAt(var8) == '2') {
               var4 = clientCColor2;
            } else {
               char var13 = this.avail.charAt(var8);
               if (var13 == '0') {
                  var4 = clientAColor0;
               } else if (var13 == '1') {
                  var4 = clientAColor1;
               } else if (var13 == '2') {
                  var4 = clientAColor2;
               } else {
                  var4 = clientAColor1;
               }
            }

            if (var9 == 0) {
               var10 = (Color)var4;
            } else {
               var12++;
            }

            if (!var10.equals(var4)) {
               this.drawGradient(var7, var3, var10, var11, var12);
               var10 = (Color)var4;
               var11 = var9;
               var12 = 0;
            }

            var8 += var5;
         }

         if (var11 < var9) {
            int var15 = var9 - var11;
            this.drawGradient(var7, var3, var10, var11, var15);
         }

         this.imageData = this.mirrorImageData(var6.getImageData());
         var7.dispose();
         var6.dispose();
         if (this.resizedImageData == null) {
            this.resizedImageData = this.imageData;
         }

         this.resizeImage(null, true);
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

         Display var6 = this.getDisplay();
         Color var7 = var6.getSystemColor(2);
         Color var8 = null;
         Image var9 = new Image(var6, var1, 18);
         GC var10 = new GC(var9);
         ArrayList var11 = new ArrayList();
         Color var12 = null;
         int var13 = 0;
         int var14 = 0;
         int var15 = 0;

         for (int var16 = 0; var14 < var1; var14++) {
            if (this.chunks.charAt(var13) == '2') {
               var8 = fileCColor2;
            } else if (this.chunks.charAt(var13) == '3') {
               var8 = fileCColor3;
            } else {
               char var19 = this.avail.charAt(var13);
               if (var19 == 0) {
                  var8 = fileAColor0;
               } else {
                  int var17 = (int)((float)var19 / (float)var3 * 10.0F);
                  var17 = 10 - var17;
                  var17 = -var17 * 10;
                  Color var18 = WidgetFactory.changeColor(fileIRGB, var17);
                  if (!var18.equals(var8)) {
                     var11.add(var18);
                     var8 = var18;
                  } else {
                     var18.dispose();
                  }
               }
            }

            if (var14 == 0) {
               var12 = var8;
            } else {
               var16++;
            }

            if (!var12.equals(var8)) {
               this.drawGradient(var10, var7, var12, var15, var16);
               var12 = var8;
               var15 = var14;
               var16 = 0;
            }

            var13 += var5;
         }

         if (var15 < var14) {
            int var24 = var14 - var15;
            this.drawGradient(var10, var7, var12, var15, var24);
         }

         for (int var27 = 0; var27 < var11.size(); var27++) {
            Color var28 = (Color)var11.get(var27);
            if (var28 != null && !var28.isDisposed()) {
               var28.dispose();
            }
         }

         this.imageData = this.mirrorImageData(var9.getImageData());
         var10.dispose();
         var9.dispose();
         var9 = new Image(var6, this.imageData);
         var10 = new GC(var9);
         var13 = 0;
         var14 = 0;
         var10.setLineWidth(1);
         var10.setForeground(fileCColor1);

         for (int var29 = var9.getBounds().height; var14 < var1; var14++) {
            if (this.chunks.charAt(var13) == '1') {
               var10.drawRectangle(var14, var29 - 2, 0, 2);
            }

            var13 += var5;
         }

         this.imageData = var9.getImageData();
         var10.dispose();
         var9.dispose();
         if (this.resizedImageData == null) {
            this.resizedImageData = this.imageData;
         }

         this.resizeImage(null, true);
      } else {
         if (var1 != 0) {
            Sancho.pDebug("CFI: " + var1 + "!=" + this.chunks.length());
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

   private void createProgressBar(int var1, GC var2) {
      int var3 = (int)((double)this.file.getPercent() / 100.0 * (double)(var1 - 1));
      var2.setBackground(progressColor2);
      var2.setForeground(progressColor1);
      var2.fillGradientRectangle(0, 0, var3, 4, false);
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

   private void drawGradient(GC var1, Color var2, Color var3, int var4, int var5) {
      var1.setBackground(var3);
      var1.setForeground(var2);
      var1.fillGradientRectangle(var4, 0, var5, 9, true);
   }

   private boolean hasChanged() {
      boolean var1 = false;
      if (this.client == null) {
         boolean var2 = this.chunks.hashCode() != this.file.getChunks().hashCode();
         boolean var3 = this.avail.hashCode() != this.file.getAvail().hashCode();
         var1 = var2 || var3;
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

   public synchronized void paintControl(PaintEvent var1) {
      if (this.resizedImageData != null) {
         Image var2 = new Image(this.getDisplay(), this.resizedImageData);
         GC var3 = new GC(var2);
         if (this.client == null) {
            this.createProgressBar(this.resizedImageData.width, var3);
         }

         this.roundCorners(this.resizedImageData.width, this.resizedImageData.height, var3);
         var3.dispose();
         boolean var4 = var1.x + var1.width <= var2.getBounds().width && var1.y + var1.height <= var2.getBounds().height;

         try {
            if (var4) {
               var1.gc.drawImage(var2, var1.x, var1.y, var1.width, var1.height, var1.x, var1.y, var1.width, var1.height);
            }
         } catch (Exception var11) {
            Sancho.pDebug("e.width: " + var1.width + " e.height: " + var1.height + " bw: " + var2.getBounds().width + " bh: " + var2.getBounds().height);
            var11.printStackTrace();
         }

         var2.dispose();
      } else if (this.rectangle != null) {
         Image var12 = new Image(this.getDisplay(), this.rectangle);
         GC var13 = new GC(var12);
         int var14 = this.rectangle.height / 2;
         int var5 = var14;
         if (this.rectangle.height % 2 != 0) {
            var5 = var14 + 1;
         }

         Color var6 = this.getDisplay().getSystemColor(2);
         Color var7 = new Color(this.getDisplay(), 62, 62, 62);
         var13.setBackground(var7);
         var13.setForeground(var6);
         var13.fillGradientRectangle(0, 0, this.rectangle.width, var14, true);
         var13.setBackground(var6);
         var13.setForeground(var7);
         var13.fillGradientRectangle(0, var14, this.rectangle.width, var5, true);
         this.roundCorners(this.rectangle.width, this.rectangle.height, var13);
         boolean var8 = var1.x + var1.width <= var12.getBounds().width && var1.y + var1.height <= var12.getBounds().height;

         try {
            if (var8) {
               var1.gc.drawImage(var12, var1.x, var1.y, var1.width, var1.height, var1.x, var1.y, var1.width, var1.height);
            }
         } catch (Exception var10) {
            Sancho.pDebug("e.width: " + var1.width + " e.height: " + var1.height + " bw: " + var12.getBounds().width + " bh: " + var12.getBounds().height);
            var10.printStackTrace();
         }

         var7.dispose();
         var13.dispose();
         var12.dispose();
      } else {
         var1.gc.setBackground(this.getBackground());
         var1.gc.fillRectangle(var1.x, var1.y, var1.width, var1.height);
      }
   }

   private void refresh(boolean var1) {
      if (var1 || this.hasChanged()) {
         this.createImage();
         this.redraw();
      }
   }

   private void refresh() {
      this.refresh(false);
   }

   protected synchronized void resizeImage(ControlEvent var1, boolean var2) {
      if (this.imageData != null) {
         int var3 = this.getClientArea().width;
         int var4 = this.getClientArea().height;
         int var5 = this.resizedImageData.width;
         int var6 = this.resizedImageData.height;
         if (var3 > 0 && var4 > 0 && (var2 || var3 != var5 || var4 != var6)) {
            this.resizedImageData = this.imageData.scaledTo(var3, var4);
         }
      } else {
         this.rectangle = this.getClientArea();
      }
   }

   private void roundCorners(int var1, int var2, GC var3) {
      var3.setForeground(this.getBackground());
      byte var4 = 0;
      var3.drawPoint(0, var4);
      var3.drawPoint(0, var2 - 1 - var4);
      var3.drawPoint(var1 - 1, var4);
      var3.drawPoint(var1 - 1, var2 - var4 - 1);
   }

   public void run() {
      if (!this.isDisposed() && this.isVisible()) {
         this.refresh();
      }
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (!this.isDisposed()) {
         boolean var4 = false;
         if (var1 instanceof Client && var2 == null) {
            var4 = var3 == 16;
         } else if (var1 instanceof File) {
            var4 = ((File)var1).hasChangedBit(1024);
         }

         if (var4) {
            this.getDisplay().syncExec(this);
         }
      }
   }

   public synchronized void widgetDisposed(DisposeEvent var1) {
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
