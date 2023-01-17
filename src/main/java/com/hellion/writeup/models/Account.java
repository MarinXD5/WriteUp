package com.hellion.writeup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String userName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_authority",
            joinColumns = {@JoinColumn(name="account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authority = new HashSet<>();

    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name="account_friend",
            joinColumns = {@JoinColumn(name="account_id")},
            inverseJoinColumns = {@JoinColumn(name="friend_id")})
    private Set<Account> friends = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="account_friend",
            joinColumns = {@JoinColumn(name="friend_id")},
            inverseJoinColumns = {@JoinColumn(name="account_id")})
    private Set<Account> friendOf;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="account_following",
            joinColumns = {@JoinColumn(name="account_id")},
            inverseJoinColumns = {@JoinColumn(name="following_id")})
    private Set<Account> following = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private Set<Account> followers = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="account_blocked",
            joinColumns = {@JoinColumn(name="account_id")},
            inverseJoinColumns = {@JoinColumn(name="blocked_id")})
    private Set<Account> blockedUsers = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "blockedUsers", cascade = CascadeType.ALL)
    private Set<Account> blockedBy = new HashSet<>();


    public Account(String email, String password, String firstName, String lastName, String userName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Account{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", authorities=" + authority +
        "}";
    }

    public boolean hasFriend(Account user) {
        return this.getFriends() != null && this.getFriends().contains(user);
    }


    public boolean hasFollower(Account user) {
        return this.getFollowers() != null && this.getFollowers().contains(user);
    }

    public boolean hasBlocked(Account user) {
        return this.getBlockedUsers() != null && this.getBlockedUsers().contains(user);
    }

    public void addFriend(Account friend) {
        this.friends.add(friend);
        friend.getFriends().add(this);
    }

    public void addFollower(Account follower) {
        this.followers.add(follower);
        follower.following.add(this);
    }

    public void blockUser(Account user) {
        this.blockedUsers.add(user);
        user.blockedBy.add(this);
    }

}
