package com.example.demo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.example.demo.DAO.ClientRepository;
import com.example.demo.DAO.CompteRepository;
import com.example.demo.DAO.OperationRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;
import com.example.demo.entities.CompteCourant;
import com.example.demo.entities.CompteEpargne;
import com.example.demo.entities.Retrait;
import com.example.demo.entities.Versement;
import com.example.demo.metier.IBanqueMetier;



@SpringBootApplication
public class ApplicationGestionComptesApplication implements CommandLineRunner{
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private IBanqueMetier banqueMetier;
	public static void main(String[] args) {
		SpringApplication.run(ApplicationGestionComptesApplication.class, args);
		
		
	}

	@Override
	public void run(String... args) throws Exception {
		
		Client c1=clientRepository.save(new Client("El Ghali Laraki","ghalilaaraki@gmail.com"));
		Client c2=clientRepository.save(new Client("Ali Idboukori","aliIdboukori@gmail.com"));
		
		Compte cp1=compteRepository.save(new CompteCourant("c1", new Date(), 90000, c1, 6000));
		Compte cp2=compteRepository.save(new CompteEpargne("c2", new Date(), 3000, c2, 5.5));
		
		operationRepository.save(new Versement(new Date(), 9000, cp1));
		operationRepository.save(new Retrait(new Date(), 6000, cp1));
		
		operationRepository.save(new Versement(new Date(), 5000, cp2));
		operationRepository.save(new Retrait(new Date(), 2000, cp2));
		
		banqueMetier.verser("c1", 111111);
		
	}

}
