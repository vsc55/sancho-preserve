package sancho.view.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import sancho.core.CoreFactory;
import sancho.core.Sancho;
import sancho.model.mldonkey.IPreview;
import sancho.utility.SwissArmy;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;

public class TransferDialog extends Dialog {
   boolean cancel;
   boolean cancelled;
   boolean transferring;
   String downloadDirectory;
   CLabel label1;
   CLabel label2;
   CLabel label3;
   IPreview iPreview;
   int[] subFileNums;

   public TransferDialog(Shell var1, IPreview var2, int[] var3, String var4) {
      super(new Shell());
      this.iPreview = var2;
      this.subFileNums = var3;
      this.downloadDirectory = var4;
      if (var3.length == 1 && var3[0] == -1) {
         String[] var5 = var2.getSubFileNames();
         if (var5 != null) {
            this.subFileNums = new int[var5.length];
            int var6 = 0;

            while (var6 < var5.length) {
               this.subFileNums[var6] = var6++;
            }
         }
      }

      this.setShellStyle(48 | (SWT.getPlatform().equals("fox") ? 0 : 0));
      this.setBlockOnOpen(false);
      this.startTransfer();
   }

   public boolean close() {
      // Signal cancel and close immediately. The transfer thread checks `cancel` each
      // read chunk and only touches widgets through updateLabel()/closeInThread(),
      // both isDisposed-guarded, so there's no need to busy-wait for it here — the old
      // loop slept up to 20 x 200ms = 4s on the UI thread, freezing the GUI on cancel.
      this.cancel = true;
      PreferenceStore var2 = PreferenceLoader.getPreferenceStore();
      PreferenceConverter.setValue(var2, "transferDialogWindowBounds", this.getShell().getBounds());
      return super.close();
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
      var1.setText(this.iPreview.getName());
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      Shell var1 = this.getShell();
      if (PreferenceLoader.contains("transferDialogWindowBounds")) {
         var1.setBounds(PreferenceLoader.loadRectangle("transferDialogWindowBounds"));
      } else {
         var1.setSize(300, 175);
         Point var2 = var1.getLocation();
         this.getShell().setLocation(var2.x - 200, var2.y);
      }
   }

   protected void createButtonsForButtonBar(Composite var1) {
      this.createButton(var1, 1, SResources.getString("b.cancel"), false);
   }

   protected void buttonPressed(int var1) {
      if (var1 == 1) {
         this.cancel = true;
      }

      super.buttonPressed(var1);
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 3, 2, false));
      new CLabel(var2, 0).setText(SResources.getString("td.file"));
      this.label1 = new CLabel(var2, 0);
      this.label1.setLayoutData(new GridData(768));
      new CLabel(var2, 0).setText(SResources.getString("td.destination"));
      this.label2 = new CLabel(var2, 0);
      new CLabel(var2, 0).setText(SResources.getString("td.status"));
      this.label2.setLayoutData(new GridData(768));
      this.label3 = new CLabel(var2, 0);
      this.label3.setLayoutData(new GridData(768));
      this.label1.setText(this.iPreview.getName());
      this.label2.setText(this.downloadDirectory);
      return var2;
   }

   public static long getLongLength(HttpURLConnection var0) {
      for (int var1 = 0; var1 < 20; var1++) {
         String var2 = var0.getHeaderFieldKey(var1);
         String var3 = var0.getHeaderField(var1);
         if (var3 == null) {
            break;
         }

         if (var2 != null && var2.toLowerCase().equals("content-length")) {
            return Long.valueOf(var3);
         }
      }

      return (long)var0.getContentLength();
   }

   public void updateLabel(final CLabel var1, final String var2) {
      Shell var3 = this.getShell();
      if (var3 != null && !var3.isDisposed()) {
         var3.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (var1 != null && !var1.isDisposed()) {
                  var1.setText(var2);
               }
            }
         });
      }
   }

   public void closeInThread() {
      Shell var1 = this.getShell();
      if (var1 != null && !var1.isDisposed()) {
         var1.getDisplay().asyncExec(new Runnable() {
            public void run() {
               TransferDialog.this.close();
            }
         });
      }
   }

   public void startTransfer() {
      Thread transferThread = new Thread() {
         public void run() {
            try {
               for (int var1 = 0; var1 < TransferDialog.this.subFileNums.length; var1++) {
                  URL var2 = new URL(TransferDialog.this.iPreview.getPreviewURL());
                  if (!new File(TransferDialog.this.downloadDirectory).exists()) {
                     TransferDialog.this.updateLabel(TransferDialog.this.label3, SResources.getString("td.invalidDirectory"));
                     return;
                  }

                  String var3 = TransferDialog.this.iPreview.getName();
                  int var4 = TransferDialog.this.subFileNums[var1];
                  if (var4 != -1) {
                     String[] var5 = TransferDialog.this.iPreview.getSubFileNames();
                     if (var5 != null && var5.length >= var4) {
                        var3 = var5[var4];
                        if (var3.indexOf("/") != -1) {
                           String var6 = var3.substring(0, var3.lastIndexOf("/"));
                           new File(TransferDialog.this.downloadDirectory + File.separator + var6).mkdirs();
                        }
                     }
                  }

                  String var36 = var3;
                  int var37 = 1;

                  while (new File(TransferDialog.this.downloadDirectory + File.separator + var36).exists()) {
                     var36 = var3 + "." + var37++;
                  }

                  TransferDialog.this.updateLabel(TransferDialog.this.label1, var36);
                  HttpURLConnection var7 = (HttpURLConnection)var2.openConnection();
                  CoreFactory var8 = Sancho.getCoreFactory();
                  String var9 = "";
                  if (!var8.getPassword().equals("")) {
                     var9 = var8.getUsername() + ":" + var8.getPassword();
                  }

                  if (!var9.equals("")) {
                     var7.setRequestProperty("Authorization", "Basic " + Base64.encode(var9.getBytes()));
                  }

                  if (var4 != -1) {
                     var7.setRequestProperty("Range", TransferDialog.this.iPreview.getContentRange(var4));
                  }

                  var7.connect();
                  int var10 = var7.getResponseCode();
                  if (var10 != 200 && var10 != 206) {
                     TransferDialog.this.updateLabel(TransferDialog.this.label3, var10 + ": " + var7.getResponseMessage() + ": " + TransferDialog.this.iPreview.getPreviewURL());
                     return;
                  }

                  String var11 = TransferDialog.this.downloadDirectory + File.separator + var36;
                  BufferedInputStream var12 = new BufferedInputStream(var7.getInputStream());
                  BufferedOutputStream var13 = new BufferedOutputStream(new FileOutputStream(var11));
                  long var14 = TransferDialog.getLongLength(var7);
                  int var16 = 131072;
                  byte[] var17 = new byte[var16];
                  long var19 = 0L;
                  long var21 = System.currentTimeMillis();
                  long var23 = var21;
                  String var25 = SwissArmy.calcStringSize(var14);
                  TransferDialog.this.transferring = true;

                  while (true) {
                     int var18;
                     if ((var18 = var12.read(var17, 0, var16)) != -1) {
                        var19 += (long)var18;
                        var13.write(var17, 0, var18);
                        long var26 = System.currentTimeMillis();
                        if (var26 > var23 + 1001L || var19 == (long)var18) {
                           var23 = var26;
                           long var28 = var26 - var21;
                           long var30 = var28 > 1000L && var19 > 0L ? var19 / (var28 / 1000L) : var19;
                           String var32 = SwissArmy.calcStringSize(var30) + "/s";
                           String var33 = SwissArmy.calcStringSize(var19) + "/" + var25 + " (" + var32 + ")";
                           TransferDialog.this.updateLabel(TransferDialog.this.label3, var33);
                        }

                        if (!TransferDialog.this.cancel) {
                           continue;
                        }

                        TransferDialog.this.cancelled = true;
                     }

                     var12.close();
                     var13.close();
                     TransferDialog.this.transferring = false;
                     break;
                  }
               }

               if (!TransferDialog.this.cancelled) {
                  TransferDialog.this.closeInThread();
               }
            } catch (MalformedURLException var34) {
               TransferDialog.this.updateLabel(TransferDialog.this.label1, var34.toString());
            } catch (IOException var35) {
               TransferDialog.this.updateLabel(TransferDialog.this.label1, var35.toString());
            }
         }
      };
      transferThread.start();
   }
}
