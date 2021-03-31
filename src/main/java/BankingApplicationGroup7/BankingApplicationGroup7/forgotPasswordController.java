package BankingApplicationGroup7.BankingApplicationGroup7;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class forgotPasswordController {
    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/forgot_password")
    public String forgotPWForm(Model model) {
        model.addAttribute("pageTitle", "Forgot Password");
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String forgotPWFormProcess(HttpServletRequest httpServletRequest, Model model) {

        String email = httpServletRequest.getParameter("email");
        String token = RandomString.make(50);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordURLLink = Util.getSiteURL(httpServletRequest) + "/reset_password?token=" +token;
            sendEmailURLLink(email, resetPasswordURLLink);
            model.addAttribute("message", "Reset password link has sent to your email.");

        }catch(UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Email Not Sent");
        }
        model.addAttribute("pageTitle", "Forgot Password");

        return "forgot_password_form";

    }

    private void sendEmailURLLink(String email, String resetPasswordURLLink) throws UnsupportedEncodingException, MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom("contact@test.com", "Test");
        mimeMessageHelper.setTo(email);
        String subject = "Link for reset your password";

        String messageContent = "<p>Click the link below to change your password:</p>"
                + "<p><b><a href=\"" + resetPasswordURLLink + "\">Reset Password</a><b></p>";

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(messageContent, true);

        javaMailSender.send(mimeMessage);
    }

    @GetMapping("/reset_password")
    public String resetPWForm(@Param(value = "token") String token, Model model) {

        User user = userService.get(token);
        if(user == null) {
            model.addAttribute("title", "Reset Password");
            model.addAttribute("message", "Invalid Access");
            return "message";
        }

        model.addAttribute("token", token);
        model.addAttribute("pageTitle", "Reset Password");

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String resetPasswordProcess(HttpServletRequest httpServletRequest, Model model) {

        String token = httpServletRequest.getParameter("token");
        String password = httpServletRequest.getParameter("password");

        User user = userService.get(token);

        if(user == null) {
            model.addAttribute("title", "Reset Password");
            model.addAttribute("pageTitle", "Reset Password");
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password");
        }
        return "message";
    }
}
