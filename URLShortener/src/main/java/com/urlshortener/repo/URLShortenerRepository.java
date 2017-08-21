package com.urlshortener.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.urlshortener.entity.ShortenedURL;

public interface URLShortenerRepository extends CrudRepository<ShortenedURL, Long> {
	
	List<ShortenedURL> findByOriginalURL(String originalURL);
	
	List<ShortenedURL> findByShortURL(String shortURL);

}
