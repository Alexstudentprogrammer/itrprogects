package com.example.demo.UserInfo;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserByEmail(username).orElseThrow(()->
            new UsernameNotFoundException("User was not found"));
    }

    public int signUp(User user){

        boolean exists = repository
                .findUserByEmail(user.getEmail())
                .isPresent();

        if(exists){
            return 0;
        }else{

           String encodedPass = encoder.encode(user.getPassword());
           user.setPassword(encodedPass);
           repository.save(user);
           return 1;
        }
    }

    public void updateLogin(Long id){
        String registerDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        repository.updateLogin(registerDate, id);
    }

    public void delete(Long id){
        repository.deleteUser(id);
    }
    public void lockUser(Long id){
        repository.lockUser(id);
    }
    public void unlockUser(Long id){
        repository.unlockUser(id);
    }

    public List<User> getAll(){
    return repository.findAll();
    }

    public Optional<User> findByEmail(String mail){
        return repository.findUserByEmail(mail);
    }
}
