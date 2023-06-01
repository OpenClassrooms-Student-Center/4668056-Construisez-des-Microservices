package com.ecommerce.micrommerce.web.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.micrommerce.web.model.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
	Product findById(int id);

	List<Product> findByPrixGreaterThan(int prixLimit);

	List<Product> findByOrderByNomAsc();

}
