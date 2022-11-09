package org.commandmap.detail;

import org.commandmap.detail.argument.DetailArgument;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MappedCommandDetail implements CommandDetail {

	private final @NotNull String command;
	private final @NotNull String owningPlugin;
	private final String[] aliases;
	private final List<DetailArgument> arguments = new LinkedList<>();

	@Deprecated
	public MappedCommandDetail(@NotNull String owningPlugin, @NotNull String command,
			Collection<DetailArgument> arguments) {
		this(owningPlugin, command, arguments, new String[0]);
	}

	public MappedCommandDetail(@NotNull String owningPlugin, @NotNull String command,
			Collection<DetailArgument> arguments, String... aliases) {
		if (aliases.length == 0) {
			throw new RuntimeException("Aliases must be specified");
		}
		this.owningPlugin = owningPlugin;
		this.arguments.addAll(arguments);
		this.command = command;
		this.aliases = aliases;
	}

	@Override
	public @NotNull DetailType getType() {
		return DetailType.MAP;
	}

	@Override
	public @NotNull String getCommand() {
		return this.command;
	}

	@Override
	public @NotNull String getOwningPlugin() {
		return this.owningPlugin;
	}

	@Override
	public @NotNull List<DetailArgument> getArguments() {
		return this.arguments;
	}

	@Override
	public @NotNull String[] getAliases() {
		return this.aliases;
	}
}
