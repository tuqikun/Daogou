package com.tutu.daogou.util;

import okhttp3.*;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * name: zhangyan
 * date:$[DATE]
 */
public class HttpClient {

    private static final Logger LOGGER = Logger.getLogger( HttpClient.class.getName() );

    // socket 时限
    public static Integer SOCKET_TIMEOUT = 30_000;
    // connect 时限
    public static Integer CONNECT_TIMEOUT = 3_000;
    // read 时限
    public static Integer READ_TIMEOUT = 3_000;
    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout( CONNECT_TIMEOUT, TimeUnit.MILLISECONDS ).
            writeTimeout( SOCKET_TIMEOUT, TimeUnit.MILLISECONDS ).readTimeout( READ_TIMEOUT, TimeUnit.MILLISECONDS ).build();

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse( "application/json; charset=utf-8" );//
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse( "text/x-markdown; charset=utf-8" );
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse( "image/png" );//
    public static final MediaType MEDIA_TYPE_FORM = MediaType.parse( "application/x-www-form-urlencoded; charset=utf-8" );//
    public static final MediaType MEDIA_TYPE_FORM_DATA = MediaType.parse( "multipart/form-data; charset=utf-8" );//
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse( "text/plan; charset=utf-8" );//

    static {
        LOGGER.info( "HTTP CLIENT" );
    }

    private static String concatUrlParams( String url, Map<String, String> params ) {
        if ( params == null || params.isEmpty() ) {
            return url;
        }
        Iterator<String> iterator = params.keySet().iterator();
        StringBuilder doGetSbf = new StringBuilder();

        String firstKey = iterator.next();
        doGetSbf.append( url ).append( "?" ).append( firstKey ).append( "=" ).append( params.get( firstKey ) );
        while ( iterator.hasNext() ) {
            String key = iterator.next();
            doGetSbf.append( "&" ).append( key ).append( "=" ).append( params.get( key ) );
        }
        url = doGetSbf.toString();
        return url;
    }

    private static void addHeaders( Request.Builder builder, Map<String, String> headers ) {
        if ( headers == null ) {
            return;
        }
        for ( Map.Entry<String, String> entry : headers.entrySet()) {
            builder.header( entry.getKey(), entry.getValue() );
        }
    }

    // get request
    public static String doGet( String url, Map<String, String> params, Map<String, String> headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( concatUrlParams( url, params ) );
        addHeaders( builder, headers );
        Request request = builder.build();
        Response response = client.newCall( request ).execute();
        if ( response.isSuccessful() ) {
            return response.body().string();
        }
        return null;
    }

    private static final Callback ASYNC_GET_CALLBACK = new Callback() {
        @Override
        public void onFailure( Request request, IOException e ) {
            LOGGER.error( "doAsyncGet error:".concat( request.url().toString() ), e );
        }
        @Override
        public void onResponse( Response response ) throws IOException {
            if ( response.isSuccessful() ) {
            } else {
                LOGGER.error( "doAsyncGet error:".concat( response.request().url().toString() ) );
            }
        }
    };

    public static void doGetAsync( String url, Map<String, String> params, Callback callback, Map<String, String> headers ) {
        Request.Builder builder = new Request.Builder().url( concatUrlParams( url, params ) );
        addHeaders( builder, headers );
        Request request = builder.build();
        if ( callback == null ) {
            callback = ASYNC_GET_CALLBACK;
        }
        client.newCall( request ).enqueue( callback );
    }

    public static String doPost( String url, Map<String, String> params, Map<String, String> headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( url );
        addHeaders( builder, headers );
        FormBody.Builder form_builder = new FormBody.Builder();
        for ( Map.Entry<String, String> entry : params.entrySet() ) {
            form_builder.add( entry.getKey(), entry.getValue() );
        }
        Request request = builder.post( form_builder.build() ).build();
        Response response = client.newCall( request ).execute();
        if ( response.isSuccessful() ) {
            return response.body().string();
        }
        return null;
    }

    public static void doPostAsync( String url, Map<String, String> params, Callback callback, Map<String, String> headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( url );
        addHeaders( builder, headers );
        FormBody.Builder formBuilder = new FormBody.Builder();
        for ( Map.Entry<String, String> entry : params.entrySet() ) {
            formBuilder.add( entry.getKey(), entry.getValue() );
        }
        Request request = builder.post( formBuilder.build() ).build();
        if ( callback == null ) {
            callback = ASYNC_POST_CALLBACK;
        }
        client.newCall( request ).enqueue( callback );
    }

    public static String doPostJson( String url, Map<String, String> params, Map<String, String> headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( url );
        addHeaders( builder, headers );
        Request request = builder.post( RequestBody.create( MEDIA_TYPE_JSON, JacksonTools.toJson( params ) ) ).build();
        Response response = client.newCall( request ).execute();
        if ( response.isSuccessful() ) {
            return response.body().string();
        }
        return null;
    }

    public static void doPostJsonAsync( String url, Map<String, String> params, Callback callback, Map<String, String> headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( url );
        addHeaders( builder, headers );
        Request request = builder.post( RequestBody.create( MEDIA_TYPE_JSON, JacksonTools.toJson( params ) ) ).build();
        if ( callback == null ) {
            callback = ASYNC_POST_CALLBACK;
        }
        client.newCall( request ).enqueue( callback );
    }

    public static String doPostFormData( String url, Map<String, String> params, Map<String, String> headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( url );
        addHeaders( builder, headers );
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType( MultipartBody.FORM );
        if ( params.isEmpty() ) {
            multipartBuilder.addFormDataPart( "", "" );
        }
        for ( String key : params.keySet() ) {
            multipartBuilder.addFormDataPart( key, params.get( key ) );
        }
        RequestBody requestBody = multipartBuilder.build();

        Request request = builder.post( requestBody ).build();
        Response response = client.newCall( request ).execute();
        if ( response.isSuccessful() ) {
            return response.body().string();
        }
        return null;
    }

    public static String doPostFormData(String url, Map<String, Object> params, File file, Map<String, String> headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( url );
        addHeaders( builder, headers );
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType( MultipartBody.FORM );
        if ( params.isEmpty() ) {
            multipartBuilder.addFormDataPart( "", "" );
        }
        for ( String key : params.keySet() ) {
            if ( ! ( params.get( key ) instanceof File ) ) {
                multipartBuilder.addFormDataPart( key, params.get( key ).toString() );
            } else {
                multipartBuilder.addFormDataPart( key, file.getName(), RequestBody.create( null, file ) );
            }
        }
        RequestBody requestBody = multipartBuilder.build();
        Request request = builder.post( requestBody ).build();
        LOGGER.info( "【HttpRequest.do_post_form_data.request】" + request.toString() );
        Response response = client.newCall( request ).execute();
        LOGGER.info( " 【HttpRequest.do_post_form_data.response】" + response.toString() );
        if ( response.isSuccessful() ) {
            return response.body().string();
        }
        return null;
    }

    public static void doPostFormDataAsync( String url, Map< String, String > params, Callback callback, Map< String, String > headers ) throws IOException {
        Request.Builder builder = new Request.Builder().url( url );
        addHeaders( builder, headers );
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType( MultipartBody.FORM );
        if ( params.isEmpty() ) {
            multipartBuilder.addFormDataPart( "", "" );
        }
        for ( String key : params.keySet() ) {
            multipartBuilder.addFormDataPart( key, params.get( key ) );
        }
        RequestBody requestBody = multipartBuilder.build();

        Request request = builder.post( requestBody ).build();
        if ( callback == null ) {
            callback = ASYNC_POST_CALLBACK;
        }
        client.newCall( request ).enqueue( callback );
    }


    private static final Callback ASYNC_POST_CALLBACK = new Callback() {
        @Override
        public void onFailure( Request request, IOException e ) {
            LOGGER.error( "doAsyncPostBytype error:".concat( request.url().toString() ), e );
        }

        @Override
        public void onResponse( Response response ) throws IOException {
            if ( response.isSuccessful() ) {
            } else {
                LOGGER.error( "doAsyncPostBytype error:".concat( response.request().url().toString() ) );
            }
        }
    };
}
