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

   public void createControl(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      composite.setLayoutData(new GridData(768));
      Label label = new Label(composite, 0);
      label.setText(SResources.getString("p.coreExecutableInfo"));
      GridData gridData = new GridData(768);
      label.setLayoutData(gridData);
      Composite inputComposite = new Composite(composite, 0);
      inputComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 5, 5, false));
      inputComposite.setLayoutData(new GridData(768));
      this.text = new Text(inputComposite, 2052);
      this.text.setLayoutData(new GridData(768));
      Button browseButton = new Button(inputComposite, 0);
      browseButton.setText(SResources.getString("b.browse"));
      browseButton.setLayoutData(new GridData());
      browseButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            FileDialog fileDialog = new FileDialog(CoreBinaryPage.this.getShell(), 0);
            fileDialog.setFilterExtensions(new String[]{"*"});
            String path;
            if ((path = fileDialog.open()) != null) {
               CoreBinaryPage.this.text.setText(path);
            }
         }
      });
      this.setControl(composite);
   }

   public void saveData() {
      if (this.text != null && !this.text.isDisposed() && !this.text.getText().equals("")) {
         PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
         preferenceStore.setValue("coreExecutable", this.text.getText());
         PreferenceLoader.saveStore();
      }
   }
}
