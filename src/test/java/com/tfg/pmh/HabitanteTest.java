package com.tfg.pmh;

import com.tfg.pmh.models.Habitante;
import com.tfg.pmh.services.HabitanteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(properties = "application-test.properties")
public class HabitanteTest {

    @Autowired
    private HabitanteService habitanteService;


    @Test
    public void noExisteHabitanteUsername() {
        String username = "habitante";
        Habitante noEncontrado = this.habitanteService.findByUsername(username);
        assert noEncontrado == null;
    }

    @Test
    public void existeHabErrorPassword() {
        String password = "habitante";
        Habitante encontrado = this.habitanteService.findByUsername("habitante0");
        assert !encontrado.getCuentaUsuario().getPassword().equals(password);
    }
}
