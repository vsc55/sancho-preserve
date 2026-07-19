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

   public SearchTab_Audio(ResultViewFrame viewFrame, SearchTab searchTab) {
      super(viewFrame, searchTab);
   }

   public Control createTab(CTabFolder tabFolder) {
      Composite composite = this.createMainComposite(tabFolder);
      this.searchCombo = this.createSavedSearchCombo(composite, "s.a.title", "audioTitleSearchFor");
      this.artistCombo = this.createSavedSearchCombo(composite, "s.a.artist", "audioArtistSearchFor");
      this.albumCombo = this.createSavedSearchCombo(composite, "s.a.album", "audioAlbumSearchFor");
      this.createSeparator(composite);
      String[] values = new String[]{"", "32kb", "64kb", "96kb", "128kb", "160kb", "192kb", "256kb", "320kb"};
      this.bitrateCombo = this.createCombo(composite, 8, "s.a.bitrate", values);
      this.createNetworkCombo(composite);
      values = new String[]{"", "mp3", "ogg", "wav", "midi"};
      this.formatCombo = this.createCombo(composite, 0, "s.format", values);
      this.searchTypeCombo = this.createSearchTypeCombo(composite);
      this.createSeparator(composite);
      values = new String[]{"", "3", "5", "10", "25", "50"};
      this.minAvailCombo = this.createCombo(composite, 0, "s.minAvail", values);
      this.minCombo = this.createMinMaxCombo(composite, 0, "s.minSize");
      this.maxCombo = this.createMinMaxCombo(composite, 0, "s.maxSize");
      values = new String[]{"", "50", "100", "200", "400"};
      this.resultCombo = this.createIntegerCombo(composite, 0, "s.maxResults", values);
      composite.setData(this);
      return composite;
   }

   public String getText() {
      return SResources.getString("s.tab.audio");
   }

   public void performSearch() {
      String titleText = this.searchCombo.getText();
      String albumText = this.albumCombo.getText();
      String artistText = this.artistCombo.getText();
      String resultName = "";
      if (!titleText.equals("") || !artistText.equals("") || !albumText.equals("")) {
         if (Sancho.hasCollectionFactory()) {
            SearchQuery query = this.viewFrame.getCore().getCollectionFactory().getSearchQuery();
            if (!titleText.equals("")) {
               this.searchCombo.add(titleText, 0);
               query.setMp3Title(titleText);
               resultName = resultName + titleText;
            }

            if (!artistText.equals("")) {
               this.artistCombo.add(artistText, 0);
               query.setMp3Artist(artistText);
               if (resultName.length() > 0) {
                  resultName = resultName + "/";
               }

               resultName = resultName + artistText;
            }

            if (!albumText.equals("")) {
               this.albumCombo.add(albumText, 0);
               query.setMp3Album(albumText);
               if (resultName.length() > 0) {
                  resultName = resultName + "/";
               }

               resultName = resultName + albumText;
            }

            this.artistCombo.setText("");
            this.albumCombo.setText("");
            this.searchCombo.setText("");
            String bitrateText = this.bitrateCombo.getText();
            if (!bitrateText.equals("")) {
               int bitrate;
               try {
                  bitrate = Integer.parseInt(bitrateText.substring(0, bitrateText.length() - 2));
               } catch (NumberFormatException notANumber) {
                  bitrate = 0;
               }

               query.setMp3Bitrate(String.valueOf(bitrate));
            }

            this.parseSearchType(this.searchTypeCombo, query);
            this.addMinSizeToQuery(query, this.minCombo);
            this.addMaxSizeToQuery(query, this.maxCombo);
            if (!this.resultCombo.getText().equals("")) {
               int maxResults = 0;

               try {
                  maxResults = Integer.parseInt(this.resultCombo.getText());
               } catch (NumberFormatException notANumber) {
                  maxResults = 100;
               }

               query.setMaxSearchResults(maxResults);
            }

            if (!this.formatCombo.getText().equals("")) {
               query.setFormat(this.formatCombo.getText());
            }

            this.addNetwork(query);
            query.send();
            new ResultTab(this.viewFrame, this.searchTab.getCTabFolder(), this.searchTab, query.getSearchId(), resultName);
         }
      }
   }
}
