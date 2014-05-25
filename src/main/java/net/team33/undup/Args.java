package net.team33.undup;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Args {

    private static final String PROBLEM = "%n" +
            "Given arguments:%n%n" +
            "    %s%n%n" +
            "Required arguments:%n%n" +
            "    [ORIGIN, TARGET, TRASH]%n%n" +
            "    - ORIGIN: a path to a file or directory to be un-duplicated (recursively)%n" +
            "    - TARGET: a path to a repository of unique files%n" +
            "    - TRASH:  a path to a repository of duplicate files%n";

    private final Path origin;
    private final Path target;
    private final Path trash;

    public Args(final String[] args) {
        if (3 == args.length) {
            origin = Paths.get(args[0]).toAbsolutePath().normalize();
            target = Paths.get(args[1]).toAbsolutePath().normalize();
            trash = Paths.get(args[2]).toAbsolutePath().normalize();
        } else {
            throw new Problem(String.format(PROBLEM, Arrays.toString(args)));
        }
    }

    public final Path getTarget() {
        return target;
    }

    public final Path getOrigin() {
        return origin;
    }

    public Path getTrash() {
        return trash;
    }

    public static class Problem extends Error {
        private Problem(final String message) {
            super(message);
        }
    }
}
