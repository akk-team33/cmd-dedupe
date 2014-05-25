package net.team33.undup;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;

public class HashCode {

    private static final int RADIX = Math.min(36, Character.MAX_RADIX);

    final byte[] bytes;

    private HashCode(final byte[] bytes) {
        this.bytes = copy(bytes);
    }

    public static Builder builder(final byte[] salt) {
        return new Builder(salt);
    }

    private static byte[] copy(final byte[] origin) {
        final byte[] result = new byte[origin.length];
        System.arraycopy(origin, 0, result, 0, result.length);
        return result;
    }

    private static Counter copy(final Counter origin) {
        return origin.copy();
    }

    @Override
    public final boolean equals(final Object other) {
        return (this == other) || ((other instanceof HashCode) && equals((HashCode) other));
    }

    private boolean equals(final HashCode other) {
        return Arrays.equals(bytes, other.bytes);
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public final String toString() {
        return toBigInteger().toString(RADIX);
    }

    public final BigInteger toBigInteger() {
        return new BigInteger(bytes);
    }

    public final byte[] toByteArray() {
        return copy(bytes);
    }

    private static class Counter {
        private long value;

        private Counter(final long startValue) {
            value = startValue;
        }

        public int toIndex(final int length) {
            return (int) (value % length);
        }

        public void increment() {
            value += 1;
        }

        public Counter copy() {
            return new Counter(value);
        }
    }

    public static class Builder {
        private static final int BUFFER_SIZE = 1024;

        final byte[] bytes;
        final Counter counter;

        private Builder(final Builder origin) {
            bytes = copy(origin.bytes);
            counter = copy(origin.counter);
        }

        private Builder(final byte[] salt) {
            final int length = salt.length;
            this.bytes = new byte[length + 1];
            this.counter = new Counter(0);
            System.arraycopy(salt, 0, bytes, 1, length);
        }

        private static void add(final byte[] target, final Counter counter, final byte[] origin, final int length) {
            for (int index = 0; index < length; ++index) {
                target[counter.toIndex(target.length - 1) + 1] += origin[index];
                counter.increment();
            }
        }

        private static void add(final byte[] target, final Counter counter, final InputStream origin) throws IOException {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int read = origin.read(buffer);
            while (0 < read) {
                add(target, counter, buffer, read);
                read = origin.read(buffer);
            }
        }

        public final Builder add(final byte[] origin) {
            add(bytes, counter, origin, origin.length);
            return this;
        }

        public final Builder add(final InputStream origin) throws IOException {
            add(bytes, counter, origin);
            return this;
        }

        public final HashCode build() {
            return build(true);
        }

        public final HashCode build(final boolean respectCount) {
            if (respectCount) {
                return new Builder(this).add(BigInteger.valueOf(counter.value).toByteArray()).build(false);
            } else {
                return new HashCode(bytes);
            }
        }
    }
}
