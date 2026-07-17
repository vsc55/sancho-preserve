package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumTagType;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Option extends AObject {
   protected String description;
   protected String name;
   protected String plugin;
   protected String section;
   protected EnumTagType type;
   protected String value;

   Option(ICore var1) {
      super(var1);
   }

   public synchronized void addPluginOption(String var1, String var2, String var3, MessageBuffer var4) {
      this.plugin = var1;
      this.description = var2;
      this.name = var3;
      this.type = this.readType(var4);
   }

   public synchronized void addSectionOption(String var1, String var2, String var3, MessageBuffer var4) {
      this.section = var1;
      this.description = var2;
      this.name = var3;
      this.type = this.readType(var4);
   }

   public String getDefaultValue() {
      return "";
   }

   public synchronized String getDescription() {
      return this.description != null ? this.description : "";
   }

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public synchronized String getPlugin() {
      return this.plugin;
   }

   public synchronized String getSection() {
      return this.section;
   }

   public synchronized EnumTagType getType() {
      return this.type;
   }

   public synchronized String getValue() {
      return this.value;
   }

   public boolean isAdvanced() {
      return false;
   }

   public void read(MessageBuffer var1) {
      this.read(var1.getString(), var1);
   }

   public void read(String var1, MessageBuffer var2) {
      synchronized (this) {
         this.name = var1;
         this.value = var2.getString();
      }
   }

   protected EnumTagType readType(MessageBuffer var1) {
      return EnumTagType.optionByteToEnum(var1.getByte());
   }

   public void setValue(String var1) {
      this.core.send((short)28, new Object[]{this.getName(), var1});
   }
}
