package com.example.dictionary;

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable {
    String text;
    ArrayList<String> definitions;

    Word(String text){
        this.text = text;
        definitions = new ArrayList<>();
    }

    Word(String text, ArrayList<String> definitions){
        this.text = text;
        this.definitions = definitions;
    }

    public void addDefinition(String definition){
        this.definitions.add(definition);
    }
}
