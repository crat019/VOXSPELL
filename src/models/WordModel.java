package models;

import VoxspellApp.Voxspell;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Created by edson on 15/09/16.
 */
public class WordModel implements Resettable, Serializable {
    List<Level> _levelList;//arraylist of Level objects
    private  int _currentLevel;
    private int _accessLevel = 0;//int of user's highest accessible level
    private List<int[]> _accuracyList;//list of int arrays showing statistic for each level
    private int[] _overallStatstic;//int array of overall frequency of each mastered(2),faulted(1),failed(0)

    public WordModel(String spellingListPath) throws IOException{
        //initialise fields
        _accuracyList = new ArrayList<int[]>();
        _overallStatstic = new int[3];



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
    //TODO should consider reset to make a new WordModel object
    public void reset(){
        for (Level level : _levelList){
            level.reset();
        }
        _overallStatstic=new int[3];
    }

    /**
     * updates model; called whenever user wishes to see the statistics.
     */
    public void updateStatistics(){
        for (int i = 0; i < Voxspell.COUNT; i++){
            Level currentLevel = _levelList.get(i+1);//i+1 because level starts at 1 not 0.
            int[] statusFrequency = new int[3];//make new status freq unique to each level and update overall simultaneous
            statusFrequency[0] = currentLevel.getFailedFrequency();
            _overallStatstic[0] += currentLevel.getFailedFrequency();//add to overall accuracy integer array
            statusFrequency[1] = currentLevel.getFaultedFrequency();//create int array representing accuracy for each level
            _overallStatstic[1] += currentLevel.getFaultedFrequency();
            statusFrequency[2] = currentLevel.getMasterFrequency();
            _overallStatstic[2] += currentLevel.getMasterFrequency();
            _accuracyList.add(statusFrequency);
        }
    }

    public int[] findAccuracy(int level){
        return _accuracyList.get(level);
    }

    public void updateLevel(int level) {
        this._currentLevel = level;
    }

    public int[] getOverall(){
        return _overallStatstic;
    }

    public List<Word> getSpellingList() {
        Level level = _levelList.get(_currentLevel-1);
        return level.getWords();
    }

    public int getAccessLevel(){
        return _accessLevel;
    }

    public Level getLevel(int level){
        return _levelList.get(level);
    }

}
