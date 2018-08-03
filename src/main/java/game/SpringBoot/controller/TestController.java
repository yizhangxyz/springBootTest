package game.SpringBoot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Controller
public class TestController
{
	static public class Person {
	    private String name;
	    private Integer age;

	    public Person() {
	    }

	    public Person(String name, Integer age) {
	        this.name = name;
	        this.age = age;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public Integer getAge() {
	        return age;
	    }

	    public void setAge(Integer age) {
	        this.age = age;
	    }
	}
	
	@RequestMapping("/home")
    public String homePage(Model model){
		Person single = new Person("aa", 11);
        List<Person> people = new ArrayList<>();
        Person p1 = new Person("zhang1", 11);
        Person p2 = new Person("zhang2", 22);
        Person p3 = new Person("zhang3", 33);
        people.add(p1);
        people.add(p2);
        people.add(p3);
        model.addAttribute("singlePerson", single);
        model.addAttribute("people", people);
        return "homepage";       
    }
	
	@RequestMapping("/show")
    public String show(){
        return "test";        
    }
}
