package com.example.server.repo;

import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.server.model.Character;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Repository
public class CharacterRepo {

    private String privateKey = "";
    private String publicKey = "7764077a55906f77b4583298794b20ec";
    public static final String MARVEL_CHARACTERS_API = "https://gateway.marvel.com:443/v1/public/characters";

    public List<Character> getCharactersByName(String searchInput, Integer offset) throws NoSuchAlgorithmException {
        Date date = new Date();
        long timeStamp = date.getTime();


        MessageDigest md = MessageDigest.getInstance("MD5");
        String keyBeforeHash = timeStamp+privateKey+publicKey;
        System.out.println("key before hash: " + keyBeforeHash);

        byte[] messageDigest = md.digest(keyBeforeHash.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);

        String apiKey = no.toString(16);
        // while (apiKey.length()<32){
        //     apiKey = "0" + apiKey;
        // }

        System.out.println("key after hash: " + apiKey);
        //md5(ts+privateKey+publicKey)

        String url = UriComponentsBuilder.fromUriString(MARVEL_CHARACTERS_API)
                            .queryParam("nameStartsWith", searchInput)
                            .queryParam("orderBy", "name")
                            .queryParam("apikey", publicKey)
                            .queryParam("ts", timeStamp)
                            .queryParam("hash", apiKey)
                            .queryParam("offset", offset)
                            .queryParam("limit","20")
                            .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .build();
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> res = null;

        res = template.exchange(req, String.class);

        String payload = res.getBody();
        // System.out.println("payload " + payload);
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject charactersRes = reader.readObject();
        // System.out.println("charactersRes object " + charactersRes);

        JsonArray jsonArr = charactersRes.getJsonObject("data").getJsonArray("results");
        System.out.println("jsonarray -----> " + jsonArr);

        return jsonArr.stream()
                    .map(c -> c.asJsonObject())
                    .map(Character::toCharacter)
                    .toList();
    }
    
}
