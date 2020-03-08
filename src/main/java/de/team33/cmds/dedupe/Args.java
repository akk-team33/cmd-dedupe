package de.team33.cmds.dedupe;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Args {

    public static final String MESSAGE = "\n" +
            "The call is incorrectly parameterized. usage:\n" +
            "\n" +
            "        dedupe SUBJECT_PATH DOUBLET_PATH\n" +
            "\n" +
            "where\n" +
            "\n" +
            "        SUBJECT_PATH: A path to be deduped.\n" +
            "        DOUBLET_PATH: A path to which duplicates are moved." +
            "\n";

    private final Path subjectPath;
    private final Path doubletPath;

    public Args(final String... args) {
        if (2 == args.length) {
            this.subjectPath = Paths.get(args[0]).toAbsolutePath().normalize();
            this.doubletPath = Paths.get(args[1]).toAbsolutePath().normalize();
        } else {
            throw new IllegalArgumentException(MESSAGE);
        }
    }
}
