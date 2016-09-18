package models;


import VoxspellApp.Voxspell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by edson on 15/09/16.
 */
public class Level implements Resettable, Serializable{

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

    /**
     * This method shuffles the word list and returns a selected list of words. If there are
     * less than 10 words in the list, the list is returned and a spelling quiz can be done
     * with less than 10 words.
     * @return List<Word>
     */
   public List<Word> getWords() {
       Collections.shuffle(_wordList);
       if (_wordList.size() < Voxspell.COUNT) {
           return _wordList;
       } else {
           List<Word> selectedWords = new ArrayList<Word>(Voxspell.COUNT);
           for (int i = 0; i < Voxspell.COUNT; i++) {
               selectedWords.add(_wordList.get(i));
           }
           return selectedWords;
       }
   }
}
