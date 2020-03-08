package de.team33.test.dedupe;

import de.team33.cmds.dedupe.Main;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class MainTest {

    private static final Path SOURCE_PATH = Paths.get("src", "main", "java");
    private static final Path SUBJECT_PATH = Paths.get("target", "testing", "subject");
    private static final Path DOUBLET_PATH = Paths.get("target", "testing", "doublet");

    @Test
    public void mainEmpty() throws Exception {
        try {
            Main.main();
            Assert.fail("expected IllegalArgumentException but was none");
        } catch (final IllegalArgumentException caught) {
            caught.printStackTrace();
            // TODO: verify caught;
        }
    }

    @Test
    public void main() throws Exception {
        Main.main(SUBJECT_PATH.toString(), DOUBLET_PATH.toString());
        Assert.assertEquals(new Entry(SOURCE_PATH), new Entry(DOUBLET_PATH));
    }

    private class Entry implements Comparable<Entry> {

        private final String name;
        private final Set<Entry> entries;

        private transient Supplier<String> stringSupplier = new NewString();

        Entry(final Path path) {
            this.name = path.getFileName().toString();
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    this.entries = Files.list(path)
                                        .map(Entry::new)
                                        .collect(Collectors.toCollection(TreeSet::new));
                } catch (IOException e) {
                    throw new IllegalStateException("cannot read directory <" + path + ">");
                }
            } else {
                this.entries = null;
            }
        }

        private String newString(final String indent) {
            return new StringBuilder(indent).append(name)
                                            .append(": ")
                                            .append(newString(indent, entries))
                                            .toString();
        }

        private String newString(final String indent, final Set<Entry> entries) {
            return null == entries
                    ? "null"
                    : (entries.isEmpty() ? "{}" : entries.stream()
                                                         .map(entry -> entry.newString("   " + indent))
                                                         .collect(joining("\n", "{\n", "\n" + indent + "}")));
        }

        private class NewString implements Supplier<String> {

            @Override
            public synchronized String get() {
                if (this == stringSupplier) {
                    final String result = newString("");
                    stringSupplier = () -> result;
                }
                return stringSupplier.get();
            }
        }
    }
}
