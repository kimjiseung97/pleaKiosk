package kiosk.pleaKiosk.domain.controller;
import kiosk.pleaKiosk.domain.dto.request.PageRequeust;
import kiosk.pleaKiosk.domain.dto.request.ProductModifyRequest;
import kiosk.pleaKiosk.domain.dto.request.ProductSaveRequest;
import kiosk.pleaKiosk.domain.entity.Product;
import kiosk.pleaKiosk.domain.exception.CustomException;
import kiosk.pleaKiosk.domain.exception.ErrorCode;
import kiosk.pleaKiosk.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/register")
    public ResponseEntity<String> saveProduct(@Valid @RequestBody ProductSaveRequest productSaveRequest) {

            productService.registerProduct(productSaveRequest);
            return ResponseEntity.ok().body("상품저장완료");

    }

    @GetMapping("/getall-product")
    public Page<Product> allProducts(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sort){
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.BAD_REQUEST_PAGE);
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return productService.getAllProduct(pageable);
    }

    @RequestMapping(value = "/modify",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ResponseEntity<String> modifyProduct(@Valid @RequestBody ProductModifyRequest productModifyRequest){

        productService.modifyProduct(productModifyRequest);

        return ResponseEntity.ok().body(productModifyRequest.getId() + "번 상품 정보 수정완료");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){

        productService.deleteProduct(id);

        return ResponseEntity.ok().body(id + "번 상품 삭제완료");
    }



}
