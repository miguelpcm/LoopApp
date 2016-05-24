package airsign.loopAPI.loopAPI.data;

public class User
{
    public String id, firstName, middleName, lastName, city, country, bio, imgUrl, affiliation, degree, jobTitle;
    public boolean isPublic;

    public User(String id, String firstName, String middleName, String lastName, String city,
                String country, String degree, String jobTitle, String bio, String imgUrl,
                String affiliation, boolean isPublic)
    {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.bio = bio;
        this.imgUrl = imgUrl;
        this.affiliation = affiliation;
        this.degree = degree;
        this.jobTitle = jobTitle;
        this.isPublic = isPublic;
    }

    public String getName()
    {
        String name = firstName;
        if (middleName!=null) name += " " + middleName;
        if (lastName!=null) name += " " + lastName;
        return name;
    }

    public String getProvenance()
    {
        String provenance = city;
        if (country!=null)
        {
            if (provenance!=null) provenance += ", " + country;
            else provenance = country;
        }
        return provenance;
    }

}
