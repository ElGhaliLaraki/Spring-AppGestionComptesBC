package com.example.demo.DAO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Compte;

public interface CompteRepository extends JpaRepository<Compte, String>{
	@Query("select c from Compte c  where c.client.code=:x order by c.dateCreation")
	  public Page<Compte> listComptes(@Param("x")Long codeCte,Pageable page);

	
}
