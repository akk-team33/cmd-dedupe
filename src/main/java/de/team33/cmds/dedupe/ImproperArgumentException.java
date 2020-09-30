package de.team33.cmds.dedupe;

import de.team33.cmds.dedupe.patterns.Lazy;
import de.team33.cmds.dedupe.patterns.Resources;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

public class ImproperArgumentException extends RuntimeException {

    private static final Lazy<String> MESSAGE_FORMAT =
            new Lazy<>(() -> Resources.load(ImproperArgumentException.class, "ImproperArgumentMessage.txt"));

    ImproperArgumentException(final String[] args) {
        super(String.format(MESSAGE_FORMAT.get(), Arrays.asList(args)));
    }

    ImproperArgumentException(final Path path, final Exception cause) {
        super(String.format(MESSAGE_FORMAT.get(), Collections.singletonList(path)), cause);
    }
}
