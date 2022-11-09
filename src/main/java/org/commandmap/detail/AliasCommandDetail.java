package org.commandmap.detail;

import org.commandmap.detail.argument.AliasDetailArgument;
import org.commandmap.detail.argument.DetailArgument;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class AliasCommandDetail implements CommandDetail {

	private final @NotNull String command;
	private final @NotNull String owningPlugin;
	private final @NotNull String[] aliases;

	@Deprecated
	public AliasCommandDetail(@NotNull String owningPlugin, @NotNull String command) {
		this(owningPlugin, command, new String[0]);
	}

	public AliasCommandDetail(@NotNull String owningPlugin, @NotNull String command, String... aliases) {
		if (aliases.length == 0) {
			throw new RuntimeException("Aliases must be specified");
		}
		this.command = command;
		this.aliases = aliases;
		this.owningPlugin = owningPlugin;
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
	@Deprecated
	public @NotNull List<DetailArgument> getArguments() {
		return Collections.singletonList(this.getArgument());
	}

	public @NotNull AliasDetailArgument getArgument() {
		return new AliasDetailArgument(this, "arguments", true);
	}

	@Override
	public @NotNull String[] getAliases() {
		return this.aliases;
	}

	@Override
	public @NotNull DetailType getType() {
		return DetailType.ALIAS;
	}
}
