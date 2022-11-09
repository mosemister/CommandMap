package org.commandmap.commands.aliasmap;

import org.commandmap.detail.argument.ServerBoundArgument;
import org.spongepowered.api.command.parameter.Parameter;

public class Arguments {

	public static Parameter.Value.Builder<String> alias(Parameter.Key<String> key, ServerBoundArgument argument) {
		Parameter.Value.Builder<String> result = Parameter.remainingJoinedStrings()
				.key(key)
				.completer(argument::getSuggestionsSorted);
		if (argument.isOptional()) {
			result = result.optional();
		}
		return result;

	}
}
