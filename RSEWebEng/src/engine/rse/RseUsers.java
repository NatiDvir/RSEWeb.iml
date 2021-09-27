package engine.rse;

import java.util.ArrayList;
import java.util.List;

public class RseUsers {
    List<RseUser> users;

    public RseUsers() {
        this.users = new ArrayList<>();
    }

    public RseUsers(List<RseUser> users) {
        this.users = users;
    }

    public List<RseUser> getUsers() {
        return users;
    }
    public void addUser(RseUser u){
        users.add(u);
    }
    public boolean checkUserExist(String u){
        for (RseUser use: users) {
            if(use.getUserName().equalsIgnoreCase(u))
                return true;
        }
        return false;
    }

    public RseUser getUser(String u) {
        for (RseUser use: users) {
            if(use.getUserName().equalsIgnoreCase(u))
                return use;
        }
        return null;
    }
}
