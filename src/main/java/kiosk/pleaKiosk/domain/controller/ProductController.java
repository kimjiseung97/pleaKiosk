package kiosk.pleaKiosk.domain.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kiosk.pleaKiosk.domain.dto.request.ProductModifyRequest;
import kiosk.pleaKiosk.domain.dto.request.ProductSaveRequest;
import kiosk.pleaKiosk.domain.dto.response.ErrorResponse;
import kiosk.pleaKiosk.domain.dto.response.commonResponse;
import kiosk.pleaKiosk.domain.entity.Product;
import kiosk.pleaKiosk.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/kiosk/product")
@RequiredArgsConstructor
@Tag(name = "PRODUCT", description = "상품 API")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/register")
    @Operation(description = "상품등록 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품등록 성공"), @ApiResponse(responseCode = "400", description = "상품등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<Product> saveProduct(@Valid @RequestBody ProductSaveRequest productSaveRequest) {
        return productService.registerProduct(productSaveRequest);
    }

    @GetMapping("/getall-product")
    @Operation(description = "상품목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품조회 성공"), @ApiResponse(responseCode = "400", description = "상품조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<Page<Product>> allProducts(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String sort){
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("페이지는 음수나 0페이지로 반환할 수 없습니다");
        }
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort));
        return productService.getAllProduct(pageable);
    }

    @RequestMapping(value = "/modify",method = {RequestMethod.PUT ,RequestMethod.PATCH})
    @Operation(description = "상품수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품수정 성공"), @ApiResponse(responseCode = "400", description = "상품수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse<Product> modifyProduct(@Valid @RequestBody ProductModifyRequest productModifyRequest){
        return productService.modifyProduct(productModifyRequest);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = "상품삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품삭제 성공"), @ApiResponse(responseCode = "400", description = "상품삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) })
    public commonResponse deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
    }

}
