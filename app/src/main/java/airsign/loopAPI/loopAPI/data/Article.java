package airsign.loopAPI.loopAPI.data;

import java.util.ArrayList;

public class Article
{
    public String id, title, abstrakt, journal, date, url;
    public ArrayList<Affiliation> affiliations;
    public ArrayList<Author> authors;
    public ArrayList<String> keywords;

    public Article(String id, String title, String abstrakt,
           String journal, String date, String url,
           ArrayList<String> keywords,
           ArrayList<Affiliation> affiliations,
           ArrayList<Author> authors)
    {
        this.id = id;
        this.title = title;
        this.abstrakt = abstrakt;
        this.journal = journal;
        this.date = date;
        this.url = url;
        this.keywords = keywords;
        this.affiliations = affiliations;
        this.authors = authors;
    }
}
