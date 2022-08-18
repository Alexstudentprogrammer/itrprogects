package com.example.course_project.userInfo;

import com.example.course_project.user_collections.*;
import com.example.course_project.user_collections.repositories.CommentRepository;
import com.example.course_project.user_collections.repositories.ItemRepository;
import com.example.course_project.user_collections.repositories.TagRepository;
import com.example.course_project.user_collections.repositories.UserCollectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserCollectionRepository userCollectionRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;
    private final TagRepository tagRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User was not found"));
    }

    public int signUp(User user) {

        boolean exists = repository
                .findUserByUsername(user.getUsername())
                .isPresent();

        if (exists) {
            return 0;
        } else {

            String encodedPass = encoder.encode(user.getPassword());
            user.setPassword(encodedPass);
            repository.save(user);
            return 1;
        }
    }


    public List<User> getAll(){
        return repository.findAll();
    }

    public Optional<User> findByUsername(String mail){
        return repository.findUserByUsername(mail);
    }

    public void addCollection(long userId, String name, String info, Theme theme){
        User user = repository.findUserByUserId(userId).get();
        UserCollection col = new UserCollection();
        col.setTheme(theme);
        col.setName(name);
        col.setInformation(info);
        col.setUser(user);
        user.getUserCollections().add(col);
        repository.save(user);
    }
    public void removeCollection(long colId){
        UserCollection col = userCollectionRepository.findUserCollectionById(colId).get();
        for(Item item : col.getItems()) {
            item.prepareForDelete();
            itemRepository.save(item);
        }
       userCollectionRepository.delete(col);
    }
    public void addItem(long colId, Item item, Set<String> tagsSet){
        UserCollection col = userCollectionRepository.findUserCollectionById(colId).get();
        item.setTags(prepareTags(tagsSet));
        col.getItems().add(item);
        userCollectionRepository.save(col);
    }
    public void removeItem(long itemId){
        Item item = itemRepository.findById(itemId).get();
        item.prepareForDelete();
        itemRepository.save(item);
        itemRepository.delete(item);
    }
    public void updateItem(Long id, Set<String> tagsSet, String name){
      Item item = itemRepository.findById(id).get();
      item.setTags(prepareTags(tagsSet));
      item.setName(name);
      itemRepository.save(item);
    }

    public void addComment(String author, String content, long itemId){
        Item item = itemRepository.findById(itemId).get();
        Comment comment = new Comment();
        comment.setContentOfComment(content);
        comment.setAuthorOfComment(author);
        item.getComments().add(comment);
        itemRepository.save(item);
    }
    public void removeComment(long commentId){
        Comment com = commentRepository.findById(commentId).get();
        commentRepository.delete(com);
    }

    private Set<Tag> prepareTags(Set<String> tags){
        Set<Tag> set = new HashSet<>();
        for(String t : tags){
            if(!tagRepository.findTagByName(t).isPresent()) {
                Tag tag = new Tag();
                tag.setName(t);
                set.add(tagRepository.save(tag));
            }else{
                set.add(tagRepository.findTagByName(t).get());
            }
        }
        return set;
    }
}
