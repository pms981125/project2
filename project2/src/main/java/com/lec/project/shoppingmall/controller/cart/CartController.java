package com.lec.project.shoppingmall.controller.cart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.service.cart.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Log4j2
public class CartController {
    
    private final CartService cartService;
    
    @GetMapping("/list")
    public void list(
        PageRequestDTO pageRequestDTO,
        Model model
    ) {
        PageResponseDTO<CartListDTO> responseDTO = cartService.list(pageRequestDTO);
        int totalPrice = cartService.getTotalPrice();
        
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("totalPrice", totalPrice);
    }
    
    @GetMapping("/read")
    public void read(@RequestParam("id") Long id, Model model) {
        CartListDTO dto = cartService.readOne(id);
        model.addAttribute("dto", dto);
    }
    
    @GetMapping("/modify")
    public void modify(@RequestParam("id") Long id, Model model) {
        CartListDTO dto = cartService.readOne(id);
        model.addAttribute("dto", dto);
    }
    
    @PostMapping("/modify")
    public String modify(
        @RequestParam Long id,
        @RequestParam int count,
        RedirectAttributes redirectAttributes
    ) {
        try {
            cartService.modify(id, count);
            redirectAttributes.addFlashAttribute("result", "수정완료");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/modify?id=" + id;
        }
        return "redirect:/cart/read?id=" + id;
    }
    
    @PostMapping("/remove")
    public String remove(@RequestParam("id") Long id) {
        cartService.remove(id);
        return "redirect:/cart/list";
    }
}