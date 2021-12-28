package com.example.dictionary;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    String failureMessage;
    Word word;

    Response(){
        this.failureMessage = "";
        this.word = new Word("No word",new ArrayList<>());
    }
    Response(Word word){
        this.failureMessage = "";
        this.word = word;
    }
    Response(String failureMessage){
        this.failureMessage = failureMessage;
        this.word = new Word("No word",new ArrayList<>());
    }
}
