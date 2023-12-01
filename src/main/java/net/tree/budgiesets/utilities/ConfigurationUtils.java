package net.tree.budgiesets.utilities;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationUtils {
    public static List<ConfigurationSection> getConfigurationSectionList(ConfigurationSection parentSection, String key) {
        List<ConfigurationSection> sectionList = new ArrayList<>();

        if (parentSection.isConfigurationSection(key)) {
            ConfigurationSection listSection = parentSection.getConfigurationSection(key);
            for (String sectionKey : listSection.getKeys(false)) {
                ConfigurationSection subsection = listSection.getConfigurationSection(sectionKey);
                if (subsection != null) {
                    sectionList.add(subsection);
                }
            }
        }

        return sectionList;
    }
}
