package com.example.course_project.userInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByUserId(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.isLocked = true where u.id = ?1")
    int lockUser(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.isLocked = false where u.id = ?1")
    int unlockUser(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.role = ?2 where u.id = ?1")
    int promoteUser(Long id, UserRole r);

    @Transactional
    @Modifying
    @Query("update User u set u.role = ?2 where u.id = ?1")
    int downgradeUser(Long id, UserRole r);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM item_user_like_map WHERE user_id = ?1")
    void prepareForDeleteUser(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM comment WHERE author_of_comment = ?1")
    void prepareForDeleteUserComments(String name);


}
