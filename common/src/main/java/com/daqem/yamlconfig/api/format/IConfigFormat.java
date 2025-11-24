package com.daqem.yamlconfig.api.format;

import com.daqem.yamlconfig.api.node.IMapNode;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface IConfigFormat {
    /**
     * Reads the file format into our generic node system.
     */
    IMapNode read(Reader reader) throws IOException;

    /**
     * Writes our generic node system to the file format.
     */
    void write(Writer writer, IMapNode rootNode) throws IOException;

    String getExtension();
}