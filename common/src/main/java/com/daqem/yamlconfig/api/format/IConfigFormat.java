package com.daqem.yamlconfig.api.format;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.daqem.yamlconfig.api.node.IMapNode;

/**
 * Represents a configuration file format (e.g., YAML, JSON, TOML).
 */
public interface IConfigFormat {
    /**
     * Reads the file format into our generic node system.
     */
    /**
     * Reads the file format into our generic node system.
     *
     * @param reader The reader to read from.
     * @return The root {@link IMapNode}.
     * @throws IOException If an I/O error occurs.
     */
    IMapNode read(Reader reader) throws IOException;

    /**
     * Writes our generic node system to the file format.
     */
    /**
     * Writes our generic node system to the file format.
     *
     * @param writer   The writer to write to.
     * @param rootNode The root {@link IMapNode} to write.
     * @throws IOException If an I/O error occurs.
     */
    void write(Writer writer, IMapNode rootNode) throws IOException;

    /**
     * Gets the file extension associated with this format.
     *
     * @return The file extension (e.g., ".yaml").
     */
    String getExtension();
}