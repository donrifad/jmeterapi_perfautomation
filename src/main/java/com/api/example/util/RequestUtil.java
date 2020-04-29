package com.api.example.util;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

/**
 * QE-CORE CONTAINS API RELATED FUNCTIONS
 * Refer  com.api.example.core.api
 */
public class RequestUtil {

    RequestUtil() {

    }

    public static String API_HOST;
    public static String BASE_PATH;
    public static int PORT = 0;

    public static Response send(Map<String, String> headers, String bodyString, String uri, String requestMethod, Map<String, String> queryParameters) {
        RestAssured.baseURI = API_HOST;
        RestAssured.basePath = BASE_PATH;
        if (PORT != 0) {
            RestAssured.port = PORT;
        }

        System.out.println("\n\nHEADERS\n" + headers + "\n*********\n\n");
        System.out.println("\n\nREQUEST_URL\n" + RestAssured.baseURI + RestAssured.basePath + "/" + uri + "\n*********\n\n");

        RequestSpecification requestSpecification = getRequestSpec(headers, bodyString);

        System.out.println("\n\nREQUEST_BODY\n" + bodyString + "\n*********\n\n");
        RestAssured.useRelaxedHTTPSValidation();
        requestSpecification = given().spec(requestSpecification);
        String theUri = setQueryParameters(uri, queryParameters);

        Response response = execute(requestMethod, requestSpecification, theUri);

        System.out.println("\n\nRESPONSE\n" + response.getBody().asString() + "\n*********\n\n");
        System.out.println("\n\nRESPONSE_STATUS_CODE\n" + response.getStatusCode() + "\n*********\n\n");

        return response;
    }

    public static Response send(Map<String, String> headers, String bodyString, String uri, String requestMethod) {
        return send(headers, bodyString, uri, requestMethod, null);
    }


    public static Response execute(String reqMethod, RequestSpecification requestSpec, String uri) {
        RequestSpecification requestSpecification = requestSpec;
        requestSpecification = given(requestSpecification).config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));

        Response response = null;
        if ("GET".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().get(uri, new Object[0]);
        }
        if ("POST".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().post(uri, new Object[0]);
        }
        if ("PUT".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().put(uri, new Object[0]);
        }
        if ("DELETE".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().delete(uri, new Object[0]);
        }
        if ("PATCH".equalsIgnoreCase(reqMethod)) {
            response = requestSpecification.expect().when().patch(uri, new Object[0]);
        }
        if (response != null)
            return response;
        else
            return null;
    }


    public static RequestSpecification getRequestSpec(Map<String, String> headers, String body) {
        RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder();
        if (headers != null)
            reqSpecBuilder.addHeaders(headers);
        if (body != null && body.length() > 0) {
            reqSpecBuilder.setBody(body);
        }
        return reqSpecBuilder.build();
    }

    public static String send(Map<String, String> headers, String body, String baseUri, String basePath, String uri, String requestMethod) {
        /*
         * will return the send response as string
         * */
        if (PORT != 0) {
            RestAssured.port = PORT;
        }
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
        System.out.println("\n\nHEADERS\n" + headers + "\n*********\n\n");
        System.out.println("\n\nREQUEST_URL\n" + RestAssured.baseURI + RestAssured.basePath + "/" + uri + "\n*********\n\n");
        RequestSpecification requestSpecification = getRequestSpec(headers, body);
        requestSpecification = given().spec(requestSpecification);
        System.out.print("\n\nREQUEST_BODY\n" + body + "\n*********\n\n");
        String response = executeAndGetResponseAsString(requestMethod, requestSpecification, uri);
        System.out.print("\n\nRESPONSE_BODY\n" + response + "\n*********\n\n");
        return response;
    }

    public static String executeAndGetResponseAsString(String reqMethod, RequestSpecification requestSpecification, String uri) {
        /*
         * will return the response as a string
         * */
        requestSpecification = given(requestSpecification).config(RestAssured.config().encoderConfig(encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        Response response = null;
        if (reqMethod.equalsIgnoreCase("GET")) {
            response = requestSpecification.expect().when().get("/" + uri);
        }
        if (reqMethod.equalsIgnoreCase("POST")) {
            response = requestSpecification.expect().when().post("/" + uri);
        }
        if (reqMethod.equalsIgnoreCase("PUT")) {
            response = requestSpecification.expect().when().put("/" + uri);
        }
        if (reqMethod.equalsIgnoreCase("DELETE")) {
            response = requestSpecification.expect().when().delete("/" + uri);
        }
        return response.asString();
    }

    public static String setQueryParameters(String url, Map<String, String> queryParameters) {
        if (queryParameters == null || queryParameters.isEmpty())
            return url;
        String newUrl = url.concat("?");
        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            newUrl = newUrl.concat(key).concat("=").concat(value).concat("&");
        }
        return newUrl.substring(0, newUrl.length() - 1);
    }

    public static int getResponseCode(Response response) {
        return response.getStatusCode();
    }


}
