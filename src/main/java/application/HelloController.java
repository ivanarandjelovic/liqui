package application;

import org.springframework.web.bind.annotation.RestController;

import application.service.TablesAndTablesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

	@Autowired
	TablesAndTablesService service;
	
    @RequestMapping("/")
    public String index() {
    	List<?> records = service.getAll();
        return "Greetings from Spring Boot. I found "+records.size()+" records!";
    }

}