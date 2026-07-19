package sancho.view.utility.setupWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class WelcomePage extends WizardPage {
   public WelcomePage() {
      super("welcomePage");
      this.setTitle(SResources.getString("hm.welcome"));
      this.setMessage(SResources.getString("hm.configureCore"));
   }

   public void createControl(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      Label label = new Label(composite, 0);
      Image image = SResources.getImage("welcome");
      label.setImage(image);
      label.setLayoutData(new GridData(576));
      CLabel prefFileLabel = new CLabel(composite, 64);
      GridData gridData = new GridData(64);
      gridData.widthHint = image.getBounds().width;
      prefFileLabel.setLayoutData(gridData);
      prefFileLabel.setText(SResources.getString("hm.prefFile") + " " + PreferenceLoader.getPrefFile());
      this.setControl(composite);
   }
}
