package sancho.view.utility.setupWizard;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.BSpinner;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class HostPage extends WizardPage {
   Combo core;
   Text desc;
   Group group;
   Text host;
   List hostArray = new ArrayList();
   int initialNumHosts;
   org.eclipse.swt.widgets.List list;
   Text pass;
   BSpinner port;
   Text user;
   int hm_num;
   Button ask_pass;

   public HostPage() {
      super("hostPage");
      this.setTitle(SResources.getString("hm.coreSettings"));
      this.setMessage(SResources.getString("hm.info"));
   }

   public void addAsNew() {
      HostObject var1 = new HostObject();
      this.saveCurrent(var1);
      this.hostArray.add(var1);
      this.fillList();
      this.list.setSelection(this.list.getItemCount() - 1);
      this.resetInfo();
   }

   public Label addLabel(Composite var1, String var2) {
      Label var3 = new Label(var1, 0);
      var3.setText(var2);
      var3.setLayoutData(new GridData());
      return var3;
   }

   public void createControl(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 0, 0, false));
      this.loadHosts();
      this.createMyControl(var2);
      this.fillList();
      this.list.setSelection(0);
      this.resetInfo();
      this.setControl(var2);
   }

   public Combo createCore(Composite var1, String var2, String var3) {
      this.addLabel(var1, var2);
      Combo var4 = new Combo(var1, 2056);
      var4.add("mldonkey");
      var4.select(0);
      var4.setLayoutData(new GridData(768));
      this.addLabel(var1, var3);
      return var4;
   }

   protected void createMyControl(Composite var1) {
      Group var3 = new Group(var1, 16);
      var3.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      GridData var4 = new GridData(768);
      var4.horizontalSpan = 2;
      var3.setLayoutData(var4);
      Label var5 = new Label(var3, 0);
      var5.setText(SResources.getString("hm.message"));
      var5.setLayoutData(new GridData(768));
      this.group = new Group(var1, 16);
      this.group.setText(SResources.getString("hm.hostSettings"));
      this.group.setLayout(WidgetFactory.createGridLayout(3, 5, 5, 5, 2, false));
      this.group.setLayoutData(new GridData(768));
      this.host = this.createText(this.group, SResources.getString("hm.host"), SResources.getString("l.default") + " = 127.0.0.1");
      this.port = this.createPort(this.group, SResources.getString("hm.port"), SResources.getString("l.default") + " = 4001");
      this.user = this.createText(this.group, SResources.getString("hm.username"), SResources.getString("l.default") + " = admin");
      this.pass = this.createText(
         this.group, SResources.getString("hm.password"), SResources.getString("l.default") + " = " + SResources.getString("l.empty"), true
      );
      this.ask_pass = this.createCheck(this.group, SResources.getString("hm.ask_pass"), "");
      this.desc = this.createText(
         this.group, SResources.getString("hm.description"), SResources.getString("l.default") + " = " + SResources.getString("l.empty")
      );
      this.core = this.createCore(this.group, SResources.getString("hm.protocol"), SResources.getString("l.default") + " = mldonkey");
      Composite var6 = new Composite(var1, 0);
      var6.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      var6.setLayoutData(new GridData(768));
      this.list = new org.eclipse.swt.widgets.List(var6, 2816);
      GridData var2 = new GridData(1808);
      var2.heightHint = 60;
      var2.widthHint = 120;
      var2.horizontalSpan = 2;
      this.list.setLayoutData(var2);
      this.list.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            resetInfo();
         }
      });
      Button var7 = new Button(var6, 8);
      var7.setLayoutData(new GridData(256));
      var7.setText(SResources.getString("b.addAsNewEntry"));
      var7.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            addAsNew();
         }
      });
      Button var8 = new Button(var6, 8);
      var8.setLayoutData(new GridData(256));
      var8.setText(SResources.getString("b.deleteEntry"));
      var8.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (list.getSelectionIndex() != 0) {
               removeCurrent();
            }
         }
      });
      Button var9 = new Button(var6, 8);
      var4 = new GridData(768);
      var4.horizontalSpan = 2;
      var9.setLayoutData(var4);
      var9.setText(SResources.getString("b.saveAsCurrent"));
      var9.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            saveAsCurrent();
         }
      });
      Button var10 = new Button(var6, 8);
      var4 = new GridData(768);
      var4.horizontalSpan = 2;
      var10.setLayoutData(var4);
      var10.setText(SResources.getString("b.makeDefault"));
      var10.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (list.getSelectionIndex() != 0) {
               makeDefault();
            }
         }
      });
   }

   public BSpinner createPort(Composite var1, String var2, String var3) {
      this.addLabel(var1, var2);
      BSpinner var4 = new BSpinner(var1, 2048);
      var4.setMaximum(65536);
      var4.setLayoutData(new GridData(768));
      this.addLabel(var1, var3);
      return var4;
   }

   public SpinnerLabel createPort2(Composite var1, String var2, String var3) {
      SpinnerLabel var4 = new SpinnerLabel();
      var4.addLabel(this.addLabel(var1, var2));
      BSpinner var5 = new BSpinner(var1, 2048);
      var5.setMaximum(65536);
      var5.setLayoutData(new GridData(768));
      var4.addSpinner(var5);
      this.addLabel(var1, var3);
      return var4;
   }

   public Button createCheck(Composite var1, String var2, String var3) {
      this.addLabel(var1, "");
      Composite var4 = new Composite(var1, 0);
      GridData var5 = new GridData(768);
      var5.horizontalSpan = 2;
      var4.setLayoutData(var5);
      var4.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 5, 0, false));
      Button var6 = new Button(var4, 32);
      this.addLabel(var4, var2);
      return var6;
   }

   public Text createText(Composite var1, String var2, String var3) {
      return this.createText(var1, var2, var3, false);
   }

   public Text createText(Composite var1, String var2, String var3, boolean var4) {
      this.addLabel(var1, var2);
      Text var5 = new Text(var1, 2052 | (var4 ? 4194304 : 0));
      var5.setLayoutData(new GridData(768));
      this.addLabel(var1, var3);
      return var5;
   }

   public TextLabel createText2(Composite var1, String var2, String var3) {
      return this.createText2(var1, var2, var3, false);
   }

   public TextLabel createText2(Composite var1, String var2, String var3, boolean var4) {
      TextLabel var5 = new TextLabel();
      var5.addLabel(this.addLabel(var1, var2));
      Text var6 = new Text(var1, 2052 | (var4 ? 4194304 : 0));
      var6.setLayoutData(new GridData(768));
      this.addLabel(var1, var3);
      var5.addText(var6);
      return var5;
   }

   public void fillList() {
      this.list.removeAll();

      for (int var1 = 0; var1 < this.hostArray.size(); var1++) {
         HostObject var2 = (HostObject)this.hostArray.get(var1);
         String var3 = var2.description.equals("") ? var2.hostname + ":" + var2.port : var2.description;
         this.list.add(var1 == 0 ? SResources.getString("l.default") + "(" + var3 + ")" : var3);
      }
   }

   public void loadHost(HostObject var1, int var2) {
      var1.hostname = PreferenceLoader.loadString("hm_" + var2 + "_hostname");
      var1.username = PreferenceLoader.loadString("hm_" + var2 + "_username");
      var1.password = PreferenceLoader.loadString("hm_" + var2 + "_password");
      var1.port = PreferenceLoader.loadInt("hm_" + var2 + "_port");
      var1.description = PreferenceLoader.loadString("hm_" + var2 + "_description");
      var1.coreProtocol = PreferenceLoader.loadInt("hm_" + var2 + "_coreProtocol");
      var1.ask_pass = PreferenceLoader.loadBoolean("hm_" + var2 + "_ask_pass");
   }

   public void loadHosts() {
      for (int var1 = 0; var1 <= 0 || PreferenceLoader.contains("hm_" + var1 + "_hostname"); var1++) {
         HostObject var2 = new HostObject();
         this.loadHost(var2, var1);
         this.hostArray.add(var2);
      }

      this.initialNumHosts = this.hostArray.size();
   }

   public void makeDefault() {
      HostObject var1 = (HostObject)this.hostArray.get(this.list.getSelectionIndex());
      this.hostArray.remove(this.list.getSelectionIndex());
      this.hostArray.add(0, var1);
      this.fillList();
      this.list.setSelection(0);
      this.resetInfo();
   }

   public void removeCurrent() {
      int var1 = this.list.getSelectionIndex();
      this.hostArray.remove(var1);
      this.fillList();
      int var2 = this.list.getItemCount() > var1 ? var1 : var1 - 1;
      this.list.setSelection(var2);
      this.resetInfo();
   }

   public void resetInfo() {
      this.hm_num = this.list.getSelectionIndex();
      HostObject var1 = (HostObject)this.hostArray.get(this.hm_num);
      this.resetInfo(var1);
   }

   public void resetInfo(HostObject var1) {
      this.host.setText(var1.hostname);
      this.port.setSelection(var1.port);
      this.user.setText(var1.username);
      this.pass.setText(var1.password);
      this.desc.setText(var1.description);
      this.core.select(var1.coreProtocol);
      this.ask_pass.setSelection(var1.ask_pass);
      String var2 = var1.description.equals("") ? var1.hostname + ":" + var1.port : var1.description;
      this.group.setText(SResources.getString("hm.hostSettings") + var2);
   }

   public void saveAsCurrent() {
      int var1 = this.list.getSelectionIndex();
      HostObject var2 = (HostObject)this.hostArray.get(this.list.getSelectionIndex());
      this.saveCurrent(var2);
      this.fillList();
      this.list.setSelection(var1);
      this.resetInfo();
   }

   public void saveCurrent(HostObject var1) {
      var1.hostname = this.host.getText();
      var1.password = this.pass.getText();
      var1.username = this.user.getText();
      var1.port = this.port.getSelection();
      var1.description = this.desc.getText();
      var1.coreProtocol = this.core.getSelectionIndex();
      var1.ask_pass = this.ask_pass.getSelection();
   }

   public void saveData() {
      this.saveAsCurrent();
      PreferenceStore var1 = PreferenceLoader.getPreferenceStore();
      int var2 = Math.max(this.initialNumHosts, this.hostArray.size());

      for (int var3 = 0; var3 < var2; var3++) {
         if (var3 < this.hostArray.size()) {
            HostObject var4 = (HostObject)this.hostArray.get(var3);
            this.setValue(var1, var3, var4);
         } else {
            this.setToDefault(var1, var3);
         }
      }

      PreferenceLoader.saveStore();
   }

   public void setToDefault(PreferenceStore var1, int var2) {
      var1.setToDefault("hm_" + var2 + "_hostname");
      var1.setToDefault("hm_" + var2 + "_port");
      var1.setToDefault("hm_" + var2 + "_username");
      var1.setToDefault("hm_" + var2 + "_password");
      var1.setToDefault("hm_" + var2 + "_description");
      var1.setToDefault("hm_" + var2 + "_coreProtocol");
      var1.setToDefault("hm_" + var2 + "_ask_pass");
   }

   public void setValue(PreferenceStore var1, int var2, HostObject var3) {
      var1.setValue("hm_" + var2 + "_hostname", var3.hostname);
      var1.setValue("hm_" + var2 + "_port", var3.port);
      var1.setValue("hm_" + var2 + "_username", var3.username);
      var1.setValue("hm_" + var2 + "_password", var3.password);
      var1.setValue("hm_" + var2 + "_description", var3.description);
      var1.setValue("hm_" + var2 + "_coreProtocol", var3.coreProtocol);
      var1.setValue("hm_" + var2 + "_ask_pass", var3.ask_pass);
   }

   public int getNum() {
      return this.hm_num;
   }

   // Pairs a BSpinner with its Label so both can be toggled/queried together.
   public static class SpinnerLabel {
      BSpinner spinner;
      Label label;

      public void addSpinner(BSpinner var1) {
         this.spinner = var1;
      }

      public void addLabel(Label var1) {
         this.label = var1;
      }

      public void setSelection(int var1) {
         this.spinner.setSelection(var1);
      }

      public int getSelection() {
         return this.spinner.getSelection();
      }

      public void setEnabled(boolean var1) {
         this.spinner.setEnabled(var1);
         this.label.setEnabled(var1);
      }
   }

   // Pairs a Text field with its Label so both can be toggled/queried together.
   public static class TextLabel {
      Text text;
      Label label;

      public void setText(String var1) {
         this.text.setText(var1);
      }

      public void addText(Text var1) {
         this.text = var1;
      }

      public void addLabel(Label var1) {
         this.label = var1;
      }

      public String getText() {
         return this.text.getText();
      }

      public void setEchoChar(char var1) {
         this.text.setEchoChar(var1);
      }

      public void setEnabled(boolean var1) {
         this.text.setEnabled(var1);
         this.label.setEnabled(var1);
      }
   }
}
