package com.tutu.daogou.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;

import java.io.IOException;

/**
 * Iceman
 * 2017/11/10
 */
public class JacksonTools {

    private static final Logger LOGGER = Logger.getLogger( JacksonTools.class.getName() );
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setDateFormat( new java.text.SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) );

        objectMapper.configure( JsonParser.Feature.ALLOW_SINGLE_QUOTES, true );

        objectMapper.configure( JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );

        objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    }

    private JacksonTools() {
    }

    public static String toJson( Object object ) {
        try {
            return objectMapper.writeValueAsString( object );
        } catch ( JsonProcessingException e ) {
            LOGGER.error( "json", e );
        }
        return "";
    }

    public static < T > T toObject( String json, Class< T > valueType ) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue( json, valueType );
    }

    @SuppressWarnings( "unchecked" )
    public static < T > T toObject( String json, TypeReference< T > typeReference ) throws JsonParseException, JsonMappingException, IOException {
        return ( T ) objectMapper.readValue( json, typeReference );
    }

}
