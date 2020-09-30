package de.team33.test.dedupe;

import de.team33.cmds.dedupe.ImproperArgumentException;
import de.team33.cmds.dedupe.Main;
import de.team33.cmds.dedupe.patterns.Lazy;
import de.team33.cmds.dedupe.patterns.Resources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainTest {

    private static final Path TEST_PATH = Paths.get("target", "testing", UUID.randomUUID().toString());
    private static final Path SOURCE_PATH = Paths.get("src", "main", "java");
    private static final Path SUBJECT_PATH;
    private static final Path DOUBLET_PATH;

    static {
        SUBJECT_PATH = TEST_PATH.resolve("unique");
        DOUBLET_PATH = TEST_PATH.resolve("doublet");
    }

    @BeforeClass
    public static void beforeClass() throws IOException {
        Files.createDirectories(TEST_PATH);
        copy(SOURCE_PATH, SUBJECT_PATH);
        copy(SOURCE_PATH, DOUBLET_PATH);
    }

    private static void copy(final Path source, final Path target) throws IOException {
        Files.createDirectory(target);
        final List<Path> children = Files.list(source).collect(Collectors.toList());
        for (final Path srcChild : children) {
            final Path tgtChild = target.resolve(srcChild.getFileName());
            if (Files.isDirectory(srcChild)) {
                copy(srcChild, tgtChild);
            } else {
                Files.copy(srcChild, tgtChild);
            }
        }
    }

    @Test
    public void mainNoArgs() throws Exception {
        mainImproperArgs(Resources.load(MainTest.class, "expectedNoArgs.txt"));
    }

    @Test
    public final void mainTwoArgs() throws Exception {
        mainImproperArgs(Resources.load(MainTest.class, "expectedTwoArgs.txt"), "arg1", "arg2");
    }

    @Test
    public final void mainMoreArgs() throws Exception {
        mainImproperArgs(Resources.load(MainTest.class, "expectedMoreArgs.txt"), "arg1", "arg2", "arg3");
    }

    private static void mainImproperArgs(final String expected, final String... args) throws Exception {
        try {
            Main.main(args);
            Assert.fail("expected ImproperArgumentException but was none");
        } catch (final ImproperArgumentException caught) {
            caught.printStackTrace();
            Assert.assertEquals(expected, caught.getMessage());
        }
    }

    @Test
    public final void mainNewJob() throws Exception {
        final Path path = TEST_PATH.resolve("new.dedupe");
        Files.deleteIfExists(path);
        Main.main(path.toString());
        Assert.assertTrue(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS));
    }

    @Test
    public final void main() throws Exception {
        final Path path = TEST_PATH.resolve("default.dedupe");
        Files.deleteIfExists(path);
        Main.main(path.toString());
        Assert.assertTrue(Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS));
        Main.main(path.toString());
        Assert.assertEquals(treeOf(SOURCE_PATH), treeOf(SUBJECT_PATH));
        Assert.assertEquals("", treeOf(DOUBLET_PATH));
        Assert.assertEquals(treeOf(SOURCE_PATH), treeOf(TEST_PATH.resolve("(trash)")));
    }

    private Object treeOf(final Path path) {
        try {
            if (Files.isDirectory(path)) {
                return treeOf(Files.list(path));
            } else {
                return Files.size(path);
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private Map<String, Object> treeOf(final Stream<? extends Path> paths) {
        final Map<String, Object> result = new TreeMap<>();
        final List<Path> children = paths.collect(Collectors.toList());
        for (final Path child : children) {
            result.put(child.getFileName().toString(), treeOf(child));
        }
        return result;
    }
}
