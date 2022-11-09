package org.commandmap.detail;

import org.commandmap.detail.argument.DetailArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandDetail {

	@NotNull String getCommand();

	@NotNull String getOwningPlugin();

	@NotNull List<DetailArgument> getArguments();

	@NotNull String[] getAliases();

	@NotNull DetailType getType();

	default @NotNull String getUserCommand() {
		return this.getOwningPlugin() + ":" + this.getCommand();
	}
}
