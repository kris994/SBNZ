package com.sbnz.project.services;

import java.util.ArrayList;

import com.sbnz.project.model.BaseUser;

public interface BaseUserServices {
	
	public ArrayList<BaseUser> getAllUsers();
	public BaseUser save(BaseUser user);
	
}
