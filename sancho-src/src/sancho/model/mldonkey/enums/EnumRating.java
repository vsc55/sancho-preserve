package sancho.model.mldonkey.enums;

import org.eclipse.swt.graphics.Image;
import sancho.view.utility.SResources;

public class EnumRating extends AbstractEnum {
   private static final String S_I_FAKE = "epRatingFake";
   private static final String S_I_EXCELLENT = "epRatingExcellent";
   private static final String S_I_GOOD = "epRatingGood";
   private static final String S_I_FAIR = "epRatingFair";
   private static final String S_I_POOR = "epRatingPoor";
   public static final EnumRating EXCELLENT = new EnumRating(1, "excellent");
   public static final EnumRating VERY_HIGH = new EnumRating(2, "veryHigh");
   public static final EnumRating HIGH = new EnumRating(4, "high");
   public static final EnumRating NORMAL = new EnumRating(8, "normal");
   public static final EnumRating LOW = new EnumRating(16, "low");
   public static final EnumRating FAKE = new EnumRating(32, "fake");

   private EnumRating(int var1, String var2) {
      super(var1, "e.rating." + var2);
   }

   public static EnumRating intToEnum(int var0) {
      if (var0 > 100) {
         return EXCELLENT;
      } else if (var0 > 50) {
         return VERY_HIGH;
      } else if (var0 > 10) {
         return HIGH;
      } else {
         return var0 > 5 ? NORMAL : LOW;
      }
   }

   public Image getImage() {
      if (this == FAKE) {
         return SResources.getImage("epRatingFake");
      } else if (this == EXCELLENT) {
         return SResources.getImage("epRatingExcellent");
      } else if (this == VERY_HIGH) {
         return SResources.getImage("epRatingExcellent");
      } else if (this == HIGH) {
         return SResources.getImage("epRatingGood");
      } else {
         return this == NORMAL ? SResources.getImage("epRatingFair") : SResources.getImage("epRatingPoor");
      }
   }
}
