package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Created by edson on 15/09/16.
 */
public class WordModel implements Resettable, Serializable {
    List<Level> _levelList;
    private  int _currentLevel;

    public WordModel(String spellingListPath) throws IOException{
        int currentLevelValue = 1;
        Level currentLevel;
        String currentLine;
        FileReader fr = new FileReader(spellingListPath);
        BufferedReader br = new BufferedReader(fr);
        currentLine = br.readLine();
        if (!currentLine.substring(0,1).equals("%")){//check if word not level
            throw new IOException();//TODO create exception class for invalid IO of text input
        } else {
            currentLevel = new Level(currentLevelValue);
            _levelList = new ArrayList<>();
            _levelList.add(currentLevel);
            currentLevelValue+=1;
        }
        while((currentLine = br.readLine())!=null){
            if (!currentLine.substring(0,1).equals("%")){//check if word not level
                currentLevel.addWord(currentLine);//add to level object
            } else {
                currentLevel = new Level(currentLevelValue);
                _levelList.add(currentLevel);
                currentLevelValue+=1;
            }

        }
    }

    //reset signal propagate to contained object
    public void reset(){
        for (Level level : _levelList){
            level.reset();
        }
    }

    public void updateLevel(int level) {
        this._currentLevel = level;
    }

    public List<Word> getSpellingList() {
        Level level = _levelList.get(_currentLevel-1);
        return level.getWords();
    }
}
