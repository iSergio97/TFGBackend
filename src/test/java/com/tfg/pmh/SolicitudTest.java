package com.tfg.pmh;

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

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(properties = "application-test.properties")
public class SolicitudTest {

    //org.junit.runners.model.InvalidTestClassError: Invalid test class 'com.tfg.pmh.SolicitudTest':

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private HabitanteService habitanteService;

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

        assert solicitudCollection != null && solicitudCollection.size() > 0;
    }
}
