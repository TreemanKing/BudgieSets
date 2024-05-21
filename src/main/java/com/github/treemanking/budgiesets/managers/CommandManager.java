package com.github.treemanking.budgiesets.managers;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.commands.BudgieSetsCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class CommandManager implements BudgieSetsCommand {

    private final BudgieSets PLUGIN = BudgieSets.getBudgieSets();

    public CommandManager(BudgieSets plugin) {
        new CommandAPICommand("budgiesets")
                .withSubcommand(createArmorSet)
                .withSubcommand(removeArmorSet)
                .register(plugin);
    }


    CommandAPICommand createArmorSet = new CommandAPICommand("create")
            .withArguments(new StringArgument("name"))
            .withPermission("budgiesets.create")
            .executes((sender, args) -> {
                String armorSetConfig = (String) args.get("name");
                try {
                    createNewArmorSet(armorSetConfig, generateYamlContent());
                    sender.sendMessage(armorSetConfig + " successfully generated.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    CommandAPICommand removeArmorSet = new CommandAPICommand("remove")
            .withArguments(new StringArgument("name")
                    .replaceSuggestions(ArgumentSuggestions.stringsAsync(info -> CompletableFuture.supplyAsync(this::getArmorSetFileNames))))
            .withPermission("budgiesets.remove")
            .executes((sender, args) -> {
                String armorSetConfig = (String) args.get("name");
                deleteArmorSetFile(armorSetConfig);
                sender.sendMessage(armorSetConfig + " was successfully removed.");
            });

}
