package com.example.course_project.homePage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.userInfo.costumAuth.CostumClient;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.commonmark.parser.Parser;


import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/mycollections/")
@AllArgsConstructor
public class EditCollectionController {
    private final UserCollectionService userCollectionService;
    private final UserService userService;
    private final TagService tagService;
    private final ItemService itemService;
    private final Cloudinary cloudinary = getCloudinary();

    @GetMapping
    public String getAllCollections(Model model,@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
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
    public String addCollectionsPage(Model model, @AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
        model.addAttribute("user_id",user.getUserId());
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "addCollection";
        }


    @PostMapping("/add")
    public String createCollection(@ModelAttribute AddCollectionRequest request, @RequestParam("image") MultipartFile multipartFile,@AuthenticationPrincipal OAuth2User oAuth2User) throws IOException {

        Pair p = new Pair();
        p.id="";
        p.url = "";
        User user = getCurrentUser(oAuth2User);
        System.out.println(user.toString());
        if(!multipartFile.isEmpty())
        p = upload(multipartFile);
        userService.addCollection(user,
                request.getName(),
                request.getDesc(),
                Theme.valueOf(request.getTheme().toUpperCase()),
                markdownToHTML(request.getDesc()),
                request.getData(),
                p.url,
                p.id);
        return "redirect:https://itrcourseproject.herokuapp.com/mycollections/";
    }
    @GetMapping("/edit/{id}")
    public String editCollection(@PathVariable("id") Long id, Model model,@AuthenticationPrincipal OAuth2User oAuth2User){
    UserCollection col = userCollectionService.findById(id).get();//no value present ADMIN
        User user = getCurrentUser(oAuth2User);
    model.addAttribute("col",col);
    model.addAttribute("items", col.getItems());
    model.addAttribute("skin",user.getSkin());
    model.addAttribute("lang",user.getLang());
    return "edit_my_collection";
    }

    @GetMapping("/edit/{id}/sort")
    public String editCollectionSortItems(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal OAuth2User oAuth2User){
        UserCollection col = userCollectionService.findById(id).get();//no value present ADMIN
        User user = getCurrentUser(oAuth2User);
        model.addAttribute("col",col);
        model.addAttribute("items", itemService.getSortedOfCollection(col.getId()));
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "edit_my_collection";
    }
    @GetMapping("/edit/{id}/{tag}")
    public String editCollectionItemsOfTag(@PathVariable("id") Long id,@PathVariable("tag") String tag, Model model,@AuthenticationPrincipal OAuth2User oAuth2User){
        UserCollection col = userCollectionService.findById(id).get();//no value present ADMIN
        Tag t = tagService.findByName(tag);
        User user = getCurrentUser(oAuth2User);
        model.addAttribute("col",col);
        model.addAttribute("items", itemService.filterByTagOfCollection(Arrays.asList(t),id));
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "edit_my_collection";
    }
    @PostMapping("/edit")
    public String saveCollectionChanges(@ModelAttribute AddCollectionRequest request, @RequestParam("id") Long id, @RequestParam("image") MultipartFile file,@AuthenticationPrincipal OAuth2User oAuth2User) throws IOException {
        User user = getCurrentUser(oAuth2User);
        UserCollection col = userCollectionService.findById(id).get();
        if(user.getUserId() == col.getUser().getUserId() || user.getRole() == UserRole.ADMIN) {;
              String url = col.getImage();
              if(!file.isEmpty()){
                  if(!col.getPublicId().equals(""))
                  cloudinary.uploader().destroy(col.getPublicId(),ObjectUtils.emptyMap());
                  Pair p = upload(file);
                  col.setPublicId(p.id);
                  col.setImage(p.url);
                  url = p.url;
              }
              if(request.getDelete() != null && request.getDelete() && !col.getPublicId().equals("")){
                  cloudinary.uploader().destroy(col.getPublicId(),ObjectUtils.emptyMap());
              }
              userService.updateCollection(col,
                      request.getName(),
                      request.getDesc(),
                      Theme.valueOf(request.getTheme().toUpperCase()),
                      markdownToHTML(request.getDesc()),
                      request.getData(),
                      url);
            return "redirect:https://itrcourseproject.herokuapp.com/mycollections/";
        }
        return "";
    }

    @PostMapping("/add/item/save")
    public String saveItemChanges(@ModelAttribute AddItemRequest request,@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
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
            return "redirect:https://itrcourseproject.herokuapp.com/mycollections/";
        }
        return "";

    }
    @GetMapping("/add/item/{id}")
    public String createItem(@PathVariable("id") Long id, Model model,@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
        model.addAttribute("col_data",userCollectionService.findById(id).get().getData());
        model.addAttribute("col_id",id);
        model.addAttribute("tags",tagService.findAll());
        model.addAttribute("skin",user.getSkin());
        model.addAttribute("lang",user.getLang());
        return "add_item";
    }

    @GetMapping("edit/item/{id}")
    public String editItem(@PathVariable("id") Long id, Model model,@AuthenticationPrincipal OAuth2User oAuth2User){
        Item item = itemService.findItemById(id).get();
        User user = getCurrentUser(oAuth2User);
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
    public String editItemSave(@ModelAttribute AddItemRequest request, @RequestParam Long id,@AuthenticationPrincipal OAuth2User oAuth2User){
        Item item = itemService.findItemById(id).get();
        if(item.getCollection().getUser().getUserId() == getCurrentUser(oAuth2User).getUserId() || getCurrentUser(oAuth2User).getRole() == UserRole.ADMIN) {
            userService.updateItem(id, new HashSet<>(Arrays.asList(request.getTags().split(","))), request.getName(), request.getData());
            return "redirect:https://itrcourseproject.herokuapp.com/mycollections/";
        }
        return "";
    }
    @PostMapping("edit/item/delete/")
    public String deleteItem(@RequestParam("id") Long id,@AuthenticationPrincipal OAuth2User oAuth2User){
        User user = getCurrentUser(oAuth2User);
        Item item = itemService.findItemById(id).get();
        if(item.getCollection().getUser().getUserId() == user.getUserId() || user.getRole() == UserRole.ADMIN){
           userService.deleteItem(item.getCollection().getId(), item.getItemId());
        }
        return "redirect:https://itrcourseproject.herokuapp.com/mycollections/";
    }

    @PostMapping("edit/collection/delete/")
    public String deleteCollection(@RequestParam("id") Long id,@AuthenticationPrincipal OAuth2User oAuth2User) throws IOException {
        User user = getCurrentUser(oAuth2User);
        UserCollection col = userCollectionService.findById(id).get();
        if(col.getUser().getUserId() == user.getUserId() || user.getRole() == UserRole.ADMIN){
            userService.removeCollection(col.getId());

            if(!col.getPublicId().equals(""))
            cloudinary.uploader().destroy(col.getPublicId(),ObjectUtils.emptyMap());
        }
        return "redirect:https://itrcourseproject.herokuapp.com/mycollections/";
    }

    @GetMapping("edit/export/{id}")
    public String requestExportOfMyCollections(@PathVariable("id") Long id,Model model){
        List<UserCollection> cols = userCollectionService.findAllUserCollection(id);
        model.addAttribute("cols",cols);
        return "export";
    }

    private User getCurrentUser(OAuth2User oAuth2User){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if(authentication.getPrincipal() != null && authentication.getPrincipal() instanceof CostumClient){
            CostumClient cred  = (CostumClient) authentication.getPrincipal();
            username = cred.getUsername();
        }else{
            username = authentication.getName();
        }
        Optional<User> user = userService.findByUsername(username);
        return  !user.isPresent() ? new User():user.get();
    }

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();

        return renderer.render(document);
    }
    private Pair upload(MultipartFile file) throws IOException {
        Map uploadRequest = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String publicId = uploadRequest.get("public_id").toString();
        String url = cloudinary.url().secure(true).format("jpg")
                .publicId(publicId)
                .generate();

        Pair p = new Pair();
        p.url = url;
        p.id = publicId;
        return p;
    }

    private Cloudinary getCloudinary(){
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "");
        config.put("api_key", ");
        config.put("api_secret", "");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    class Pair{
        public String id;
        public String url;
    }

}













