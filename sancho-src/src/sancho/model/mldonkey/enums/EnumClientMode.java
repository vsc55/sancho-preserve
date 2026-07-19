package sancho.model.mldonkey.enums;

import org.eclipse.swt.graphics.Image;
import sancho.view.utility.SResources;

public class EnumClientMode extends AbstractEnum {
   public static final EnumClientMode DIRECT = new EnumClientMode(1, "direct");
   public static final EnumClientMode FIREWALLED = new EnumClientMode(2, "firewalled");

   private EnumClientMode(int value, String name) {
      super(value, "e.clientmode." + name);
   }

   public static EnumClientMode byteToEnum(byte value) {
      switch (value) {
         case 1:
            return FIREWALLED;
         default:
            return DIRECT;
      }
   }

   public Image getImage() {
      return this == FIREWALLED ? SResources.getImage("firewalled") : SResources.getImage("direct");
   }
}
