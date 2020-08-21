/**
 * The ScraperGui class works alongside the RedditScraper class to provide the user with
 * a graphical interface when searching subreddits.
 * This is the main class to run when using the program.
 */

package source;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;


public class ScraperGui {
    private JTextField subreddit;
    private JTextField search_term;
    private JTextArea results;
    private JFrame frame;
    private RedditScraper scraper;

    /**
     * Creates an instance of the ScraperGui class and has it perform its basic function
     */
    public static void main(String[] args){
        ScraperGui gui = new ScraperGui();
        gui.go();
    }

    /**
     * Involved with building the GUI and setting up buttons to listen for clicks
     */
    public void go(){
        // Build the GUI
        frame = new JFrame("Reddit Scraper");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel main_panel = new JPanel();

        subreddit = new JTextField(20);
        search_term = new JTextField(20);
        results = new JTextArea(37, 75);

        JScrollPane results_scroller = new JScrollPane(results);
        results_scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        results_scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JLabel sub_label = new JLabel("Subreddit:");
        JLabel search_label = new JLabel("Search:");

        JButton search_sub_button = new JButton("Search Subreddit");
        JButton search_posts_button = new JButton("Search Posts");

        search_sub_button.addActionListener(new ScrapeListener());
        search_posts_button.addActionListener(new KeywordListener());

        main_panel.add(sub_label);
        main_panel.add(subreddit);
        main_panel.add(search_sub_button);
        main_panel.add(search_label);
        main_panel.add(search_term);
        main_panel.add(search_posts_button);
        main_panel.add(results);
        frame.getContentPane().add(BorderLayout.CENTER, main_panel);
        frame.setSize(800, 700);
        frame.setVisible(true);
    }

    /**
     * Inner class ScrapeListener corresponds to button on GUI which is used to search the requested
     * subreddit for top posts
     */
    public class ScrapeListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            scraper = new RedditScraper(subreddit.getText());
            StringBuilder search_results = new StringBuilder();
            for (org.jsoup.nodes.Element post : scraper.posts){
                String href = post.attributes().get("href");
                String full_link = "https://www.reddit.com" + href;
                String[] tokens = href.split("/");
                search_results.append("------------------------------------------------------");
                search_results.append("\n");
                search_results.append(tokens[2] + " ------ " + tokens[tokens.length - 1]);
                search_results.append("\n");
                search_results.append("Full Link: \n" + full_link);
                search_results.append("\n");
                search_results.append("------------------------------------------------------");
                search_results.append("\n");
            }
            results.setText(search_results.toString());
        }
    }

    /**
     * Inner class KeywordListener corresponds to button on GUI which is used to search the presented top
     * posts for a specific keyword entered by the user
     */
    public class KeywordListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            StringBuilder search_results = new StringBuilder(); 
            for (org.jsoup.nodes.Element post : scraper.posts){
                String href = post.attributes().get("href");
                String[] tokens = href.split("/");
                for (String word : tokens){
                    if (word.contains(search_term.getText())){
                        String full_link = "https://www.reddit.com/r/" + href;
                        search_results.append("------------------------------------------------------");
                        search_results.append("\n");
                        search_results.append(tokens[2] + " ------ " + tokens[tokens.length - 1]);
                        search_results.append("\n");
                        search_results.append("Full Link: \n" + full_link);
                        search_results.append("\n");
                        search_results.append("------------------------------------------------------");
                        search_results.append("\n");
                        break;
                    }
                }
            }
            results.setText(search_results.toString());
        }
    }
}