package com.ra.inventory_management.Controller;


import com.ra.inventory_management.model.dto.request.ProductRequest;
import com.ra.inventory_management.model.entity.Categories;
import com.ra.inventory_management.model.entity.ProductInfo;
import com.ra.inventory_management.service.CategoryService;
import com.ra.inventory_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/")
public class ProductController {
    @Value("${path-upload}")
    private String pathUpload;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product")
    public String productPage(Model model,
                              @RequestParam(defaultValue = "5", name = "limit") int limit,
                              @RequestParam(defaultValue = "0", name = "page") int page,
                              @RequestParam(defaultValue = "id", name = "sort") String sort,
                              @RequestParam(defaultValue = "asc", name = "order") String order,
                              @RequestParam(value = "nameSearch", required = false) String nameSearch
    ) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }

        if(nameSearch !=null && nameSearch.trim().isEmpty()){
            nameSearch =null;
        }

        Page<ProductInfo> products = productService.getAll(pageable,nameSearch);
        int currentPage = products.getNumber();
        model.addAttribute("products", products);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("nameSearch",nameSearch);
        return "/admin/product/product";
    }

    //  add product
    @GetMapping("/product/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("categories", categoryService.findAll());
        return "/admin/product/add-product";
    }

    @PostMapping("/product/add-product")
    public String saveProduct(@Valid @ModelAttribute("product") ProductRequest productRequest,
                              BindingResult bindingResult,
                              @RequestParam("imageProduct") MultipartFile file,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "/admin/product/add-product";
        }

        try {
            String fileName = file.getOriginalFilename();
            File uploadDir = new File(pathUpload);

            // Kiểm tra và tạo thư mục nếu nó không tồn tại
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destinationFile = new File(pathUpload + fileName);
            file.transferTo(destinationFile);
            // Lưu tên file vào database
            productRequest.setImage(fileName);

            productService.save(productRequest);
            return "redirect:/admin/product";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            model.addAttribute("err", e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            return "/admin/product/add-product";
        }
    }
    //  edit Category
    @GetMapping("/product/edit-product/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        List<Categories> categories = categoryService.getbyActiveFlag();
        model.addAttribute("categories", categories);
        ProductInfo product = productService.findById(id);
        model.addAttribute("product", product);
        return "/admin/product/edit-product";
    }

    @PostMapping("/product/edit-product")
    public String update(@ModelAttribute("product") ProductRequest product, @RequestParam("imageProduct") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(pathUpload + fileName));
            // lưu tên file vào database
            product.setImage(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productService.save(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/product/search")
    public String searchByName(@RequestParam("nameSearch") String keyword, Model model) {
        List<ProductInfo> products = productService.searchByName(keyword);
        model.addAttribute("products", products);
        return "/admin/product/product";
    }

    @GetMapping("/product/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/admin/product";
    }
}
