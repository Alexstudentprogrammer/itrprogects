package com.example.course_project.search;

import com.example.course_project.search.models.CollectionSearchProduct;
import com.example.course_project.search.models.SearchProduct;
import com.example.course_project.search.repositories.CollectionSearchRepository;
import com.example.course_project.search.repositories.SearchRepository;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.UserCollection;
import com.example.course_project.user_collections.services.UserCollectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SearchService {

    private SearchRepository repository;
    private CollectionSearchRepository collectionSearchRepository;
    private UserCollectionService userCollectionService;

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
        product1.setName(collection.getName());
        product1.setUserid(collection.getUser().getUserId().toString());
        collectionSearchRepository.save(product1);
    }

    public void updateCollection(UserCollection collection){
        Optional<CollectionSearchProduct> product = collectionSearchRepository.findByPk(collection.getId().toString());
        if(product.isPresent()){
            CollectionSearchProduct pr = product.get();
            pr.setName(collection.getName());
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
        List<SearchProduct> pr = repository.findByUserIdAndDataContaining(String.valueOf(id),data);
        List<CollectionSearchProduct> pr1 = collectionSearchRepository.findByNameContainingAndUserid(data, String.valueOf(id));
        if(pr1.size()>0) {
            UserCollection col = userCollectionService.findById(Long.valueOf(pr1.get(0).getPk())).get();
            if(col.getItems().size()>0){
                SearchProduct product = new SearchProduct();
                product.setPk(col.getItems().get(0).getItemId().toString());
                pr.add(product);
        }
        }
        return pr;
    }
    public List<SearchProduct> findByCollectionAndData(String data, String id){
        return repository.findByCollectionIdAndDataContaining(id, data);
    }

    public void update(Item item,String data){
        SearchProduct itemSearch = repository.findByPk(item.getItemId().toString()).get();
        itemSearch.setData(data);
        repository.save(itemSearch);
    }

    public List<SearchProduct> findByData(String data){
        List<SearchProduct> pr = repository.findByDataContaining(data);
        List<CollectionSearchProduct> pr1 = collectionSearchRepository.findByNameContaining(data);
        if(pr1.size()>0) {
            UserCollection col = userCollectionService.findById(Long.valueOf(pr1.get(0).getPk())).get();
            if(col.getItems().size()>0){
                SearchProduct product = new SearchProduct();
                product.setPk(col.getItems().get(0).getItemId().toString());
                pr.add(product);
            }
        }
        return pr;
    }
    public Optional<SearchProduct> findByPk(String pk){
        return repository.findByPk(pk);
    }
    public List<SearchProduct> getAll(){
        return (List<SearchProduct>) repository.findAll();
    }

}
