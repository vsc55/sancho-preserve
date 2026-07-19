package sancho.view.preferences;

import org.eclipse.jface.preference.PreferenceStore;
import sancho.model.mldonkey.Option;
import sancho.model.mldonkey.OptionCollection;

public class MLDonkeyPreferenceStore extends PreferenceStore {
   private OptionCollection optionCollection;

   public boolean contains(String name) {
      return this.optionCollection.containsKey(name);
   }

   public Option getOption(String name) {
      return (Option)this.optionCollection.get(name);
   }

   public boolean getBoolean(String name) {
      return this.contains(name) ? Boolean.valueOf(this.getOption(name).getValue()) : false;
   }

   public boolean getDefaultBoolean(String name) {
      return this.contains(name) ? Boolean.valueOf(this.getOption(name).getDefaultValue()) : false;
   }

   public int getDefaultInt(String name) {
      try {
         return this.contains(name) ? Integer.parseInt(this.getOption(name).getDefaultValue()) : 0;
      } catch (Exception exception) {
         return 0;
      }
   }

   public String getDefaultString(String name) {
      return this.contains(name) ? this.getOption(name).getDefaultValue() : "";
   }

   public int getInt(String name) {
      try {
         return this.contains(name) ? Integer.parseInt(this.getOption(name).getValue()) : 0;
      } catch (Exception exception) {
         return 0;
      }
   }

   public String getString(String name) {
      return this.contains(name) ? this.getOption(name).getValue() : "";
   }

   public void setInput(OptionCollection optionCollection) {
      this.optionCollection = optionCollection;
   }

   public void setToDefault(String name) {
      this.setValue(name, this.getDefaultString(name));
   }

   public void setValue(String name, boolean value) {
      this.setValue(name, value ? "true" : "false");
   }

   public void setValue(String name, int value) {
      this.setValue(name, String.valueOf(value));
   }

   public void setValue(String name, String value) {
      String currentValue = this.getString(name);
      if (currentValue == null || !currentValue.equals(value)) {
         this.getOption(name).setValue(value);
      }
   }
}
