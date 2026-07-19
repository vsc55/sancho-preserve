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

   Option(ICore core) {
      super(core);
   }

   public synchronized void addPluginOption(String plugin, String description, String name, MessageBuffer buffer) {
      this.plugin = plugin;
      this.description = description;
      this.name = name;
      this.type = this.readType(buffer);
   }

   public synchronized void addSectionOption(String section, String description, String name, MessageBuffer buffer) {
      this.section = section;
      this.description = description;
      this.name = name;
      this.type = this.readType(buffer);
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

   public void read(MessageBuffer buffer) {
      this.read(buffer.getString(), buffer);
   }

   public void read(String name, MessageBuffer buffer) {
      synchronized (this) {
         this.name = name;
         this.value = buffer.getString();
      }
   }

   protected EnumTagType readType(MessageBuffer buffer) {
      return EnumTagType.optionByteToEnum(buffer.getByte());
   }

   public void setValue(String value) {
      this.core.send((short)28, new Object[]{this.getName(), value});
   }
}
