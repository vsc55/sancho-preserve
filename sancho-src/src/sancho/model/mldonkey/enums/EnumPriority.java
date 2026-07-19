package sancho.model.mldonkey.enums;

public class EnumPriority extends AbstractEnum {
   public static final EnumPriority VERY_LOW = new EnumPriority(1, "very_low", -20);
   public static final EnumPriority LOW = new EnumPriority(2, "low", -10);
   public static final EnumPriority NORMAL = new EnumPriority(4, "normal", 0);
   public static final EnumPriority HIGH = new EnumPriority(8, "high", 10);
   public static final EnumPriority VERY_HIGH = new EnumPriority(16, "very_high", 20);
   int maxValue;

   private EnumPriority(int value, String name, int maxValue) {
      super(value, "e.priority." + name);
      this.maxValue = maxValue;
   }

   public int getMaxValue() {
      return this.maxValue;
   }

   public static EnumPriority intToEnum(int value) {
      if (value < LOW.getMaxValue()) {
         return VERY_LOW;
      } else if (value < NORMAL.getMaxValue()) {
         return LOW;
      } else if (value == NORMAL.getMaxValue()) {
         return NORMAL;
      } else {
         return value <= HIGH.getMaxValue() ? HIGH : VERY_HIGH;
      }
   }
}
