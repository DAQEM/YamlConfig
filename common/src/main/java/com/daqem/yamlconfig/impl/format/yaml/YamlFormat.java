package com.daqem.yamlconfig.impl.format.yaml;

import com.daqem.yamlconfig.api.config.ConfigExtension;
import com.daqem.yamlconfig.api.format.IConfigFormat;
import com.daqem.yamlconfig.api.node.IMapNode;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.StreamDataWriter;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.nodes.Node;

import java.io.Reader;
import java.io.Writer;

public class YamlFormat implements IConfigFormat {

    private final LoadSettings loadSettings = LoadSettings.builder().setParseComments(true).build();
    private final DumpSettings dumpSettings = DumpSettings.builder().setDumpComments(true).build();

    @Override
    public IMapNode read(Reader reader) {
        Compose compose = new Compose(loadSettings);
        Node root = compose.composeReader(reader).orElse(null);
        return YamlNodeConverter.toGeneric(root);
    }

    @Override
    public void write(Writer writer, IMapNode rootNode) {
        // Convert generic IMapNode -> SnakeYAML Node (Recursively)
        Node snakeRoot = YamlNodeConverter.fromGeneric(rootNode);

        // Wrap the generic Java Writer into SnakeYAML's specific StreamDataWriter
        StreamDataWriter streamWriter = new YamlWriterWrapper(writer);

        new Dump(dumpSettings).dumpNode(snakeRoot, streamWriter);
    }

    @Override
    public String getExtension() {
        return ConfigExtension.YAML.getExtension();
    }
}