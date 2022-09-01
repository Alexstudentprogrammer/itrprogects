package com.example.course_project.userInfo;

import com.example.course_project.search.SearchService;
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

import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserCollectionRepository userCollectionRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;
    private final TagRepository tagRepository;
    private final SearchService searchService;

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
    public User save(User user){
         return repository.save(user);
    }

    public Optional<User> findByUsername(String mail){
        return repository.findUserByUsername(mail);
    }

    public void addCollection(User user, String name, String info, Theme theme, String html,String data, String image){
        UserCollection col = new UserCollection();
        col.setTheme(theme);
        col.setName(name);
        col.setInformation(info);
        col.setUser(user);
        col.setInfoHtml(html);
        col.setItems(new LinkedList<>());
        col.setData(data);
        col.setImage(image);
        user.getUserCollections().add(col);
        UserCollection collection = userCollectionRepository.save(col);
        searchService.addCollection(collection);
        repository.save(user);
    }
    public void removeCollection(long colId){
        UserCollection col = userCollectionRepository.findUserCollectionById(colId).get();
        searchService.deleteCollection(colId);
        for(Item item : col.getItems()) {
            item.prepareForDelete();
            itemRepository.save(item);
            searchService.remove(item);
        }
        User user = repository.findUserByUserId(col.getUser().getUserId()).get();
        user.getUserCollections().remove(col);
        repository.save(user);
    }

    public void updateCollection(UserCollection col, String name, String info, Theme theme, String html,String data, String image){
        col.setTheme(theme);
        col.setName(name);
        col.setInformation(info);
        col.setInfoHtml(html);
        col.setData(data);
        col.setImage(image);
        UserCollection collection = userCollectionRepository.save(col);
        searchService.updateCollection(collection);
    }
    public void addItem(long colId, Item item, Set<String> tagsSet){
        UserCollection col = userCollectionRepository.findUserCollectionById(colId).get();
        item.setTags(prepareTags(tagsSet));
        col.getItems().add(item);
        Item searchItem = itemRepository.save(item);//TODO not forget to save item first than user
        userCollectionRepository.save(col);
        searchService.add(searchItem);

    }
    public void updateItem(Long id, Set<String> tagsSet, String name,String data){
      Item item = itemRepository.findById(id).get();
      item.setTags(prepareTags(tagsSet));
      item.setData(data);
      item.setName(name);
      itemRepository.save(item);
    }
    public void deleteItem(Long colId, Long itemId){
        UserCollection col = userCollectionRepository.findUserCollectionById(colId).get();
        Item item = itemRepository.findItemByItemId(itemId).get();
        col.getItems().remove(item);
        searchService.remove(item);
        item.prepareForDelete();
        itemRepository.delete(item);
        userCollectionRepository.save(col);
    }

    public Long addComment(String author, String content, long itemId){

        Item item = itemRepository.findById(itemId).get();
        Comment comment = new Comment();
        comment.setContentOfComment(content);
        comment.setAuthorOfComment(author);
        item.getComments().add(comment);
        comment = commentRepository.save(comment);
        searchService.update(item, item.prepareForSearch());
        itemRepository.save(item);
        return comment.getCommentId();

    }
    public void removeComment(long commentId, long itemId){
        Comment com = commentRepository.findById(commentId).get();
        Item item = itemRepository.findItemByItemId(itemId).get();
        item.getComments().remove(com);
        searchService.update(item,item.prepareForSearch());
        itemRepository.save(item);
    }


        public void deleteUser(Long id){//TODO Handle deletion of comment (done)
        User user = repository.findUserByUserId(id).get();
        for(UserCollection col : user.getUserCollections()){
            searchService.deleteCollection(col.getId());
            for(Item item : col.getItems()){
                searchService.remove(item);
            }
        }
//        answered Oct 3, 2015 at 21:29
//        Roman
        for(Iterator<UserCollection> iterator = user.getUserCollections().iterator(); iterator.hasNext(); ) {
            UserCollection c = iterator.next();
            if(c != null)
                iterator.remove();
        }
        repository.save(user);
        repository.prepareForDeleteUser(user.getUserId());
        repository.delete(user);
    }
    public void promoteUser(Long id){
        repository.promoteUser(id,UserRole.ADMIN);
    }

    public void downgradeUser(Long id){
        repository.downgradeUser(id,UserRole.USER);
    }
    public void lockUser(Long id){
        repository.lockUser(id);
    }
    public void unlockUser(Long id){
        repository.unlockUser(id);
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
