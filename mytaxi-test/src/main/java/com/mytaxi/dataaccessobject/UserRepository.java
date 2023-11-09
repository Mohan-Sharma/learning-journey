package com.mytaxi.dataaccessobject;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mytaxi.domainobject.UserDO;

/**
 * @author Mohan Sharma Created on 24/09/18.
 */
public interface UserRepository extends JpaRepository<UserDO, Long>
{
	UserDO findOneByUsername(String username);
}
