package com.qv.ballet_api;

import com.qv.ballet_api.dto.CanjeRequest;
import com.qv.ballet_api.dto.PagoResponse;
import com.qv.ballet_api.entity.Compra;
import com.qv.ballet_api.entity.Usuario;
import com.qv.ballet_api.repository.CompraRepository;
import com.qv.ballet_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@SpringBootApplication
public class BalletApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalletApiApplication.class, args);
	}
}


