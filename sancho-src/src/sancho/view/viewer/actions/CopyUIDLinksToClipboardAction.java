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

   public CopyUIDLinksToClipboardAction(int uidType, int style, List objects) {
      this.linkStyle = style;
      String textKey;
      String imageKey;
      switch (uidType) {
         case 1:
            textKey = "mi.sig2datCopy";
            imageKey = "e.network.fasttrack.connected";
            this.linkArray = this.buildSig2DatLinks(objects);
            break;
         case 2:
            textKey = "mi.magnetCopy";
            imageKey = "magnet";
            this.linkArray = this.buildMagnetLinks(objects);
            break;
         default:
            textKey = "mi.ed2kCopy";
            imageKey = "edonkey";
            this.linkArray = this.buildED2KLinks(objects);
      }

      String styleSuffix = "";
      if (this.linkStyle == HTML) {
         styleSuffix = " (html)";
      }

      if (this.linkStyle == BBCODE) {
         styleSuffix = " (bbcode)";
      }

      this.setText(SResources.getString(textKey) + styleSuffix);
      this.setImageDescriptor(SResources.getImageDescriptor(imageKey));
   }

   private String[] buildED2KLinks(List objects) {
      int count = objects.size();
      String[] links = new String[count];

      for (int i = 0; i < count; i++) {
         IObject_UID object = (IObject_UID)objects.get(i);
         links[i] = this.formatLink(object, object.getED2K());
      }

      return links;
   }

   private String[] buildMagnetLinks(List objects) {
      int count = objects.size();
      ArrayList links = new ArrayList();

      for (int i = 0; i < count; i++) {
         IObject_UID object = (IObject_UID)objects.get(i);
         String[] uids = object.getUIDs();
         if (uids != null) {
            String query = "";

            for (int j = 0; j < uids.length; j++) {
               if (uids[j].startsWith("urn:sha1:")) {
                  query = query + "&xt=" + uids[j];
               } else if (uids[j].startsWith("urn:ttr:")) {
                  String ttrHash = uids[j].substring(8);
                  query = query + "&xt=urn:tree:tiger:" + ttrHash;
               }
            }

            if (!query.equals("")) {
               String link = "magnet:?dn=" + object.getName() + query;
               links.add(this.formatLink(object, link));
            }
         }
      }

      String[] result = new String[links.size()];
      links.toArray(result);
      return result;
   }

   private String[] buildSig2DatLinks(List objects) {
      int count = objects.size();
      ArrayList links = new ArrayList();

      for (int i = 0; i < count; i++) {
         IObject_UID object = (IObject_UID)objects.get(i);
         String[] uids = object.getUIDs();
         if (uids != null) {
            for (int j = 0; j < uids.length; j++) {
               if (uids[j].startsWith("urn:sig2dat:")) {
                  String hash = uids[j].substring(12);
                  String uuHash = Base6427_to_string(Base32_of_string(hash));
                  long size = object.getSize();
                  String sizeInKB = "0KB";
                  if (size > 1024L) {
                     sizeInKB = (int)(size / 1024L) + "KB";
                  }

                  String sizeInBytes = size + " Bytes";
                  String name = object.getName();
                  String link = "sig2dat:///|File:" + name + "|Length:" + sizeInBytes + "," + sizeInKB + "|UUHash:" + uuHash + "|";
                  links.add(this.formatLink(object, link));
               }
            }
         }
      }

      String[] result = new String[links.size()];
      links.toArray(result);
      return result;
   }

   private String formatLink(IObject_UID object, String link) {
      if (this.linkStyle == HTML) {
         return "<a href=\"" + link + "\">" + object.getName() + "</a>";
      } else {
         return this.linkStyle == BBCODE ? "[URL=\"" + link + "\"]" + object.getName() + "[/URL]" : link;
      }
   }

   public void run() {
      if (this.linkArray != null && this.linkArray.length != 0) {
         String text = "";
         String lineSeparator = System.getProperty("line.separator");

         for (int i = 0; i < this.linkArray.length; i++) {
            String link = this.linkArray[i];
            if (!link.equals("")) {
               if (text.length() > 0) {
                  text = text + lineSeparator;
               }

               text = text + link;
            }
         }

         MainWindow.copyToClipboard(text);
      }
   }

   public static String Base6427_to_string(String input) {
      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
      byte length = 30;
      StringBuffer buffer = new StringBuffer(length);

      for (int i = 0; i < length; i++) {
         buffer.append('\u0000');
      }

      buffer.setCharAt(0, '=');
      int index = 1;

      for (int i = 0; i <= 6; i++) {
         int bits = i < 6
            ? input.charAt(3 * i) << 16 | input.charAt(3 * i + 1) << '\b' | input.charAt(3 * i + 2)
            : input.charAt(3 * i) << 16 | input.charAt(3 * i + 1) << '\b';

         for (int j = 0; j <= 3; j++) {
            char c = alphabet.charAt(bits >> (3 - j) * 6 & 63);
            buffer.setCharAt(index, c);
            index++;
         }
      }

      buffer.setCharAt(index - 1, '=');
      return buffer.substring(0, index);
   }

   public static String Base32_of_string(String input) {
      String text = input;
      byte length = 20;
      StringBuffer buffer = new StringBuffer(length);

      for (int i = 0; i < length; i++) {
         buffer.append('\u0000');
      }

      for (int i = 0; i < text.length(); i++) {
         int bitPosition = i * 5;
         int byteIndex = bitPosition / 8;
         int bitOffset = bitPosition % 8;
         int value = int5_of_char(text.charAt(i));
         if (bitOffset < 3) {
            int bits = value << 3 - bitOffset;
            char updatedChar = (char)(buffer.charAt(byteIndex) | bits);
            buffer.setCharAt(byteIndex, updatedChar);
         } else {
            int bits = value >> bitOffset - 3 & 0xFF;
            char updatedChar = (char)(buffer.charAt(byteIndex) | bits);
            buffer.setCharAt(byteIndex, updatedChar);
         }

         if (byteIndex + 1 < length) {
            int bits = value << 11 - bitOffset & 0xFF;
            char updatedChar = (char)(buffer.charAt(byteIndex + 1) | bits);
            buffer.setCharAt(byteIndex + 1, updatedChar);
         }
      }

      return buffer.toString();
   }

   private static int int5_of_char(char c) {
      if ('A' <= c && c <= 'Z') {
         return c - 65;
      } else {
         return 97 <= c && c <= 122 ? c - 97 : c + 26 - 50;
      }
   }
}
