package de.team33.cmds.dedupe;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private Main(final Args args) {
    }

    public static void main(final String ... args) {
        if (1 == args.length) {
            main(Paths.get(args[0]).toAbsolutePath().normalize());
        } else {
            throw new ImproperArgumentException(args);
        }
    }

    private static void main(final Path path) {
        if (Files.isRegularFile(path)) {
            main(Config.read(path));
        } else {
            main(Config.init(path), path);
        }
    }

    private static void main(final Config initial, final Path path) {
        System.out.println();
        System.out.append("created new job configuration in <").append(path.toString()).append(">")
                  .println();
        System.out.append("content: ").append(initial.toString())
                  .println();
    }

    private static void main(final Config config) {
        System.out.append("config: ").append(config.toString())
                  .println();
    }
}
