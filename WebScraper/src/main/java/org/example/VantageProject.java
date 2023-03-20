package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class VantageProject {

    // Function to change the format of the date to desired format
    public static String StringToDate(String inp_date) {
        Hashtable<String, String> my_dict = new Hashtable<String, String>();
        my_dict.put("January", "01");
        my_dict.put("February", "02");
        my_dict.put("March", "03");
        my_dict.put("April", "04");
        my_dict.put("May", "05");
        my_dict.put("June", "06");
        my_dict.put("July", "07");
        my_dict.put("August", "08");
        my_dict.put("September", "09");
        my_dict.put("October", "10");
        my_dict.put("November", "11");
        my_dict.put("December", "12");

        String[] splitDate = inp_date.trim().split("\\s+");
        if (splitDate[0].length() == 1) {
            splitDate[0] = "0" + splitDate[0];
        }
        String formated_date = splitDate[2] + "-" + my_dict.get(splitDate[1]) + "-" + splitDate[0];

        return formated_date;
    }

    // Function to write each review info of each topic in a file
    public static void innerReviews(String url, String Topic, File myObj) throws IOException {
        Document inner_document = Jsoup.connect(url).get();

        FileWriter myWriter = new FileWriter(myObj.getName(), true);

        // iterating over each review
        for (Element review : inner_document.select("div.search-results-item-body ")) {

            Element reviewLink = review.select("a").first();

            String reviewAuthor = review.select("div.search-result-authors div ").text();
            String reviewDate = review.select("div.search-result-date div ").text();

            String reviewlHref = reviewLink.attr("href");
            String reviewlText = reviewLink.text();

            // formatting and storing all the review info
            String result = "https://www.cochranelibrary.com" + reviewlHref + " | " + Topic + " | " + reviewlText + " | " + reviewAuthor + " | "
                    + StringToDate(reviewDate);

            // writing the output in the text file
            myWriter.write(result + "\n");
            myWriter.write("\n");

            // MetaData of the review
            for (Element metaData : review.select("div.search-result-metadata-block")) {
                String metaType = metaData
                        .select("div.search-result-metadata-item div.search-result-type div.custom-tooltip")
                        .attr("aria-label");
                String metaStage = metaData
                        .select("div.search-result-metadata-item div.search-result-stage div.custom-tooltip")
                        .attr("aria-label");
                String metaDate = metaData.select("div.search-result-metadata-item div.search-result-date ").text();
                String meta = metaType + "\n" + metaStage + "\n" + metaDate;
                myWriter.write("MetaData:\n");
                myWriter.write(meta + "\n");
            }

        }
        myWriter.close();

    }

    public static void main(String[] args) throws IOException, ParseException {

        // creating a new text file
        File myFile = new File("cochrane_reviews.txt");

        // checks if the file already exists else creates a new file
        try {
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        // url of the cochrane library topics page
        String url = "https://www.cochranelibrary.com/cdsr/reviews/topics";
        Document document = Jsoup.connect(url).get();

        // iterating over each topic
        for (Element link : document.select("div.span4 li.browse-by-list-item a")) {
            String Topic = link.text();

            // url for the cochrane library reviews page for the current topic
            String linkHref = link.attr("href");

            // getting and storing the content of reviews page for the current topic
            Document current_document = Jsoup.connect(linkHref).get();

            // calling the innerReviews function to get the desired output
            innerReviews(linkHref, Topic, myFile);

            // iterating over paginates of each review
            for (Element nextPage : current_document.select(" li.pagination-page-list-item ")) {
                Element nextPageLink = nextPage.select(" a").first();

                // url for the paginated pages of the reviews page
                String nextPageHref = nextPageLink.attr("href");

                // calling the innerReviews function to get the desired output for all the paginated pages for each topic
                 //innerReviews(nextPageHref, Topic, myFile);

            }

        }

    }
}
