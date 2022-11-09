package org.commandmap.detail.argument;

import org.commandmap.detail.AliasCommandDetail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class AliasDetailArgument implements ServerBoundArgument {

	private final @Nullable String name;
	private final boolean optional;
	private final AliasCommandDetail detail;

	public AliasDetailArgument(@NotNull AliasCommandDetail detail, @Nullable String name, boolean optional) {
		this.name = name;
		this.optional = optional;
		this.detail = detail;
	}

	@Override
	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	@Override
	public boolean isOptional() {
		return this.optional;
	}

	@Override
	public Collection<CommandCompletion> getSuggestions(CommandContext context, String currentInput) {
		return Sponge.server()
				.commandManager()
				.complete(context.subject(), context.cause().audience(),
						this.detail.getUserCommand() + " " + currentInput);
	}

	@Override
	public Optional<Comparator<CommandCompletion>> getSorting() {
		return Optional.empty();
	}
}
