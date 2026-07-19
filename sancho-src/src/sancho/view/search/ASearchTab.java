package sancho.view.search;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.SearchQuery;
import sancho.utility.MyObservable;
import sancho.utility.MyObserver;
import sancho.view.SearchTab;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.search.result.ResultViewFrame;
import sancho.view.utility.NoDuplicatesCombo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public abstract class ASearchTab implements MyObserver {
   protected static String S_NO_NETWORK = SResources.getString("s.m.noNetwork");
   protected static String S_ALL_NETWORKS = SResources.getString("s.m.all");
   protected Combo networkCombo;
   private List networkList = new ArrayList();
   protected Combo searchCombo;
   protected SearchTab searchTab;
   protected List tempList = new ArrayList();
   protected List searchComboList = new ArrayList();
   protected ResultViewFrame viewFrame;
   protected Combo searchTypeCombo;

   public ASearchTab(ResultViewFrame var1, SearchTab var2) {
      this.viewFrame = var1;
      this.searchTab = var2;
      this.onConnect();
   }

   public void autoSearch(String var1) {
      this.searchCombo.setText(var1);
      this.performSearch();
   }

   public synchronized void addNetwork(SearchQuery var1) {
      int var2 = this.networkCombo.getSelectionIndex();
      if (var2 >= 0 && var2 < this.networkList.size()) {
         EnumNetwork var3 = (EnumNetwork)this.networkList.get(var2);
         if (Sancho.hasCollectionFactory()) {
            Network var4 = Sancho.getCore().getNetworkCollection().getByEnum(var3);
            if (var4 != null) {
               var1.setNetwork(var4.getId());
            }
         }
      }
   }

   protected Combo createCombo(Composite var1, int var2, String var3, String[] var4) {
      Label var5 = new Label(var1, 0);
      var5.setLayoutData(new GridData(256));
      var5.setText(SResources.getString(var3));
      Combo var6 = new Combo(var1, var2 | 4 | 2048);
      var6.setLayoutData(new GridData(768));
      var6.setItems(var4);
      var6.select(0);
      return var6;
   }

   protected Combo createFileType(Composite var1) {
      String[] var2 = new String[]{
         "s.m.all", "s.m.audio", "s.m.video", "s.m.image", "s.m.document", "s.m.program", "s.m.archive", "s.m.cdimage", "s.m.emulecollection"
      };
      return this.createResCombo(var1, 8, "s.fileType", var2);
   }

   protected Combo createIntegerCombo(Composite var1, int var2, String var3, String[] var4) {
      Combo var5 = this.createCombo(var1, var2, var3, var4);
      var5.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            switch (var1.keyCode) {
               case 8:
               case 127:
               case 16777219:
               case 16777220:
                  return;
               default:
                  try {
                     Integer.parseInt(String.valueOf(var1.character));
                  } catch (NumberFormatException var3) {
                     var1.doit = false;
                  }
            }
         }
      });
      return var5;
   }

   protected Composite createMainComposite(CTabFolder var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(2, 7, 5, 5, 5, false));
      return var2;
   }

   protected void createNetworkCombo(Composite var1) {
      Label var2 = new Label(var1, 0);
      var2.setLayoutData(new GridData(256));
      var2.setText(SResources.getString("s.network"));
      this.networkCombo = new Combo(var1, 2060);
      this.networkCombo.setLayoutData(new GridData(768));
      this.syncNetworkCombo(true);
   }

   protected Combo createResCombo(Composite var1, int var2, String var3, String[] var4) {
      String[] var5 = new String[var4.length];

      for (int var6 = 0; var6 < var4.length; var6++) {
         var5[var6] = SResources.getString(var4[var6]);
      }

      return this.createCombo(var1, var2, var3, var5);
   }

   protected Combo createSearchCombo(Composite var1, String var2) {
      Label var3 = new Label(var1, 0);
      var3.setLayoutData(new GridData(256));
      var3.setText(SResources.getString(var2));
      NoDuplicatesCombo var4 = new NoDuplicatesCombo(var1, 2052);
      var4.setLayoutData(new GridData(768));
      var4.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent var1) {
            if (var1.character == '\r' || var1.character == 16777296) {
               ASearchTab.this.performSearch();
               NoDuplicatesCombo var2 = (NoDuplicatesCombo)var1.widget;
               var2.add(var2.getText(), 0);
               var2.setText("");
            }
         }
      });
      this.searchComboList.add(var4);
      return var4;
   }

   protected void clearAllSearchCombos() {
      for (int var1 = 0; var1 < this.searchComboList.size(); var1++) {
         Combo var2 = (Combo)this.searchComboList.get(var1);
         var2.removeAll();
      }
   }

   protected Combo createSavedSearchCombo(Composite var1, String var2, final String var3) {
      Combo var4 = this.createSearchCombo(var1, var2);
      if (PreferenceLoader.loadBoolean("searchSaveEntries")) {
         var4.setItems(PreferenceLoader.loadStringArray(var3 + ".sArray"));
      }

      var4.addDisposeListener(new DisposeListener() {
         public void widgetDisposed(DisposeEvent var1) {
            Combo var2 = (Combo)var1.widget;
            if (PreferenceLoader.loadBoolean("searchSaveEntries")) {
               PreferenceLoader.setValue(var3 + ".sArray", var2.getItems(), 25);
            } else {
               PreferenceLoader.getPreferenceStore().setValue(var3 + ".sArray", "");
            }
         }
      });
      return var4;
   }

   protected Combo createSearchTypeCombo(Composite var1) {
      String[] var2 = new String[]{"s.st.remote", "s.st.local", "s.st.subscribe"};
      return this.createResCombo(var1, 8, "s.searchType", var2);
   }

   public void createSeparator(Composite var1) {
      Label var2 = new Label(var1, 258);
      GridData var3 = new GridData(768);
      var3.horizontalSpan = 2;
      var2.setLayoutData(var3);
   }

   public abstract Control createTab(CTabFolder var1);

   public abstract String getText();

   public void onConnect() {
      if (Sancho.hasCollectionFactory()) {
         this.viewFrame.getCore().getNetworkCollection().addObserver(this);
         if (this.networkCombo != null) {
            this.syncNetworkCombo(true);
         }
      }
   }

   public void parseFileType(Combo var1, SearchQuery var2) {
      String[] var10000 = new String[]{
         "s.m.all", "s.m.audio", "s.m.video", "s.m.image", "s.m.document", "s.m.program", "s.m.archive", "s.m.cdimage", "s.m.emulecollection"
      };
      switch (var1.getSelectionIndex()) {
         case 1:
            var2.setMedia("Audio");
            break;
         case 2:
            var2.setMedia("Video");
            break;
         case 3:
            var2.setMedia("Image");
            break;
         case 4:
            var2.setMedia("Doc");
            break;
         case 5:
            var2.setMedia("Pro");
            break;
         case 6:
            var2.setMedia("Arc");
            break;
         case 7:
            var2.setMedia("Iso");
            break;
         case 8:
            var2.setMedia("EmuleCollection");
      }
   }

   public void parseSearchType(Combo var1, SearchQuery var2) {
      switch (var1.getSelectionIndex()) {
         case 1:
            var2.setLocalSearch();
            break;
         case 2:
            var2.setSubscribeSearch();
      }
   }

   public abstract void performSearch();

   public boolean setFocus() {
      return this.searchCombo.setFocus();
   }

   public synchronized void syncNetworkCombo(boolean var1) {
      boolean var2 = false;
      if (!var1) {
         this.tempList.clear();
         if (!Sancho.hasCollectionFactory()) {
            return;
         }

         Network[] var3 = this.viewFrame.getCore().getNetworkCollection().getNetworks();

         for (int var4 = 0; var4 < var3.length; var4++) {
            Network var5 = var3[var4];
            if (var5.isEnabled() && var5.isSearchable()) {
               this.tempList.add(var5.getEnumNetwork());
            }
         }

         if (this.tempList.size() != this.networkList.size()) {
            var2 = true;
         } else {
            for (int var10 = 0; var10 < this.tempList.size(); var10++) {
               if (!this.networkList.contains(this.tempList.get(var10))) {
                  var2 = true;
                  break;
               }
            }

            if (!this.networkCombo.isEnabled() && this.tempList.size() == 1) {
               var2 = true;
            }
         }
      }

      if (var2 || var1) {
         this.networkCombo.removeAll();
         this.networkList.clear();
         if (this.tempList.size() == 0) {
            if (!Sancho.hasCollectionFactory()) {
               return;
            }

            Network[] var6 = this.viewFrame.getCore().getNetworkCollection().getNetworks();

            for (int var8 = 0; var8 < var6.length; var8++) {
               Network var11 = var6[var8];
               if (var11.isEnabled() && var11.isSearchable()) {
                  this.tempList.add(var11.getEnumNetwork());
               }
            }
         }

         for (int var7 = 0; var7 < this.tempList.size(); var7++) {
            EnumNetwork var9 = (EnumNetwork)this.tempList.get(var7);
            this.networkList.add(var9);
            Network var12 = Sancho.getCore().getNetworkCollection().getByEnum(var9);
            if (var12 != null) {
               this.networkCombo.add(var12.getName());
            }
         }

         if (this.networkCombo.getItemCount() > 1) {
            this.networkCombo.add(S_ALL_NETWORKS);
         }

         this.networkCombo.select(this.networkCombo.getItemCount() - 1);
         if (!this.networkCombo.isEnabled()) {
            this.networkCombo.setEnabled(true);
         }

         if (!this.searchCombo.isEnabled()) {
            this.searchCombo.setText("");
            this.searchCombo.setEnabled(true);
         }

         if (!this.searchTab.isButtonEnabled()) {
            this.searchTab.setButtonEnabled(true);
         }
      }
   }

   public void update(MyObservable var1, Object var2, int var3) {
      if (this.networkCombo != null && !this.networkCombo.isDisposed()) {
         this.networkCombo.getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (ASearchTab.this.networkCombo != null && !ASearchTab.this.networkCombo.isDisposed()) {
                  if (Sancho.hasCollectionFactory() && ASearchTab.this.viewFrame.getCore().getNetworkCollection().getEnabledAndSearchable() != 0) {
                     ASearchTab.this.syncNetworkCombo(false);
                  } else {
                     if (ASearchTab.this.searchCombo.isEnabled()) {
                        ASearchTab.this.searchCombo.setText(ASearchTab.S_NO_NETWORK);
                        ASearchTab.this.searchCombo.setEnabled(false);
                     }

                     if (ASearchTab.this.networkCombo.isEnabled()) {
                        ASearchTab.this.networkCombo.removeAll();
                        ASearchTab.this.networkCombo.setText(ASearchTab.S_NO_NETWORK);
                        ASearchTab.this.networkCombo.setEnabled(false);
                     }

                     if (ASearchTab.this.searchTab.isButtonEnabled()) {
                        ASearchTab.this.searchTab.setButtonEnabled(false);
                     }
                  }
               }
            }
         });
      }
   }

   public void dispose() {
      if (Sancho.hasCollectionFactory()) {
         this.viewFrame.getCore().getNetworkCollection().deleteObserver(this);
      }

      if (this.networkCombo != null && !this.networkCombo.isDisposed()) {
         this.networkCombo.dispose();
      }

      for (int var1 = 0; var1 < this.searchComboList.size(); var1++) {
         Combo var2 = (Combo)this.searchComboList.get(var1);
         if (var2 != null && !var2.isDisposed()) {
            var2.dispose();
         }
      }
   }
}
