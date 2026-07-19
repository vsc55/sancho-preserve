package sancho.view.utility.dialogs;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.transfer.ChunkCanvas;
import sancho.view.transfer.ChunkImageData;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class ChunkColorDialog extends Dialog {
   private List prefCSList = new ArrayList();

   public ChunkColorDialog(Shell var1) {
      super(var1 == null ? new Shell() : var1);
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(SResources.getString("l.chunkGraphs"));
      var1.setImage(VersionInfo.getProgramIcon());
   }

   protected void buttonPressed(int var1) {
      if (var1 == 0) {
         for (int var3 = 0; var3 < this.prefCSList.size(); var3++) {
            PrefColorSelector var2 = (PrefColorSelector)this.prefCSList.get(var3);
            var2.save();
         }

         PreferenceLoader.saveStore();
         ChunkCanvas.loadColors();
         ChunkImageData.loadColors();
      }

      super.buttonPressed(var1);
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      String[] var3 = PreferenceLoader.getChunkStrings();

      for (int var4 = 0; var4 < var3.length; var4++) {
         String var5 = "chunk" + var3[var4].substring(0, 1).toUpperCase() + var3[var4].substring(1);
         String var6 = "p.d.chunk." + var3[var4];
         Label var7 = new Label(var2, 0);
         var7.setText(SResources.getString(var6));
         this.prefCSList.add(new PrefColorSelector(var2, var5));
      }

      return var2;
   }

   // One color picker bound to a single chunk-color preference key.
   private static class PrefColorSelector {
      ColorSelector cs;
      String prefString;

      public PrefColorSelector(Composite var1, String var2) {
         this.cs = new ColorSelector(var1);
         this.cs.setColorValue(PreferenceLoader.loadRGB(var2));
         this.prefString = var2;
      }

      public void save() {
         PreferenceLoader.setRGB(this.prefString, this.cs.getColorValue());
      }
   }
}
