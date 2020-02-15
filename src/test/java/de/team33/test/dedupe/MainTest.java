package de.team33.test.dedupe;

import de.team33.cmds.dedupe.Main;
import net.team33.undup.Args;
import org.junit.Test;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainTest {

    private static final String[] EMPTY = {};
    private static final String[] SINGLE = {"source"};
    private static final String[] DOUBLE = {"source", "target"};
    private static final String[] QUADRUPLE = {"source", "target", "trash", "more"};
    private static final String[] ILLEGAL = {"?*:", "*:?", ":?*"};
    private static final Path TEST_IO = Paths.get("target", "test.io", MainTest.class.getName());

    @Test
    public void testMain() throws Exception {
        final Path testMain = TEST_IO.resolve("testMain");
        final Path origin = testMain.resolve("origin");
        final Path target = testMain.resolve("target");
        final Path trash = testMain.resolve("trash");

        //prepare(origin);

        testMain(new String[]{
                origin.toString(),
                target.toString(),
                trash.toString()
        });
    }

    @Test(expected = InvalidPathException.class)
    public void testMainArgsIllegal() throws Exception {
        testMain(ILLEGAL);
    }

    @Test(expected = Args.Problem.class)
    public void testMainArgsEmpty() throws Exception {
        testMain(EMPTY);
    }

    @Test(expected = Args.Problem.class)
    public void testMainArgsSingle() throws Exception {
        testMain(SINGLE);
    }

    @Test(expected = Args.Problem.class)
    public void testMainArgsPair() throws Exception {
        testMain(DOUBLE);
    }

    @Test(expected = Args.Problem.class)
    public void testMainArgsQuadruple() throws Exception {
        testMain(QUADRUPLE);
    }

    private void testMain(final String[] args) {
        try {
            Main.main(args);
        } catch (Throwable caught) {
            caught.printStackTrace();
            throw caught;
        }
    }
}
