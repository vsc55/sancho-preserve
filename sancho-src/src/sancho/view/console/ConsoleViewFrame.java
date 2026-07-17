package sancho.view.console;

import org.eclipse.swt.widgets.Composite;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class ConsoleViewFrame extends ViewFrame {
   int numToolItems;

   public ConsoleViewFrame(Composite var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.numToolItems = PreferenceLoader.loadInt("consoleToolItems");

      for (int var1 = 1; var1 < this.numToolItems + 1; var1++) {
         this.addToolItem(PreferenceLoader.loadString("consoleToolItem" + var1), String.valueOf(var1), new ConsoleViewFrame$1(this, var1));
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      if (this.numToolItems != PreferenceLoader.loadInt("consoleToolItems") && this.toolBar != null) {
         for (int var2 = this.toolBar.getItemCount() - 1; var2 >= 0; var2--) {
            this.toolBar.getItems()[var2].dispose();
         }

         this.toolBar.dispose();
         this.createViewToolBar();
         this.toolBar.getParent().layout();
      } else if (this.toolBar != null) {
         for (int var1 = 1; var1 <= this.toolBar.getItemCount(); var1++) {
            this.toolBar.getItems()[var1 - 1].setToolTipText(PreferenceLoader.loadString("consoleToolItem" + var1));
         }
      }
   }
}
