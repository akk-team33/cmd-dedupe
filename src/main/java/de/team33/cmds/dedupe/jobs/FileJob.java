package de.team33.cmds.dedupe.jobs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.Optional;

public class FileJob {

    private final Path path;
    private final HashCodes hashCodes;

    public FileJob(final Path path, final HashCodes hashCodes) {
        this.path = path;
        this.hashCodes = hashCodes;
    }

    public void run() throws IOException {
        final FileTime hashCodesTime = hashCodes.getTime();
        final FileTime lastModified = Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS);
        if (lastModified.compareTo(hashCodesTime) < 0) {
            final HashCode hashCode = getHashCode();
        }
        throw new UnsupportedOperationException("not yet implemented");
    }

    private HashCode getHashCode(final HashCode pre) throws IOException {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private HashCode getHashCode() throws IOException {
        final HashCode hashCode = hashCodes.get(path.getFileName().toString());
        return null == hashCode ? newHashCode() : hashCode;
    }

    private HashCode newHashCode() throws IOException {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
