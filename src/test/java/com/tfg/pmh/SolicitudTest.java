package com.tfg.pmh;

import com.tfg.pmh.email.Email;
import com.tfg.pmh.email.EmailService;
import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.SolicitudService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(properties = "application-test.properties")
public class SolicitudTest {

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private HabitanteService habitanteService;

    @Autowired
    private EmailService emailService;

    @Test
    public void devuelveTodasSolicitudes() {
        List<Solicitud> todas = this.solicitudService.findAll();

        assert todas != null;
        assert todas.size() > 0;
    }


    @Test
    public void existeSolicitd() {
        Long id = 17215L;
        Solicitud solicitud = this.solicitudService.findById(id);

        assert solicitud != null;
        assert solicitud.getDocumentos().size() > 0;
        assert solicitud.getSolicitante() != null;
        assert solicitud.getSubtipo().contains(solicitud.getTipo());

        solicitud = this.solicitudService.findById(1L);

        assert solicitud == null;
    }

    @Test
    public void solicitudesDeHabitante() {
        Habitante habitante = this.habitanteService.findByUsername("habitante0");

        Collection<Solicitud> solicitudCollection = this.solicitudService.findBySolicitante(habitante.getId());

        long cantidadSolicitudes = solicitudCollection
                .stream()
                .filter(solicitud -> Objects.equals(solicitud.getSolicitante().getId(), habitante.getId())).count();

        assert solicitudCollection.size() > 0;

        assert cantidadSolicitudes == solicitudCollection.size();
    }

    @Test
    public void enviaEmail() {
        String correo = "stb_1997@hotmail.com";

        String content = "Mensaje de prueba realizada en SolicitudService para el requisito funcional RF06";

        Email email = new Email("POH - Requisito funcional RF06", correo, content);
        emailService.sendEmail(email);
    }
}
