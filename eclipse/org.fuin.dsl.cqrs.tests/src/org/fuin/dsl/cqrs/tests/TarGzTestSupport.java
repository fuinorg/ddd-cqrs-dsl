package org.fuin.dsl.cqrs.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * Builds a tiny ustar <code>.tar.gz</code> archive in memory for the remote-scope maven tests, so
 * the tests do not depend on Apache Commons Compress or an external <code>tar</code> binary.
 */
public final class TarGzTestSupport {

    private static final int BLOCK = 512;

    private TarGzTestSupport() {
    }

    /** Returns a gzipped tar containing each {@code entries} key as a top-level regular file. */
    public static byte[] tarGz(Map<String, String> entries) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gz = new GZIPOutputStream(out)) {
            for (Map.Entry<String, String> e : entries.entrySet()) {
                byte[] content = e.getValue().getBytes(StandardCharsets.UTF_8);
                gz.write(header(e.getKey(), content.length));
                gz.write(content);
                gz.write(new byte[(BLOCK - (content.length % BLOCK)) % BLOCK]);
            }
            gz.write(new byte[2 * BLOCK]); // two zero blocks mark the end of the archive
        }
        return out.toByteArray();
    }

    private static byte[] header(String name, int size) {
        byte[] h = new byte[BLOCK];
        putAscii(h, 0, name);
        putAscii(h, 100, "0000644"); // mode
        putAscii(h, 108, "0000000"); // uid
        putAscii(h, 116, "0000000"); // gid
        putAscii(h, 124, String.format("%011o", size)); // size (octal)
        putAscii(h, 136, "00000000000"); // mtime
        h[156] = '0'; // typeflag: regular file
        putAscii(h, 257, "ustar"); // magic
        // Checksum is computed with the 8 checksum bytes treated as spaces.
        for (int i = 148; i < 156; i++) {
            h[i] = ' ';
        }
        int sum = 0;
        for (byte b : h) {
            sum += b & 0xFF;
        }
        putAscii(h, 148, String.format("%06o", sum));
        h[154] = 0;
        h[155] = ' ';
        return h;
    }

    private static void putAscii(byte[] buf, int offset, String value) {
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(bytes, 0, buf, offset, bytes.length);
    }
}
