package csulb.cecs323.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User represents the superclass and it is any person that signs up for an account
 * User makes up a chef entity or food critic entity or even neither
 * but its purpose is to create an account for new users
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Table(name = "user_table", uniqueConstraints = {@UniqueConstraint(columnNames = {"first_name","last_name","username","password"})})
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userID;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String username;

    private String password;

    private String email;

    @Column(name = "date_registered", nullable = false)
    private LocalDateTime dateRegistered;

    @ManyToMany
    @JoinTable(name="followers",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="follower_id")
    )
    private Set<User> followers;

    @ManyToMany(mappedBy = "followers")
    private Set<User> followings;

    /**
     * No-arg Constructor: initializes each attribute to null and compose an empty Set for Set attributes
     */
    public User() {
        this.followers = new HashSet<>();
        this.followings = new HashSet<>();
    }

    /**
     * Arg Constructor: sets the attribute for User with the given arguments and initialize an empty Set for Set attributes
     * @param firstName   the first name of the new user
     * @param lastName    the last name of the user
     * @param username    the username that the user created
     * @param password    the password that will give access into their account of the user
     * @param email       the email address associated with the new user
     * @param dateRegistered   the date that the new user had create an account
     */
    public User(String firstName, String lastName, String username, String password, String email, LocalDateTime dateRegistered){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateRegistered = dateRegistered;
        this.followers = new HashSet<>();
        this.followings = new HashSet<>();
    }

    /**
     * get the id surrogate of the user
     * @return id of the user in long
     */
    public long getUserID() {
        return userID;
    }

    /**
     * Obtains the first name of the user
     * @return  the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *Sets the first name of the user
     * @param firstName the first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user
     * @param lastName the last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the username of the user
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user
     * @param password the new password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the user
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user
     * @param email the new email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the date of when the user registered
     * @return the date registry
     */
    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    /**
     * Sets the date of when the user registered
     * @param dateRegistered the new date registry
     */
    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    /**
     * Obtains the set of users follow the current user
     * @return  the list of users that follow the current user entity
     */
    public Set<User> getFollowers() { return this.followers; }

    /**
     * Adds a follower to a user's set of followers
     * @param follower a new follower that follows the user
     */
    public void addFollower(User follower){
        this.followers.add(follower);
        follower.getFollowings().add(this);
    }

    /**
     * Removes a follower from a user's set of followers
     * @param follower a follower to be removed from user's followers
     */
    public void removeFollower(User follower){
        if(this.followers.contains(follower)){
            this.followers.remove(follower);
            follower.getFollowings().remove(this);
        }
    }

    /**
     * Obtains the set of users that the current user is following
     * @return  the list of users that the current user entity is following
     */
    public Set<User> getFollowings() { return this.followings; }

    /**
     * Adds another user to the followings,
     * as that another user is being followed by the user
     * @param following the new person user follows
     */
    public void addFollowing(User following){
        this.followings.add(following);
        following.getFollowers().add(this);
    }

    /**
     * Removes another user from the followings,
     * as the user does not follow another user being passed in the parameter
     * @param following the person to unfollow from user's followings
     */
    public void removeFollowing(User following){
        if(this.followings.contains(following)){
            this.followings.remove(following);
            following.getFollowers().remove(this);
        }
    }

    /**
     * Returns a string representation of the user when called
     * @return string representation of user
     */
    @Override
    public String toString(){
        return String.format("User[id = %d, first_name = %s, last_name = %s, username = %s, password = %s, email = %s, registered = %s]", userID, firstName, lastName, username, password, email, dateRegistered);
    }

}
