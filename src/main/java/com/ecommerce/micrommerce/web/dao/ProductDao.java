package com.ecommerce.micrommerce.web.dao;

import com.ecommerce.micrommerce.web.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
    Product findById(int id);
    List<Product> findByPrixGreaterThan(int prixLimit);

}
