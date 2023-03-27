# WebScraper
A simple web scraping program using JSoup.

Cochrane Library Reviews Scraper

This is a simple web scraper that collects reviews from the Cochrane Library website and outputs them to a text file. The program is written in Java and uses the Jsoup library for web scraping.

How to use

Clone or download the repository.
Open the project in your preferred Java IDE.
Run the VantageProject.java file.
How it works

The program first creates a text file to store the reviews. It then navigates to the topics page on the Cochrane Library website and scrapes the links for each topic. For each topic, it scrapes the content of the reviews page and writes the review information to the text file. If there are multiple pages of reviews, the program scrapes each page and writes the information to the text file.

The review information that is collected includes the review title, author, date, URL, topic, and metadata. The date is reformatted from the original format to YYYY-MM-DD.
