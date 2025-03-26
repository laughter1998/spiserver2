package org.zerock.spiserver2.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.spiserver2.dto.PageRequestDTO;
import org.zerock.spiserver2.dto.PageResponseDTO;
import org.zerock.spiserver2.dto.ProductDTO;

@Transactional
public interface ProductService {
    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    void modify(ProductDTO productDTO);

    void remove(Long pno);
}
