package org.zerock.spiserver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.spiserver2.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
