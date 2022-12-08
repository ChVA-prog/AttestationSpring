package com.example.attestationspring.controllers;

import com.example.attestationspring.models.Image;
import com.example.attestationspring.models.Order;
import com.example.attestationspring.models.Person;
import com.example.attestationspring.models.Product;
import com.example.attestationspring.repositories.CategoryRepository;
import com.example.attestationspring.repositories.OrderRepository;
import com.example.attestationspring.repositories.PersonRepository;
import com.example.attestationspring.security.PersonDetails;
import com.example.attestationspring.services.OrderService;
import com.example.attestationspring.services.ProductService;
import com.example.attestationspring.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${upload.path}")
    private String uploadPath;
    private final ProductValidator productValidator;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PersonRepository personRepository;

    @Autowired
    public AdminController(ProductValidator productValidator, ProductService productService, CategoryRepository categoryRepository, OrderRepository orderRepository, OrderService orderService, PersonRepository personRepository) {
        this.productValidator = productValidator;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.personRepository = personRepository;
    }

    @GetMapping()
    public String admin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        String role = personDetails.getPerson().getRole();

        if(role.equals("ROLE_USER")){
            return "redirect:/index";
        }
        model.addAttribute("products", productService.getAllProduct());
        return "admin/admin";
    }

    @GetMapping("/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one") MultipartFile file_one, @RequestParam("file_two") MultipartFile file_two, @RequestParam("file_three") MultipartFile file_three, @RequestParam("file_four") MultipartFile file_four, @RequestParam("file_five") MultipartFile file_five) throws IOException {

        productValidator.validate(product, bindingResult);
        if(bindingResult.hasErrors()){
            return "product/addProduct";
        }
        if(file_one != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();

            file_one.transferTo(new File(uploadPath + "/" + resultFileName));

            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);

            product.addImageProduct(image);
        }

        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();

            file_two.transferTo(new File(uploadPath + "/" + resultFileName));

            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);

            product.addImageProduct(image);
        }

        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();

            file_three.transferTo(new File(uploadPath + "/" + resultFileName));

            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);

            product.addImageProduct(image);
        }

        if(file_four != null){
            File uploadDir = new File(uploadPath);

            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();

            file_four.transferTo(new File(uploadPath + "/" + resultFileName));

            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);

            product.addImageProduct(image);
        }

        if(file_five != null){
            File uploadDir = new File(uploadPath);

            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));

            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);

            product.addImageProduct(image);
        }

        productService.saveProduct(product);
        return "redirect:/admin";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);

        return "redirect:/admin";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("editProduct", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());

        return "product/editProduct";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("editProduct") Product product, @PathVariable("id") int id){
        productService.updateProduct(id, product);

        return "redirect:/admin";
    }

    @GetMapping("/orders")
    public String ordersList (Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);

        return "admin/orders";
    }

    @PostMapping("/orders/search")
    public String productSearch(@RequestParam("search") String search, Model model)
    {
        model.addAttribute("search_order", orderRepository.findByNumberContainingIgnoreCase(search));
        model.addAttribute("value_search", search);

        return "admin/orders";
    }

    @GetMapping("/order/edit/{id}")
    public String editOrder(@PathVariable("id") int id, Model model){
        model.addAttribute("editOrder", orderService.getOrderId(id));

        return "admin/editOrder";
    }

    @PostMapping("/order/edit/{id}")
    public String editOrder(@ModelAttribute("editOrder") Order order, @PathVariable("id") int id){
        orderService.updateOrder(id, order);

        return "redirect:/admin/orders";
    }

    @GetMapping("/users")
    public String usersList (Model model)
    {
        List<Person> usersList = personRepository.findAll();
        model.addAttribute("users", usersList);

        return "admin/users";
    }

    @GetMapping("/users/editRoleUser/{id}")
    public String editRoleUser(@PathVariable("id") int id){
        personRepository.SetRoleUser(id);

        return "redirect:/admin/users";
    }

    @GetMapping("/users/editRoleAdmin/{id}")
    public String editRoleAdmin(@PathVariable("id") int id){
        personRepository.SetRoleAdmin(id);

        return "redirect:/admin/users";
    }

    @GetMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable("id") int id){
        orderService.deleteOrder(id);

        return "redirect:/admin/orders";
    }

}
