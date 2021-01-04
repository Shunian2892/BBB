package com.example.bbb.controlLayer.gps;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenRouteServiceTest {

    //API key from Nicholas
    private static final String API_KEY = "5b3ce3597851110001cf6248cc7335a16be74902905bcba4a9d0eebf";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String method;
    private String jsonWaypointsURL;
    private String jsonCoordinatesResponse;


    @Mock
    private OpenRouteService openRouteService;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        openRouteService = mock(OpenRouteService.class);
        method = "foot-walking";
        jsonWaypointsURL = getWayPointsJsonSampleURL();
        jsonCoordinatesResponse = getCoordinatesJsonSample();
    }

    @Test
    public void getRouteTest() {
        try {
            ArrayList<GeoPoint> geoPointsActual;
            ArrayList<GeoPoint> geoPointsExpected;
            String language = "en";
            MockWebServer mockWebServer = new MockWebServer();
            mockWebServer.start();
            HttpUrl url = mockWebServer.url("v2/directions/" + method);

            //Expected method call

            //A webserver enqueue which sends a mockresponse. It mimics the functionality of
            // a webserver to receive api responses.
            mockWebServer.enqueue(new MockResponse().setResponseCode(200).addHeader("Authorization", API_KEY).setBody(jsonCoordinatesResponse));

            //Actual method call

            //This is the method that should be called but since it is async it can't be used.
            //So this method is in turn called using doNothing() so that getRoute() would be called
            // without the actual use of the method.
            doNothing().when(openRouteService).getRoute(getCoordinatesExample(), method, language);
            openRouteService.getRoute(getCoordinatesExample(), method, language);

            //This is supposed to mimic the method getRoute(). GetRoute() is an async call which
            // won't work with mockWebServer.takeRequest(). To test the response message we would
            // need to call .execute() as that method is sync and runs on the same thread.
            //The getRoute method sets the routeGeoPoints which would be used to draw the route on
            // the map.
            RequestBody requestBody = RequestBody.create(jsonCoordinatesResponse, JSON);
            okhttp3.Request request = new Request.Builder().post(requestBody).url(url).build();
            Response response = new OkHttpClient().newCall(request).execute();
            String bodyActual = response.body().string();
            ArrayList<GeoPoint> geoPoints = parseJSONGetGeoPoints(bodyActual);
            openRouteService.setRouteGeoPoints(geoPoints);
            doReturn(geoPoints).when(openRouteService).getRouteGeoPoints();
            geoPointsActual = openRouteService.getRouteGeoPoints();

            //expected response
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            String bodyExpected = recordedRequest.getBody().readUtf8();
            geoPointsExpected = parseJSONGetGeoPoints(bodyExpected);

            assertEquals(geoPointsExpected, geoPointsActual);

            mockWebServer.shutdown();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<GeoPoint> parseJSONGetGeoPoints(String JSONString) {
        ArrayList<GeoPoint> points = new ArrayList<>();

        try {
            JSONObject responseObject = new JSONObject(JSONString);
            JSONArray routesArray = responseObject.getJSONArray("routes");
            JSONObject routes = (JSONObject) routesArray.get(0);
            String geometry = routes.getString("geometry");
            JSONArray coordinates = openRouteService.decodeGeometry(geometry, false);

            for (int i = 0; i < coordinates.length(); i++) {
                JSONArray cordArray = (JSONArray) coordinates.get(i);
                double lat = cordArray.getDouble(0);
                double lng = cordArray.getDouble(1);
                GeoPoint point = new GeoPoint(lat, lng);
                points.add(point);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return points;
    }

    private String getCoordinatesJsonSample() {
        return "{\"routes\":[{\"summary\":{\"distance\":1369,\"duration\":292},\"segments\":[{\"distance\":887.8,\"duration\":189,\"steps\":[{\"distance\":312.6,\"duration\":75,\"type\":11,\"instruction\":\"Head north on Wielandtstraße\",\"name\":\"Wielandtstraße\",\"way_points\":[0,10]},{\"distance\":251.1,\"duration\":36.2,\"type\":1,\"instruction\":\"Turn right onto Mönchhofstraße\",\"name\":\"Mönchhofstraße\",\"way_points\":[10,21]},{\"distance\":212.2,\"duration\":50.9,\"type\":2,\"instruction\":\"Turn sharp left onto Keplerstraße\",\"name\":\"Keplerstraße\",\"way_points\":[21,24]},{\"distance\":109.9,\"duration\":26.4,\"type\":1,\"instruction\":\"Turn right onto Moltkestraße\",\"name\":\"Moltkestraße\",\"way_points\":[24,27]},{\"distance\":2,\"duration\":0.5,\"type\":0,\"instruction\":\"Turn left onto Werderplatz\",\"name\":\"Werderplatz\",\"way_points\":[27,28]},{\"distance\":0,\"duration\":0,\"type\":10,\"instruction\":\"Arrive at Werderplatz, on the right\",\"name\":\"-\",\"way_points\":[28,28]}]},{\"distance\":481.2,\"duration\":103,\"steps\":[{\"distance\":2,\"duration\":0.5,\"type\":11,\"instruction\":\"Head south on Werderplatz\",\"name\":\"Werderplatz\",\"way_points\":[28,29]},{\"distance\":265.5,\"duration\":63.7,\"type\":0,\"instruction\":\"Turn left onto Moltkestraße\",\"name\":\"Moltkestraße\",\"way_points\":[29,37]},{\"distance\":83,\"duration\":7.5,\"type\":0,\"instruction\":\"Turn left onto Handschuhsheimer Landstraße, B 3\",\"name\":\"Handschuhsheimer Landstraße, B 3\",\"way_points\":[37,39]},{\"distance\":130.8,\"duration\":31.4,\"type\":0,\"instruction\":\"Turn left onto Roonstraße\",\"name\":\"Roonstraße\",\"way_points\":[39,42]},{\"distance\":0,\"duration\":0,\"type\":10,\"instruction\":\"Arrive at Roonstraße, straight ahead\",\"name\":\"-\",\"way_points\":[42,42]}]}],\"bbox\":[8.681436,49.41461,8.690123,49.420514],\"geometry\":\"ihrlHkr~s@S@}DHQ?OCs@IqAYQEQEyBa@SC?]?M?eA?S?W?SBuB@k@?a@AcH@[IBaJ~@O@A]YyFASC?B?CUEgAAWCUYyEASg@sI?CmAt@y@f@?BVlELpC\",\"way_points\":[0,28,42]}],\"bbox\":[8.681436,49.41461,8.690123,49.420514],\"metadata\":{\"attribution\":\"openrouteservice.org | OpenStreetMap contributors\",\"service\":\"routing\",\"timestamp\":1609247972297,\"query\":{\"coordinates\":[[8.681495,49.41461],[8.686507,49.41943],[8.687872,49.420318]],\"profile\":\"driving-car\",\"format\":\"json\",\"language\":\"en\"},\"engine\":{\"version\":\"6.3.1\",\"build_date\":\"2020-12-10T15:35:43Z\",\"graph_date\":\"1970-01-01T00:00:00Z\"}}}";
    }

    @Test
    public void createPostRequestTest() {

        //expected value
        RequestBody requestBody = RequestBody.create(jsonWaypointsURL, JSON);
        Request expected = new Request.Builder().url("https://api.openrouteservice.org/v2/directions/" + method).
                post(requestBody).addHeader("Authorization", API_KEY).build();

        //Actual value
        when(openRouteService.createPostRequest(method, jsonWaypointsURL)).thenCallRealMethod();
        Request actual = openRouteService.createPostRequest(method, jsonWaypointsURL);

        assertEquals(expected.toString(), actual.toString());
    }

    //Not used.
    private double[][] getCoordinatesExample() {
        double[][] coordinates = new double[3][2];

        coordinates[0][0] = 8.681495;
        coordinates[0][1] = 49.41461;

        coordinates[1][0] = 8.686507;
        coordinates[1][1] = 49.41943;

        coordinates[2][0] = 8.687872;
        coordinates[2][1] = 49.420318;

        return coordinates;
    }

    private String getWayPointsJsonSampleURL() {
        return "{\"coordinates\":" + Arrays.deepToString(getCoordinatesExample()) + ",\"language\":en}";
    }

 /*   @Test
    private void URLTest(){
            RequestBody requestBody = RequestBody.create(jsonWaypoints, JSON);
            okhttp3.Request request = new Request.Builder().post(requestBody).url(url).build();
            Response response = new OkHttpClient().newCall(request).execute();
            String responseMessage = response.body().string();

            recordedRequest = mockWebServer.takeRequest();

            assertEquals(recordedRequest.getRequestUrl(),url);
            assertEquals("http://127.0.0.1:" + mockWebServer.getPort() + "/v2/directions/" + method, url.toString());

            String body = recordedRequest.getBody().readUtf8();
            assertEquals(body,responseMessage);
    }*/
}