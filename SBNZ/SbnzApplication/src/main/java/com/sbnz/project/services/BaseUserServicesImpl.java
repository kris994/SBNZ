package com.sbnz.project.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.project.model.BaseUser;
import com.sbnz.project.repository.BaseUserRepository;

@Service
public class BaseUserServicesImpl implements BaseUserServices{

	@Autowired private BaseUserRepository repository;
	
	@Override
	public ArrayList<BaseUser> getAllUsers() {
		return repository.getAllUsers();
	}

	@Override
	public BaseUser save(BaseUser user) {
		return repository.save(user);
	}

}
