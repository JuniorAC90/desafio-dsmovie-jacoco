package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private CustomUserUtil userUtil;

	private UserEntity entity;
	private String validUsername, invalidUsername;

	private List<UserDetailsProjection> list;

	@BeforeEach
	void setUp() {
		validUsername = "maria@gmail.com";
		invalidUsername = "maria@gmail.com";
		entity = UserFactory.createUserEntity();
		list = UserDetailsFactory.createCustomAdminClientUser(validUsername);

		Mockito.when(userUtil.getLoggedUsername()).thenReturn(validUsername);
		Mockito.when(repository.findByUsername(validUsername)).thenReturn(Optional.of(entity));
		Mockito.when(repository.findByUsername(invalidUsername)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(repository.searchUserAndRolesByUsername(validUsername)).thenReturn(list);
		Mockito.when(repository.searchUserAndRolesByUsername(invalidUsername)).thenReturn(new ArrayList<>());
	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {

		UserEntity result = service.authenticated();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(validUsername, result.getUsername());
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			UserEntity result = service.authenticated();
		});
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {

		UserDetails result = service.loadUserByUsername(validUsername);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(validUsername, result.getUsername());

	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			UserDetails result = service.loadUserByUsername(validUsername);
		});
	}
}
