package net.team33.undup;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Origin {

    private final Path root;
    private final Consumer<? super IOException> catcher;

    public Origin(final Path root, Consumer<? super IOException> catcher) {
        this.root = root;
        this.catcher = catcher;
    }

    public void forEach(final Consumer<Path> consumer) {
        forEach(root, consumer);
    }

    private void forEach(final Path path, final Consumer<Path> consumer) {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                entries.forEach(entry -> forEach(entry, consumer));
            } catch (IOException caught) {
                catcher.accept(caught);
            }
        } else {
            consumer.accept(path);
        }
    }
}
