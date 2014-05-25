package net.team33.undup;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapping {

    private final Map<BigInteger, Path> backing = new HashMap<>();

    public Mapping(final Path target) {
        final List<Throwable> problems = new LinkedList<>();
        try {
            Files.createDirectories(target);
            build(target, backing, problems);
        } catch (IOException caught) {
            problems.add(caught);
        }
        if (0 < problems.size()) {
            throw new Problem(problems);
        }
    }

    private static void build(final Path target, final Map<BigInteger, Path> backing, final List<Throwable> problems) {
        if (Files.isDirectory(target, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(target)) {
                paths.forEach(path -> build(path, backing, problems));
            } catch (IOException caught) {
                problems.add(caught);
            }

        } else {
            try {
                final FileId fileId = new FileId(target.getFileName().toString());
                backing.put(fileId.getHashCode(), target);
            } catch (FileId.Problem caught) {
                problems.add(caught);
            }
        }
    }

    public static class Problem extends RuntimeException {
        private Problem(final List<Throwable> problems) {
            problems.forEach(this::addSuppressed);
        }
    }
}
