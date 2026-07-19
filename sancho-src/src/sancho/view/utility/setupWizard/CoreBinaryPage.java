package sancho.view.utility.setupWizard;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class CoreBinaryPage extends WizardPage {
   Text text;

   public CoreBinaryPage() {
      super("hostPage");
      this.setTitle(SResources.getString("hm.coreSettings"));
      this.setMessage(SResources.getString("hm.info"));
   }

   public void createControl(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      var2.setLayoutData(new GridData(768));
      Label var3 = new Label(var2, 0);
      var3.setText(SResources.getString("p.coreExecutableInfo"));
      GridData var4 = new GridData(768);
      var3.setLayoutData(var4);
      Composite var5 = new Composite(var2, 0);
      var5.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 5, 5, false));
      var5.setLayoutData(new GridData(768));
      this.text = new Text(var5, 2052);
      this.text.setLayoutData(new GridData(768));
      Button var6 = new Button(var5, 0);
      var6.setText(SResources.getString("b.browse"));
      var6.setLayoutData(new GridData());
      var6.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            FileDialog var2 = new FileDialog(CoreBinaryPage.this.getShell(), 0);
            var2.setFilterExtensions(new String[]{"*"});
            String var3;
            if ((var3 = var2.open()) != null) {
               CoreBinaryPage.this.text.setText(var3);
            }
         }
      });
      this.setControl(var2);
   }

   public void saveData() {
      if (this.text != null && !this.text.isDisposed() && !this.text.getText().equals("")) {
         PreferenceStore var1 = PreferenceLoader.getPreferenceStore();
         var1.setValue("coreExecutable", this.text.getText());
         PreferenceLoader.saveStore();
      }
   }
}
