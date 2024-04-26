package kz.myproject.techboot.springsecurity.repository;

import kz.myproject.techboot.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface UserRepostitory extends JpaRepository<User, Long> {

    User findByEmail(String email);




}
