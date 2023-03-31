package com.example.server.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.server.model.Character;
import com.example.server.model.Comment;
import com.example.server.service.CharacterService;


@Controller
@CrossOrigin("*")
public class CharacterController {
    
    @Autowired
    private CharacterService charSvc;

    @GetMapping(path="/api/characters")
    public ResponseEntity<List<Character>> getCharactersByName(@RequestParam String nameStartsWith,
        @RequestParam(defaultValue = "0") Integer offset) throws NoSuchAlgorithmException {

        List<com.example.server.model.Character> characters = charSvc.getCharactersByName(nameStartsWith, offset);

        return ResponseEntity.ok(characters);
    }

    @GetMapping(path="/api/character/{characterId}")
    public ResponseEntity<Character> getCharacterById(@PathVariable String characterId) throws NoSuchAlgorithmException {

        Character character = charSvc.getCharacterById(characterId);

        return ResponseEntity.ok(character);

    }

    @PostMapping(path="/api/character/{characterId}")
    public ResponseEntity<Comment> postCommentById(@PathVariable String characterId, @RequestBody String newComment) {
        System.out.println("test" + characterId + " comment " + newComment);
        Comment newC = new Comment();
        newC.setComment(newComment);
        System.out.println(newC.getComment() + "NEWC GET COMMENT");
        charSvc.postCommentById(characterId, newC);

        return ResponseEntity.ok(newC);
    }
}
