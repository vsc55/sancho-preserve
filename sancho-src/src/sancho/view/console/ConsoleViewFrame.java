package sancho.view.console;

import java.util.StringTokenizer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class ConsoleViewFrame extends ViewFrame {
   int numToolItems;

   public ConsoleViewFrame(Composite composite, String prefString, String imageName, AbstractTab tab) {
      super(composite, prefString, imageName, tab);
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.numToolItems = PreferenceLoader.loadInt("consoleToolItems");

      for (int i = 1; i < this.numToolItems + 1; i++) {
         final int toolItemNumber = i;
         this.addToolItem(PreferenceLoader.loadString("consoleToolItem" + i), String.valueOf(i), new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               String command = PreferenceLoader.loadString("consoleToolItem" + toolItemNumber);
               if (!command.equals("")) {
                  if (command.indexOf(";") != -1) {
                     StringTokenizer tokenizer = new StringTokenizer(command, ";");

                     while (tokenizer.hasMoreTokens()) {
                        Sancho.send((short)29, tokenizer.nextToken());
                     }
                  } else {
                     Sancho.send((short)29, command);
                  }
               }
            }
         });
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      if (this.numToolItems != PreferenceLoader.loadInt("consoleToolItems") && this.toolBar != null) {
         for (int i = this.toolBar.getItemCount() - 1; i >= 0; i--) {
            this.toolBar.getItems()[i].dispose();
         }

         this.toolBar.dispose();
         this.createViewToolBar();
         this.toolBar.getParent().layout();
      } else if (this.toolBar != null) {
         for (int i = 1; i <= this.toolBar.getItemCount(); i++) {
            this.toolBar.getItems()[i - 1].setToolTipText(PreferenceLoader.loadString("consoleToolItem" + i));
         }
      }
   }
}
