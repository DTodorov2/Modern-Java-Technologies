package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.comparator.ProfileByNumOfFriendsDescComparator;
import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {

    Set<UserProfile> users = new HashSet<>();
    Set<Post> posts = new HashSet<>();

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("The profile of the user should not be null!");
        }

        if (!users.add(userProfile)) {
            throw new UserRegistrationException("This user is already registered!");
        }

        users.add(userProfile);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("The user profile cannot be null!");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("The content cannot be null!");
        }

        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("This user is not registered yet!");
        }

        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);

        return newPost;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableSet(posts);
    }

    private boolean haveCommonInterest(UserProfile author, UserProfile user) {
        for (Interest interest : user.getInterests()) {
            if (author.getInterests().contains(interest)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("The post cannot be null!");
        }
        UserProfile author = post.getAuthor();
        Set<UserProfile> reachedUsers = new HashSet<>();
        Deque<UserProfile> usersDeque = new ArrayDeque<>();
        Set<UserProfile> visitedUsers = new HashSet<>();
        usersDeque.addFirst(author);
        visitedUsers.add(author);

        while (!usersDeque.isEmpty()) {
            UserProfile currUser = usersDeque.removeFirst();
            visitedUsers.add(currUser);

            if (!currUser.equals(author) && haveCommonInterest(author, currUser)) {
                reachedUsers.add(currUser);
            }

            for (UserProfile userFriend : currUser.getFriends()) {
                if (!visitedUsers.contains(userFriend)) {
                    usersDeque.addFirst(userFriend);
                }
            }
        }
        return reachedUsers;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException {
        if (userProfile1 == null) {
            throw new IllegalArgumentException("The profile of the first user cannot be null!");
        }

        if (userProfile2 == null) {
            throw new IllegalArgumentException("The profile of the second user cannot be null!");
        }

        if (!users.contains(userProfile1) || !users.contains(userProfile2)) {
            throw new UserRegistrationException("One of the users is not registered!");
        }

        Set<UserProfile> friends1 = (Set<UserProfile>)userProfile1.getFriends();
        Set<UserProfile> friends2 = (Set<UserProfile>)userProfile2.getFriends();

        if (friends1 == null || friends2 == null) {
            return Collections.emptySet();
        }

        Set<UserProfile> intersect = new HashSet<>(friends1);
        intersect.retainAll(friends2);
        return Collections.unmodifiableSet(intersect);
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> listOfUsers = new TreeSet<>(new ProfileByNumOfFriendsDescComparator());
        listOfUsers.addAll(users);
        return listOfUsers;
    }
}
