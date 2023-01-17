package com.hellion.writeup.service;

import com.hellion.writeup.models.Account;
import com.hellion.writeup.models.Authority;
import com.hellion.writeup.repository.AccountRepository;
import com.hellion.writeup.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    public Account save(Account account) {

        if (account.getId() == null) {
            if (account.getAuthority().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
                account.setAuthority(authorities);
            }
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account findOneByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }

    @Transactional
    public void addFriend(String email1, String email2) throws Exception{

        if(email1 != null && email1.equals(email2)) {
            throw new Exception("Cannot connect same users");
        }

        Account person = accountRepository.findOneByEmailIgnoreCase(email1);
        Account friend = accountRepository.findOneByEmailIgnoreCase(email2);

        if(person == null || friend == null) {
            throw new Exception("User(s) not found");
        } else if(person.hasFriend(friend)) {
            throw new Exception("Already friends");
        } else if(person.hasBlocked(friend) || friend.hasBlocked(person)) {
            throw new Exception("User is blocked");
        }

        person.addFriend(friend);
        accountRepository.save(friend);
    }

    @Transactional(readOnly = true)
    public Set<Account> getFriends(String email) throws Exception{
        Account user = accountRepository.findOneByEmailIgnoreCase(email);

        if(user == null) {
            throw new Exception("User not found");
        }

        return user.getFriends();
    }

    @Transactional(readOnly=true)
    public Set<Account> getMutualFriends(String email1, String email2) throws Exception{

        if(email1 != null && email1.equals(email2)) {
            throw new Exception("Invalid request");
        }

        Account user1 = accountRepository.findOneByEmailIgnoreCase(email1);
        Account user2 = accountRepository.findOneByEmailIgnoreCase(email2);

        if(user1 == null || user2 == null) {
            throw new Exception("User(s) not found");
        }

        Set<Account> user2Friends = user2.getFriends();

        return user1.getFriends().stream()
                .filter(user2Friends::contains)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void addFollower(String requestor, String target) throws Exception {

        if(requestor != null && requestor.equals(target)) {
            throw new Exception("Cannot follow self");
        }

        Account user1 = accountRepository.findOneByEmailIgnoreCase(requestor);
        Account user2 = accountRepository.findOneByEmailIgnoreCase(target);

        if(user1 == null || user2 == null) {
            throw new Exception("User(s) not found");
        } else if(user2.hasFollower(user1)) {
            throw new Exception("Already followed");
        }

        user2.addFollower(user1);
        accountRepository.save(user2);
    }

    @Transactional
    public void addBlockList(String requestor, String target) throws Exception{

        if(requestor != null && requestor.equals(target)) {
            throw new Exception("Cannot block self");
        }

        Account user1 = accountRepository.findOneByEmailIgnoreCase(requestor);
        Account user2 = accountRepository.findOneByEmailIgnoreCase(target);

        if(user1 == null || user2 == null) {
            throw new Exception("User(s) not found");
        } else if(user1.hasBlocked(user2)){
            throw new Exception("Already blocked");
        }

        user1.blockUser(user2);
        accountRepository.save(user1);
    }

    @Transactional(readOnly = true)
    public Set<Account> getUsersToBeNotified(String sender, String text) throws Exception{

        Account user = accountRepository.findOneByEmailIgnoreCase(sender);

        if(user == null) {
            throw new Exception("User not found");
        }

        // friends to be notified
        Set<Account> recipients = accountRepository.findFriendsNotBlocked(sender);

        // get followers
        recipients.addAll(accountRepository.findFollowersNotBlocked(sender));

        // mentioned user
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        String[] mentioned = Arrays.stream(text.split(" "))
                .filter(t ->
                        pattern.matcher(t).matches()
                )
                .toArray(String[]::new);
        if(mentioned != null && mentioned.length != 0){
            recipients.addAll(accountRepository.findUsersMentionedNotBlocked(sender, mentioned));
        }

        return recipients;
    }
}