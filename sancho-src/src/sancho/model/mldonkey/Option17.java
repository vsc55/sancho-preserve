package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumTagType;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Option17 extends Option {
   private boolean advanced;
   private String optionDefault;
   private String optionHelp;

   public Option17(ICore var1) {
      super(var1);
   }

   public void addPluginOption(String var1, String var2, String var3, MessageBuffer var4) {
      super.addPluginOption(var1, var2, var3, var4);
      synchronized (this) {
         this.optionHelp = var4.getString();
         this.value = var4.getString();
         this.optionDefault = var4.getString();
         this.advanced = var4.getBool();
      }
   }

   public void addSectionOption(String var1, String var2, String var3, MessageBuffer var4) {
      super.addSectionOption(var1, var2, var3, var4);
      synchronized (this) {
         this.optionHelp = var4.getString();
         this.value = var4.getString();
         this.optionDefault = var4.getString();
         this.advanced = var4.getBool();
      }
   }

   public synchronized String getDefaultValue() {
      return this.optionDefault;
   }

   public synchronized String getDescription() {
      return this.optionHelp;
   }

   public synchronized boolean isAdvanced() {
      return this.advanced;
   }

   protected EnumTagType readType(MessageBuffer var1) {
      return EnumTagType.stringToEnum(var1.getString());
   }
}
