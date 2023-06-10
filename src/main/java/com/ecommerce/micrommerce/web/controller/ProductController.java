package com.ecommerce.micrommerce.web.controller;

import com.ecommerce.micrommerce.web.dao.ProductDao;
import com.ecommerce.micrommerce.web.exceptions.ProduitIntrouvableException;
import com.ecommerce.micrommerce.web.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.*;

@Api( "API pour les opérations CRUD sur les produits.")
@RestController
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }

    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {
        productDao.save(product);
    }

    //Récupérer la liste des produits
    @GetMapping("/Produits")
    public List<Product> listeProduits() {
        return productDao.findAll();
    }

    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);
        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
        return produit;
    }

    @GetMapping(value = "test/produits/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(prixLimit);
    }

    @PostMapping(value = "/Produits")
    public ResponseEntity<Product> ajouterProduit(@RequestBody @Valid Product product) {
        Product productAdded = productDao.save(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // TODO A RETIRER DE SWAGGER
    @ApiOperation(value = "calculate margin on a product")
    @GetMapping(value = "/Produits/diff/{id}")
    public int calculerMargeProduit(@PathVariable int id){
        Product product = productDao.findById(id);

        return product.getPrix() - product.getPrixAchat();
    }

    @ApiOperation(value = "Récupérer la liste des produits triée par ordre alphabetique")
    @GetMapping("/Produits/triaphabetique/")
    public List<Product> trierProduitsParOrdreAlphabetique(){
        List<Product> productList = productDao.findAll();
        productList.sort(new Comparator<Product>() {

            @Override
            public int compare(Product curentProduct, Product nextProduct) {
                String s1 = curentProduct.getNom();
                String s2 = nextProduct.getNom();
                return s1.compareToIgnoreCase(s2);
            }

        });

        return productList;
    }
}