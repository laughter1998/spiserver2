package org.zerock.spiserver2.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.spiserver2.domain.Product;
import org.zerock.spiserver2.domain.ProductImage;
import org.zerock.spiserver2.dto.PageRequestDTO;
import org.zerock.spiserver2.dto.PageResponseDTO;
import org.zerock.spiserver2.dto.ProductDTO;
import org.zerock.spiserver2.repository.ProductRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor

public class ProductServiceImpl implements  ProductService{

    private  final ProductRepository productRepository;
    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO){

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending() );
        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDTO> dtoList = result.get().map(arr -> {
            ProductDTO productDTO = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));

            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return  PageResponseDTO.<ProductDTO>withALL()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long register(ProductDTO productDTO){

        Product product = dtoToEntity(productDTO);

        log.info("-------------------------");
        log.info(product);
        log.info(product.getImageList());
        Long pno = productRepository.save(product).getPno();
        return pno;
    }

    @Override
    public ProductDTO get(Long pno){

        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();
        
        return entityDTO(product);
    }

    @Override
    public void modify(ProductDTO productDTO){
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();

        product.changePrice(productDTO.getPrice());
        product.changeName(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changeDel(product.isDelFlag());

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        product.clearList();

        if(uploadFileNames != null && !uploadFileNames.isEmpty()){
            uploadFileNames.forEach(uploadName -> {
                product.addImageString(uploadName);
            });
        }

        productRepository.save(product);
    }

    private ProductDTO entityDTO(Product product){

        ProductDTO productDTO = ProductDTO.builder()
        .pno(product.getPno())
        .pname(product.getPname())
        .pdesc(product.getPdesc())
        .price(product.getPrice())
        .defFlag(product.isDelFlag())
        .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.isEmpty()){
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage ->
            productImage.getFileName()).toList();

            productDTO.setUploadFileNames(fileNameList);
            return productDTO;
        
    }


    private Product dtoToEntity(ProductDTO productDTO){
        Product product = Product.builder().pno(productDTO.getPno()).pname(productDTO.getPname())
        .pdesc(productDTO.getPdesc())
        .price(productDTO.getPrice())
        .build();

        List<String> uploadFileNames =  productDTO.getUploadFileNames();

        if(uploadFileNames == null || uploadFileNames.size() == 0){
            return product;
        }

        uploadFileNames.forEach(fileName -> {
            product.addImageString(fileName);
        });

        return product;
    }

    @Override
    public void modify(ProductDTO productDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modify'");
    }
}
