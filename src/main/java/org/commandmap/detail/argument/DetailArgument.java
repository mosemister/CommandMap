package org.commandmap.detail.argument;

import java.util.Optional;

public interface DetailArgument {

	Optional<String> getName();

	boolean isOptional();
}
