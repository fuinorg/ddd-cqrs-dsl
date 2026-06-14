package org.fuin.dsl.cqrs.intellij.remote;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

/**
 * Minimal extractor for gzipped tar archives. It writes every regular-file entry whose name ends in
 * <code>.cqrs</code> into a target directory, flattened to the entry's base name. Only the small
 * subset of the (ustar) format needed for that is implemented; everything else is skipped.
 *
 * <p>Uses only the JDK ({@link GZIPInputStream} plus a hand-rolled 512-byte block reader), so no
 * Apache Commons Compress dependency is required and the same source can be shared verbatim by the
 * Maven and Eclipse projects.</p>
 */
public final class TarGz {

    private static final int BLOCK = 512;

    private TarGz() {
    }

    /**
     * Extracts all <code>*.cqrs</code> entries from the gzipped tar stream {@code in} into
     * {@code targetDir} (creating it if necessary). The stream is fully consumed and closed.
     */
    public static void extractCqrsFiles(InputStream in, File targetDir) throws IOException {
        Files.createDirectories(targetDir.toPath());
        try (GZIPInputStream gz = new GZIPInputStream(in)) {
            byte[] header = new byte[BLOCK];
            while (readFully(gz, header)) {
                if (isAllZero(header)) {
                    return; // end-of-archive marker (two zero blocks, first is enough to stop)
                }
                String name = cString(header, 0, 100);
                long size = parseOctal(header, 124, 12);
                byte typeFlag = header[156];
                boolean regular = typeFlag == 0 || typeFlag == '0';
                String baseName = baseName(name);
                if (regular && baseName.endsWith(".cqrs")) {
                    writeEntry(gz, new File(targetDir, baseName), size);
                } else {
                    skipFully(gz, size);
                }
                skipFully(gz, padding(size));
            }
        }
    }

    private static void writeEntry(InputStream in, File target, long size) throws IOException {
        try (OutputStream out = Files.newOutputStream(target.toPath())) {
            byte[] buf = new byte[8192];
            long remaining = size;
            while (remaining > 0) {
                int read = in.read(buf, 0, (int) Math.min(buf.length, remaining));
                if (read < 0) {
                    throw new IOException("Unexpected end of tar entry '" + target + "'");
                }
                out.write(buf, 0, read);
                remaining -= read;
            }
        }
    }

    /** Fills {@code buf} completely; returns {@code false} on a clean EOF before any byte was read. */
    private static boolean readFully(InputStream in, byte[] buf) throws IOException {
        int offset = 0;
        while (offset < buf.length) {
            int read = in.read(buf, offset, buf.length - offset);
            if (read < 0) {
                if (offset == 0) {
                    return false;
                }
                throw new IOException("Truncated tar header");
            }
            offset += read;
        }
        return true;
    }

    private static void skipFully(InputStream in, long count) throws IOException {
        byte[] buf = new byte[8192];
        long remaining = count;
        while (remaining > 0) {
            int read = in.read(buf, 0, (int) Math.min(buf.length, remaining));
            if (read < 0) {
                return;
            }
            remaining -= read;
        }
    }

    private static long padding(long size) {
        int rem = (int) (size % BLOCK);
        return rem == 0 ? 0 : BLOCK - rem;
    }

    private static boolean isAllZero(byte[] block) {
        for (byte b : block) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    private static String cString(byte[] bytes, int offset, int len) {
        int end = offset;
        int limit = offset + len;
        while (end < limit && bytes[end] != 0) {
            end++;
        }
        return new String(bytes, offset, end - offset, StandardCharsets.UTF_8);
    }

    private static long parseOctal(byte[] bytes, int offset, int len) {
        long value = 0;
        for (int i = offset; i < offset + len; i++) {
            byte b = bytes[i];
            if (b >= '0' && b <= '7') {
                value = value * 8 + (b - '0');
            } else if (value != 0) {
                break; // trailing space/NUL after the digits
            }
        }
        return value;
    }

    private static String baseName(String name) {
        int slash = name.lastIndexOf('/');
        return slash < 0 ? name : name.substring(slash + 1);
    }
}
