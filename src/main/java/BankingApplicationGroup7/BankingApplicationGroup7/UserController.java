package BankingApplicationGroup7.BankingApplicationGroup7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class UserController {
    @Autowired
   private UserRepository userRepository;



     @GetMapping("/register")
    public String registerForm(Model model) {
       model.addAttribute("user", new User());

       return "register_form";
   }

   @PostMapping("/user_register")
    public String userRegister(User user) {
      BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
      String encodedPw = bCryptPasswordEncoder.encode(user.getPassword());
      user.setPassword(encodedPw);
      userRepository.save(user);

       return "success_register";
   }

   @GetMapping("/main")
   public String mainPage() {
      return "main";
   }


   @GetMapping("/login")
   public String loginPage() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
           return "login";
       }
       return "redirect:/";
   }

}
