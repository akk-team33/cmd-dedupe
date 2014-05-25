package net.team33.undup;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HashCodeTest {

    private static final byte[][] SUBJECTS = {
            {},
            {0},
            {0, 0},
            {0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-1},
            {-1, -1},
            {-1, -1, -1},
            {-1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {1},
            {2, 3},
            {4, 5, 6, 7, 8},
            {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29}
    };
    private static final BigInteger[] BIG_INTEGERS = {
            BigInteger.ZERO,
            BigInteger.ONE,
            BigInteger.TEN,
            BigInteger.valueOf(278),
            BigInteger.valueOf(4587),
            BigInteger.valueOf(35478),
            BigInteger.valueOf(751236),
            BigInteger.valueOf(9451247),
            BigInteger.valueOf(85632149),
            BigInteger.valueOf(945124745952L),
            BigInteger.valueOf(856321499654127896L)
    };
    private static final byte[] SALT = {
            8, 18, 22, 30,
            86, 102, 111, 124,
            -95, -71, -97, -63,
            43, 52, 65, 74};

    private static boolean equals(final byte[] left, final byte[] right) {
        return toList(left).equals(toList(right));
    }

    private static void forEach(final byte[][] lefts, final byte[][] rights, final Tester tester) {
        for (final byte[] left : lefts) {
            forEach(left, rights, tester);
        }
    }

    private static void forEach(final byte[] left, final byte[][] rights, final Tester tester) {
        for (final byte[] right : rights) {
            tester.test(left, right);
        }
    }

    private static String toString(final byte[] bytes) {
        return toList(bytes).toString();
    }

    private static List<Byte> toList(final byte[] bytes) {
        final List<Byte> result = new ArrayList<>();
        for (final byte b : bytes) {
            result.add(b);
        }
        return result;
    }

    @Test
    public void testBigInteger() throws Exception {
        Stream.of(BIG_INTEGERS).forEach(value -> {
            final byte[] byteArray = value.toByteArray();
            try (final InputStream inputStream = new ByteArrayInputStream(byteArray)) {
                final HashCode result = HashCode.builder(new byte[byteArray.length])
                        .add(inputStream)
                        .build(false);
                // System.out.println(result);
                Assert.assertEquals(value, result.toBigInteger());
            } catch (IOException e) {
                throw new Error(e);
            }
        });
    }

    @Test
    public void testZero() throws Exception {
        final HashCode hashCode = HashCode.builder(new byte[8])
                .add(new byte[0])
                .build();
        Assert.assertEquals(BigInteger.ZERO, hashCode.toBigInteger());
    }

    @Test
    public void testSalt() throws Exception {
        final HashCode hashCode = HashCode.builder(SALT)
                .add(new byte[0])
                .build();
        Assert.assertEquals(new BigInteger(SALT), hashCode.toBigInteger());
    }

    @Test
    public void testEquals() throws Exception {
        forEach(SUBJECTS, SUBJECTS, (left, right) -> {
            final HashCode leftHashCode = HashCode.builder(SALT).add(left).build();
            final HashCode rightHashCode = HashCode.builder(SALT).add(right).build();
            final String message = String.format(
                    "[left(%s), right(%s), leftHashCode(%s), rightHashCode(%s)]",
                    toString(left), toString(right), leftHashCode, rightHashCode);
            // System.out.println(message);
            Assert.assertEquals(message, equals(left, right), leftHashCode.equals(rightHashCode));
        });
    }

    @FunctionalInterface
    private interface Tester {
        void test(byte[] left, byte[] right);
    }
}
