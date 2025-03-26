package org.zerock.spiserver2.service;


import lombok.extern.log4j.Log4j2;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.spiserver2.dto.PageRequestDTO;
import org.zerock.spiserver2.dto.PageResponseDTO;
import org.zerock.spiserver2.dto.ProductDTO;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private  ProductService productService;

    @Test
    public  void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> responseDTO =  productService.getList(pageRequestDTO);

        log.info(responseDTO.getDtoList());
    }

    @Test
    public void testRegister(){
        ProductDTO productDTO = ProductDTO.builder()
        .pname("새로운 상품")
        .pdesc("신규 추가 상품입니다.")
        .price(1000)
        .build();

        productDTO.setUploadFileNames(
            java.util.List.of(
                UUID.randomUUID()+"_" + "test1.jpg",
                UUID.randomUUID()+"_" + "test2.jsp"
            )
        );
        productService.register(productDTO);
    }
}
