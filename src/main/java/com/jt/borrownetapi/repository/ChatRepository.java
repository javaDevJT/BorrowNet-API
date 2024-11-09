package com.jt.borrownetapi.repository;

import com.jt.borrownetapi.entity.Chat;
import com.jt.borrownetapi.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer>, PagingAndSortingRepository<Chat, Integer> {
    List<Chat> findByTargetInAndSenderInOrderBySendTimeDesc(Collection<User> targets, Collection<User> senders);

    @Query("SELECT c FROM Chat c WHERE " +
            "(c.sender = :loggedInUser OR c.target = :loggedInUser) " +
            "AND c.sendTime = (SELECT MAX(c2.sendTime) FROM Chat c2 " +
            "WHERE (c2.sender = :loggedInUser AND c2.target = c.target) " +
            "OR (c2.target = :loggedInUser AND c2.sender = c.sender)) " +
            "GROUP BY CASE " +
            "WHEN c.sender = :loggedInUser THEN c.target " +
            "ELSE c.sender END " +
            "ORDER BY c.sendTime DESC")
    List<Chat> findRecentChatSessions(@Param("loggedInUser") User loggedInUser);
}
