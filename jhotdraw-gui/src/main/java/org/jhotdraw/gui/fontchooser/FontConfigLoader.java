package org.jhotdraw.gui.fontchooser;

import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.InputStream;

public class FontConfigLoader {
     public static FontConfig loadFontConfig() {
          InputStream in = FontConfig.class.getResourceAsStream("/org/jhotdraw/font/font-groups.yml");

          if (in == null) {
               throw new IllegalStateException("font-groups.yml not found");
          }

          Yaml yaml = new Yaml();
          FontConfig config = yaml.loadAs(in, FontConfig.class);
          return config;
     }
}
