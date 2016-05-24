package airsign.loopAPI.loopAPI.data;

import java.util.ArrayList;

public class UserList
{
    public int count; // Is not equal to list.size
    public ArrayList<User> list;

    public UserList(int count, ArrayList<User> list)
    {
        this.count = count;
        this.list = list;
    }
}
