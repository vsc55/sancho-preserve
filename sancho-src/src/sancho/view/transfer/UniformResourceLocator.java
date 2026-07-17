package sancho.view.transfer;

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

   public Object nativeToJava(TransferData var1) {
      if (!this.isSupportedType(var1)) {
         return null;
      } else {
         byte[] var2 = (byte[])super.nativeToJava(var1);
         if (var2 == null) {
            return null;
         } else {
            int var3 = 0;

            for (int var4 = 0; var4 < var2.length && var2[var4] != 0; var4++) {
               var3++;
            }

            return new String(var2, 0, var3);
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
