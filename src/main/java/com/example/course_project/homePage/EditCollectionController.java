package com.example.course_project.homePage;

import com.example.course_project.search.models.CollectionSearchProduct;
import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.user_collections.Item;
import com.example.course_project.user_collections.Tag;
import com.example.course_project.user_collections.Theme;
import com.example.course_project.user_collections.UserCollection;
import com.example.course_project.user_collections.services.ItemService;
import com.example.course_project.user_collections.services.TagService;
import com.example.course_project.user_collections.services.UserCollectionService;
import lombok.AllArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.commonmark.parser.Parser;

import java.util.*;

@Controller
@RequestMapping("/mycollections/")
@AllArgsConstructor
public class EditCollectionController {
    private final UserCollectionService userCollectionService;
    private final UserService userService;
    private final TagService tagService;
    private final ItemService itemService;

    @GetMapping
    public String getAllCollections(Model model){
        User user = getCurrentUser();
        List<UserCollection> col;
        if(user.getRole() == UserRole.ADMIN){
            col = userCollectionService.findAll();
        }else {
            col = userCollectionService.findAllUserCollection(user.getUserId());
        }
        model.addAttribute("cols",col);
        model.addAttribute("user_id",user.getUserId());
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "edit_collections";
    }

    @GetMapping("/add")
    public String addCollectionsPage(Model model){
        User user = getCurrentUser();
        model.addAttribute("user_id",user.getUserId());
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "addCollection";
        }


    @PostMapping("/add")
    public String createCollection(@ModelAttribute AddCollectionRequest request, @RequestParam("image") MultipartFile image){
        User user = getCurrentUser();
        userService.addCollection(user,
                request.getName(),
                request.getDesc(),
                Theme.valueOf(request.getTheme().toUpperCase()),
                markdownToHTML(request.getDesc()),
                request.getData(),
                "will be image url");
        return "redirect:http://localhost:8080/mycollections/";
    }
    @GetMapping("/edit/{id}")
    public String editCollection(@PathVariable("id") Long id, Model model){
    UserCollection col = userCollectionService.findById(id).get();//no value present ADMIN
        User user = getCurrentUser();
    model.addAttribute("col",col);
    model.addAttribute("items", col.getItems());
    model.addAttribute("skin",user.getSkin());
    model.addAttribute("lang",user.getLang());
    return "edit_my_collection";
    }

    @GetMapping("/edit/{id}/sort")
    public String editCollectionSortItems(@PathVariable("id") Long id, Model model){
        UserCollection col = userCollectionService.findById(id).get();//no value present ADMIN
        User user = getCurrentUser();
        model.addAttribute("col",col);
        model.addAttribute("items", itemService.getSortedOfCollection(col.getId()));
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "edit_my_collection";
    }
    @GetMapping("/edit/{id}/{tag}")
    public String editCollectionItemsOfTag(@PathVariable("id") Long id,@PathVariable("tag") String tag, Model model){
        UserCollection col = userCollectionService.findById(id).get();//no value present ADMIN
        Tag t = tagService.findByName(tag);
        User user = getCurrentUser();
        model.addAttribute("col",col);
        model.addAttribute("items", itemService.filterByTagOfCollection(Arrays.asList(t),id));
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "edit_my_collection";
    }
    @PostMapping("/edit")
    public String saveCollectionChanges(@ModelAttribute AddCollectionRequest request, @RequestParam("id") Long id){
        User user = getCurrentUser();
        UserCollection col = userCollectionService.findById(id).get();
        if(user.getUserId() == col.getUser().getUserId() || user.getRole() == UserRole.ADMIN) {
//            col.setName(request.getName());
//            col.setTheme(Theme.valueOf(request.getTheme().toUpperCase()));
//            col.setInformation(request.getDesc());
//            col.setData(request.getData());
//            col.setInfoHtml(markdownToHTML(request.getDesc()));
//            userCollectionService.save(col);
              userService.updateCollection(col,
                      request.getName(),
                      request.getDesc(),
                      Theme.valueOf(request.getTheme().toUpperCase()),
                      markdownToHTML(request.getDesc()),
                      request.getData(),
                      "image will be here");
            return "redirect:http://localhost:8080/mycollections/";
        }
        return "";
    }

    @PostMapping("/add/item/save")
    public String saveItemChanges(@ModelAttribute AddItemRequest request){
        User user = getCurrentUser();
        UserCollection col = userCollectionService.findById(request.getCollectionId()).get();
        if(user.getUserId() == col.getUser().getUserId() || user.getRole() == UserRole.ADMIN) {
            Item item = new Item();
            item.setComments(new LinkedList<>());
            item.setName(request.getName());
            item.setLiked(new LinkedHashSet<>());
            item.setData(request.getData());
            item.setCollection(col);
            item.setData(request.getData());
            userService.addItem(col.getId(),item,new HashSet<>(Arrays.asList(request.getTags().split(","))));
            return "redirect:http://localhost:8080/mycollections/";
        }
        return "";

    }
    @GetMapping("/add/item/{id}")
    public String createItem(@PathVariable("id") Long id, Model model){
        User user = getCurrentUser();
        model.addAttribute("col_data",userCollectionService.findById(id).get().getData());
        model.addAttribute("col_id",id);
        model.addAttribute("tags",tagService.findAll());
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "add_item";
    }

    @GetMapping("edit/item/{id}")
    public String editItem(@PathVariable("id") Long id, Model model){
        Item item = itemService.findItemById(id).get();
        User user = getCurrentUser();
        model.addAttribute("item_id",id);
        model.addAttribute("item_name",item.getName());
        model.addAttribute("tags",item.getTags());
        model.addAttribute("item_data",item.getData());
        System.out.println(item.getData());
        model.addAttribute("col_data",userCollectionService.findById(item.getCollection().getId()).get().getData());
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "edit_my_item";
    }

    @PostMapping("edit/item/save")
    public String editItemSave(@ModelAttribute AddItemRequest request, @RequestParam Long id){
        Item item = itemService.findItemById(id).get();
        if(item.getCollection().getUser().getUserId() == getCurrentUser().getUserId() || getCurrentUser().getRole() == UserRole.ADMIN) {
            userService.updateItem(id, new HashSet<>(Arrays.asList(request.getTags().split(","))), request.getName(), request.getData());
            return "redirect:http://localhost:8080/mycollections/";
        }
        return "";
    }
    @PostMapping("edit/item/delete/")
    public String deleteItem(@RequestParam("id") Long id){
        User user = getCurrentUser();
        Item item = itemService.findItemById(id).get();
        if(item.getCollection().getUser().getUserId() == user.getUserId() || user.getRole() == UserRole.ADMIN){
           userService.deleteItem(item.getCollection().getId(), item.getItemId());
        }
        return "redirect:http://localhost:8080/mycollections/";
    }

    @PostMapping("edit/collection/delete/")
    public String deleteCollection(@RequestParam("id") Long id){
        User user = getCurrentUser();
        UserCollection col = userCollectionService.findById(id).get();
        if(col.getUser().getUserId() == user.getUserId() || user.getRole() == UserRole.ADMIN){
            userService.removeCollection(col.getId());
        }
        return "redirect:http://localhost:8080/mycollections/";
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).get();
        return user;
    }

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();

        return renderer.render(document);
    }

}















