package models;

import VoxspellApp.SpellingQuizScene;

import java.util.List;

/**
 * Created by ratterz on 16/09/16.
 */
public class SpellingQuiz {

    private WordModel _wordModel;
    private boolean _setUpFlag;
    private boolean _attemptFlag;
    private int _position;
    private List<Word> _spellingList;

    public void setUpSpellingQuiz(WordModel wordModel) {
        this._wordModel = wordModel;
        _setUpFlag = false;
        _attemptFlag = false;
        _position = 0;
        _spellingList = _wordModel.getSpellingList();
        spellingLogic("");
    }

    public void spellingLogic(String userinput) {
        if (!_setUpFlag) {
            System.out.println("Please Spell " + _spellingList.get(_position).getWord());
            // Festival please spell the word
            _setUpFlag = true;
            return;
        } else if (!_attemptFlag) {
            if (_spellingList.get(_position).compareWords(userinput)) {
                System.out.println("Correct");
                // Festival Correct
                // Update Mastered on word
                _position++;
            } else {
                System.out.println("Incorrect");
                System.out.println("Please Spell " + _spellingList.get(_position).getWord());
                // Festival Incorrect Please Try Again
                _attemptFlag = true;
                return;
            }
        } else {
            if (_spellingList.get(_position).compareWords(userinput)) {
                System.out.println("Correct");
                // Festival Correct
                // Update Faulted on Word
            } else {
                System.out.println("Incorrect");
                // Festival Incorrect
                // Update Failed on word
            }
            _position++;
            _attemptFlag = false;
        }

        if (_position < _spellingList.size()) {
            System.out.println("Please Spell " + _spellingList.get(_position).getWord());
            // Festival please spell next word
        } else {
            return;
        }
    }

}
