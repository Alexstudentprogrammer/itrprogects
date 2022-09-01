package com.example.course_project;

import com.example.course_project.search.repositories.SearchRepository;
import com.example.course_project.search.SearchService;
import com.example.course_project.userInfo.UserRole;
import com.example.course_project.user_collections.*;
import com.example.course_project.userInfo.User;
import com.example.course_project.userInfo.UserRepository;
import com.example.course_project.userInfo.UserService;
import com.example.course_project.user_collections.repositories.CommentRepository;
import com.example.course_project.user_collections.repositories.ItemRepository;
import com.example.course_project.user_collections.repositories.TagRepository;
import com.example.course_project.user_collections.repositories.UserCollectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;

@SpringBootTest
class CourseProjectApplicationTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private UserCollectionRepository repositoryCol;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserService service;

	@Autowired
	private TagRepository rep;

	@Autowired
	private SearchService searchService;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	SearchRepository searchRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	@Test
	void contextLoads() {


//User user = (User)service.loadUserByUsername("user");
//user.setRole(UserRole.ADMIN);
//service.save(user);

//		User u = new User();
//		u.setFName("a");
//		u.setPassword("123");
//		u.setUsername("a");
//		u.setLocked(false);
//		u.setRole(UserRole.ADMIN);
//		service.signUp(u);
//
//		User u1 = new User();
//		u1.setFName("a");
//		u1.setPassword("123");
//		u1.setUsername("user");
//		u1.setLocked(false);
//		u1.setRole(UserRole.USER);
//		service.signUp(u1);


//		SearchProduct p = new SearchProduct();
//		p.setPk("2");
//		p.setData("First product");
//		p.setCollectionName("col");
//		p.setUserId("12");
//		//searchRepository.save(p);
//		System.out.println(searchRepository.findByPk("2"));
//		searchRepository.deleteById("Zya2y4IBnGSL8phd1Wp_");


//		System.out.println(searchService.findByData("product"));
//
//		searchService.remove(p);

	//	service.removeItem(4);


//		Tag t = new Tag();
//		t.setName("Some tag");
//		Tag t1 = new Tag();
//		t1.setName("Some tag-1");
//		Tag t2 = new Tag();
//		t2.setName("Some tag-2");
//		Tag t3 = new Tag();
//		t3.setName("Some tag-3");
//		rep.save(t);
//		rep.save(t1);
//		rep.save(t2);
//		rep.save(t3);
/*
        Comment comment = new Comment();
		comment.setAuthorOfComment("Alex");
		comment.setContentOfComment("First Comment");
		List<Comment> comments = new ArrayList<>();
		comments.add(comment);

		Tag t = new Tag();
		Set<Tag> l  = new HashSet<>();

		Item i = new Item();
		i.setName("Item");
		i.setTags(new HashSet<>());

		Item i1 = new Item();
		i1.setName("Item1");
		i1.setTags(new HashSet<>());

		Item i2 = new Item();
		i2.setName("Item2");

		t.setName("Travel");
		l.add(t);
		i2.setTags(l);

		List<Item> list = new ArrayList<>();
		list.add(i);
		list.add(i1);
		list.add(i2);

		UserCollection cl = new UserCollection();
		UserCollection cl1 = new UserCollection();


		List<Item> list1 = new ArrayList<>();
		HashSet<Tag> tagHashSet = new HashSet<>();
		tagHashSet.add(t);

		Item item = new Item();
		item.setComments(comments);
		item.setName("item_col_2");
		item.setTags(tagHashSet);
		list1.add(item);
		cl1.setItems(list1);
		cl1.setName("Collection of planes");

		cl.setImage("");
		cl.setInformation("New collection");
		cl.setItems(list);
		cl.setName("First Collection");
		cl.setTheme(Theme.BOOKS);

		User u = new User();
		u.setFName("a");
		u.setPassword("123");
		u.setUsername("user");
		u.setLocked(false);
		u.setRole(UserRole.USER);
		service.signUp(u);
		//cl.setComments(comments);
//		u.getUserCollections().add(cl);
//		u.getUserCollections().add(cl1);
		service.addCollection(1,"First Collection", "SOME INFO", Theme.BOOKS);
		service.addCollection(1,"Second Collection", "SOME INFO", Theme.BOOKS);
		for(Item it : list){
			service.addItem(1,1, it,tagHashSet);
		}
		for(Item it : list1){
			service.addItem(1,1, it,tagHashSet);
		}
		*/
	//	System.out.println();
 //System.out.println(repositoryCol.findUserCollectionById((long)2).get().getItems());
	//	System.out.println();


//man();
	}
	@Test
	@Transactional
	private void man(){
		User user = repo.findUserByUserId((long)1).get();
		UserCollection col = user.getUserCollections().get(0);  //TODO code for data deletion (user collection)

		for(Item item : col.getItems()) {
			item.prepareForDelete();
			itemRepository.save(item);
		}
		user.getUserCollections().remove(col);


		user.getUserCollections().add(new UserCollection());
		repo.save(user);


	}

}
