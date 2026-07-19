package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumTagType;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Option17 extends Option {
   private boolean advanced;
   private String optionDefault;
   private String optionHelp;

   public Option17(ICore core) {
      super(core);
   }

   public void addPluginOption(String plugin, String description, String name, MessageBuffer buffer) {
      super.addPluginOption(plugin, description, name, buffer);
      synchronized (this) {
         this.optionHelp = buffer.getString();
         this.value = buffer.getString();
         this.optionDefault = buffer.getString();
         this.advanced = buffer.getBool();
      }
   }

   public void addSectionOption(String section, String description, String name, MessageBuffer buffer) {
      super.addSectionOption(section, description, name, buffer);
      synchronized (this) {
         this.optionHelp = buffer.getString();
         this.value = buffer.getString();
         this.optionDefault = buffer.getString();
         this.advanced = buffer.getBool();
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

   protected EnumTagType readType(MessageBuffer buffer) {
      return EnumTagType.stringToEnum(buffer.getString());
   }
}
