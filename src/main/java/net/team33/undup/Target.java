package net.team33.undup;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardOpenOption.READ;
import static java.util.Objects.requireNonNull;

public class Target {

    private static final byte[] SALT = {
            109, 57, 61, -48,
            -5, 70, -128, 100,
            98, -40, 67, -1,
            83, -15, -108, -17
    };

    private final Path main;
    private final Path trash;
    private final Consumer<? super IOException> consumer;

    public Target(final Path main, final Path trash, final Consumer<? super IOException> consumer) {
        this.main = requireNonNull(main);
        this.trash = requireNonNull(trash);
        this.consumer = requireNonNull(consumer);
    }

    public void move(final Path origin) {
        try {
            Files.move(origin, target(hashCode(origin)));
        } catch (IOException e) {
            consumer.accept(e);
        }
    }

    private Path target(final BigInteger hashCode) {
        return null;
    }

    private BigInteger hashCode(final Path path) throws IOException {
        try (final InputStream in = Files.newInputStream(path, NOFOLLOW_LINKS, READ)) {
            return HashCode.builder(SALT).add(in).build().toBigInteger();
        }
    }
}
