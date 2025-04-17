package org.zerock.spiserver2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.spiserver2.dto.CartItemDTO;
import org.zerock.spiserver2.dto.CartItemListDTO;
import org.zerock.spiserver2.service.CartService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;


    @PreAuthorize("#itemDTO.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart( @RequestBody CartItemDTO itemDTO){

        log.info(itemDTO);

        if(itemDTO.getQty() <= 0) {
            return cartService.remove(itemDTO.getCino());
        }


        return cartService.addOrModify(itemDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal){
        String email = principal.getName();
        log.info("email: " + email);
        return  cartService.getCartItems(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart( @PathVariable("cino") Long cino){

        log.info("cart item no: " + cino);

        return cartService.remove(cino);
    }

}
