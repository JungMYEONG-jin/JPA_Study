package com.ecommerce.controller;

import com.ecommerce.entity.user.auth.Principal;
import com.ecommerce.entity.user.dto.UserResDto;
import com.ecommerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;


    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal Principal principal,
                       @PageableDefault(size=60, sort="id", direction = Sort.Direction.ASC) Pageable pageable){
        if (principal != null){
            model.addAttribute("user", UserResDto.of(principal.getUser()));
        }
        model.addAttribute("productPage", productService.getAllProductPage(pageable));
        return "home";
    }
}
