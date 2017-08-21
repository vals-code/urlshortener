package com.urlshortener.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.urlshortener.entity.ShortenedURL;
import com.urlshortener.repo.URLShortenerRepository;
import com.urlshortener.util.Base62Util;

@Controller
public class URLShortenerController {
	
	@Autowired
	private URLShortenerRepository urlRepo;

	@RequestMapping(value="/", method=RequestMethod.GET)
    public String index(ShortenedURL shortenedURL) {
        return "index";
    }
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public @ResponseBody ModelAndView shortenURL(HttpServletRequest httpRequest, @Valid ShortenedURL shortenedURL, BindingResult bindingResult) {
		ModelAndView index = new ModelAndView("index");
		String url = shortenedURL.getOriginalURL();
		List<ShortenedURL> shortenedURLList = urlRepo.findByOriginalURL(url);
		if (!shortenedURLList.isEmpty()) {
			//use existing shortened url
			ShortenedURL existingURL = shortenedURLList.get(0);	
	        index.addObject("shortURL", existingURL.getId());
		} else {
			//save new url to mysql database
			urlRepo.save(shortenedURL);
			
			//generate shortURL
			shortenedURL.setShortURL(Base62Util.fromBase10(shortenedURL.getId()));
			
			urlRepo.save(shortenedURL);
			
			String requestUrl = httpRequest.getRequestURL().toString();
            String prefix = requestUrl.substring(0, requestUrl.indexOf(httpRequest.getRequestURI(), "http://".length()));

            index.addObject("shortURL", prefix + "/" + shortenedURL.getShortURL());
		}
		return index;
	}
	
	@RequestMapping(value="/{shortURL}", method=RequestMethod.GET)
	public void goToOriginalURL(@PathVariable final String shortURL, HttpServletResponse resp) throws IOException {
		Long id = Base62Util.toBase10(shortURL);
		ShortenedURL shortenedURL = urlRepo.findOne(id);
        if (shortenedURL != null) {
            resp.sendRedirect(shortenedURL.getOriginalURL());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
	} 
}
