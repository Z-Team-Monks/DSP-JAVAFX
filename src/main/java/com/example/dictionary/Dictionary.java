package com.example.dictionary;

import java.util.HashMap;

public class Dictionary {
    static HashMap<String,Word> map = new HashMap<>();
    final String WORD_NOT_FOUND = "Word not found in the dictionary";
    final String WORD_ALREADY_EXISTS = "Word already exists in the dictionary";
    final String AT_LEAST_ONE_DEFINITION = "There should be at least one definition";

    //singleton implementation
    private Dictionary(){
        try {
            FileParser fp = new FileParser("C:\\Users\\Natnael Abay Akalu\\IdeaProjects\\dictionary\\src\\main\\java\\com\\example\\dictionary\\dictionary.txt");
            map = fp.parseToDict();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Dictionary dictionary_instance = null;

    public static Dictionary getInstance(){
        if(dictionary_instance == null){
            dictionary_instance = new Dictionary();
        }
        return dictionary_instance;
    }

    public class DictionaryException extends Exception {
        public DictionaryException(String errorMessage) {
            super(errorMessage);
        }
    }


    //class methods

    /*
    @params text the word to be searched
    @return word object that contains array of definitions
    throws an error if the word is not found in the dictionary
    * */
    public Word get(String text) throws DictionaryException {
        if(map.get(text) == null){
            throw new DictionaryException(WORD_NOT_FOUND);
        }
        return map.get(text);
    }

    /*
    @param word object containing text and definitions
    throws an error if word already exists in the dictionary
    throws an error if there is no definition provided
    * */
    public void add(Word word) throws DictionaryException {
        if(map.get(word.text) != null){
            throw new DictionaryException(WORD_ALREADY_EXISTS);
        }
        if(word.definitions.size() == 0){
            throw new DictionaryException(AT_LEAST_ONE_DEFINITION);
        }
        map.put(word.text,word);
    }


    /*
    @param text word to be removed
    throws an error if word doesn't exist
    * */
    public void remove(String text) throws DictionaryException {
        if(map.get(text) == null){
            throw new DictionaryException(WORD_NOT_FOUND);
        }
        map.remove(text);
    }
}

