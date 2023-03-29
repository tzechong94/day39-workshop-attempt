package com.example.server.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.server.service.CharacterService;

@Controller
@CrossOrigin("*")
public class CharacterController {
    
    @Autowired
    private CharacterService charSvc;

    @GetMapping(path="/api/characters")
    public ResponseEntity<List<com.example.server.model.Character>> getCharactersByName(@RequestParam String nameStartsWith,
        @RequestParam(defaultValue = "0") Integer offset) throws NoSuchAlgorithmException {

        List<com.example.server.model.Character> characters = charSvc.getCharactersByName(nameStartsWith, offset);

        return ResponseEntity.ok(characters);
    }
}
