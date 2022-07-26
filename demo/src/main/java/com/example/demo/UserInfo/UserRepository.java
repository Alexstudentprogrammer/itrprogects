package com.example.demo.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(Long id);
    @Transactional
    @Modifying
    @Query("update User u set u.locked = true where u.id = ?1")
    int lockUser(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.locked = false where u.id = ?1")
    int unlockUser(Long id);

    @Transactional
    @Modifying
    @Query("delete User u where u.id = ?1")
    int deleteUser(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.lastLogin = :date where u.id = :id")
    int updateLogin(@Param("date") String date, @Param("id") Long id);


}
