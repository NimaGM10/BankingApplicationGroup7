package BankingApplicationGroup7.BankingApplicationGroup7;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.email = ?1")
    public User findByEmail(String email);

    public User findByResetPasswordToken(String token);

}