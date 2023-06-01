package com.ecommerce.micrommerce.web.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecommerce.micrommerce.web.dao.ProductDao;
import com.ecommerce.micrommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.micrommerce.web.exceptions.ProduitIntrouvableException;
import com.ecommerce.micrommerce.web.model.Product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("API pour les opérations CRUD sur les produits.")
@RestController
public class ProductController {

	private final ProductDao productDao;

	public ProductController(ProductDao productDao) {
		this.productDao = productDao;
	}

	@DeleteMapping(value = "/Produits/{id}")
	public void supprimerProduit(@PathVariable int id) {
		productDao.deleteById(id);
	}

	@PutMapping(value = "/Produits")
	public void updateProduit(@RequestBody Product product) {
		productDao.save(product);
	}

	// Récupérer la liste des produits
	@GetMapping("/Produits")
	public List<Product> trierProduitsParOrdreAlphabetique() {

		return productDao.findByOrderByNomAsc();
	}

	@ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
	@GetMapping(value = "/Produits/{id}")
	public Product afficherUnProduit(@PathVariable int id) {
		Product produit = productDao.findById(id);
		if (produit == null)
			throw new ProduitIntrouvableException(
					"Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
		return produit;
	}

	@GetMapping(value = "test/produits/{prixLimit}")
	public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
		return productDao.findByPrixGreaterThan(prixLimit);
	}

	@PostMapping(value = "/Produits")
	public ResponseEntity<Product> ajouterProduit(@RequestBody Product product) {
		if (product.getPrix() == 0)
			throw new ProduitGratuitException(
					"Le produit ne peut etre gratuit. Veuillez fournir un prix de vente supérieur à 0");
		Product productAdded = productDao.save(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(productAdded.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(value = "/AdminProduits", method = RequestMethod.GET)
	public List<String> calculerMargeProduit() {
		List<Product> produits = productDao.findAll();
		List<String> results = new ArrayList<String>();
		for (Product p : produits) {
			results.add(p + ":" + (p.getPrix() - p.getPrixAchat()));
		}

		return results;

	}

}