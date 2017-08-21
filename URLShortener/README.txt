# URL Shortener

User is presented with a text field where a url can be entered. Upon clicking the 'Shorten URL' button, the user will be presented with a shortened URL. Navigating to this URL will redirect the user to the original URL provided. If the user enters a URL that has already been shortened by the application, they will be presented with the same shortened URL.

## Getting Started

This application was built using maven, java, thymeleaf, spring boot, a local instance of mysql database and using eclipse IDE. I ran this application using mvn spring-boot:run from the command prompt.

### Prerequisites

MySQL database named urlshortenerdb with a table named shortenedurl which has the following fields:
id int
shortURL varchar(255)
originalURL varchar(255)

Assumptions:
shortened URLs are never deleted from the database and do not expire.
user enters full url including http prefix