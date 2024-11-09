package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {

    private String username;
    private Set<Interest> interests = new LinkedHashSet<>();
    private Set<UserProfile> friends = new LinkedHashSet<>();

    private void setUsername(String username) {
        if (username == null || username.isBlank()) {
            return;
        }
        this.username = username;
    }

    public DefaultUserProfile(String username) {
        setUsername(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUserProfile that = (DefaultUserProfile) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("The interest must not be null!");
        }

        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("The interest must not be null!");
        }
        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null || this.equals(userProfile)) {
            throw new IllegalArgumentException("User profile should not be null or yourself!");
        }

        if (friends.add(userProfile)) {
            userProfile.addFriend(this);
            return true;
        }

        return false;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null || userProfile == this) {
            throw new IllegalArgumentException("The user profile should not be null!");
        }

        if (friends.remove(userProfile)) {
            userProfile.unfriend(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The user profile should not be null!");
        }

        if (friends == null) {
            return false;
        }

        return friends.contains(userProfile);
    }
}
