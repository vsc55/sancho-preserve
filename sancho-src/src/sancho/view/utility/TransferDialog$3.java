package sancho.view.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import sancho.core.CoreFactory;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;

class TransferDialog$3 extends Thread {
   // $VF: synthetic field
   private final TransferDialog this$0;

   TransferDialog$3(TransferDialog var1) {
      this.this$0 = var1;
   }

   public void run() {
      try {
         for (int var1 = 0; var1 < this.this$0.subFileNums.length; var1++) {
            URL var2 = new URL(this.this$0.iPreview.getPreviewURL());
            if (!new File(this.this$0.downloadDirectory).exists()) {
               this.this$0.updateLabel(this.this$0.label3, SResources.getString("td.invalidDirectory"));
               return;
            }

            String var3 = this.this$0.iPreview.getName();
            int var4 = this.this$0.subFileNums[var1];
            if (var4 != -1) {
               String[] var5 = this.this$0.iPreview.getSubFileNames();
               if (var5 != null && var5.length >= var4) {
                  var3 = var5[var4];
                  if (var3.indexOf("/") != -1) {
                     String var6 = var3.substring(0, var3.lastIndexOf("/"));
                     new File(this.this$0.downloadDirectory + File.separator + var6).mkdirs();
                  }
               }
            }

            String var36 = var3;
            int var37 = 1;

            while (new File(this.this$0.downloadDirectory + File.separator + var36).exists()) {
               var36 = var3 + "." + var37++;
            }

            this.this$0.updateLabel(this.this$0.label1, var36);
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
               var7.setRequestProperty("Range", this.this$0.iPreview.getContentRange(var4));
            }

            var7.connect();
            int var10 = var7.getResponseCode();
            if (var10 != 200 && var10 != 206) {
               this.this$0.updateLabel(this.this$0.label3, var10 + ": " + var7.getResponseMessage() + ": " + this.this$0.iPreview.getPreviewURL());
               return;
            }

            String var11 = this.this$0.downloadDirectory + File.separator + var36;
            BufferedInputStream var12 = new BufferedInputStream(var7.getInputStream());
            BufferedOutputStream var13 = new BufferedOutputStream(new FileOutputStream(var11));
            long var14 = TransferDialog.getLongLength(var7);
            int var16 = 131072;
            byte[] var17 = new byte[var16];
            long var19 = 0L;
            long var21 = System.currentTimeMillis();
            long var23 = var21;
            String var25 = SwissArmy.calcStringSize(var14);
            this.this$0.transferring = true;

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
                     this.this$0.updateLabel(this.this$0.label3, var33);
                  }

                  if (!this.this$0.cancel) {
                     continue;
                  }

                  this.this$0.cancelled = true;
               }

               var12.close();
               var13.close();
               this.this$0.transferring = false;
               break;
            }
         }

         if (!this.this$0.cancelled) {
            this.this$0.closeInThread();
         }
      } catch (MalformedURLException var34) {
         this.this$0.updateLabel(this.this$0.label1, var34.toString());
      } catch (IOException var35) {
         this.this$0.updateLabel(this.this$0.label1, var35.toString());
      }
   }
}
