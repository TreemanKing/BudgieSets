package com.github.treemanking.budgiesets.managers;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.commands.BudgieSetsCommand;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class CommandManager implements BudgieSetsCommand {

    public CommandManager(BudgieSets plugin) {
        new CommandAPICommand("budgiesets")
                .withSubcommand(createArmorSet)
                .withSubcommand(removeArmorSet)
                .withSubcommand(reloadCommand)
                .withSubcommand(renameCommand)
                .register(plugin);
    }

    // TODO: Add help messages to each sub command

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
            .withAliases("delete", "yeet")
            .executes((sender, args) -> {
                String armorSetConfig = (String) args.get("name");
                deleteArmorSetFile(armorSetConfig);
                sender.sendMessage(armorSetConfig + " was successfully removed.");
            });

    CommandAPICommand reloadCommand = new CommandAPICommand("reload")
            .withPermission("budgiesets.reload")
            .executes(((sender, args) -> {
                reloadPlugin();
            }));

    CommandAPICommand renameCommand = new CommandAPICommand("rename")
            .withPermission("budgiesets.rename")
            .withArguments(new StringArgument( "oldName")
                    .replaceSuggestions(ArgumentSuggestions.stringsAsync(info -> CompletableFuture.supplyAsync(this::getArmorSetFileNames))))
            .withArguments(new StringArgument("newName"))
            .executes(((sender, args) -> {
                if (renameArmorSetFile((String) args.get(0), (String) args.get(1))) {
                    BudgieSets.getBudgieSets().getLogger().info(args.get(0) + " was renamed to " + args.get(1));
                }
            }));

    CommandAPICommand disableSet;

    CommandAPICommand enableSet = new CommandAPICommand("enableSet")
            .withPermission("budgiesets.enableSet")
            .withArguments(new StringArgument( "oldName")
                    .replaceSuggestions(ArgumentSuggestions.stringsAsync(info -> CompletableFuture.supplyAsync(this::getArmorSetFileNames))));
}
