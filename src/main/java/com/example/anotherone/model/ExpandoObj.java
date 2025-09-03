package com.example.anotherone.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "RegisterUser")
public class ExpandoObj{
    public String id;
    public Map<String,Object> data = new HashMap<>();

}
