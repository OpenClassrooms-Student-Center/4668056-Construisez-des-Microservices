package com.ecommerce.micrommerce.web.controller;

import com.ecommerce.micrommerce.web.dao.ProductDao;
import com.ecommerce.micrommerce.web.model.Product;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    //Récupérer la liste des produits
    @GetMapping("/Produits")
    public MappingJacksonValue listeProduits() {
        List<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }

    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        return productDao.findById(id);
    }

    @PostMapping(value = "/Produits")
    public ResponseEntity<Product> ajouterProduit(@RequestBody Product product) {
        Product productAdded = productDao.save(product);
        if (Objects.isNull(productAdded)) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}