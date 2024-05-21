package com.github.treemanking.budgiesets.managers;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.configuration.TemplateArmorSetConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;

import java.io.IOException;

public class CommandManager implements TemplateArmorSetConfig {

    private final BudgieSets PLUGIN = BudgieSets.getBudgieSets();

    public CommandManager(BudgieSets plugin) {
        new CommandAPICommand("budgiesets").withSubcommand(createArmorSet).register(plugin);
    }


    CommandAPICommand createArmorSet = new CommandAPICommand("create")
            .withArguments(new StringArgument("name"))
            .withPermission("budgiesets.create")
            .executes((sender, args) -> {
                String armorSetConfig = (String) args.get("name");
                try {
                    createNewArmorSet(armorSetConfig, generateYamlContent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

}
