package com.tfg.pmh.controllers;

import com.tfg.pmh.models.Administrador;
import com.tfg.pmh.models.CuentaUsuario;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Demo {
	
	@GetMapping("demo")
	public @ResponseBody CuentaUsuario helloWorld() {
		Administrador admin = new Administrador();
		CuentaUsuario cuenta = new CuentaUsuario();
		cuenta.setUsername("Sergio");
		cuenta.setPassword("A");
		cuenta.setSalt("Adf5AS6DF54");
		admin.setCuentaUsuario(cuenta);
		return admin.getCuentaUsuario();
	}

}
