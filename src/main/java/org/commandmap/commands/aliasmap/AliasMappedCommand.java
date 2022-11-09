package org.commandmap.commands.aliasmap;

import org.commandmap.detail.AliasCommandDetail;
import org.commandmap.detail.CommandDetail;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;

public class AliasMappedCommand {

	private final @NotNull AliasCommandDetail detail;
	private final @NotNull Parameter.Value<String> arg;

	public AliasMappedCommand(@NotNull AliasCommandDetail detail) {
		this.detail = detail;
		this.arg = Arguments.alias(Parameter.key("arguments", String.class),
				this.detail.getArgument()).build();
	}

	public CommandDetail getDetail() {
		return detail;
	}

	public Command.Parameterized buildCommand() {
		return Command
				.builder()
				.executor(context -> {
					String args = context.one(arg).orElse("");
					String command = detail.getUserCommand() + " " + args;

					System.out.println("Running command: '" + command + "'");

					return Sponge.server()
							.commandManager()
							.process(context.subject(), context.cause().audience(),
									command);
				})
				.addParameter(arg)
				.build();
	}
}
