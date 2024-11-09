package bg.sofia.uni.fmi.mjt.socialnetwork.comparator;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Comparator;

public class ProfileByNumOfFriendsDescComparator implements Comparator<UserProfile> {

    @Override
    public int compare(UserProfile o1, UserProfile o2) {

        int countFriendsFirstUser = o1.getFriends().size();
        int countFriendsSecondUser = o2.getFriends().size();

        return Integer.compare(countFriendsSecondUser, countFriendsFirstUser);

    }

}
