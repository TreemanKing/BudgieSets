package com.github.treemanking.budgiesets.commands;

import com.github.treemanking.budgiesets.commands.subcommands.CreateCommand;
import com.github.treemanking.budgiesets.commands.subcommands.ReloadCommand;
import com.github.treemanking.budgiesets.commands.subcommands.RemoveCommand;
import com.github.treemanking.budgiesets.commands.subcommands.RenameCommand;

public interface BudgieSetsCommand extends CreateCommand, RemoveCommand, ReloadCommand, RenameCommand {

}
