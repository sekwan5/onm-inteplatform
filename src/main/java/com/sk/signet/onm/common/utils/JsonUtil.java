package com.sk.signet.onm.common.utils;

import java.io.IOException;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.signet.onm.common.exception.JsonException;

import static java.lang.String.format;


public final class JsonUtil {
    
    private static ObjectMapper MAPPER = createNewObjectMapper(true);

    private static ObjectMapper MAPPER_NON_EMPTY = createNewObjectMapper(false);

    private JsonUtil() {
    }

    public static ObjectMapper createNewObjectMapper(boolean includeType) {
        final ObjectMapper MAPPER = new ObjectMapper();
        if(includeType) {
            // MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        } else {
            // MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        // MAPPER.setVisibility(forMethod, visibility);
        // MAPPER.configure(forMethod, visibility);
        // MAPPER.configure(forMethod, visibility);
        // MAPPER.configure(forMethod, visibility);
        // MAPPER.configure(forMethod, visibility);
        MAPPER.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return MAPPER;
    }

    public static <T> T readValue(byte[] object, Class<T> clazz) {
        try {
            if (object == null) {
                return null;
            } else {
                return MAPPER.readValue(object, clazz);
            }
        } catch (IllegalArgumentException e) {
            throw new JsonException(format("Unable to read object '%s' from input '%s'", clazz.getSimpleName(), object), e);
        } catch (JsonParseException e) {
            throw new JsonException(format("Unable to read object '%s' from input '%s'", clazz.getSimpleName(), object), e);
        } catch (JsonMappingException e) {
            throw new JsonException(format("Unable to read object '%s' from input '%s'", clazz.getSimpleName(), object), e);
        } catch (IOException e) {
            throw new JsonException(format("Unable to read object '%s' from input '%s'", clazz.getSimpleName(), object), e);
        }
    }

    public static <T> T readJson(final String jsonString, final Class<T> clazz) {
        try{
            return MAPPER.readValue(jsonString, clazz);
        } catch(IOException e) {
            throw new JsonException(format("Unable to read JSON object '%s' from input '%s'", clazz.getSimpleName(), jsonString), e);
        }
    }

    public static <T> T readJson(final String jsonString, final TypeReference<T> typeReference) {
        try{
            return MAPPER.readValue(jsonString, typeReference);
        } catch(IOException e) {
            throw new JsonException(format("Unable to read JSON object '%s' from input '%s'", typeReference.getType(), jsonString), e);
        }
    }

    public static <T> T readJson(final byte[] jsonByte, final TypeReference<T> typeReference) {
        try{
            return MAPPER.readValue(new String(jsonByte, "UTF-8"), typeReference);
        } catch(IOException e) {
            throw new JsonException(format("Unable to read JSON object '%s' from input '%s'", typeReference.getType(), jsonByte), e);
        }
    }

    public static JsonNode readJson(final String jsonString) {
        try{
            return MAPPER.readTree(jsonString);
        } catch(IOException e) {
            throw new JsonException(format("Unable to read JSON node from input '%s'", jsonString), e);
        }
    }

    public static <T> String toJson(final T object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch(final JsonProcessingException e) {
            throw new JsonException(format("Unable to read JSON node from input '%s'", object.getClass().getSimpleName()), e);
        }
    }
}
