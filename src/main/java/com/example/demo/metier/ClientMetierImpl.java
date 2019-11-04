package com.example.demo.metier;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.ClientRepository;
import com.example.demo.DAO.CompteRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;

@Service
@Transactional
public class ClientMetierImpl implements IClientMetier {

	@Autowired
	ClientRepository clientRepository;
	@Autowired
	CompteRepository compteRepository;
	
	@Override
	public Client consulterClient(Long codeClt) {
		Optional<Client> cl=clientRepository.findById(codeClt);
		Client clr=cl.get();
if(clr==null) throw new RuntimeException("Compte introuvable");
		
		return clr;
		
	}
	@Override
	public void supprimerClient(Long codeClt) {
		clientRepository.deleteById(codeClt);
	}

	@Override
	public Client ajouter(Client c) {
		return clientRepository.save(c);
	}

	@Override
	public Page<Compte> listComptes(Long codeClt,int page, int size) {	
		return compteRepository.listComptes(codeClt, new PageRequest(page, size)) ;
	}

	
	@Override
	public Page<Client> listClient(Long code, int page, int size) {
		return clientRepository.listClient(code, new PageRequest(page, size));
	}

}
