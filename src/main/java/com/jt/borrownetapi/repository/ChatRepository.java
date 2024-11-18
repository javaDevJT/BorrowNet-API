package com.jt.borrownetapi.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jt.borrownetapi.entity.Chat;
import com.jt.borrownetapi.entity.User;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer>, PagingAndSortingRepository<Chat, Integer> {
    List<Chat> findByTargetInAndSenderInOrderBySendTimeDesc(Collection<User> targets, Collection<User> senders);

    @Query("SELECT c FROM Chat c WHERE " +
    "(c.sender = :loggedInUser OR c.target = :loggedInUser) " +
    "AND NOT EXISTS (" +
    "    SELECT 1 FROM Chat c2 " +
    "    WHERE ((c2.sender = c.sender AND c2.target = c.target) " +
    "        OR (c2.sender = c.target AND c2.target = c.sender)) " +
    "    AND c2.sendTime > c.sendTime" +
    ") " +
    "ORDER BY c.sendTime DESC")
List<Chat> findRecentChatSessions(@Param("loggedInUser") User loggedInUser);

}
