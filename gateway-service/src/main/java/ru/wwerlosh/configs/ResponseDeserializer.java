package ru.wwerlosh.configs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wwerlosh.controllers.dto.ErrorResponse;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlResponse;

public class ResponseDeserializer extends StdDeserializer<Response> {

    private static final Logger log = LoggerFactory.getLogger(ResponseDeserializer.class);

    public ResponseDeserializer() {
        this(null);
    }

    public ResponseDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Response deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        final ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        final JsonNode request = mapper.readTree(jsonParser);

        final JsonNode errorField = request.get("error");
        final JsonNode shortUrlField = request.get("shortUrl");
        System.out.println(request);
        if (errorField != null) {
            return mapper.treeToValue(request, ErrorResponse.class);
        }

        if (shortUrlField != null) {
            return mapper.treeToValue(request, UrlResponse.class);
        }

        log.warn("Unexpected request type : {}", request);
        throw new IllegalStateException("Unexpected request type : " + request);

    }
}
