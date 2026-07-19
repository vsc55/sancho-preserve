package sancho.view;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.ConsoleMessage;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.console.Console;
import sancho.view.console.ConsoleViewFrame;
import sancho.view.utility.AbstractTab;

public class ConsoleTab extends AbstractTab implements MyObserver {
   private Console console;

   public ConsoleTab(MainWindow mainWindow, String name) {
      super(mainWindow, name);
   }

   protected void createContents(Composite composite) {
      ConsoleViewFrame consoleViewFrame = new ConsoleViewFrame(composite, "tab.console", "tab.console.buttonSmall", this);
      this.addViewFrame(consoleViewFrame);
      this.console = new Console(consoleViewFrame.getChildComposite(), 0);
   }

   public void setInActive() {
      super.setInActive();
      this.console.setInactive();
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getConsoleMessage().deleteObserver(this);
      }
   }

   public void setActive() {
      super.setActive();
      this.setObservers();
      this.console.setActive();
      this.console.setFocus();
   }

   public void setObservers() {
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getConsoleMessage().addObserver(this);
         this.updateConsole(this.getCore().getConsoleMessage().getMessage());
      }
   }

   public void update(MyObservable observable, Object arg, int id) {
      if (observable instanceof ConsoleMessage) {
         this.updateConsole((String)arg);
      }
   }

   public void updateConsole(final String text) {
      if (this.getContent() != null && !this.getContent().isDisposed()) {
         this.getContent().getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (Sancho.hasCollectionFactory() && ConsoleTab.this.console != null && !ConsoleTab.this.console.isDisposed()) {
                  ConsoleTab.this.console.append(text);
               }
            }
         });
      }
   }

   public void onConnect() {
      if (this.isActive()) {
         this.setObservers();
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.console.updateDisplay();
   }

   public void dispose() {
      if (Sancho.hasCollectionFactory()) {
         this.getCore().getConsoleMessage().deleteObserver(this);
      }

      this.console.dispose();
      super.dispose();
   }
}
