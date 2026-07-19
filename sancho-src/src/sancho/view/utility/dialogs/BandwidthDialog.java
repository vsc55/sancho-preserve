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
      String[] var1 = PreferenceLoader.loadStringArray("bwPresets");
      if (var1.length > 1) {
         this.presetStrings = var1;
      }
   }

   public BandwidthDialog(Shell var1, IMenuManager var2) {
      super(var1);
      this.createMenu(var1, var2);
   }

   public void createMenu(Shell var1, IMenuManager var2) {
      this.initPresets();
      MyMenuManager var3 = new MyMenuManager(SResources.getString("l.bandwidthSettings"));
      var3.setImageString("updown");
      var3.add(new RateBandwidthDialogAction(var1));
      var3.add(new Separator());
      String[] var4 = new String[]{"bw.default", "bw.preset1", "bw.preset2", "bw.preset3"};
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 < var4.length; var6++) {
         String var7 = PreferenceLoader.loadString("bwPreset" + var6 + "_name");
         if (var6 != 0) {
            if (!var7.equals("")) {
               var5.add(var7);
            } else {
               var5.add(SResources.getString(var4[var6]));
            }
         }
      }

      String var18;
      for (int var8 = 4; !(var18 = PreferenceLoader.loadString("bwPreset" + var8 + "_name")).equals(""); var8++) {
         var5.add(var18);
      }

      int var9 = 0;
      if (Sancho.hasCollectionFactory()) {
         ICore var10 = Sancho.getCore();

         for (int var11 = 0; var11 < var5.size(); var11++) {
            TObjectIntHashMap var12 = new TObjectIntHashMap();

            for (int var13 = 0; var13 < this.presetStrings.length; var13++) {
               String var14 = this.presetStrings[var13];
               Option var15 = (Option)var10.getOptionCollection().get(this.presetStrings[var13]);
               if (var15 != null) {
                  String var16 = "bwPreset" + (var11 + 1) + "_" + var14;
                  int var17 = PreferenceLoader.loadInt(var16);
                  var12.put(var15, var17);
               }
            }

            if (var12.size() > 0) {
               var9++;
               var3.add(new ChangePresetAction((String)var5.get(var11), var12));
            }
         }
      }

      if (var9 > 0) {
         var2.add(var3);
      } else {
         var2.add(new RateBandwidthDialogAction(var1));
      }
   }

   public BandwidthDialog(Shell var1) {
      super(var1 == null ? new Shell() : var1);
      this.setBlockOnOpen(false);
      this.initPresets();
      if (Sancho.hasCollectionFactory()) {
         ICore var2 = Sancho.getCore();

         for (int var3 = 0; var3 < this.presetStrings.length; var3++) {
            Option var4 = (Option)var2.getOptionCollection().get(this.presetStrings[var3]);
            if (var4 != null) {
               this.optionArray.add(var4);
            }
         }

         this.intArray = new int[this.optionArray.size()];
         this.foundStrings = new String[this.optionArray.size()];

         for (int var6 = 0; var6 < this.intArray.length; var6++) {
            Option var5 = (Option)this.optionArray.get(var6);
            this.intArray[var6] = Integer.parseInt(var5.getValue());
            this.foundStrings[var6] = var5.getName();
         }
      }
   }

   protected void buttonPressed(int var1) {
      if (Sancho.hasCollectionFactory()) {
         if (var1 == 0) {
            this.saveAndSetCurrentPreset();
         } else if (var1 == this.BUTTON_PLUS) {
            this.createPreset();
         } else if (var1 == this.BUTTON_MINUS) {
            this.deletePreset(this.selectedPreset);
         } else if (var1 == this.BUTTON_CONFIG) {
            new ConfigOptionsDialog(this.getShell(), this.foundStrings).open();
            this.close();
         }
      }

      super.buttonPressed(var1);
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(SResources.getString("l.bandwidthSettings"));
      var1.setImage(VersionInfo.getProgramIcon());
   }

   protected void createButtonsForButtonBar(Composite var1) {
      Button var2 = this.createButton(var1, this.BUTTON_CONFIG, "", false);
      var2.setImage(SResources.getImage("preferences"));
      var2.setLayoutData(new GridData());
      var2 = this.createButton(var1, this.BUTTON_PLUS, "", false);
      var2.setImage(SResources.getImage("plus"));
      var2.setLayoutData(new GridData());
      var2 = this.createButton(var1, this.BUTTON_MINUS, "", false);
      var2.setImage(SResources.getImage("minus"));
      var2.setLayoutData(new GridData());
      super.createButtonsForButtonBar(var1);
      ((GridLayout)var1.getLayout()).makeColumnsEqualWidth = false;
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(3, 5, 5, 5, 5, false));
      this.presetCombo = new Combo(var2, 0);
      this.presetCombo.setVisibleItemCount(8);
      GridData var3 = new GridData(768);
      var3.horizontalSpan = 3;
      this.presetCombo.setLayoutData(var3);
      String[] var4 = new String[]{"bw.default", "bw.preset1", "bw.preset2", "bw.preset3"};
      String[] var5 = new String[var4.length];

      for (int var6 = 0; var6 < var5.length; var6++) {
         String var7 = PreferenceLoader.loadString("bwPreset" + var6 + "_name");
         if (!var7.equals("")) {
            var5[var6] = var7;
         } else {
            var5[var6] = SResources.getString(var4[var6]);
         }
      }

      this.presetCombo.setItems(var5);

      String var11;
      for (int var8 = 4; !(var11 = PreferenceLoader.loadString("bwPreset" + var8 + "_name")).equals(""); var8++) {
         this.presetCombo.add(var11);
      }

      this.presetCombo.select(0);

      for (int var9 = 0; var9 < this.optionArray.size(); var9++) {
         Option var10 = (Option)this.optionArray.get(var9);
         this.spinnerArray.add(this.createOption(var2, var10, this.intArray[var9]));
      }

      this.presetCombo.addVerifyListener(new VerifyListener() {
         public void verifyText(VerifyEvent var1) {
            if (!BandwidthDialog.this.modifying) {
               BandwidthDialog.this.modifying = true;
               Combo var2 = (Combo)var1.widget;
               String var3 = var2.getText() + var1.character;
               if (!var3.equals("\u0000")) {
                  var2.setItem(BandwidthDialog.this.selectedPreset, var3);
               }

               BandwidthDialog.this.modifying = false;
            }
         }
      });
      this.presetCombo.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            Combo var2 = (Combo)var1.widget;
            BandwidthDialog.this.savePreset(BandwidthDialog.this.oldSelection, var2.getItem(BandwidthDialog.this.oldSelection));
            BandwidthDialog.this.selectedPreset = var2.getSelectionIndex();
            BandwidthDialog.this.oldSelection = BandwidthDialog.this.selectedPreset;
            BandwidthDialog.this.loadPreset(BandwidthDialog.this.selectedPreset, false);
         }
      });
      return var2;
   }

   protected BSpinner createOption(Composite var1, final Option var2, int var3) {
      if (var2 == null) {
         return null;
      } else {
         Label var4 = new Label(var1, 0);
         var4.setText(var2.getName());
         var4.setToolTipText(var2.getDescription());
         var4.setLayoutData(new GridData(768));
         final BSpinner var5 = new BSpinner(var1, 2048);
         var5.setToolTipText(var2.getDefaultValue());
         var5.setMinimum(0);
         var5.setMaximum(1000000);
         var5.setSelection(var3);
         var5.setLayoutData(new GridData());
         Button var6 = new Button(var1, 8);
         var6.setLayoutData(new GridData());
         var6.setToolTipText(SResources.getString("l.default"));
         var6.setText(SResources.getString("l.default"));
         var6.setImage(SResources.getImage("rotate"));
         var6.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               var5.setSelection(Integer.parseInt(var2.getDefaultValue()));
            }
         });
         return var5;
      }
   }

   public void createPreset() {
      int var1 = this.presetCombo.getItemCount();
      this.presetCombo.add(var1 + ": " + SResources.getString("bw.default"));
      int var2 = this.presetCombo.getItemCount() - 1;
      this.loadPreset(var2, true);
      this.presetCombo.select(var2);
      this.selectedPreset = var2;
      this.oldSelection = this.selectedPreset;
      this.savePreset(var2, this.presetCombo.getText());
   }

   public void deletePreset(int var1) {
      if (this.presetCombo.getItemCount() - 1 >= var1 && var1 != 0) {
         this.modifying = true;
         this.presetCombo.remove(var1);
         this.selectedPreset = 0;
         this.oldSelection = 0;
         this.presetCombo.select(0);
         this.loadPreset(0, false);
         this.modifying = false;
         PreferenceLoader.getPreferenceStore().setValue("bwPreset" + var1 + "_name", "");

         for (int var2 = 0; var2 < this.presetStrings.length; var2++) {
            String var3 = this.presetStrings[var2];
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + var1 + "_" + var3, 0);
         }

         for (int var8 = var1; var8 < this.presetCombo.getItemCount(); var8++) {
            String var4 = PreferenceLoader.loadString("bwPreset" + (var8 + 1) + "_name");
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + var8 + "_name", var4);

            for (int var5 = 0; var5 < this.presetStrings.length; var5++) {
               String var6 = this.presetStrings[var5];
               int var7 = PreferenceLoader.loadInt("bwPreset" + (var8 + 1) + "_" + var6);
               PreferenceLoader.getPreferenceStore().setValue("bwPreset" + var8 + "_" + var6, var7);
            }
         }

         PreferenceLoader.getPreferenceStore().setValue("bwPreset" + this.presetCombo.getItemCount() + "_name", "");

         for (int var9 = 0; var9 < this.presetStrings.length; var9++) {
            String var10 = this.presetStrings[var9];
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + this.presetCombo.getItemCount() + "_" + var10, 0);
         }
      }
   }

   public void loadPreset(int var1, boolean var2) {
      for (int var3 = 0; var3 < this.spinnerArray.size(); var3++) {
         BSpinner var4 = (BSpinner)this.spinnerArray.get(var3);
         int var5 = 0;
         if (var1 != 0 && !var2) {
            var5 = PreferenceLoader.loadInt("bwPreset" + var1 + "_" + this.foundStrings[var3]);
         } else {
            var5 = this.intArray[var3];
         }

         var4.setSelection(var5);
      }
   }

   public void saveAndSetCurrentPreset() {
      this.savePreset(this.selectedPreset, this.presetCombo.getText());

      for (int var1 = 0; var1 < this.optionArray.size(); var1++) {
         Option var2 = (Option)this.optionArray.get(var1);
         BSpinner var3 = (BSpinner)this.spinnerArray.get(var1);
         int var4 = var3.getSelection();
         var2.setValue(String.valueOf(var4));
      }
   }

   public void savePreset(int var1, String var2) {
      if (var1 > 0) {
         PreferenceLoader.getPreferenceStore().setValue("bwPreset" + var1 + "_name", var2);

         for (int var3 = 0; var3 < this.optionArray.size(); var3++) {
            Option var4 = (Option)this.optionArray.get(var3);
            BSpinner var5 = (BSpinner)this.spinnerArray.get(var3);
            PreferenceLoader.getPreferenceStore().setValue("bwPreset" + var1 + "_" + var4.getName(), var5.getSelection());
         }
      }
   }

   // Menu action that applies a saved preset's stored option values to the core.
   private static class ChangePresetAction extends Action {
      private TObjectIntHashMap hm;

      public ChangePresetAction(String var1, TObjectIntHashMap var2) {
         super(var1);
         this.hm = var2;
      }

      public void run() {
         if (Sancho.hasCollectionFactory()) {
            TObjectIntIterator var1 = this.hm.iterator();

            while (var1.hasNext()) {
               var1.advance();
               Option var2 = (Option)var1.key();
               int var3 = var1.value();
               var2.setValue(String.valueOf(var3));
            }
         }
      }
   }

   // Dialog for choosing which bandwidth options appear as presets.
   private static class ConfigOptionsDialog extends Dialog {
      String[] foundList;
      List rightList;

      public ConfigOptionsDialog(Shell var1, String[] var2) {
         super(var1);
         this.foundList = var2;
      }

      protected void configureShell(Shell var1) {
         super.configureShell(var1);
         var1.setImage(VersionInfo.getProgramIcon());
         var1.setText(SResources.getString("l.bandwidthConfigTitle"));
      }

      protected void createButtonsForButtonBar(Composite var1) {
         this.createButton(var1, 0, SResources.getString("b.ok"), true);
         this.createButton(var1, 1, SResources.getString("b.cancel"), false);
      }

      protected Control createDialogArea(Composite var1) {
         Composite var2 = (Composite)super.createDialogArea(var1);
         var2.setLayout(WidgetFactory.createGridLayout(4, 5, 5, 10, 5, false));
         if (!Sancho.hasCollectionFactory()) {
            return var2;
         } else {
            final List var3 = new List(var2, 2818);
            String[] var4 = Sancho.getCore().getOptionCollection().getAllIntOptions();
            Arrays.sort(var4, new StringComparator());
            var3.setItems(var4);
            GridData var5 = new GridData();
            var5.heightHint = 100;
            var3.setLayoutData(var5);
            Button var6 = new Button(var2, 8);
            var6.setImage(SResources.getImage("plus"));
            var6.setLayoutData(new GridData());
            this.rightList = new List(var2, 2818);
            var5 = new GridData();
            var5.heightHint = 100;
            this.rightList.setLayoutData(var5);
            this.rightList.setItems(this.foundList);
            Button var7 = new Button(var2, 8);
            var7.setImage(SResources.getImage("minus"));
            var7.setLayoutData(new GridData());
            var7.addSelectionListener(new SelectionAdapter() {
               public void widgetSelected(SelectionEvent var1) {
                  if (ConfigOptionsDialog.this.rightList.getSelectionCount() > 0) {
                     ConfigOptionsDialog.this.rightList.remove(ConfigOptionsDialog.this.rightList.getSelectionIndices());
                  }
               }
            });
            var6.addSelectionListener(new SelectionAdapter() {
               public void widgetSelected(SelectionEvent var1) {
                  if (var3.getSelectionCount() > 0) {
                     String[] var2 = var3.getSelection();

                     for (int index = 0; var2 != null && index < var2.length; index++) {
                        ConfigOptionsDialog.this.rightList.add(var2[index]);
                     }
                  }
               }
            });
            return var2;
         }
      }

      protected void buttonPressed(int var1) {
         if (var1 == 0 && this.rightList != null) {
            PreferenceLoader.setValue("bwPresets", this.rightList.getItems());
            PreferenceLoader.saveStore();
         }

         super.buttonPressed(var1);
      }
   }

   // Alphabetical ordering for option-name strings.
   private static class StringComparator implements Comparator {
      public int compare(Object var1, Object var2) {
         return ((String)var1).compareTo((String)var2);
      }
   }
}
