package com.example.server.model;

import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Character {

    private Integer id;
    private String name;
    private String description;
    private String thumbnail;
    private List<String> comments;


    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public static Character toCharacter(JsonObject obj){
        Character character = new Character();
        character.setId(obj.getInt("id"));
        character.setName(obj.getString("name"));
        character.setDescription(obj.getString("description"));
        character.setThumbnail(obj.getJsonObject("thumbnail").getString("path"));
        return character;
    }

    public JsonObject toJson() {
        JsonObject obj = Json.createObjectBuilder()
                        .add("id", this.getId())
                        .add("description", this.getDescription())
                        .add("thumbnail", this.getThumbnail())
                        .add("name", this.getName())
                        .build();
        return obj;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getThumbnail() {
        return thumbnail;
    }


    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}