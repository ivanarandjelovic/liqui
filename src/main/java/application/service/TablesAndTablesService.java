package application.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.TablesAndTables;
import application.repository.TablesAndTablesRepository;

@Service
@Transactional
public class TablesAndTablesService {

	@Autowired
	TablesAndTablesRepository repo;
	
	public List<TablesAndTables> getAll() {
		return repo.findAll();
	}
}
