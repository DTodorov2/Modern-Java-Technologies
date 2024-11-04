package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class SocialFeedPost implements Post {

    private UUID postId;
    private UserProfile author;
    private String content;
    private LocalDateTime publishingTime;
    private Map<ReactionType, Set<UserProfile>> reacts = new EnumMap<>(ReactionType.class);

    public SocialFeedPost(UserProfile author, String content) {
        if (author == null || content == null) {
            throw new IllegalArgumentException("Neither author nor content can be null!");
        }

        this.author = author;
        this.content = content;
        postId = UUID.randomUUID();
        publishingTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialFeedPost that = (SocialFeedPost) o;
        return Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(postId);
    }

    @Override
    public String getUniqueId() {
        return postId.toString();
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishingTime;
    }

    @Override
    public String getContent() {
        return content;
    }

    private ReactionType getReactionFromUser(UserProfile userProfile) {
        if (reacts.isEmpty()) {
            return null;
        }
        for (Map.Entry<ReactionType, Set<UserProfile>> entry : reacts.entrySet()) {
            if (entry.getValue().contains(userProfile)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void addUserToReaction(ReactionType reactionType, UserProfile userProfile) {
        Set<UserProfile> setOfProfiles = reacts.get(reactionType);
        setOfProfiles.add(userProfile);
        reacts.put(reactionType, setOfProfiles);
    }

    private void removeUserFromReaction(ReactionType reactionType, UserProfile userProfile) {
        Set<UserProfile> setOfProfiles = reacts.get(reactionType);
        setOfProfiles.remove(userProfile);
        if (setOfProfiles.isEmpty()) {
            reacts.remove(reactionType);
        }
    }

    private boolean changeReactionOfUser(UserProfile profile, ReactionType reaction, Set<UserProfile> setOfProfiles) {
        ReactionType previousReact = getReactionFromUser(profile);

        if (reacts.get(reaction) == null) {
            reacts.put(reaction, new HashSet<>());
        }

        if (previousReact != null && !previousReact.equals(reaction)) {
            removeUserFromReaction(previousReact, profile);
        }

        addUserToReaction(reaction, profile);

        return previousReact == null;

    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The user profile cannot be null!");
        }

        if (reactionType == null) {
            throw new IllegalArgumentException("The reaction type cannot be null!");
        }

        return changeReactionOfUser(userProfile, reactionType, new HashSet<>());
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {

        if (userProfile == null) {
            throw new IllegalArgumentException("The user profile cannot be null!");
        }

        ReactionType currentUserReaction = getReactionFromUser(userProfile);
        if (currentUserReaction != null) {
            reacts.get(currentUserReaction).remove(userProfile);
            if (reacts.get(currentUserReaction).isEmpty()) {
                reacts.remove(currentUserReaction);
            }
            return true;
        }

        return false;

    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        return Collections.unmodifiableMap(reacts);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("The reaction type cannot be null!");
        }

        if (reacts.get(reactionType) == null) {
            return 0;
        }
        return reacts.get(reactionType).size();
    }

    @Override
    public int totalReactionsCount() {

        if (reacts.isEmpty()) {
            return 0;
        }

        int countTotalReactions = 0;
        for (Set<UserProfile> userProfiles : reacts.values()) {
            countTotalReactions += userProfiles.size();
        }
        return countTotalReactions;

    }
}
