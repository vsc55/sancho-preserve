package sancho.view.preferences;

import org.eclipse.jface.preference.PreferenceStore;
import sancho.model.mldonkey.Option;
import sancho.model.mldonkey.OptionCollection;

public class MLDonkeyPreferenceStore extends PreferenceStore {
   private OptionCollection optionCollection;

   public boolean contains(String var1) {
      return this.optionCollection.containsKey(var1);
   }

   public Option getOption(String var1) {
      return (Option)this.optionCollection.get(var1);
   }

   public boolean getBoolean(String var1) {
      return this.contains(var1) ? Boolean.valueOf(this.getOption(var1).getValue()) : false;
   }

   public boolean getDefaultBoolean(String var1) {
      return this.contains(var1) ? Boolean.valueOf(this.getOption(var1).getDefaultValue()) : false;
   }

   public int getDefaultInt(String var1) {
      try {
         return this.contains(var1) ? Integer.parseInt(this.getOption(var1).getDefaultValue()) : 0;
      } catch (Exception var3) {
         return 0;
      }
   }

   public String getDefaultString(String var1) {
      return this.contains(var1) ? this.getOption(var1).getDefaultValue() : "";
   }

   public int getInt(String var1) {
      try {
         return this.contains(var1) ? Integer.parseInt(this.getOption(var1).getValue()) : 0;
      } catch (Exception var3) {
         return 0;
      }
   }

   public String getString(String var1) {
      return this.contains(var1) ? this.getOption(var1).getValue() : "";
   }

   public void setInput(OptionCollection var1) {
      this.optionCollection = var1;
   }

   public void setToDefault(String var1) {
      this.setValue(var1, this.getDefaultString(var1));
   }

   public void setValue(String var1, boolean var2) {
      this.setValue(var1, var2 ? "true" : "false");
   }

   public void setValue(String var1, int var2) {
      this.setValue(var1, String.valueOf(var2));
   }

   public void setValue(String var1, String var2) {
      String var3 = this.getString(var1);
      if (var3 == null || !var3.equals(var2)) {
         this.getOption(var1).setValue(var2);
      }
   }
}
