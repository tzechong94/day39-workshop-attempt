package com.example.server.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.repo.CharacterRepo;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepo charRepo;

    public List<com.example.server.model.Character> getCharactersByName(String searchInput, Integer offset) throws NoSuchAlgorithmException {
        return charRepo.getCharactersByName(searchInput, offset);
    }
    
}
