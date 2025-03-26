package org.zerock.spiserver2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.spiserver2.dto.PageRequestDTO;
import org.zerock.spiserver2.dto.PageResponseDTO;
import org.zerock.spiserver2.dto.ProductDTO;
import org.zerock.spiserver2.service.ProductService;
import org.zerock.spiserver2.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


import org.springframework.web.bind.annotation.PutMapping;








@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductContoller {

    private final CustomFileUtil fileUtil;

    private final ProductService productService;

    // @PostMapping("/")
    // public Map<String, String> register(ProductDTO productDTO){
    //     log.info("register:" + productDTO);
    //     List<MultipartFile> files = productDTO.getFiles();
    //     List<String> uploadedFileNames = fileUtil.saveFiles(files);
    //     productDTO.setUploadFileNames(uploadedFileNames);
    //     log.info(uploadedFileNames);
    //     return Map.of("RESULT", "SUCCESS");
    // }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName){
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> List(PageRequestDTO pageRequestDTO){
        return productService.getList(pageRequestDTO);
    }
    
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO){
        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames =  fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info(uploadFileNames);
        Long pno = productService.register(productDTO);
        return Map.of("result", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, @ModelAttribute ProductDTO productDTO){
        productDTO.setPno(pno);
        ProductDTO oldProductDTO = productService.get(pno);
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        List<String> uploadFileNames = productDTO.getUploadFileNames();
        if(currentUploadFileNames != null && !currentUploadFileNames.isEmpty()){
            uploadFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);

        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        if(oldFileNames != null && oldFileNames.size() > 0){
            List<String> removeFiles =
            oldFileNames.stream().filter(fileName -> uploadFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");

    }
   
    @DeleteMapping("/{pno}")

    public Map<String, String> remove(@PathVariable("pno") Long pno) {
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        productService.remove(pno);
        fileUtil.deleteFiles(oldFileNames);
        return Map.of("RESULT", "SUCCESS");
    }
    
    
}
