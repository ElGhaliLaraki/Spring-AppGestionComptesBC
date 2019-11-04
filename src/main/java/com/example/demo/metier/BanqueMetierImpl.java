package com.example.demo.metier;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DAO.ClientRepository;
import com.example.demo.DAO.CompteRepository;
import com.example.demo.DAO.OperationRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;
import com.example.demo.entities.CompteCourant;
import com.example.demo.entities.Operation;
import com.example.demo.entities.Retrait;
import com.example.demo.entities.Versement;
@Service
@Transactional
public class BanqueMetierImpl implements IBanqueMetier{
	@Autowired
    private CompteRepository compteRepository;
	@Autowired
    private ClientRepository clientRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Override
	public Compte consulterCompte(String codeCpte) {
		Optional<Compte> cp=compteRepository.findById(codeCpte);
		Compte cp1=cp.get();
		if (cp1==null) throw new RuntimeException("Compte introuvable");
		return cp1;
	}

	@Override
	public void verser(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
		Versement v=new Versement(new Date(), montant, cp);
        operationRepository.save(v);
        cp.setSolde(cp.getSolde()+montant);
        compteRepository.save(cp);
	}

	@Override
	public void retirer(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
		double facilitesCaisse=0;
		if(cp instanceof CompteCourant)
			facilitesCaisse=((CompteCourant)cp).getDecouvert();
		if(cp.getSolde()+facilitesCaisse<montant)
			throw new RuntimeException("Solde insuffisant");
		Retrait r=new Retrait(new Date(), montant, cp);
		operationRepository.save(r);
        cp.setSolde(cp.getSolde()-montant);
        compteRepository.save(cp);
		
		
        
	}
	@Override
	public Compte ajouterCompte(Compte compte) {
		
		return compteRepository.save(compte);
	}

	@Override
	public void virement(String codeCpte1, String codeCpte2, double montant) {
		if(codeCpte1.equals(codeCpte2))
			throw new RuntimeException("Impossible d'effectuer un virement sur le meme compte");
		retirer(codeCpte1, montant);
		verser(codeCpte2, montant);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public Page<Operation> listOperation(String codeCpte, int page, int size) {
		
		return operationRepository.listOperation(codeCpte, new PageRequest(page, size));
	}

	@Override
	public Client index(Long code) {
		return null;
	}

	@Override
	public Page<Client> listClient(Long code, int page, int size) {
		return clientRepository.listClient(code, new PageRequest(page, size));
	}

}
