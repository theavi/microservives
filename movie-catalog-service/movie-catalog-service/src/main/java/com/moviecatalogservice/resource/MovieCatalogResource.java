package com.moviecatalogservice.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.moviecatalogservice.model.CatalogItem;
import com.moviecatalogservice.model.Movie;
import com.moviecatalogservice.model.Rating;
import com.moviecatalogservice.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(value = "/catlog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	/*
	 * @Autowired private WebClient.Builder WebClientBulider;
	 */
	@RequestMapping(value = "/{userId}")
	@HystrixCommand(fallbackMethod = "getCatalogFallback")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		List<CatalogItem> listCatalog = new ArrayList<>();
		// get all rated movie
		UserRating ratings = restTemplate.getForObject("http://RATING-DATA-SERVICE/ratingdata/" + userId,
				UserRating.class);

		// List<Rating> ratings = Arrays.asList(new Rating("1", 3), new Rating("2", 4));
		// for each movie id call movie info service and get details
		for (Rating rating : ratings.getUserRating()) {
			Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movie/" + rating.getMovieId(),
					Movie.class);
			/*
			 * Movie movie =WebClientBulider.build() .get()
			 * .uri("http://localhost:8084/movie/" + rating.getMovieId()) .retrieve()
			 * .bodyToMono(Movie.class) .block();
			 */
			// PUT THEM TOGETHER
			listCatalog.add(new CatalogItem(movie.getName(), "desc", rating.getRating()));
		}

		return listCatalog;

	}

	public List<CatalogItem> getCatalogFallback(@PathVariable("userId") String userId) {
		return Arrays.asList(new CatalogItem("No Movie", "", 0));
	}

}
