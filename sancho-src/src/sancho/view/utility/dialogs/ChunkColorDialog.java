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

   public ChunkColorDialog(Shell shell) {
      super(shell == null ? new Shell() : shell);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(SResources.getString("l.chunkGraphs"));
      shell.setImage(VersionInfo.getProgramIcon());
   }

   protected void buttonPressed(int buttonId) {
      if (buttonId == 0) {
         for (int index = 0; index < this.prefCSList.size(); index++) {
            PrefColorSelector prefColorSelector = (PrefColorSelector)this.prefCSList.get(index);
            prefColorSelector.save();
         }

         PreferenceLoader.saveStore();
         ChunkCanvas.loadColors();
         ChunkImageData.loadColors();
      }

      super.buttonPressed(buttonId);
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      String[] chunkStrings = PreferenceLoader.getChunkStrings();

      for (int i = 0; i < chunkStrings.length; i++) {
         String prefKey = "chunk" + chunkStrings[i].substring(0, 1).toUpperCase() + chunkStrings[i].substring(1);
         String labelKey = "p.d.chunk." + chunkStrings[i];
         Label label = new Label(composite, 0);
         label.setText(SResources.getString(labelKey));
         this.prefCSList.add(new PrefColorSelector(composite, prefKey));
      }

      return composite;
   }

   // One color picker bound to a single chunk-color preference key.
   private static class PrefColorSelector {
      ColorSelector cs;
      String prefString;

      public PrefColorSelector(Composite parent, String prefKey) {
         this.cs = new ColorSelector(parent);
         this.cs.setColorValue(PreferenceLoader.loadRGB(prefKey));
         this.prefString = prefKey;
      }

      public void save() {
         PreferenceLoader.setRGB(this.prefString, this.cs.getColorValue());
      }
   }
}
