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
      HostObject hostObject = new HostObject();
      this.saveCurrent(hostObject);
      this.hostArray.add(hostObject);
      this.fillList();
      this.list.setSelection(this.list.getItemCount() - 1);
      this.resetInfo();
   }

   public Label addLabel(Composite composite, String text) {
      Label label = new Label(composite, 0);
      label.setText(text);
      label.setLayoutData(new GridData());
      return label;
   }

   public void createControl(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 0, 0, false));
      this.loadHosts();
      this.createMyControl(composite);
      this.fillList();
      this.list.setSelection(0);
      this.resetInfo();
      this.setControl(composite);
   }

   public Combo createCore(Composite composite, String labelText, String hintText) {
      this.addLabel(composite, labelText);
      Combo combo = new Combo(composite, 2056);
      combo.add("mldonkey");
      combo.select(0);
      combo.setLayoutData(new GridData(768));
      this.addLabel(composite, hintText);
      return combo;
   }

   protected void createMyControl(Composite parent) {
      Group messageGroup = new Group(parent, 16);
      messageGroup.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      messageGroup.setLayoutData(gridData);
      Label label = new Label(messageGroup, 0);
      label.setText(SResources.getString("hm.message"));
      label.setLayoutData(new GridData(768));
      this.group = new Group(parent, 16);
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
      Composite listComposite = new Composite(parent, 0);
      listComposite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      listComposite.setLayoutData(new GridData(768));
      this.list = new org.eclipse.swt.widgets.List(listComposite, 2816);
      GridData listGridData = new GridData(1808);
      listGridData.heightHint = 60;
      listGridData.widthHint = 120;
      listGridData.horizontalSpan = 2;
      this.list.setLayoutData(listGridData);
      this.list.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            resetInfo();
         }
      });
      Button addButton = new Button(listComposite, 8);
      addButton.setLayoutData(new GridData(256));
      addButton.setText(SResources.getString("b.addAsNewEntry"));
      addButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            addAsNew();
         }
      });
      Button deleteButton = new Button(listComposite, 8);
      deleteButton.setLayoutData(new GridData(256));
      deleteButton.setText(SResources.getString("b.deleteEntry"));
      deleteButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (list.getSelectionIndex() != 0) {
               removeCurrent();
            }
         }
      });
      Button saveButton = new Button(listComposite, 8);
      gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      saveButton.setLayoutData(gridData);
      saveButton.setText(SResources.getString("b.saveAsCurrent"));
      saveButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            saveAsCurrent();
         }
      });
      Button defaultButton = new Button(listComposite, 8);
      gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      defaultButton.setLayoutData(gridData);
      defaultButton.setText(SResources.getString("b.makeDefault"));
      defaultButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            if (list.getSelectionIndex() != 0) {
               makeDefault();
            }
         }
      });
   }

   public BSpinner createPort(Composite composite, String labelText, String hintText) {
      this.addLabel(composite, labelText);
      BSpinner spinner = new BSpinner(composite, 2048);
      spinner.setMaximum(65536);
      spinner.setLayoutData(new GridData(768));
      this.addLabel(composite, hintText);
      return spinner;
   }

   public SpinnerLabel createPort2(Composite composite, String labelText, String hintText) {
      SpinnerLabel spinnerLabel = new SpinnerLabel();
      spinnerLabel.addLabel(this.addLabel(composite, labelText));
      BSpinner spinner = new BSpinner(composite, 2048);
      spinner.setMaximum(65536);
      spinner.setLayoutData(new GridData(768));
      spinnerLabel.addSpinner(spinner);
      this.addLabel(composite, hintText);
      return spinnerLabel;
   }

   public Button createCheck(Composite composite, String labelText, String hintText) {
      this.addLabel(composite, "");
      Composite checkComposite = new Composite(composite, 0);
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      checkComposite.setLayoutData(gridData);
      checkComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 5, 0, false));
      Button button = new Button(checkComposite, 32);
      this.addLabel(checkComposite, labelText);
      return button;
   }

   public Text createText(Composite composite, String labelText, String hintText) {
      return this.createText(composite, labelText, hintText, false);
   }

   public Text createText(Composite composite, String labelText, String hintText, boolean isPassword) {
      this.addLabel(composite, labelText);
      Text text = new Text(composite, 2052 | (isPassword ? 4194304 : 0));
      text.setLayoutData(new GridData(768));
      this.addLabel(composite, hintText);
      return text;
   }

   public TextLabel createText2(Composite composite, String labelText, String hintText) {
      return this.createText2(composite, labelText, hintText, false);
   }

   public TextLabel createText2(Composite composite, String labelText, String hintText, boolean isPassword) {
      TextLabel textLabel = new TextLabel();
      textLabel.addLabel(this.addLabel(composite, labelText));
      Text text = new Text(composite, 2052 | (isPassword ? 4194304 : 0));
      text.setLayoutData(new GridData(768));
      this.addLabel(composite, hintText);
      textLabel.addText(text);
      return textLabel;
   }

   public void fillList() {
      this.list.removeAll();

      for (int i = 0; i < this.hostArray.size(); i++) {
         HostObject hostObject = (HostObject)this.hostArray.get(i);
         String label = hostObject.description.equals("") ? hostObject.hostname + ":" + hostObject.port : hostObject.description;
         this.list.add(i == 0 ? SResources.getString("l.default") + "(" + label + ")" : label);
      }
   }

   public void loadHost(HostObject hostObject, int index) {
      hostObject.hostname = PreferenceLoader.loadString("hm_" + index + "_hostname");
      hostObject.username = PreferenceLoader.loadString("hm_" + index + "_username");
      hostObject.password = PreferenceLoader.loadString("hm_" + index + "_password");
      hostObject.port = PreferenceLoader.loadInt("hm_" + index + "_port");
      hostObject.description = PreferenceLoader.loadString("hm_" + index + "_description");
      hostObject.coreProtocol = PreferenceLoader.loadInt("hm_" + index + "_coreProtocol");
      hostObject.ask_pass = PreferenceLoader.loadBoolean("hm_" + index + "_ask_pass");
   }

   public void loadHosts() {
      for (int index = 0; index <= 0 || PreferenceLoader.contains("hm_" + index + "_hostname"); index++) {
         HostObject hostObject = new HostObject();
         this.loadHost(hostObject, index);
         this.hostArray.add(hostObject);
      }

      this.initialNumHosts = this.hostArray.size();
   }

   public void makeDefault() {
      HostObject hostObject = (HostObject)this.hostArray.get(this.list.getSelectionIndex());
      this.hostArray.remove(this.list.getSelectionIndex());
      this.hostArray.add(0, hostObject);
      this.fillList();
      this.list.setSelection(0);
      this.resetInfo();
   }

   public void removeCurrent() {
      int index = this.list.getSelectionIndex();
      this.hostArray.remove(index);
      this.fillList();
      int selection = this.list.getItemCount() > index ? index : index - 1;
      this.list.setSelection(selection);
      this.resetInfo();
   }

   public void resetInfo() {
      this.hm_num = this.list.getSelectionIndex();
      HostObject hostObject = (HostObject)this.hostArray.get(this.hm_num);
      this.resetInfo(hostObject);
   }

   public void resetInfo(HostObject hostObject) {
      this.host.setText(hostObject.hostname);
      this.port.setSelection(hostObject.port);
      this.user.setText(hostObject.username);
      this.pass.setText(hostObject.password);
      this.desc.setText(hostObject.description);
      this.core.select(hostObject.coreProtocol);
      this.ask_pass.setSelection(hostObject.ask_pass);
      String label = hostObject.description.equals("") ? hostObject.hostname + ":" + hostObject.port : hostObject.description;
      this.group.setText(SResources.getString("hm.hostSettings") + label);
   }

   public void saveAsCurrent() {
      int index = this.list.getSelectionIndex();
      HostObject hostObject = (HostObject)this.hostArray.get(this.list.getSelectionIndex());
      this.saveCurrent(hostObject);
      this.fillList();
      this.list.setSelection(index);
      this.resetInfo();
   }

   public void saveCurrent(HostObject hostObject) {
      hostObject.hostname = this.host.getText();
      hostObject.password = this.pass.getText();
      hostObject.username = this.user.getText();
      hostObject.port = this.port.getSelection();
      hostObject.description = this.desc.getText();
      hostObject.coreProtocol = this.core.getSelectionIndex();
      hostObject.ask_pass = this.ask_pass.getSelection();
   }

   public void saveData() {
      this.saveAsCurrent();
      PreferenceStore store = PreferenceLoader.getPreferenceStore();
      int count = Math.max(this.initialNumHosts, this.hostArray.size());

      for (int i = 0; i < count; i++) {
         if (i < this.hostArray.size()) {
            HostObject hostObject = (HostObject)this.hostArray.get(i);
            this.setValue(store, i, hostObject);
         } else {
            this.setToDefault(store, i);
         }
      }

      PreferenceLoader.saveStore();
   }

   public void setToDefault(PreferenceStore store, int index) {
      store.setToDefault("hm_" + index + "_hostname");
      store.setToDefault("hm_" + index + "_port");
      store.setToDefault("hm_" + index + "_username");
      store.setToDefault("hm_" + index + "_password");
      store.setToDefault("hm_" + index + "_description");
      store.setToDefault("hm_" + index + "_coreProtocol");
      store.setToDefault("hm_" + index + "_ask_pass");
   }

   public void setValue(PreferenceStore store, int index, HostObject hostObject) {
      store.setValue("hm_" + index + "_hostname", hostObject.hostname);
      store.setValue("hm_" + index + "_port", hostObject.port);
      store.setValue("hm_" + index + "_username", hostObject.username);
      store.setValue("hm_" + index + "_password", hostObject.password);
      store.setValue("hm_" + index + "_description", hostObject.description);
      store.setValue("hm_" + index + "_coreProtocol", hostObject.coreProtocol);
      store.setValue("hm_" + index + "_ask_pass", hostObject.ask_pass);
   }

   public int getNum() {
      return this.hm_num;
   }

   // Pairs a BSpinner with its Label so both can be toggled/queried together.
   public static class SpinnerLabel {
      BSpinner spinner;
      Label label;

      public void addSpinner(BSpinner spinner) {
         this.spinner = spinner;
      }

      public void addLabel(Label label) {
         this.label = label;
      }

      public void setSelection(int selection) {
         this.spinner.setSelection(selection);
      }

      public int getSelection() {
         return this.spinner.getSelection();
      }

      public void setEnabled(boolean enabled) {
         this.spinner.setEnabled(enabled);
         this.label.setEnabled(enabled);
      }
   }

   // Pairs a Text field with its Label so both can be toggled/queried together.
   public static class TextLabel {
      Text text;
      Label label;

      public void setText(String text) {
         this.text.setText(text);
      }

      public void addText(Text text) {
         this.text = text;
      }

      public void addLabel(Label label) {
         this.label = label;
      }

      public String getText() {
         return this.text.getText();
      }

      public void setEchoChar(char echoChar) {
         this.text.setEchoChar(echoChar);
      }

      public void setEnabled(boolean enabled) {
         this.text.setEnabled(enabled);
         this.label.setEnabled(enabled);
      }
   }
}
