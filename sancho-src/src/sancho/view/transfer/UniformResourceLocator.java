package sancho.view.transfer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

public class UniformResourceLocator extends ByteArrayTransfer {
   private static final String TYPENAME1 = "UniformResourceLocator";
   private static final String TYPENAME2 = "text/x-moz-url-data";
   private static final int TYPEID1 = Transfer.registerType("UniformResourceLocator");
   private static final int TYPEID2 = Transfer.registerType("text/x-moz-url-data");
   private static UniformResourceLocator _instance = new UniformResourceLocator();

   public static UniformResourceLocator getInstance() {
      return _instance;
   }

   public Object nativeToJava(TransferData transferData) {
      if (!this.isSupportedType(transferData)) {
         return null;
      } else {
         byte[] bytes = (byte[])super.nativeToJava(transferData);
         if (bytes == null) {
            return null;
         } else if (transferData.type == TYPEID2) {
            // Gecko "text/x-moz-url-data": the URL as UTF-16LE. Decoding with the JVM
            // default charset (UTF-8 since JDK 18) turned every char into mojibake; and
            // the old scan-to-first-NUL truncated it to one character (UTF-16 has a NUL
            // high byte on ASCII). Decode as UTF-16LE and cut at the first NUL.
            String text = new String(bytes, StandardCharsets.UTF_16LE);
            int nulIndex = text.indexOf(0);
            return nulIndex == -1 ? text : text.substring(0, nulIndex);
         } else {
            // Windows shell "UniformResourceLocator": a NUL-terminated single-byte
            // (ANSI/windows-1252) string. Decode it explicitly rather than with the JVM
            // default charset, which is UTF-8 on JDK 18+ and mangles bytes >= 0x80.
            int length = 0;

            for (int i = 0; i < bytes.length && bytes[i] != 0; i++) {
               length++;
            }

            return new String(bytes, 0, length, Charset.forName("windows-1252"));
         }
      }
   }

   protected String[] getTypeNames() {
      return new String[]{"UniformResourceLocator", "text/x-moz-url-data"};
   }

   protected int[] getTypeIds() {
      return new int[]{TYPEID1, TYPEID2};
   }
}
