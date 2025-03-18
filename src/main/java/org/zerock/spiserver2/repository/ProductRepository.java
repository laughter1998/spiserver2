package org.zerock.spiserver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.spiserver2.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
