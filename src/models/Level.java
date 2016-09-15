package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edson on 15/09/16.
 */
public class Level implements Resettable{

    private int _level;
    private List<Word> _wordList;

    public Level(int level){
        _wordList = new ArrayList<Word>();
        _level = level;
    }

    protected void addWord(String word){
        _wordList.add(new Word(word));
    }

    public void reset(){
        for (Word word: _wordList){
            word.reset();
        }
    }

}
