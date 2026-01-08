package org.jhotdraw.gui.fontchooser;

import java.util.Map;

public class FontConfig {
     private Map<String, FontGroup> groups;

     public FontConfig() {
     }

     public Map<String, FontGroup> getGroups() {
          return groups;
     }

     public void setGroups(Map<String, FontGroup> groups) {
          this.groups = groups;
     }
}
