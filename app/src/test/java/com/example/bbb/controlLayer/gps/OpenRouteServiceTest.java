package com.example.bbb.controlLayer.gps;

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

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenRouteServiceTest {

    private MockWebServer mockWebServer;
    private String api_key;
    private String method;
    private String jsonWaypoints;
    private String jsonCoordinates;

    @Mock
    private OpenRouteService openRouteService;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockWebServer = new MockWebServer();
        //API key from Nicholas
        api_key = "5b3ce3597851110001cf6248cc7335a16be74902905bcba4a9d0eebf";
        method = "foot-walking";
        jsonWaypoints = getWayPointsJsonSample();
        openRouteService = mock(OpenRouteService.class);
//        jsonCoordinates = getCoordinatesJsonSample();
    }

    //TODO - Not sure how to test void method. Maybe change method in class we are testing.
    //       Maybe split the method so that getRoute only gets/decodes the coordinates
    //       and another method uses those coordinates in to draw the routes on the map.
/*    @Test
    public void getRouteTest() {
        ArrayList<GeoPoint> geoPointsActual = new ArrayList<>();
        ArrayList<GeoPoint> geoPointsExpected = new ArrayList<>();

        String language = "en";

        try {

            mockWebServer.url("https://api.openrouteservice.org/v2/directions/" + method);
            mockWebServer.enqueue(new MockResponse().setBody(jsonWaypoints).addHeader("Authorization", api_key).setResponseCode(200));
            mockWebServer.start();
            RecordedRequest recordedRequest = mockWebServer.takeRequest();
            recordedRequest.getBody();

            openRouteService.getRoute(getCoordinates(), method, language);

        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }

    }

    private String getCoordinatesJsonSample() {
        return "{\"routes\":[{\"summary\":{\"distance\":1369,\"duration\":292},\"segments\":[{\"distance\":887.8,\"duration\":189,\"steps\":[{\"distance\":312.6,\"duration\":75,\"type\":11,\"instruction\":\"Head north on Wielandtstraße\",\"name\":\"Wielandtstraße\",\"way_points\":[0,10]},{\"distance\":251.1,\"duration\":36.2,\"type\":1,\"instruction\":\"Turn right onto Mönchhofstraße\",\"name\":\"Mönchhofstraße\",\"way_points\":[10,21]},{\"distance\":212.2,\"duration\":50.9,\"type\":2,\"instruction\":\"Turn sharp left onto Keplerstraße\",\"name\":\"Keplerstraße\",\"way_points\":[21,24]},{\"distance\":109.9,\"duration\":26.4,\"type\":1,\"instruction\":\"Turn right onto Moltkestraße\",\"name\":\"Moltkestraße\",\"way_points\":[24,27]},{\"distance\":2,\"duration\":0.5,\"type\":0,\"instruction\":\"Turn left onto Werderplatz\",\"name\":\"Werderplatz\",\"way_points\":[27,28]},{\"distance\":0,\"duration\":0,\"type\":10,\"instruction\":\"Arrive at Werderplatz, on the right\",\"name\":\"-\",\"way_points\":[28,28]}]},{\"distance\":481.2,\"duration\":103,\"steps\":[{\"distance\":2,\"duration\":0.5,\"type\":11,\"instruction\":\"Head south on Werderplatz\",\"name\":\"Werderplatz\",\"way_points\":[28,29]},{\"distance\":265.5,\"duration\":63.7,\"type\":0,\"instruction\":\"Turn left onto Moltkestraße\",\"name\":\"Moltkestraße\",\"way_points\":[29,37]},{\"distance\":83,\"duration\":7.5,\"type\":0,\"instruction\":\"Turn left onto Handschuhsheimer Landstraße, B 3\",\"name\":\"Handschuhsheimer Landstraße, B 3\",\"way_points\":[37,39]},{\"distance\":130.8,\"duration\":31.4,\"type\":0,\"instruction\":\"Turn left onto Roonstraße\",\"name\":\"Roonstraße\",\"way_points\":[39,42]},{\"distance\":0,\"duration\":0,\"type\":10,\"instruction\":\"Arrive at Roonstraße, straight ahead\",\"name\":\"-\",\"way_points\":[42,42]}]}],\"bbox\":[8.681436,49.41461,8.690123,49.420514],\"geometry\":\"ihrlHkr~s@S@}DHQ?OCs@IqAYQEQEyBa@SC?]?M?eA?S?W?SBuB@k@?a@AcH@[IBaJ~@O@A]YyFASC?B?CUEgAAWCUYyEASg@sI?CmAt@y@f@?BVlELpC\",\"way_points\":[0,28,42]}],\"bbox\":[8.681436,49.41461,8.690123,49.420514],\"metadata\":{\"attribution\":\"openrouteservice.org | OpenStreetMap contributors\",\"service\":\"routing\",\"timestamp\":1609247972297,\"query\":{\"coordinates\":[[8.681495,49.41461],[8.686507,49.41943],[8.687872,49.420318]],\"profile\":\"driving-car\",\"format\":\"json\",\"language\":\"en\"},\"engine\":{\"version\":\"6.3.1\",\"build_date\":\"2020-12-10T15:35:43Z\",\"graph_date\":\"1970-01-01T00:00:00Z\"}}}";

    }*/

    //works
    @Test
    public void createPostRequestTest() {

        //expected value
        RequestBody requestBody = RequestBody.create(jsonWaypoints, MediaType.parse("application/json; charset=utf-8"));
        Request expected = new Request.Builder().url("https://api.openrouteservice.org/v2/directions/" + method).
                post(requestBody).addHeader("Authorization", api_key).build();

        //Actual value
        when(openRouteService.createPostRequest(method, jsonWaypoints)).thenCallRealMethod();
        Request actual = openRouteService.createPostRequest(method, jsonWaypoints);

        assertEquals(expected.toString(), actual.toString());
    }

    private double[][] getCoordinates() {
        double[][] coordinates = new double[3][2];

        coordinates[0][0] = 8.681495;
        coordinates[0][1] = 49.41461;

        coordinates[1][0] = 8.686507;
        coordinates[1][1] = 49.41943;

        coordinates[2][0] = 8.687872;
        coordinates[2][1] = 49.420318;

        return coordinates;
    }

    private String getWayPointsJsonSample() {
        return "{\"coordinates\":" + Arrays.deepToString(getCoordinates()) + ",\"language\":en}";
    }
}