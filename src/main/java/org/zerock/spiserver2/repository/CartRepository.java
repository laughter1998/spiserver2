package org.zerock.spiserver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.spiserver2.domain.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select cart from Cart cart where  cart.owner.email = :email")

    Optional<Cart> getCartOfMember( @Param("email") String email);
}
