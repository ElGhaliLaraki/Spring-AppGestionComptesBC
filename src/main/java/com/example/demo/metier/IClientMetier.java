package com.example.demo.metier;



import org.springframework.data.domain.Page;

import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;

public interface IClientMetier {

	public Client consulterClient(Long codeClt);
	public void supprimerClient(Long codeClt);
	public Client ajouter(Client c);
	//public Client modifier(Client c);
	public Page<Compte> listComptes(Long codeClt,int page,int size);
	Page<Client> listClient(Long code, int page, int size);
	
}
