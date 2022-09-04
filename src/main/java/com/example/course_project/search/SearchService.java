package com.example.course_project.search;

import com.example.course_project.search.models.CollectionSearchProduct;
import com.example.course_project.search.models.CommentSearchProduct;
import com.example.course_project.search.models.SearchProduct;
import com.example.course_project.search.repositories.CollectionSearchRepository;
import com.example.course_project.search.repositories.CommentSearchRepository;
import com.example.course_project.search.repositories.SearchRepository;
import com.example.course_project.user_collections.Comment;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.UserCollection;
import com.example.course_project.user_collections.services.UserCollectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class SearchService {

    private SearchRepository repository;
    private CollectionSearchRepository collectionSearchRepository;
    private UserCollectionService userCollectionService;
    private CommentSearchRepository commentSearchRepository;

    public void add(Item item){
       SearchProduct product = new SearchProduct();
       product.setData(item.prepareForSearch());
       product.setCollectionId(item.getCollection().getId().toString());
       product.setUserId(item.getCollection().getUser().getUserId().toString());
       product.setPk(item.getItemId().toString());
        repository.save(product);
    }

    public void remove(Item item){
        SearchProduct itemSearch = repository.findByPk(item.getItemId().toString()).get();
        repository.deleteById(itemSearch.getId());

    }


    public void addCollection(UserCollection collection){
        CollectionSearchProduct product1 = new CollectionSearchProduct();
        product1.setPk(collection.getId().toString());
        product1.setName(collection.getName().replace(" ","_"));
        product1.setUserid(collection.getUser().getUserId().toString());
        collectionSearchRepository.save(product1);
    }

    public void updateCollection(UserCollection collection){
        Optional<CollectionSearchProduct> product = collectionSearchRepository.findByPk(collection.getId().toString());
        if(product.isPresent()){
            CollectionSearchProduct pr = product.get();
            pr.setName(collection.getName().replace(" ","_"));
            collectionSearchRepository.save(pr);
        }
    }

    public void deleteCollection(Long id){
        Optional<CollectionSearchProduct> product = collectionSearchRepository.findByPk(String.valueOf(id));
        if(product.isPresent()){
            CollectionSearchProduct pr = product.get();
            collectionSearchRepository.delete(pr);
        }
    }

    public List<SearchProduct> findByUserIdAndData(String data, Long id){
        data = data.replace(" ", "_");
        Set<SearchProduct> pr = new HashSet<>(repository.findByUserIdAndDataContaining(String.valueOf(id),data));
        List<CollectionSearchProduct> pr1 = collectionSearchRepository.findByNameContainingAndUserid(data, String.valueOf(id));
        List<CommentSearchProduct> pr2 = commentSearchRepository.findByDataContainingAndUserid(data,String.valueOf(id));
        for(CommentSearchProduct p : pr2){
            SearchProduct product = new SearchProduct();
            product.setPk(p.getPk());
            pr.add(product);
        }
        if(pr1.size()>0) {
            UserCollection col = userCollectionService.findById(Long.valueOf(pr1.get(0).getPk())).get();
            if(col.getItems().size()>0){
                SearchProduct product = new SearchProduct();
                product.setPk(col.getItems().get(0).getItemId().toString());
                pr.add(product);
        }
        }
        return new LinkedList<>(pr);
    }
    public List<SearchProduct> findByCollectionAndData(String data, String id){
        data = data.replace(" ", "_");
        Set<SearchProduct> pr = new HashSet<>(repository.findByCollectionIdAndDataContaining(id,data));
        List<CommentSearchProduct> pr2 = commentSearchRepository.findByDataContainingAndCollectionid(data,id);
        for(CommentSearchProduct p : pr2){
            SearchProduct product = new SearchProduct();
            product.setPk(p.getPk());
            pr.add(product);
        }
        return repository.findByCollectionIdAndDataContaining(id, data);
    }

    public void update(Item item,String data){
        SearchProduct itemSearch = repository.findByPk(item.getItemId().toString()).get();
        itemSearch.setData(data.replace(" ","_"));
        repository.save(itemSearch);
    }
    public List<SearchProduct> findByData(String data){
        data = data.replace(" ", "_");
        Set<SearchProduct> pr = new HashSet<>(repository.findByDataContaining(data));
        List<CollectionSearchProduct> pr1 = collectionSearchRepository.findByNameContaining(data);
        List<CommentSearchProduct> pr2 = commentSearchRepository.findByDataContaining(data);

        for(CommentSearchProduct p : pr2){
            SearchProduct product = new SearchProduct();
            product.setPk(p.getPk());
            pr.add(product);
        }

        if(pr1.size()>0) {
            UserCollection col = userCollectionService.findById(Long.valueOf(pr1.get(0).getPk())).get();
            if(col.getItems().size()>0){
                SearchProduct product = new SearchProduct();
                product.setPk(col.getItems().get(0).getItemId().toString());
                pr.add(product);
            }
        }
        return new LinkedList<>(pr);
    }
    public Optional<SearchProduct> findByPk(String pk){
        return repository.findByPk(pk);
    }

    @Transactional
    public void addComment(Comment comment, Item item){
        CommentSearchProduct product = new CommentSearchProduct();
        product.setCollectionid(item.getCollection().getId().toString());
        product.setUserid(item.getCollection().getUser().getUserId().toString());
        product.setData(comment.getContentOfComment().replace(" ","_"));
        product.setPk(item.getItemId().toString());
        product.setCommentid(comment.getCommentId().toString());
        commentSearchRepository.save(product);
    }


    public void deleteComment(Comment comment){
        Optional<CommentSearchProduct> p = commentSearchRepository.findByCommentid(comment.getCommentId().toString());
        if(p.isPresent()){
            CommentSearchProduct product = p.get();
            commentSearchRepository.delete(product);
        }
    }
    public void deleteCommentsOfUser(Long id){
        for(CommentSearchProduct p :commentSearchRepository.findAllByUserid(String.valueOf(id)))
            commentSearchRepository.delete(p);

    }

}
