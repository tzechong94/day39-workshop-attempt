package com.example.server.repo;

import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.server.model.Character;
import com.example.server.model.Comment;
import com.mongodb.client.result.UpdateResult;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Repository
public class CharacterRepo {

    @Autowired 
    private MongoTemplate template;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String privateKey = "";
    private String publicKey = "7764077a55906f77b4583298794b20ec";
    public static final String MARVEL_CHARACTERS_API = "https://gateway.marvel.com:443/v1/public/characters";

    public List<Character> getCharactersByName(String searchInput, Integer offset) throws NoSuchAlgorithmException {
        Date date = new Date();
        long timeStamp = date.getTime();

        // String searchInputChanged = searchInput.replace(" ", "%20");

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
        System.out.println(searchInput + "SEARCH INPUT");
        String url = UriComponentsBuilder.fromUriString(MARVEL_CHARACTERS_API)
                            .queryParam("nameStartsWith", searchInput)
                            .queryParam("orderBy", "name")
                            .queryParam("apikey", publicKey)
                            .queryParam("ts", timeStamp)
                            .queryParam("hash", apiKey)
                            .queryParam("offset", offset)
                            .queryParam("limit","20")
                            .build()
                            .toUriString();

        System.out.println(url + "URL");

        RequestEntity<Void> req = RequestEntity.get(url)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> res = null;

        res = template.exchange(req, String.class);

        String payload = res.getBody();
        System.out.println("payload " + payload);
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject charactersRes = reader.readObject();
        // System.out.println("charactersRes object " + charactersRes);

        JsonArray jsonArr = charactersRes.getJsonObject("data").getJsonArray("results");
        System.out.println("jsonarray -----> " + jsonArr);
        
        redisTemplate.delete("CACHE");

        redisTemplate.opsForHash()
                .put("CACHE","marvelcache",charactersRes.toString());

        redisTemplate.expire("CACHE",3600, TimeUnit.SECONDS);

        return jsonArr.stream()
                    .map(c -> c.asJsonObject())
                    .map(Character::toCharacter)
                    .toList();

        
    }

    public Character getCharacterById(String characterId) throws NoSuchAlgorithmException {

        // check if redis has it, if not, fetch from API
        Map<Object, Object> data = redisTemplate.opsForHash().entries("CACHE");
        // System.out.println(data);

        String dataToString = data.get("marvelcache").toString();
        System.out.println(dataToString + " VALUE OF MAP");

        JsonReader jsonReader = Json.createReader(new StringReader(dataToString));
        JsonObject obj = jsonReader.readObject();
        JsonArray jsonArr = obj.getJsonObject("data").getJsonArray("results");
        List<Character> listOfCharactersFromRedis = jsonArr.stream()
                    .map(c -> c.asJsonObject())
                    .map(Character::toCharacter)
                    .toList();
        Character character = new Character();

        for (Integer i = 0; i < listOfCharactersFromRedis.size(); i++) {
            if (Integer.parseInt(characterId) == listOfCharactersFromRedis.get(i).getId()){
                System.out.println("returning char: " + listOfCharactersFromRedis.get(i).getName());

                character = listOfCharactersFromRedis.get(i);
                System.out.println(character.getDescription() + character.getName() + " test character");
                System.out.println(findCommentsById(characterId));

                character.setComments(findCommentsById(characterId));
                System.out.println("COMMMENTSSS " + character.getComments());
                redisTemplate.delete("CHARACTER");

                redisTemplate.opsForHash()
                        .put("CHARACTER","charcache", character.toString());
        
                redisTemplate.expire("CHARACTERCACHE",3600, TimeUnit.SECONDS);
        
                return character;
            }
        }
            Date date = new Date();
            long timeStamp = date.getTime();
    
            // String searchInputChanged = searchInput.replace(" ", "%20");
    
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
            String url = UriComponentsBuilder.fromUriString(MARVEL_CHARACTERS_API+"/"+characterId)
                                .queryParam("apikey", publicKey)
                                .queryParam("ts", timeStamp)
                                .queryParam("hash", apiKey)
                                .build()
                                .toUriString();
    
            System.out.println(url + "URL");
    
            RequestEntity<Void> req = RequestEntity.get(url)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .build();
    
            RestTemplate template = new RestTemplate();
    
            ResponseEntity<String> res = null;
    
            res = template.exchange(req, String.class);
            String payload = res.getBody();
            System.out.println("payload " + payload);
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject charactersRes = reader.readObject();

            JsonArray charArr = charactersRes.getJsonObject("data").getJsonArray("results");
            
            redisTemplate.delete("CHARACTERCACHE");

            redisTemplate.opsForHash()
                    .put("CHARACTERCACHE","charcache",charArr.get(0).toString());
    
            redisTemplate.expire("CHARACTERCACHE",3600, TimeUnit.SECONDS);


            return charArr.stream()
            .map(c -> c.asJsonObject())
            .map(Character::toCharacter)
            .toList().get(0);

        }

        public List<String> findCommentsById(String characterId) {
            Criteria criteria = Criteria.where("id").is(characterId);
            Query query = Query.query(criteria);
            query.fields().exclude("_id").include("comments");
            List<Document> resultList = template.find(query, Document.class, "comments");

            if (resultList.size() > 0) {

                Document result = resultList.get(0);
                JsonReader reader = Json.createReader(new StringReader(result.toJson()));
                
                JsonObject commentObject = reader.readObject();
                JsonArray jsonArr = commentObject.getJsonArray("comments");
                
                List<String> listOfString = new ArrayList<>();
                
                for (JsonValue x : jsonArr) {
                    listOfString.add(x.toString());
                }
                
                System.out.println(listOfString + "Results");
                
                return listOfString;
            }

            return null;

        }

        public void postCommentById(String characterId, Comment newC) {
            Criteria criteria = Criteria.where("id").is(characterId);
            Query query = Query.query(criteria);
            System.out.println(characterId + newC.getComment() + "REPO");
            Update updateOps = new Update().push("comments", newC.getComment());
            UpdateResult updateResult = template.upsert(query, updateOps, "comments");

            if (updateResult == null) {
                System.out.println("not updated");
            }
            else {
                System.out.println(updateResult.getUpsertedId() + " document(s) updated..");
            }
        }
    }

