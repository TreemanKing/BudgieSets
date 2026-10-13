package com.github.treemanking.budgiesets.commands;

import dev.jorel.commandapi.CommandAPICommand;

/**
 * A single "/budgiesets" subcommand.
 * <p>
 * Implementations receive the collaborators they need through their constructor and
 * describe themselves as a {@link CommandAPICommand}, allowing the command manager to
 * register any number of subcommands without knowing anything about them.
 */
public interface SubCommand {

    /**
     * Builds the CommandAPI representation of this subcommand.
     *
     * @return the configured subcommand, ready to be registered
     */
    CommandAPICommand build();
}

