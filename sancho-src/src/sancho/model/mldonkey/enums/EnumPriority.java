package sancho.model.mldonkey.enums;

public class EnumPriority extends AbstractEnum {
   public static final EnumPriority VERY_LOW = new EnumPriority(1, "very_low", -20);
   public static final EnumPriority LOW = new EnumPriority(2, "low", -10);
   public static final EnumPriority NORMAL = new EnumPriority(4, "normal", 0);
   public static final EnumPriority HIGH = new EnumPriority(8, "high", 10);
   public static final EnumPriority VERY_HIGH = new EnumPriority(16, "very_high", 20);
   int maxValue;

   private EnumPriority(int var1, String var2, int var3) {
      super(var1, "e.priority." + var2);
      this.maxValue = var3;
   }

   public int getMaxValue() {
      return this.maxValue;
   }

   public static EnumPriority intToEnum(int var0) {
      if (var0 < LOW.getMaxValue()) {
         return VERY_LOW;
      } else if (var0 < NORMAL.getMaxValue()) {
         return LOW;
      } else if (var0 == NORMAL.getMaxValue()) {
         return NORMAL;
      } else {
         return var0 <= HIGH.getMaxValue() ? HIGH : VERY_HIGH;
      }
   }
}
