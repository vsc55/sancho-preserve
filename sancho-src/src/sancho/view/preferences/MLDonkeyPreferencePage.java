package sancho.view.preferences;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import sancho.model.mldonkey.Option;
import sancho.model.mldonkey.enums.EnumTagType;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class MLDonkeyPreferencePage extends FieldEditorPreferencePage {
   private static final int inputFieldLength = 20;

   // JFace 3.2 FieldEditorPreferencePage exposed clearEditors(); it is gone in
   // modern JFace, so reset the private `fields` list reflectively to let this
   // page rebuild its editors dynamically.
   private void clearEditors() {
      try {
         java.lang.reflect.Field field = FieldEditorPreferencePage.class.getDeclaredField("fields");
         field.setAccessible(true);
         List list = (List)field.get(this);
         if (list != null) {
            list.clear();
         }
      } catch (Exception var2) {
         // ignore if the JFace field layout changed
      }
   }

   private static int extentX = -1;
   private boolean empty;
   private boolean allOptions;
   private List options = new ArrayList();
   private List filteredOptions = new ArrayList();
   private Composite group;
   private ScrolledComposite sc;
   private Composite myComp;
   private boolean wasEmpty = true;
   private boolean fChanged = false;
   private Pattern regex = null;

   protected MLDonkeyPreferencePage(String var1, int var2) {
      super(var1, var2);
   }

   public void setAllOptions() {
      this.allOptions = true;
   }

   public void addOption(Option var1) {
      this.options.add(var1);
   }

   protected void contributeButtons(Composite var1) {
      var1.setLayoutData(new GridData(768));
      ((GridLayout)var1.getLayout()).numColumns++;
      Label var2 = new Label(var1, 0);
      var2.setLayoutData(new GridData(768));
      var2.setText(SResources.getString("p.mouseOverHelp"));
   }

   public void reFilter() {
      this.filteredOptions.clear();

      for (Object var2o : this.options) { Option var2 = (Option)var2o;
         if (this.fChanged) {
            if (!var2.getDefaultValue().equals(var2.getValue()) && (this.regex == null || this.regex.matcher(var2.getName()).find())) {
               this.filteredOptions.add(var2);
            }
         } else if (this.regex == null || this.regex.matcher(var2.getName()).find()) {
            this.filteredOptions.add(var2);
         }
      }
   }

   public void updateRefine(KeyEvent var1) {
      Text var2 = (Text)var1.widget;
      switch (var1.keyCode) {
         case 8:
            if (this.wasEmpty && var2.getText().equals("")) {
               return;
            } else if (var2.getText().equals("")) {
               this.wasEmpty = true;
            }
         default:
            this.wasEmpty = false;

            try {
               this.regex = Pattern.compile(var2.getText(), Pattern.CASE_INSENSITIVE);
            } catch (PatternSyntaxException var7) {
            }

            if (this.regex == null) {
               return;
            } else {
               this.group.setRedraw(false);
               this.filteredOptions.clear();
               this.clearEditors();
               String var3 = var2.getText();
               Control[] var4 = this.myComp.getChildren();

               for (int var5 = var4.length - 1; var5 >= 0; var5--) {
                  var4[var5].dispose();
               }

               boolean var6 = var3 == null || var3.equals("");
               if (var6) {
                  this.regex = null;
               }

               this.reFilter();
               this.createFieldEditors();
               this.sc.setMinSize(this.myComp.computeSize(-1, -1, true));
               this.group.setRedraw(true);
               this.myComp.layout(true);
            }
         case 13:
         case 16777217:
         case 16777218:
         case 16777219:
         case 16777220:
         case 16777221:
         case 16777222:
         case 16777296:
      }
   }

   protected Control createContents(Composite var1) {
      Collections.sort(this.options, new OptionsComparator());
      this.filteredOptions = new ArrayList(this.options.size());
      Iterator var2 = this.options.iterator();

      while (var2.hasNext()) {
         this.filteredOptions.add(var2.next());
      }

      if (this.allOptions) {
         Composite var5 = new Composite(var1, 0);
         var5.setLayout(WidgetFactory.createGridLayout(3, 5, 5, 5, 5, false));
         var5.setLayoutData(new GridData(768));
         Label var6 = new Label(var5, 0);
         var6.setText(SResources.getString("ti.refine"));
         Text var7 = new Text(var5, 2052);
         var7.setLayoutData(new GridData(768));
         if (SWT.getPlatform().equals("fox")) {
            var7.addKeyListener(new KeyAdapter() {
               public void keyPressed(KeyEvent var1) {
                  MLDonkeyPreferencePage.this.updateRefine(var1);
               }
            });
         } else {
            var7.addKeyListener(new KeyAdapter() {
               public void keyReleased(KeyEvent var1) {
                  MLDonkeyPreferencePage.this.updateRefine(var1);
               }
            });
         }

         Button var8 = new Button(var5, 32);
         var8.setText(SResources.getString("l.changed"));
         var8.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent var1) {
               Button var2 = (Button)var1.widget;
               MLDonkeyPreferencePage.this.fChanged = var2.getSelection();
               MLDonkeyPreferencePage.this.group.setRedraw(false);
               MLDonkeyPreferencePage.this.filteredOptions.clear();
               MLDonkeyPreferencePage.this.clearEditors();
               Control[] var3 = MLDonkeyPreferencePage.this.myComp.getChildren();

               for (int var4 = var3.length - 1; var4 >= 0; var4--) {
                  var3[var4].dispose();
               }

               MLDonkeyPreferencePage.this.reFilter();
               MLDonkeyPreferencePage.this.createFieldEditors();
               MLDonkeyPreferencePage.this.sc.setMinSize(MLDonkeyPreferencePage.this.myComp.computeSize(-1, -1, true));
               MLDonkeyPreferencePage.this.group.setRedraw(true);
               MLDonkeyPreferencePage.this.myComp.layout(true);
            }
         });
      }

      Composite var3;
      if (this.empty) {
         var3 = (Composite)super.createContents(var1);
         var3.setLayoutData(new GridData(1808));
         Label var9 = new Label(var3, 0);
         var9.setText(SResources.getString("p.empty"));
         GridData var11 = new GridData(1808);
         var11.verticalAlignment = 2;
         var11.horizontalAlignment = 2;
         var9.setLayoutData(var11);
         var3.layout();
      } else {
         this.group = new Composite(var1, 2048);
         GridLayout var10 = WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false);
         this.group.setLayout(var10);
         this.group.setLayoutData(new GridData(1808));
         this.sc = new ScrolledComposite(this.group, 768) {
            public Point computeSize(int var1, int var2, boolean var3) {
               return new Point(-1, -1);
            }
         };
         this.sc.setLayoutData(new GridData(1808));
         this.sc.setLayout(new FillLayout());
         this.sc.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent var1) {
               ScrolledComposite var2 = (ScrolledComposite)var1.widget;
               int var3 = var2.getClientArea().height;
               if (var3 > 10) {
                  var3 -= 10;
               }

               ScrollBar var4 = var2.getVerticalBar();
               if (var4 != null) {
                  var4.setIncrement(15);
                  var4.setPageIncrement(var3);
               }
            }
         });
         var3 = (Composite)super.createContents(this.sc);
         var3.setLayoutData(new GridData(1808));
         this.sc.setExpandHorizontal(true);
         this.sc.setExpandVertical(true);
         this.sc.setContent(var3);
         Point var12 = var3.computeSize(-1, -1);
         this.sc.setMinSize(var12);
         var3.layout();
      }

      this.myComp = var3;
      return var3;
   }

   protected void createFieldEditors() {
      Iterator var2 = this.filteredOptions.iterator();

      while (var2.hasNext()) {
         Composite var1 = this.getFieldEditorParent();
         Option var3 = (Option)var2.next();
         String var4 = var3.getName();
         String var5 = var3.getDescription();
         EnumTagType var6 = var3.getType();
         String var7 = var3.getValue();
         String var8 = var3.getDefaultValue();
         if (var5.equals("")) {
            var5 = var3.getName();
         }

         if (var8 != null && !var8.equals("")) {
            var5 = var5 + "\n(" + SResources.getString("b.default") + ": " + var8 + ")";
         }

         if (var6 == EnumTagType.BOOL || this.isBoolean(var7)) {
            this.setupEditor(var1, new BooleanFieldEditor(var4, var4, 1, var1), var5);
         } else if (var6 != EnumTagType.INT && !this.isInteger(var7)) {
            this.setupEditor(var1, new StringFieldEditor(var4, var4, 20, var1), var5);
         } else {
            IntegerFieldEditor var9 = new IntegerFieldEditor(var4, var4, var1) {
               protected void doFillIntoGrid(Composite var1, int var2) {
                  this.getLabelControl(var1);
                  Text var3 = this.getTextControl(var1);
                  GridData var4 = new GridData();
                  var4.horizontalSpan = var2 - 1;
                  if (extentX < 0) {
                     GC var5 = new GC(var3);
                     Point var6 = var5.textExtent("X");
                     var5.dispose();
                     extentX = var6.x;
                  }

                  var4.widthHint = 20 * extentX;
                  var3.setLayoutData(var4);
               }
            };
            var9.setValidRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.setupEditor(var1, var9, var5);
         }
      }
   }

   private boolean isBoolean(String var1) {
      return var1 != null && (var1.equalsIgnoreCase("true") || var1.equalsIgnoreCase("false"));
   }

   private boolean isInteger(String var1) {
      try {
         int var2 = Integer.parseInt(var1);
         return var2 >= Integer.MIN_VALUE && var2 <= Integer.MAX_VALUE;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   public void setEmpty(boolean var1) {
      this.empty = var1;
   }

   private void setupEditor(Composite var1, FieldEditor var2, String var3) {
      var2.setPage(this);
      var2.setPreferenceStore(this.getPreferenceStore());
      var2.getLabelControl(var1).setToolTipText(var3);
      var2.load();
      this.addField(var2);
   }

   // Case-insensitive ordering of options by name for the preference list.
   private static class OptionsComparator implements Comparator {
      public int compare(Object var1, Object var2) {
         Option var3 = (Option)var1;
         Option var4 = (Option)var2;
         return var3.getName().compareToIgnoreCase(var4.getName());
      }
   }
}
