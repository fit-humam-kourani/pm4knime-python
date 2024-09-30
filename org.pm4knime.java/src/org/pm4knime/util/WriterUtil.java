package org.pm4knime.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Functions;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.KNIMEException;
import org.knime.core.node.message.MessageBuilder;
import org.knime.core.util.FileUtil;
import org.knime.core.util.exception.HttpExceptionUtils;

/**
 * Helper class for working with location URLs (local paths, file: paths, and KNIME URLs) and writing to the destination
 * in PDF and HTML Writer nodes.
 *
 * @author Manuel Hotz, KNIME GmbH, Konstanz, Germany
 */
public final class WriterUtil {

    /**
     * Converts the given output URL to a valid URI.
     *
     * @param <X> exception converting the given location
     * @param url to convert
     * @param validUrl validates supported urls
     * @param uriExcHandler handler for exceptions converting to URI
     * @return valid URI
     * @throws X exception type thrown by handler
     */
    public static <X extends Throwable> URI toURI(final URL url, final Predicate<URL> validUrl,
            final BiFunction<Throwable, URL, X> uriExcHandler) throws X {
        // check that we accept the given URL
        try {
            final var valid = validUrl.test(url);
            if (!valid) {
                throw uriExcHandler.apply(new IllegalArgumentException("Unsupported output location specifier"),
                    url);
            }
            return url.toURI();
        } catch (final URISyntaxException | InvalidPathException e) {
            throw uriExcHandler.apply(e, url);
        }
    }

    /**
     * Converts the given output location to a URL.
     *
     * @param <X> exception converting the given location
     * @param location to convert
     * @param urlExcHandler handler for exceptions converting to URL
     * @return URL
     * @throws X exception type thrown by handler
     */
    public static <X extends Throwable> URL toURL(final String location,
            final BiFunction<MalformedURLException, String, X> urlExcHandler) throws X {
        try {
            return FileUtil.toURL(location);
        } catch (final MalformedURLException e) {
            throw urlExcHandler.apply(e, location);
        }
    }

    /**
     * Copies the file contents at the given path to the provided output location.
     *
     * @param file contents to copy from
     * @param outputLocation location to copy to
     * @throws IOException when reading or writing fails
     */
    public static void copyTo(final Path file, final URI outputLocation) throws IOException {
        writeTo(outputLocation, os -> {
            try (final var is = Files.newInputStream(file, StandardOpenOption.READ)) {
                FileUtil.copy(is, os);
            }
        });
    }

    private static void writeTo(final URI destination,
        final Functions.FailableConsumer<OutputStream, IOException> writer) throws IOException {

        final var destinationURL = destination.toURL();
        final var local = FileUtil.getFileFromURL(destinationURL);
        if (local != null) {
            try (final var os = Files.newOutputStream(local.toPath())) {
                writer.accept(os);
            }
            return;
        }

        final var conn = FileUtil.openOutputConnection(destinationURL, "PUT");
        try (final var os = conn.getOutputStream()) {
            writer.accept(os);
        }
        if (conn instanceof HttpURLConnection httpConn) {
            final var status = httpConn.getResponseCode();
            if (status < 200 || status >= 300) {
                try (final var err = httpConn.getErrorStream()) {
                    final var errorMessage = IOUtils.toString(err, StandardCharsets.UTF_8);
                    throw HttpExceptionUtils.wrapException(status, errorMessage);
                }
            }
        }
    }

    /**
     * Checks that the given URL is supported.
     *
     * @param url url to check
     * @return {@code true} if the given URL is supported, {@code false} otherwise
     */
    public static boolean checkLocalOrKNIMEURL(final URL url) {
        final var protocol = url.getProtocol();
        return protocol != null && ("file".equals(protocol) || "knime".equals(protocol));
    }

    /**
     * Handles the given {@link MalformedURLException} by throwing an {@link InvalidSettingsException}.
     * @param e exception to handle
     * @param invalidLocation invalid location specifier
     * @return exception
     */
    public static InvalidSettingsException handleInvalidPathSetting(final MalformedURLException e,
        final String invalidLocation) {
        return new InvalidSettingsException("The given output location is not valid: \"%s\"".formatted(invalidLocation)
            + "Only local files, \"file:\" URLs, and KNIME URLs are supported.", e);
    }

    /**
     * Handles the given {@link MalformedURLException} by throwing a {@link KNIMEException}.
     * @param e exception to handle
     * @param invalidLocation invalid location specifier
     * @param messageBuilder builder used to construct the exception message
     * @return exception
     */
    public static KNIMEException handleInvalidPath(final MalformedURLException e, final String invalidLocation,
        final Supplier<MessageBuilder> messageBuilder) {
        final var mb = messageBuilder.get()
            .withSummary("The given output location is not valid: \"%s\"".formatted(invalidLocation))
            .addResolutions("Supply a local path, a \"file:\" URL, or a valid KNIME URL.");
        return KNIMEException.of(mb.build().orElseThrow(), e);
    }

    /**
     * Handles an exception raised due to the invalid URL, by wrapping it in a {@link InvalidSettingsException}.
     *
     * @param e cause
     * @param invalidUrl invalid URL
     * @return exception
     */
    public static InvalidSettingsException handleInvalidURLSetting(final Throwable e, final URL invalidUrl) {
        final var protocol = invalidUrl.getProtocol();
        final var resolutions = new ArrayList<>();
        if (protocol == null || !"knime".equals(protocol)) {
            resolutions.add("Make sure the KNIME URL begins with \"knime:\".");
        }
        final var authority = invalidUrl.getAuthority();
        if (authority == null || authority.startsWith("knime.")) {
            resolutions.add("""
                    Check that your KNIME URL contains either a "knime.<xyz>" signifier such as "knime.workflow"
                    or a valid Mountpoint ID.
                    """);
        }
        final var sb =
            new StringBuilder("The given output location is not valid: \"%s\"".formatted(invalidUrl)).append("\n");
        for (final var r : resolutions) {
            sb.append(r);
        }
        return new InvalidSettingsException(sb.toString(), e);
    }

    /**
     * Handles an exception raised due to the invalid URL, by wrapping it in a {@link KNIMEException}.
     *
     * @param e cause
     * @param invalidUrl invalid URL
     * @param messageBuilder builder used to construct the exception message
     * @return exception
     */
    public static KNIMEException handleInvalidURL(final Throwable e, final URL invalidUrl,
        final Supplier<MessageBuilder> messageBuilder) {
        final var protocol = invalidUrl.getProtocol();
        final var resolutions = new ArrayList<>();
        if (protocol == null || !"knime".equals(protocol)) {
            resolutions.add("Make sure the KNIME URL begins with \"knime:\".");
        }
        final var authority = invalidUrl.getAuthority();
        if (authority == null || authority.startsWith("knime.")) {
            resolutions.add("""
                    Check that your KNIME URL contains either a "knime.<xyz>" signifier such as "knime.workflow"
                    or a valid Mountpoint ID.
                    """);
        }
        final var mb =
            messageBuilder.get().withSummary("The given output location is not valid: \"%s\"".formatted(invalidUrl))
                .addResolutions(resolutions.toArray(String[]::new));
        return KNIMEException.of(mb.build().orElseThrow(), e);
    }
}

