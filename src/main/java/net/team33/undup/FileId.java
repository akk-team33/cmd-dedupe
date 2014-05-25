package net.team33.undup;

import java.math.BigInteger;
import java.util.regex.Pattern;

public class FileId {

    private final long majorIndex;
    private final BigInteger hashCode;
    private final int minorIndex;
    private final String extension;

    public FileId(final String fileName) throws Problem {
        final String[] split = fileName.split(Pattern.quote("."));
        if (4 == split.length) {
            try {
                this.majorIndex = Long.parseLong(split[0]);
                this.hashCode = new BigInteger(split[1]);
                this.minorIndex = Integer.parseInt(split[2]);
                this.extension = split[3];
            } catch (Throwable caught) {
                throw new Problem("fileName(" + fileName + ")", caught);
            }
        } else {
            throw new Problem("fileName(" + fileName + ")");
        }
    }

    public BigInteger getHashCode() {
        return hashCode;
    }

    public static class Problem extends Exception {
        private Problem(final String message) {
            super(message);
        }

        private Problem(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
