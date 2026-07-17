package sancho.view.viewer.actions;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.IObject_UID;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class CopyUIDLinksToClipboardAction extends Action {
   public static int ED2K = 0;
   public static int MAGNET = 2;
   public static int SIG2DAT = 1;
   private String[] linkArray;
   private int linkStyle;
   public static int NORMAL = 0;
   public static int HTML = 1;
   public static int BBCODE = 2;

   public CopyUIDLinksToClipboardAction(int var1, int var2, List var3) {
      this.linkStyle = var2;
      String var4;
      String var5;
      switch (var1) {
         case 1:
            var4 = "mi.sig2datCopy";
            var5 = "e.network.fasttrack.connected";
            this.linkArray = this.buildSig2DatLinks(var3);
            break;
         case 2:
            var4 = "mi.magnetCopy";
            var5 = "magnet";
            this.linkArray = this.buildMagnetLinks(var3);
            break;
         default:
            var4 = "mi.ed2kCopy";
            var5 = "edonkey";
            this.linkArray = this.buildED2KLinks(var3);
      }

      String var6 = "";
      if (this.linkStyle == HTML) {
         var6 = " (html)";
      }

      if (this.linkStyle == BBCODE) {
         var6 = " (bbcode)";
      }

      this.setText(SResources.getString(var4) + var6);
      this.setImageDescriptor(SResources.getImageDescriptor(var5));
   }

   private String[] buildED2KLinks(List var1) {
      int var2 = var1.size();
      String[] var3 = new String[var2];

      for (int var4 = 0; var4 < var2; var4++) {
         IObject_UID var5 = (IObject_UID)var1.get(var4);
         var3[var4] = this.formatLink(var5, var5.getED2K());
      }

      return var3;
   }

   private String[] buildMagnetLinks(List var1) {
      int var2 = var1.size();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var2; var4++) {
         IObject_UID var5 = (IObject_UID)var1.get(var4);
         String[] var6 = var5.getUIDs();
         if (var6 != null) {
            String var7 = "";

            for (int var8 = 0; var8 < var6.length; var8++) {
               if (var6[var8].startsWith("urn:sha1:")) {
                  var7 = var7 + "&xt=" + var6[var8];
               } else if (var6[var8].startsWith("urn:ttr:")) {
                  String var9 = var6[var8].substring(8);
                  var7 = var7 + "&xt=urn:tree:tiger:" + var9;
               }
            }

            if (!var7.equals("")) {
               String var11 = "magnet:?dn=" + var5.getName() + var7;
               var3.add(this.formatLink(var5, var11));
            }
         }
      }

      String[] var10 = new String[var3.size()];
      var3.toArray(var10);
      return var10;
   }

   private String[] buildSig2DatLinks(List var1) {
      int var2 = var1.size();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var2; var4++) {
         IObject_UID var5 = (IObject_UID)var1.get(var4);
         String[] var6 = var5.getUIDs();
         if (var6 != null) {
            for (int var7 = 0; var7 < var6.length; var7++) {
               if (var6[var7].startsWith("urn:sig2dat:")) {
                  String var8 = var6[var7].substring(12);
                  String var9 = Base6427_to_string(Base32_of_string(var8));
                  long var10 = var5.getSize();
                  String var12 = "0KB";
                  if (var10 > 1024L) {
                     var12 = (int)(var10 / 1024L) + "KB";
                  }

                  String var13 = var10 + " Bytes";
                  String var14 = var5.getName();
                  String var15 = "sig2dat:///|File:" + var14 + "|Length:" + var13 + "," + var12 + "|UUHash:" + var9 + "|";
                  var3.add(this.formatLink(var5, var15));
               }
            }
         }
      }

      String[] var16 = new String[var3.size()];
      var3.toArray(var16);
      return var16;
   }

   private String formatLink(IObject_UID var1, String var2) {
      if (this.linkStyle == HTML) {
         return "<a href=\"" + var2 + "\">" + var1.getName() + "</a>";
      } else {
         return this.linkStyle == BBCODE ? "[URL=\"" + var2 + "\"]" + var1.getName() + "[/URL]" : var2;
      }
   }

   public void run() {
      if (this.linkArray != null && this.linkArray.length != 0) {
         String var1 = "";
         String var2 = System.getProperty("line.separator");

         for (int var3 = 0; var3 < this.linkArray.length; var3++) {
            String var4 = this.linkArray[var3];
            if (!var4.equals("")) {
               if (var1.length() > 0) {
                  var1 = var1 + var2;
               }

               var1 = var1 + var4;
            }
         }

         MainWindow.copyToClipboard(var1);
      }
   }

   public static String Base6427_to_string(String var0) {
      String var1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
      byte var2 = 30;
      StringBuffer var3 = new StringBuffer(var2);

      for (int var4 = 0; var4 < var2; var4++) {
         var3.append('\u0000');
      }

      var3.setCharAt(0, '=');
      int var5 = 1;

      for (int var6 = 0; var6 <= 6; var6++) {
         int var7 = var6 < 6
            ? var0.charAt(3 * var6) << 16 | var0.charAt(3 * var6 + 1) << '\b' | var0.charAt(3 * var6 + 2)
            : var0.charAt(3 * var6) << 16 | var0.charAt(3 * var6 + 1) << '\b';

         for (int var8 = 0; var8 <= 3; var8++) {
            char var9 = var1.charAt(var7 >> (3 - var8) * 6 & 63);
            var3.setCharAt(var5, var9);
            var5++;
         }
      }

      var3.setCharAt(var5 - 1, '=');
      return var3.substring(0, var5);
   }

   public static String Base32_of_string(String var0) {
      String var1 = var0;
      byte var2 = 20;
      StringBuffer var3 = new StringBuffer(var2);

      for (int var4 = 0; var4 < var2; var4++) {
         var3.append('\u0000');
      }

      for (int var5 = 0; var5 < var1.length(); var5++) {
         int var6 = var5 * 5;
         int var7 = var6 / 8;
         int var8 = var6 % 8;
         int var9 = int5_of_char(var1.charAt(var5));
         if (var8 < 3) {
            int var10 = var9 << 3 - var8;
            char var11 = (char)(var3.charAt(var7) | var10);
            var3.setCharAt(var7, var11);
         } else {
            int var12 = var9 >> var8 - 3 & 0xFF;
            char var14 = (char)(var3.charAt(var7) | var12);
            var3.setCharAt(var7, var14);
         }

         if (var7 + 1 < var2) {
            int var13 = var9 << 11 - var8 & 0xFF;
            char var15 = (char)(var3.charAt(var7 + 1) | var13);
            var3.setCharAt(var7 + 1, var15);
         }
      }

      return var3.toString();
   }

   private static int int5_of_char(char var0) {
      if ('A' <= var0 && var0 <= 'Z') {
         return var0 - 65;
      } else {
         return 97 <= var0 && var0 <= 122 ? var0 - 97 : var0 + 26 - 50;
      }
   }
}
