package com.hellion.writeup.repository;

import com.hellion.writeup.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findOneByEmailIgnoreCase(String email);

    Boolean existsByEmail(String email);

    public default Account findByUsername(String username) throws Exception {
        try {
            return this.findByUsername(username);
        }
        catch (IndexOutOfBoundsException e) {
            throw new Exception("Logged user not found, please re-login and try again");
        }
    }

    @Query("SELECT f1\n" +
            "FROM Account a1, Account a2\n" +
            "INNER JOIN a1.friends f1\n" +
            "INNER JOIN a2.friends f2\n" +
            "WHERE a1.email = :email1\n" +
            "AND a2.email = :email2\n" +
            "AND f1.email = f2.email")
    Set<Account> findMutualFriends(@Param("email1") String email1, @Param("email2") String email2);

    @Query("SELECT f from Account a "
            +"inner join a.friends f "
            +"left join f.blockedUsers b "
            +"where a.email = :email "
            +"and (b is null or u not in b)")
    Set<Account> findFriendsNotBlocked(@Param("email") String email);

    @Query("SELECT f from Account u "
            +"inner join u.followers f "
            +"left join f.blockedUsers b "
            +"where u.email = :email "
            +"and (b is null or u not in b)")
    Set<Account> findFollowersNotBlocked(@Param("email") String email);

    @Query("SELECT u from Account u "
            +"left join u.blockedUsers b "
            +"where (u.email in :mentioned) "
            +"and (b is null or :sender not in b.email)")
    Set<Account> findUsersMentionedNotBlocked(@Param("sender") String sender, @Param("mentioned") String[] mentioned);
}
