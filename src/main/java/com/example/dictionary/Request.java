package com.example.dictionary;

import java.io.Serializable;

enum RequestType{
    GET,
    ADD,
    REMOVE,
    DISCONNECT,
}

public class Request implements Serializable {
    Word word;
    RequestType requestType;
    Request (Word word,RequestType requestType){
        this.word = word;
        this.requestType = requestType;
    }
}
