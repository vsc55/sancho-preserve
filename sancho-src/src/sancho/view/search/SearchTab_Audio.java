package sancho.view.search;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import sancho.core.Sancho;
import sancho.model.mldonkey.utility.SearchQuery;
import sancho.view.SearchTab;
import sancho.view.search.result.ResultTab;
import sancho.view.search.result.ResultViewFrame;
import sancho.view.utility.SResources;

public class SearchTab_Audio extends ASearchTab2 {
   private Combo albumCombo;
   private Combo artistCombo;
   private Combo bitrateCombo;

   public SearchTab_Audio(ResultViewFrame var1, SearchTab var2) {
      super(var1, var2);
   }

   public Control createTab(CTabFolder var1) {
      Composite var2 = this.createMainComposite(var1);
      this.searchCombo = this.createSavedSearchCombo(var2, "s.a.title", "audioTitleSearchFor");
      this.artistCombo = this.createSavedSearchCombo(var2, "s.a.artist", "audioArtistSearchFor");
      this.albumCombo = this.createSavedSearchCombo(var2, "s.a.album", "audioAlbumSearchFor");
      this.createSeparator(var2);
      String[] var3 = new String[]{"", "32kb", "64kb", "96kb", "128kb", "160kb", "192kb", "256kb", "320kb"};
      this.bitrateCombo = this.createCombo(var2, 8, "s.a.bitrate", var3);
      this.createNetworkCombo(var2);
      var3 = new String[]{"", "mp3", "ogg", "wav", "midi"};
      this.formatCombo = this.createCombo(var2, 0, "s.format", var3);
      this.searchTypeCombo = this.createSearchTypeCombo(var2);
      this.createSeparator(var2);
      var3 = new String[]{"", "3", "5", "10", "25", "50"};
      this.minAvailCombo = this.createCombo(var2, 0, "s.minAvail", var3);
      this.minCombo = this.createMinMaxCombo(var2, 0, "s.minSize");
      this.maxCombo = this.createMinMaxCombo(var2, 0, "s.maxSize");
      var3 = new String[]{"", "50", "100", "200", "400"};
      this.resultCombo = this.createIntegerCombo(var2, 0, "s.maxResults", var3);
      var2.setData(this);
      return var2;
   }

   public String getText() {
      return SResources.getString("s.tab.audio");
   }

   public void performSearch() {
      String var1 = this.searchCombo.getText();
      String var2 = this.albumCombo.getText();
      String var3 = this.artistCombo.getText();
      String var4 = "";
      if (!var1.equals("") || !var3.equals("") || !var2.equals("")) {
         if (Sancho.hasCollectionFactory()) {
            SearchQuery var5 = this.viewFrame.getCore().getCollectionFactory().getSearchQuery();
            if (!var1.equals("")) {
               this.searchCombo.add(var1, 0);
               var5.setMp3Title(var1);
               var4 = var4 + var1;
            }

            if (!var3.equals("")) {
               this.artistCombo.add(var3, 0);
               var5.setMp3Artist(var3);
               if (var4.length() > 0) {
                  var4 = var4 + "/";
               }

               var4 = var4 + var3;
            }

            if (!var2.equals("")) {
               this.albumCombo.add(var2, 0);
               var5.setMp3Album(var2);
               if (var4.length() > 0) {
                  var4 = var4 + "/";
               }

               var4 = var4 + var2;
            }

            this.artistCombo.setText("");
            this.albumCombo.setText("");
            this.searchCombo.setText("");
            String var6 = this.bitrateCombo.getText();
            if (!var6.equals("")) {
               int var7;
               try {
                  var7 = Integer.parseInt(var6.substring(0, var6.length() - 2));
               } catch (NumberFormatException var10) {
                  var7 = 0;
               }

               var5.setMp3Bitrate(String.valueOf(var7));
            }

            this.parseSearchType(this.searchTypeCombo, var5);
            this.addMinSizeToQuery(var5, this.minCombo);
            this.addMaxSizeToQuery(var5, this.maxCombo);
            if (!this.resultCombo.getText().equals("")) {
               int var11 = 0;

               try {
                  var11 = Integer.parseInt(this.resultCombo.getText());
               } catch (NumberFormatException var9) {
                  var11 = 100;
               }

               var5.setMaxSearchResults(var11);
            }

            if (!this.formatCombo.getText().equals("")) {
               var5.setFormat(this.formatCombo.getText());
            }

            this.addNetwork(var5);
            var5.send();
            new ResultTab(this.viewFrame, this.searchTab.getCTabFolder(), this.searchTab, var5.getSearchId(), var4);
         }
      }
   }
}
