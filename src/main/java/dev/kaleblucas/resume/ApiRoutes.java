package dev.kaleblucas.resume;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * All REST API endpoints.
 *
 * POST /api/format/json   — pretty-print or minify raw JSON
 * POST /api/format/xml    — pretty-print or minify raw XML
 *
 * Both endpoints accept:   { "input": "<raw string>", "indent": 2, "minify": false }
 * Both endpoints return:   { "output": "<formatted string>" }
 *               on error:  { "error": "<message>" }  (HTTP 400)
 */
public class ApiRoutes {

    private static final Logger log = LoggerFactory.getLogger(ApiRoutes.class);

    public static void register(Javalin app) {
        app.post("/api/format/json", ApiRoutes::formatJson);
        app.post("/api/format/xml",  ApiRoutes::formatXml);
    }

    // ── JSON ─────────────────────────────────────────────────────────────────

    private static void formatJson(Context ctx) {
        try {
            FormatRequest req = ctx.bodyAsClass(FormatRequest.class);
            String raw = req.input == null ? "" : req.input.strip();

            // Parse with Jackson (bundled transitively via Javalin) to validate
            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Object parsed = mapper.readValue(raw, Object.class);

            String result;
            if (Boolean.TRUE.equals(req.minify)) {
                result = mapper.writeValueAsString(parsed);
            } else {
                int spaces = (req.indent != null && req.indent > 0) ? req.indent : 2;
                result = mapper.writer()
                               .withDefaultPrettyPrinter()
                               .withFeatures(com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN)
                               .writeValueAsString(parsed);
                // Jackson's pretty-printer always uses 2 spaces; re-indent if needed
                if (spaces != 2) {
                    result = reindentJson(result, spaces);
                }
            }

            ctx.json(Map.of("output", result));

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.debug("JSON format failed: {}", e.getOriginalMessage());
            ctx.status(400).json(Map.of("error", e.getOriginalMessage()));
        } catch (Exception e) {
            log.warn("Unexpected JSON format error", e);
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    /** Replace leading 2-space indentation blocks with `spaces`-space blocks. */
    private static String reindentJson(String src, int spaces) {
        String pad = " ".repeat(spaces);
        StringBuilder sb = new StringBuilder();
        for (String line : src.split("\n", -1)) {
            int leading = 0;
            while (leading < line.length() && line.charAt(leading) == ' ') leading++;
            int depth = leading / 2;
            sb.append(pad.repeat(depth)).append(line.stripLeading()).append('\n');
        }
        return sb.toString().stripTrailing();
    }

    // ── XML ──────────────────────────────────────────────────────────────────

    private static void formatXml(Context ctx) {
        try {
            FormatRequest req = ctx.bodyAsClass(FormatRequest.class);
            String raw = req.input == null ? "" : req.input.strip();

            var factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            // Disable external entity resolution (XXE protection)
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            var builder = factory.newDocumentBuilder();
            var doc = builder.parse(
                new ByteArrayInputStream(raw.getBytes(StandardCharsets.UTF_8))
            );
            doc.normalizeDocument();

            var transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            if (Boolean.TRUE.equals(req.minify)) {
                transformer.setOutputProperty(OutputKeys.INDENT, "no");
            } else {
                int spaces = (req.indent != null && req.indent > 0) ? req.indent : 2;
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount",
                    String.valueOf(spaces)
                );
            }

            var writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            ctx.json(Map.of("output", writer.toString().strip()));

        } catch (org.xml.sax.SAXParseException e) {
            String msg = "Line " + e.getLineNumber() + ", col " + e.getColumnNumber()
                       + ": " + e.getMessage();
            log.debug("XML format failed: {}", msg);
            ctx.status(400).json(Map.of("error", msg));
        } catch (Exception e) {
            log.warn("Unexpected XML format error", e);
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    // ── Request DTO ──────────────────────────────────────────────────────────

    public static class FormatRequest {
        public String  input;
        public Integer indent;
        public Boolean minify;
    }
}
