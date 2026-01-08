package org.jhotdraw.gui.fontchooser;

import java.util.List;

public class FontGroup {

     String label;
     List<String> listOfFonts;

     public FontGroup() {
     }


     public FontGroup(String labelKey, List<String> listOfFonts) {
          this.label = labelKey;
          this.listOfFonts = listOfFonts;
     }

     public String getLabel() {
          return label;

     }

     public void setLabel(String labelKey) {
          this.label = labelKey;
     }

     public List<String> getFonts() {
          return listOfFonts;
     }

     public void setFonts(List<String> listOfFonts) {
          this.listOfFonts = listOfFonts;
     }

}
