package models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edson on 15/09/16.
 */
public class Word implements Resettable{
    private String _word;
    private int _level;
    private Status _status;
    int[] _countList;



    public Word(String word){
        _word = word;
        _status = Status.Unseen;//word yet to be seen ingame
        _countList = new int[3];
    }

    public void countUp(Status status){
        _countList[status.getStatus()] += 1;
    }

    public void reset(){
        _countList = new int[3];
        _status = Status.Unseen;
    }

    @Override
    /**
     * This method overrides the default equals method so that the words will be
     * compared to their strings and not their actual objects
     */
    public boolean equals(Object object) {
        if (object instanceof Word) {
            return ((Word)object)._word.equals(this._word);
        } else {
            return false;
        }
    }

}
