package com.example.course_project.userInfo;

import com.example.course_project.search.SearchService;
import com.example.course_project.search.models.SearchProduct;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

       Optional<User> u = repository
                .findUserByUsername(user.getUsername());

        if (u.isPresent() && u.get().getProvider() != user.getProvider()) {
            return 2;
        } else if(u.isPresent()){

            return 0;
        }else{
            if(user.getProvider() == null){
                user.setProvider(Provider.LOCAL);
            }
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


    public void addCollection(User user, String name, String info, Theme theme, String html,String data, String image,String id){
        UserCollection col = new UserCollection();
        col.setTheme(theme);
        col.setName(name);
        col.setInformation(info);
        col.setUser(user);
        col.setInfoHtml(html);
        col.setItems(new LinkedList<>());
        col.setData(data);
        col.setImage(image);
        col.setPublicId(id);
        col.setSize(0);
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
            for(Comment c : item.getComments()){
                searchService.deleteComment(c);
            }
        }
        userCollectionRepository.delete(col);
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
        col.setSize(col.getSize()+1);
        item.setTags(prepareTags(tagsSet));
        col.getItems().add(item);
        Item searchItem = itemRepository.save(item);
        userCollectionRepository.save(col);
        searchService.add(searchItem);

    }
    public void updateItem(Long id, Set<String> tagsSet, String name,String data){
      Item item = itemRepository.findById(id).get();
      item.setTags(prepareTags(tagsSet));
      item.setData(data);
      item.setName(name);
      searchService.update(item,item.prepareForSearch());
      itemRepository.save(item);
    }
    public void deleteItem(Long colId, Long itemId){
        UserCollection col = userCollectionRepository.findUserCollectionById(colId).get();
        Item item = itemRepository.findItemByItemId(itemId).get();
        col.setSize(col.getSize()-1);
        col.getItems().remove(item);
        searchService.remove(item);
        item.prepareForDelete();
        itemRepository.delete(item);
        userCollectionRepository.save(col);
    }
    @Transactional
    public Long addComment(String author, String content, long itemId){
        Item item = itemRepository.findById(itemId).get();
        Comment comment = new Comment();
        comment.setContentOfComment(content);
        comment.setAuthorOfComment(author);
        item.getComments().add(comment);
        comment = commentRepository.save(comment);
        itemRepository.save(item);
        searchService.addComment(comment,item);
        return comment.getCommentId();

    }
    public void removeComment(long commentId, long itemId){
        Comment com = commentRepository.findById(commentId).get();
        Item item = itemRepository.findItemByItemId(itemId).get();
        searchService.deleteComment(com);
        item.getComments().remove(com);
        itemRepository.save(item);
        commentRepository.delete(com);
    }

        @Transactional
        public void deleteUser(Long id){
        User user = repository.findUserByUserId(id).get();

        for(UserCollection col : user.getUserCollections()){
            col.prepareForDelete();
            searchService.deleteCollection(col.getId());
            for(Item item : col.getItems()){
                searchService.remove(item);
                item.prepareForDelete();
                itemRepository.save(item);
                itemRepository.delete(item);
                for(Comment c :item.getComments())
                    searchService.deleteComment(c);
            }
            userCollectionRepository.delete(col);
        }
//        answered Oct 3, 2015 at 21:29
//        Roman
        for(Iterator<UserCollection> iterator = user.getUserCollections().iterator(); iterator.hasNext(); ) {
            UserCollection c = iterator.next();
            if(c != null)
                iterator.remove();
        }
        searchService.deleteCommentsOfUser(user.getUserId());
        repository.prepareForDeleteUserComments(user.getUsername());
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

    public void processCostumUser(String name, String email, HttpServletResponse response) throws IOException {
        User user = new User();
        user.setRole(UserRole.USER);
        user.setFName(name);
        user.setUsername(email);
        user.setProvider(Provider.GITHUB);
        user.setPassword(alphaNumericString(30));
        user.setLang("en");
        user.setSkin("light");
        if(signUp(user)==2){
            response.sendRedirect("/logout");
        }
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
//    answered Sep 11, 2017 at 13:32
//    user avatar
//    Merch0
    public static String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
