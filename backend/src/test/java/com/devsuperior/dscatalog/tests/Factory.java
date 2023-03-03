package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https:/img.com/img.png", Instant.parse("2020-02-02T03:00:00z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static Category createCategory() {
        return new Category(2L, "Electronics");
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
