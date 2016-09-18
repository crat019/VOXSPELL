package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edson on 15/09/16.
 */
public class WordModel implements Resettable, Serializable {
    List<Level> _levelList;
    private  int _currentLevel;
    private File _file = new File(".spellingData.ser");

    public WordModel(String spellingListPath) throws IOException{
        if (_file.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(_file));
            try {
                WordModel data = (WordModel)ois.readObject();
                _currentLevel = data.getCurrentLevel();
                _levelList = data.getLevelList();
                ois.close();
            } catch (ClassNotFoundException e) {

            }
        } else {
            try {
                _file.createNewFile();
            } catch (IOException e) {

            }

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
    }

    //reset signal propagate to contained object
    public void reset(){
        for (Level level : _levelList){
            level.reset();
        }
    }

    public void saveDataStorage() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(_file));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Level> getLevelList() {
        return this._levelList;
    }

    public void updateLevel(int level) {
        this._currentLevel = level;
    }

    public List<Word> getSpellingList() {
        Level level = _levelList.get(_currentLevel-1);
        return level.getWords();
    }

    public int getCurrentLevel() {
        return this._currentLevel;
    }

    public int getNumberOfLevels() {
        return this._levelList.size();
    }
}
