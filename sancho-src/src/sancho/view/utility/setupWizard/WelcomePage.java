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

   public void createControl(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      Label var3 = new Label(var2, 0);
      Image var4 = SResources.getImage("welcome");
      var3.setImage(var4);
      var3.setLayoutData(new GridData(576));
      CLabel var5 = new CLabel(var2, 64);
      GridData var6 = new GridData(64);
      var6.widthHint = var4.getBounds().width;
      var5.setLayoutData(var6);
      var5.setText(SResources.getString("hm.prefFile") + " " + PreferenceLoader.getPrefFile());
      this.setControl(var2);
   }
}
