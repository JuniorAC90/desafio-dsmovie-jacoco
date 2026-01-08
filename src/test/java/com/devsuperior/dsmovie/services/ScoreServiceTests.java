package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private ScoreRepository repository;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private UserService userService;

	private UserEntity userEntity;

	private MovieEntity movieEntity;

	private MovieDTO movieDTO;

	private ScoreDTO scoreDTO;

	private ScoreEntity entity;

	private Long existingId, nonExistingId;

	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 2L;

		userEntity = UserFactory.createUserEntity();
		movieEntity = MovieFactory.createMovieEntity();
		entity = ScoreFactory.createScoreEntity();
		scoreDTO = ScoreFactory.createScoreDTO();
		movieDTO = MovieFactory.createMovieDTO();

		Mockito.when(userService.authenticated()).thenReturn(userEntity);
		Mockito.when(movieRepository.findById(existingId)).thenReturn(Optional.of(movieEntity));
		Mockito.when(repository.saveAndFlush(entity)).thenReturn(entity);
		Mockito.when(movieRepository.save(movieEntity)).thenReturn(movieEntity);

		Mockito.when(movieRepository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {

		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingId, result.getId());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		ScoreDTO dto = new ScoreDTO(nonExistingId, 0.0);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			MovieDTO result = service.saveScore(dto);
		});
	}
}
