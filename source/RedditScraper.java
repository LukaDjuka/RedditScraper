/**
 * Class that scrapes the posts of the front page of any subreddit in search of user requested
 * key words in the titles of the posts.
 * This class works alongside a GUI to provide the user with links to those posts
 * 
 * Requires the jsoup library.
 * As of August 21, 2020 - Uses Jsoup-1.13.1.jar
 */ 
package source;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class RedditScraper {
    /**
     * The Document instance variable to hold the loaded HTML page
     */
    Document doc = null;

    /**
     * The Elements which correspond to the top posts of the page
     */
    Elements posts = null;

    /**Constructor - Provide a string of the subreddit you would like to see the posts from
     */
    public RedditScraper(String subreddit){
        String full_url = "https://www.reddit.com/r/" + subreddit;
        try{
            doc = Jsoup.connect(full_url).timeout(20000).get();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            System.out.println("Unable to connect to the webpage \n Please ensure you entered a valid subreddit");
        }
        posts = doc.getElementsByClass("SQnoC3ObvgnGjWt90zD9Z _2INHSNB8V5eaWp4P0rY_mE");
    }

    /** Returns a list of the top posts from the requested subreddit
     * 
     * @return list fo top posts for subreddit
     */
    public Elements print_top_posts(){
        for (Element post : posts){
            //System.out.println(post.attributes().get("href"));
            String href = post.attributes().get("href");
            String full_link = "https://www.reddit.com/r/" + href;
            String[] tokens = href.split("/");
            //System.out.println("Now to print the post name");
            System.out.println("------------------------------------------------------");
            System.out.println(tokens[2] + " ------ " + tokens[tokens.length - 1]);
            System.out.println("Full Link: \n" + full_link);
            System.out.println("------------------------------------------------------");
        }
        return posts;
    }

    /** Searches through the top posts of the subreddit for titles that contain the provided keyword argument
     * Returns an ArrayList of the Elements (aka posts) whose title contains the keyword
     * 
     * @return ArrayList of Elements whose title contains keyword.
     */
    public ArrayList<Element> search_posts(String keyword){
        ArrayList<Element> found_posts = new ArrayList<Element>();
        for (Element post : posts){
            String href = post.attributes().get("href");
            if (href.contains(keyword)){
                found_posts.add(post);
            }
        }
        return found_posts;
    }

    public static void main(String[] args){
        RedditScraper rs = new RedditScraper("all");
        rs.print_top_posts();
    }
}