package kiosk.pleaKiosk.domain.controller;
import kiosk.pleaKiosk.domain.codes.ErrorCode;
import kiosk.pleaKiosk.domain.dto.request.ProductModifyRequest;
import kiosk.pleaKiosk.domain.dto.request.ProductSaveRequest;
import kiosk.pleaKiosk.domain.dto.response.ApiResponse;
import kiosk.pleaKiosk.domain.entity.Product;
import kiosk.pleaKiosk.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/register")
    public ApiResponse<Product> saveProduct(@Valid @RequestBody ProductSaveRequest productSaveRequest) {
        return productService.registerProduct(productSaveRequest);
    }

    @GetMapping("/getall-product")
    public ApiResponse<Page<Product>> allProducts(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sort) throws IOException {
        if (page < 1 || size < 1) {
            throw new IOException(String.valueOf(ErrorCode.IO_ERROR));
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return productService.getAllProduct(pageable);
    }

    @RequestMapping(value = "/modify",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    public ApiResponse<Product> modifyProduct(@Valid @RequestBody ProductModifyRequest productModifyRequest){

        return productService.modifyProduct(productModifyRequest);

    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteProduct(@PathVariable Long id){

        return productService.deleteProduct(id);
    }



}
