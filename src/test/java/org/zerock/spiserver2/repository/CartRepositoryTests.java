package org.zerock.spiserver2.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.spiserver2.domain.CartItem;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private  CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct() {

        String email = "suer1@aaa.com";
        Long pno = 5L;
        int qty = 1;

        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
        if(cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }
    }

    @Test
    public void testListOfMember() {
        String email = "user1@aaa.com";

        cartItemRepository.getItemsOfCartDTOByEmail(email);

    }

}
