package com.tfg.pmh;

import com.mysql.cj.xdevapi.JsonArray;
import com.sun.jndi.toolkit.url.Uri;
import com.tfg.pmh.controllers.SolicitudController;
import com.tfg.pmh.services.DocumentoService;
import netscape.javascript.JSObject;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PmhApplication.class)
@AutoConfigureMockMvc
public class DocumentosTest {

    private static String BASE_URL = "http://localhost:8080/solicitud/document";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentoService documentoService;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getDocumentoByIdAndReturnsOK() throws Exception{
        long docId = 17212;
        long solicitudId = 17239;


        URIBuilder builder = new URIBuilder();
        HttpUriRequest request = null;
        HttpResponse response = null;

        // Hacemos Login para la petición
        String cookie = loginRes();

        builder.setScheme("http")
                .setHost("localhost:8080")
                .setPath("/solicitud/document/" + docId);
        builder.setParameter("requestId", String.valueOf(solicitudId));

        URI uri = builder.build();

        request = new HttpGet(uri);

        request.setHeader("Authorization", "Bearer " + cookie);

        response = HttpClientBuilder.create().build().execute(request);

        assert(response.getStatusLine().getStatusCode() == HttpStatus.OK.value());
    }

    @Test
    public void getDocumentoByIdAndReturnsError() throws Exception {

        long docId = 100L;
        Long solicitudId = 101L;

        URIBuilder builder = new URIBuilder();

        builder.setScheme("http")
                .setHost("localhost:8080")
                .setPath("/solicitud/document/" + docId);
        builder.setParameter("requestId", String.valueOf(solicitudId));

        URI uri = builder.build();

        HttpUriRequest request = new HttpGet(uri);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        builder.setScheme("http")
                .setHost("localhost:8080")
                .setPath("/solicitud/document/" + docId);
        builder.setParameter("requestId", String.valueOf(solicitudId));
        uri = builder.build();

        request = new HttpGet(uri);

        response = HttpClientBuilder.create().build().execute(request);

        assert(response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void solicitudSinCabeceraSeguridad() throws Exception {

        long docId = 100L;
        Long solicitudId = 101L;

        URIBuilder builder = new URIBuilder();

        builder.setScheme("http")
                .setHost("localhost:8080")
                .setPath("/solicitud/document/" + docId);
        builder.setParameter("requestId", String.valueOf(solicitudId));
        URI uri = builder.build();

        HttpUriRequest request = new HttpGet(uri);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assert(response.getStatusLine().getStatusCode() == HttpStatus.FORBIDDEN.value());
    }


    public String loginRes() throws Exception {
        URIBuilder builder = new URIBuilder();

        builder = builder.setScheme("http")
                .setHost("localhost:8080")
                .setPath("/habitante/login");
        builder.setParameter("username", "habitante0");
        builder.setParameter("password", "habitante0");

        URI uri = builder.build();

        HttpUriRequest request = new HttpPost(uri);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        String jsonResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.ISO_8859_1);

        JSONObject json = new JSONObject(jsonResponse);

        JSONArray object = json.getJSONArray("object");
        return String.valueOf(object.get(1));
    }
}
