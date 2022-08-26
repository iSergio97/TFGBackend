package com.tfg.pmh;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PmhApplication.class)
@AutoConfigureMockMvc
public class SolicitudMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void SolicitudMvcTest() throws Exception{
        long solicitudId = 17255;

        URIBuilder builder = new URIBuilder();
        HttpUriRequest request = null;
        // Hacemos Login para la petición
        String cookie = loginRes();

        builder.setScheme("http")
                .setHost("localhost:8080")
                .setPath("/solicitud/administrador/update");
        builder.addParameter("solicitudId", String.valueOf(solicitudId));
        builder.addParameter("estado", "R");
        builder.addParameter("justificacion", "Rechazo");

        URI uri = builder.build();

        request = new HttpPost(uri);
        request.setHeader("Authorization", "Bearer " + cookie);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assert(response.getStatusLine().getStatusCode() == 200);
    }

    public String loginRes() throws Exception {
        URIBuilder builder = new URIBuilder();

        builder = builder.setScheme("http")
                .setHost("localhost:8080")
                .setPath("/sistema/administrador/login");
        builder.setParameter("username", "sergio");
        builder.setParameter("password", "sergio");

        URI uri = builder.build();

        HttpUriRequest request = new HttpPost(uri);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        String jsonResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.ISO_8859_1);

        JSONObject json = new JSONObject(jsonResponse);

        JSONArray object = json.getJSONArray("object");
        return String.valueOf(object.get(1));
    }
}
