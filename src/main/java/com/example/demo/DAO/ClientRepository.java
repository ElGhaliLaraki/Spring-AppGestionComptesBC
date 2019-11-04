
package com.example.demo.DAO;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
	@Query("select c from Client c where c.code = :x")
	public Page<Client> listClient(@Param("x")Long code,Pageable paegable);

	
}