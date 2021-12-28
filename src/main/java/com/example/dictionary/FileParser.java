package com.example.dictionary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class FileParser {
    FileReader fr;
    BufferedReader br;

    FileParser(String filePath) throws FileNotFoundException {
        fr =new FileReader(filePath);
        br =new BufferedReader(fr);
    }

    public HashMap<String, Word> parseToDict(){
        HashMap<String, Word> dictionary = new HashMap<>();
        try{
            String st;
            String word = "";
            String definition;

            while ((st = br.readLine()) != null){
                //In case there is empty lines in between
                if(st.length() == 0){
                    continue;
                }
                if(st.charAt(0) == '#'){
                    //remove the word indicator
                    word = st.substring(1, st.length());
                }
                else if(st.charAt(0) == '*'){
                    //remove the meaning indicator
                    definition = st.substring(1,st.length());
                    if(dictionary.get(word) == null){
                        dictionary.put(word,new Word(word));
                    }
                    dictionary.get(word).addDefinition(definition);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return dictionary;
    }
}
