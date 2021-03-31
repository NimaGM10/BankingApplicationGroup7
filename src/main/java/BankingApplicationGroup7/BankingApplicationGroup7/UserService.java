package BankingApplicationGroup7.BankingApplicationGroup7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return new UserDetail(user);
    }

    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {

        User user = userRepository.findByEmail(email);

        if(user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }else {
            throw new UserNotFoundException("Could not find any user with email " + email);
        }

    }
    public User get(String resetPWToken) {
        return userRepository.findByResetPasswordToken(resetPWToken);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPW = encoder.encode(newPassword);

        user.setPassword(encodedPW);
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }

}
