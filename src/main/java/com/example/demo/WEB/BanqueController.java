package com.example.demo.WEB;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.DAO.ClientRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;
import com.example.demo.entities.CompteCourant;
import com.example.demo.entities.CompteEpargne;
import com.example.demo.entities.Operation;
import com.example.demo.metier.IBanqueMetier;
import com.example.demo.metier.IClientMetier;

@Controller
public class BanqueController {
@Autowired
private IBanqueMetier banqueMetier;
@Autowired
private ClientRepository clientRepository;
@Autowired
private IClientMetier clientMetier;
@RequestMapping("/operations")
public String index() {
	return "comptes";
}
@RequestMapping("/consulterOperations")
public String consulter(Model model, String codeCompte,@RequestParam(name="page",defaultValue="0") int page, @RequestParam(name="size",defaultValue="5")int size) {
	model.addAttribute("codeCompte", codeCompte);
	try {
		Compte cp=banqueMetier.consulterCompte(codeCompte);
		Page<Operation> pageOperations=banqueMetier.listOperation(codeCompte, page, size);
		model.addAttribute("listOperations", pageOperations.getContent());
		int[] pages=new int[pageOperations.getTotalPages()];
		model.addAttribute("pages", pages);
		model.addAttribute("compte", cp);
	} catch (Exception e) {
		model.addAttribute("exception", e);
	}
	
	return "comptes";
}

@RequestMapping(value="/addCompte")
public String addCompte(Model model,Long codeClient,
								            @RequestParam(defaultValue="0") int page,
								            @RequestParam(defaultValue="3")int size){
	Client c=clientMetier.consulterClient(codeClient);
	Page<Compte> comptes=clientMetier.listComptes(codeClient,page, size);
	model.addAttribute("comptes",comptes.getContent());
	model.addAttribute("pages",comptes.getTotalPages());
	model.addAttribute("codeClient",codeClient);
	model.addAttribute("(client",c);
	model.addAttribute("pageCourant",page);
	return "formCompte";
}

@RequestMapping(value="/saveCompte",method=RequestMethod.POST)
public String saveCompte(Model model,Long codeClient,
	String code	,double solde ,String typeCte,@RequestParam(defaultValue="0")double decouvert ,@RequestParam(defaultValue="0")double taux,
								            @RequestParam(defaultValue="0") int page,
								            @RequestParam(defaultValue="3")int size){
	Client c=clientMetier.consulterClient(codeClient);
	if(typeCte.equals("CC")){
		Compte cpte=new CompteCourant(code, new Date(), solde, c, decouvert);
		cpte=banqueMetier.ajouterCompte(cpte);
	}else if (typeCte.equals("CE")){
		Compte cpte=new CompteEpargne(code, new Date(), solde, c, taux);
		cpte=banqueMetier.ajouterCompte(cpte);
	}
	Page<Compte> comptes=clientMetier.listComptes(codeClient,page, size);
	model.addAttribute("comptes",comptes.getContent());
	model.addAttribute("pages",comptes.getTotalPages());
	model.addAttribute("codeClient",codeClient);
	model.addAttribute("(client",c);
	model.addAttribute("pageCourant",page);
	return "formCompte";
}

@RequestMapping(value="/clients")
public String client(){
	return "formClients";
}

@RequestMapping(value="/ajouterClient")
public String ajouterClient(){
	return "formAjouterClient";
}

@RequestMapping("/consulterclient")
public String consulterClient(Model model,long motCle,
		                      @RequestParam(defaultValue="0") int page,
		                      @RequestParam(defaultValue="3")int size){
	try {
		//Client c=clientMetier.consulterClient(codeClt);
		//Page<Compte> comptes=clientMetier.listComptes(codeClt,page, size);
		Page<Client> clients=clientMetier.listClient(motCle,page, size);
		model.addAttribute("motCle",motCle);
		model.addAttribute("clients",clients.getContent());
		model.addAttribute("pageCourant",page);
	    //model.addAttribute("comptes",comptes.getContent());
	    // System.out.println(operations.getContent());
	} catch (Exception e) {
		model.addAttribute("exception",e);
	}
	
	return "formClients";
}

@RequestMapping(value="/saveClient",method=RequestMethod.POST)
public String saveCompte(Model model,Client c){
	Client cp=clientMetier.ajouter(c);
	
	model.addAttribute("(client",cp);
	
	return "formAjouterClient";
}




@RequestMapping(value="/saveOperation", method=RequestMethod.POST)
   public String saveOperation(Model model, String typeOperation,String codeCompte,double montant, String codeCompte2) {
	try {
		if(typeOperation.equals("VERS")) {
			banqueMetier.verser(codeCompte, montant);
		}
		else if(typeOperation.equals("RET")) {
			banqueMetier.retirer(codeCompte, montant);
		}
		else {
			banqueMetier.virement(codeCompte, codeCompte2, montant);
		}
	} catch (Exception e) {
	    model.addAttribute("error", e);
	    return "redirect:/consulterOperations?codeCompte="+codeCompte+"&error="+e.getMessage();
	}
	
	
	return "redirect:/consulterOperations?codeCompte="+codeCompte;
	   
   }
}
