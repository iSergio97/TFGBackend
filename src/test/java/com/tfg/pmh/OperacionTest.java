package com.tfg.pmh;

import com.tfg.pmh.models.Operacion;
import com.tfg.pmh.models.Solicitud;
import com.tfg.pmh.services.HabitanteService;
import com.tfg.pmh.services.OperacionService;
import com.tfg.pmh.services.SolicitudService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(properties = "application-test.properties")
public class OperacionTest {

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private OperacionService operacionService;

    @Test
    public void obtenemosSolicitudPorOperacionID() {
        Long id = 17216L;

        Operacion operacion = this.operacionService.findById(id);

        assert operacion != null;
        assert operacion.getSolicitud() != null;
        assert operacion.getSolicitud().getDocumentos() != null;
        assert operacion.getSolicitud().getDocumentos().size() > 0;
    }
}
