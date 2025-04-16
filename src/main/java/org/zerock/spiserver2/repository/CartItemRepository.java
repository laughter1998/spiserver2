package org.zerock.spiserver2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.spiserver2.domain.CartItem;
import org.zerock.spiserver2.dto.CartItemListDTO;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Long cino, int qty, String pname, int price, String imageFile
    @Query("select " +
            " new org.zerock.spiserver2.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price, pi.fileName ) " +
            "from " +
            "CartItem ci inner join Cart mc on ci.cart = mc " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where " +
            "mc.owner.email = :email and pi.ord = 0 " +
            "order by ci.cino desc ")

    List<Object[]> getItemsOfCartDTOByEmail(@Param("email") String email);

}
