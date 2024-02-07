package ru.wwerlosh.configs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.BitSet;

public class BitSetSerializer extends JsonSerializer<BitSet> {

    @Override
    public void serialize(BitSet bitSet, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartArray();
        for (long l : bitSet.toLongArray())
        {
            gen.writeNumber(l);
        }
        gen.writeEndArray();
    }

    @Override
    public Class<BitSet> handledType() {
        return BitSet.class;
    }
}
