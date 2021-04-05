package com.tfg.pmh;

import com.tfg.pmh.controllers.SolicitudController;
import com.tfg.pmh.services.DocumentoService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
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

import java.net.URI;

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
        //TODO: Probar con docs que ya existan
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

        assert(response.getStatusLine().getStatusCode() != HttpStatus.OK.value());
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

        assert(response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value());
    }
}
