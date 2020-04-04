package com.ratingsdataservice.resource;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratingsdataservice.model.Rating;
import com.ratingsdataservice.model.UserRating;

@RestController
@RequestMapping(value = "/ratingdata")
public class RatingsDataResource {
	@RequestMapping(value = "/{userId}")
	public UserRating getRating(@PathVariable("userId") String userId) {
		List<Rating> ratings = Arrays.asList(new Rating("1", 4), new Rating("2", 5));
		UserRating userRating = new UserRating();
		userRating.setUserRating(ratings);
		return userRating;
	}

}
