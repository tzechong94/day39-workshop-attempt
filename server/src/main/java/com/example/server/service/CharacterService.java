package com.example.server.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.server.model.Character;
import com.example.server.model.Comment;
import com.example.server.repo.CharacterRepo;


@Service
public class CharacterService {

    @Autowired
    private CharacterRepo charRepo;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<com.example.server.model.Character> getCharactersByName(String searchInput, Integer offset) throws NoSuchAlgorithmException {
        return charRepo.getCharactersByName(searchInput, offset);
    }

    public Character getCharacterById(String characterId) throws NoSuchAlgorithmException {
        Character charReturned = charRepo.getCharacterById(characterId);
        redisTemplate.opsForHash()
        .put("CACHE","charactercache",charReturned.toString());

        redisTemplate.expire("CACHE",3600, TimeUnit.SECONDS);

        charReturned.setComments(charRepo.findCommentsById(characterId));
        return charReturned;


    }

    public void postCommentById(String characterId, Comment newC) {
        charRepo.postCommentById(characterId, newC);
    }
    
}
