package ru.toddler.model.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

public class JacksonConfigurator {

    public ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        BoolAsIntSerializer sbs = new BoolAsIntSerializer();
        BoolAsIntDeserializer sbds = new BoolAsIntDeserializer();
        module.addSerializer(Boolean.class, sbs);
        module.addSerializer(boolean.class, sbs);
        module.addDeserializer(Boolean.class, sbds);
        module.addDeserializer(boolean.class, sbds);
        mapper.registerModule(module);

        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));

        mapper.setVisibility(mapper.getDeserializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    private static class BoolAsIntSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Boolean> {
        @SuppressWarnings("DuplicateThrows") // implementing as is
        @Override
        public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeNumber(value == true ? BooleanToInt.TRUE : BooleanToInt.FALSE);
        }
    }

    private static class BoolAsIntDeserializer extends JsonDeserializer<Boolean> {
        @SuppressWarnings("DuplicateThrows") // implementing as is
        @Override
        public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.getCurrentToken().equals(JsonToken.VALUE_NUMBER_INT)) {
                return jp.getIntValue() == 1;
            } else {
                return jp.getBooleanValue();
            }
        }
    }
}
