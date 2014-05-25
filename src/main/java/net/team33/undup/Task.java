package net.team33.undup;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class Task implements Runnable {
    private final List<Throwable> problems = new LinkedList<>();
    private final Mapping mapping;
    private final Origin origin;
    private final Target target;

    public Task(final Args args) {
        origin = new Origin(args.getOrigin(), problems::add);
        target = new Target(args.getTarget(), args.getTrash(), problems::add);
        mapping = new Mapping(args.getTarget());
    }

    @Override
    public void run() {
        origin.forEach(path -> {
            target.move(path);
        });
    }

    public void report(final PrintStream out) {
        out.println();
        if (0 < problems.size()) {
            out.println("[NO PROBLEMS]");
        } else {
            out.println("[PROBLEMS]");
            problems.forEach(problem -> problem.printStackTrace(out));
        }
    }
}
