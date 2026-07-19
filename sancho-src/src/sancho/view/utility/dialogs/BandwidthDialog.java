package sancho.view.utility.dialogs;

import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.Option;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.statusline.actions.RateBandwidthDialogAction;
import sancho.view.utility.BSpinner;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class BandwidthDialog extends Dialog {
   int BUTTON_CONFIG = 8888;
   int BUTTON_MINUS = 7777;
   int BUTTON_PLUS = 3333;
   String[] foundStrings;
   int[] intArray;
   boolean modifying;
   int oldSelection;
   ArrayList optionArray = new ArrayList();
   Combo presetCombo;
   String[] presetStrings = new String[]{
      "max_hard_download_rate",
      "max_hard_upload_rate",
      "max_upload_slots",
      "max_concurrent_downloads",
      "max_opened_connections",
      "max_connections_per_second",
      "ED2K-max_indirect_connections",
      "ED2K-max_udp_sends"
   };
   int selectedPreset;
   ArrayList spinnerArray = new ArrayList();

   public void initPresets() {
      String[] presets = PreferenceLoader.loadStringArray("bwPresets");
      if (presets.length > 1) {
         this.presetStrings = presets;
      }
   }

   public BandwidthDialog(Shell shell, IMenuManager menuManager) {
      super(shell);
      this.createMenu(shell, menuManager);
   }

   public void createMenu(Shell shell, IMenuManager menuManager) {
      this.initPresets();
      MyMenuManager subMenu = new MyMenuManager(SResources.getString("l.bandwidthSettings"));
      subMenu.setImageString("updown");
      subMenu.add(new RateBandwidthDialogAction(shell));
      subMenu.add(new Separator());
      String[] presetKeys = new String[]{"bw.default", "bw.preset1", "bw.preset2", "bw.preset3"};
      ArrayList presetNames = new ArrayList();

      for (int i = 0; i < presetKeys.length; i++) {
         String name = PreferenceLoader.loadString("bwPreset" + i + "_name");
         if (i != 0) {
            if (!name.equals("")) {
               presetNames.add(name);
            } else {
               presetNames.add(SResources.getString(presetKeys[i]));
            }
         }
      }

      String presetName;
      for (int i = 4; !(presetName = PreferenceLoader.loadString("bwPreset" + i + "_name")).equals(""); i++) {
         presetNames.add(presetName);
      }

      int count = 0;
      if (Sancho.hasCollectionFactory()) {
         ICore core = Sancho.getCore();

         for (int i = 0; i < presetNames.size(); i++) {
            TObjectIntHashMap map = new TObjectIntHashMap();

            for (int j = 0; j < this.presetStrings.length; j++) {
               String presetString = this.presetStrings[j];
               Option option = (Option)core.getOptionCollection().get(this.presetStrings[j]);
               if (option != null) {
                  String key = "bwPreset" + (i + 1) + "_" + presetString;
                  int value = PreferenceLoader.loadInt(key);
                  map.put(option, value);
               }
            }

            if (map.size() > 0) {
               count++;
               subMenu.add(new ChangePresetAction((String)presetNames.get(i), map));
            }
         }
      }

      if (count > 0) {
         menuManager.add(subMenu);
      } else {
         menuManager.add(new RateBandwidthDialogAction(shell));
      }
   }

   public BandwidthDialog(Shell shell) {
      super(shell == null ? new Shell() : shell);
      this.setBlockOnOpen(false);
      this.initPresets();
      if (Sancho.hasCollectionFactory()) {
         ICore core = Sancho.getCore();

         for (int i = 0; i < this.presetStrings.length; i++) {
            Option option = (Option)core.getOptionCollection().get(this.presetStrings[i]);
            if (option != null) {
               this.optionArray.add(option);
            }
         }

         this.intArray = new int[this.optionArray.size()];
         this.foundStrings = new String[this.optionArray.size()];

         for (int i = 0; i < this.intArray.length; i++) {
            Option option = (Option)this.optionArray.get(i);
            this.intArray[i] = Integer.parseInt(option.getValue());
            this.foundStrings[i] = option.getName();
         }
      }
   }

   protected void buttonPressed(int buttonId) {
      if (Sancho.hasCollectionFactory()) {
         if (buttonId == 0) {
            this.saveAndSetCurrentPreset();
         } else if (buttonId == this.BUTTON_PLUS) {
            this.createPreset();
         } else if (buttonId == this.BUTTON_MINUS) {
            this.deletePreset(this.selectedPreset);
         } else if (buttonId == this.BUTTON_CONFIG) {
            new ConfigOptionsDialog(this.getShell(), this.foundStrings).open();
            this.close();
         }
      }

      super.buttonPressed(buttonId);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(SResources.getString("l.bandwidthSettings"));
      shell.setImage(VersionInfo.getProgramIcon());
   }

   protected void createButtonsForButtonBar(Composite parent) {
      Button button = this.createButton(parent, this.BUTTON_CONFIG, "", false);
      button.setImage(SResources.getImage("preferences"));
      button.setLayoutData(new GridData());
      button = this.createButton(parent, this.BUTTON_PLUS, "", false);
      button.setImage(SResources.getImage("plus"));
      button.setLayoutData(new GridData());
      button = this.createButton(parent, this.BUTTON_MINUS, "", false);
      button.setImage(SResources.getImage("minus"));
      button.setLayoutData(new GridData());
      super.createButtonsForButtonBar(parent);
      ((GridLayout)parent.getLayout()).makeColumnsEqualWidth = false;
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(3, 5, 5, 5, 5, false));
      this.presetCombo = new Combo(composite, 0);
      this.presetCombo.setVisibleItemCount(8);
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = 3;
      this.presetCombo.setLayoutData(gridData);
      String[] presetKeys = new String[]{"bw.default", "bw.preset1", "bw.preset2", "bw.preset3"};
      String[] presetNames = new String[presetKeys.length];

      for (int i = 0; i < presetNames.length; i++) {
         String name = PreferenceLoader.loadString("bwPreset" + i + "_name");
         if (!name.equals("")) {
            presetNames[i] = name;
         } else {
            presetNames[i] = SResources.getString(presetKeys[i]);
         }
      }

      this.presetCombo.setItems(presetNames);

      String presetName;
      for (int i = 4; !(presetName = PreferenceLoader.loadString("bwPreset" + i + "_name")).equals(""); i++) {
         this.presetCombo.add(presetName);
      }

      this.presetCombo.select(0);

      for (int i = 0; i < this.optionArray.size(); i++) {
         Option option = (Option)this.optionArray.get(i);
         this.spinnerArray.add(this.createOption(composite, option, this.intArray[i]));
      }

      this.presetCombo.addVerifyListener(new VerifyListener() {
         public void verifyText(VerifyEvent event) {
            if (!BandwidthDialog.this.modifying) {
               BandwidthDialog.this.modifying = true;
               Combo combo = (Combo)event.widget;
               String text = combo.getText() + event.character;
               if (!text.equals("\u0000")) {
                  combo.setItem(BandwidthDialog.this.selectedPreset, text);
               }

               BandwidthDialog.this.modifying = false;
            }
         }
      });
      this.presetCombo.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            Combo combo = (Combo)event.widget;
            BandwidthDialog.this.savePreset(BandwidthDialog.this.oldSelection, combo.getItem(BandwidthDialog.this.oldSelection));
            BandwidthDialog.this.selectedPreset = combo.getSelectionIndex();
            BandwidthDialog.this.oldSelection = BandwidthDialog.this.selectedPreset;
            BandwidthDialog.this.loadPreset(BandwidthDialog.this.selectedPreset, false);
         }
      });
      return composite;
   }

   protected BSpinner createOption(Composite composite, final Option option, int value) {
      if (option == null) {
         return null;
      } else {
         Label label = new Label(composite, 0);
         label.setText(option.getName());
         label.setToolTipText(option.getDescription());
         label.setLayoutData(new GridData(768));
         final BSpinner spinner = new BSpinner(composite, 2048);
         spinner.setToolTipText(option.getDefaultValue());
         spinner.setMinimum(0);
         spinner.setMaximum(1000000);
         spinner.setSelection(value);
         spinner.setLayoutData(new GridData());
         Button button = new Button(composite, 8);
         button.setLayoutData(new GridData());
         button.setToolTipText(SResources.getString("l.default"));
         button.setText(SResources.getString("l.default"));
         button.setImage(SResources.getImage("rotate"));
         button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
               spinner.setSelection(Integer.parseInt(option.getDefaultValue()));
            }
         });
         return spinner;
      }
   }

   public void createPreset() {
      int itemCount = this.presetCombo.getItemCount();
      this.presetCombo.add(itemCount + ": " + SResources.getString("bw.default"));
      int index = this.presetCombo.getItemCount() - 1;
      this.loadPreset(index, true);
      this.presetCombo.select(index);
      this.selectedPreset = index;
      this.oldSelection = this.selectedPreset;
      this.savePreset(index, this.presetCombo.getText());
   }

   public void deletePreset(int presetIndex) {
      if (this.presetCombo.getItemCount() - 1 >= presetIndex && presetIndex != 0) {
         this.modifying = true;
         this.presetCombo.remove(presetIndex);
         this.selectedPreset = 0;
         this.oldSelection = 0;
         this.presetCombo.select(0);
         this.loadPreset(0, false);
         this.modifying = false;
         PreferenceLoader.getPreferenceStore().setValue("bwPreset" + presetIndex + "_name", "");

         for (int i = 0; i < this.presetStrings.length; i++) {
            String presetString = this.presetStrings[i];
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + presetIndex + "_" + presetString, 0);
         }

         for (int i = presetIndex; i < this.presetCombo.getItemCount(); i++) {
            String name = PreferenceLoader.loadString("bwPreset" + (i + 1) + "_name");
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + i + "_name", name);

            for (int j = 0; j < this.presetStrings.length; j++) {
               String presetString = this.presetStrings[j];
               int value = PreferenceLoader.loadInt("bwPreset" + (i + 1) + "_" + presetString);
               PreferenceLoader.getPreferenceStore().setValue("bwPreset" + i + "_" + presetString, value);
            }
         }

         PreferenceLoader.getPreferenceStore().setValue("bwPreset" + this.presetCombo.getItemCount() + "_name", "");

         for (int i = 0; i < this.presetStrings.length; i++) {
            String presetString = this.presetStrings[i];
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + this.presetCombo.getItemCount() + "_" + presetString, 0);
         }
      }
   }

   public void loadPreset(int presetIndex, boolean isNew) {
      for (int i = 0; i < this.spinnerArray.size(); i++) {
         BSpinner spinner = (BSpinner)this.spinnerArray.get(i);
         int value = 0;
         if (presetIndex != 0 && !isNew) {
            value = PreferenceLoader.loadInt("bwPreset" + presetIndex + "_" + this.foundStrings[i]);
         } else {
            value = this.intArray[i];
         }

         spinner.setSelection(value);
      }
   }

   public void saveAndSetCurrentPreset() {
      this.savePreset(this.selectedPreset, this.presetCombo.getText());

      for (int i = 0; i < this.optionArray.size(); i++) {
         Option option = (Option)this.optionArray.get(i);
         BSpinner spinner = (BSpinner)this.spinnerArray.get(i);
         int value = spinner.getSelection();
         option.setValue(String.valueOf(value));
      }
   }

   public void savePreset(int presetIndex, String name) {
      if (presetIndex > 0) {
         PreferenceLoader.getPreferenceStore().setValue("bwPreset" + presetIndex + "_name", name);

         for (int i = 0; i < this.optionArray.size(); i++) {
            Option option = (Option)this.optionArray.get(i);
            BSpinner spinner = (BSpinner)this.spinnerArray.get(i);
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + presetIndex + "_" + option.getName(), spinner.getSelection());
         }
      }
   }

   // Menu action that applies a saved preset's stored option values to the core.
   private static class ChangePresetAction extends Action {
      private TObjectIntHashMap hm;

      public ChangePresetAction(String name, TObjectIntHashMap map) {
         super(name);
         this.hm = map;
      }

      public void run() {
         if (Sancho.hasCollectionFactory()) {
            TObjectIntIterator iterator = this.hm.iterator();

            while (iterator.hasNext()) {
               iterator.advance();
               Option option = (Option)iterator.key();
               int value = iterator.value();
               option.setValue(String.valueOf(value));
            }
         }
      }
   }

   // Dialog for choosing which bandwidth options appear as presets.
   private static class ConfigOptionsDialog extends Dialog {
      String[] foundList;
      List rightList;

      public ConfigOptionsDialog(Shell shell, String[] foundList) {
         super(shell);
         this.foundList = foundList;
      }

      protected void configureShell(Shell shell) {
         super.configureShell(shell);
         shell.setImage(VersionInfo.getProgramIcon());
         shell.setText(SResources.getString("l.bandwidthConfigTitle"));
      }

      protected void createButtonsForButtonBar(Composite parent) {
         this.createButton(parent, 0, SResources.getString("b.ok"), true);
         this.createButton(parent, 1, SResources.getString("b.cancel"), false);
      }

      protected Control createDialogArea(Composite parent) {
         Composite composite = (Composite)super.createDialogArea(parent);
         composite.setLayout(WidgetFactory.createGridLayout(4, 5, 5, 10, 5, false));
         if (!Sancho.hasCollectionFactory()) {
            return composite;
         } else {
            final List leftList = new List(composite, 2818);
            String[] options = Sancho.getCore().getOptionCollection().getAllIntOptions();
            Arrays.sort(options, new StringComparator());
            leftList.setItems(options);
            GridData gridData = new GridData();
            gridData.heightHint = 100;
            leftList.setLayoutData(gridData);
            Button addButton = new Button(composite, 8);
            addButton.setImage(SResources.getImage("plus"));
            addButton.setLayoutData(new GridData());
            this.rightList = new List(composite, 2818);
            gridData = new GridData();
            gridData.heightHint = 100;
            this.rightList.setLayoutData(gridData);
            this.rightList.setItems(this.foundList);
            Button removeButton = new Button(composite, 8);
            removeButton.setImage(SResources.getImage("minus"));
            removeButton.setLayoutData(new GridData());
            removeButton.addSelectionListener(new SelectionAdapter() {
               public void widgetSelected(SelectionEvent event) {
                  if (ConfigOptionsDialog.this.rightList.getSelectionCount() > 0) {
                     ConfigOptionsDialog.this.rightList.remove(ConfigOptionsDialog.this.rightList.getSelectionIndices());
                  }
               }
            });
            addButton.addSelectionListener(new SelectionAdapter() {
               public void widgetSelected(SelectionEvent event) {
                  if (leftList.getSelectionCount() > 0) {
                     String[] selection = leftList.getSelection();

                     for (int index = 0; selection != null && index < selection.length; index++) {
                        ConfigOptionsDialog.this.rightList.add(selection[index]);
                     }
                  }
               }
            });
            return composite;
         }
      }

      protected void buttonPressed(int buttonId) {
         if (buttonId == 0 && this.rightList != null) {
            PreferenceLoader.setValue("bwPresets", this.rightList.getItems());
            PreferenceLoader.saveStore();
         }

         super.buttonPressed(buttonId);
      }
   }

   // Alphabetical ordering for option-name strings.
   private static class StringComparator implements Comparator {
      public int compare(Object object1, Object object2) {
         return ((String)object1).compareTo((String)object2);
      }
   }
}
