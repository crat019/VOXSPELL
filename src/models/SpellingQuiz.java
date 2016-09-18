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
    private Thread _festivalThread;
    private Status _status;

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
            String phrase = "Please Spell " + _spellingList.get(_position).getWord();
            System.out.println(phrase);
            startFestivalThread(phrase);
            _setUpFlag = true;
            _status = Status.Unseen;
            return;
        } else if (!_attemptFlag) {
            if (_spellingList.get(_position).compareWords(userinput)) {
                startFestivalThread("Correct");
                _spellingList.get(_position).countUp(Status.Mastered);
                _position++;
                _status = Status.Mastered;
            } else {
                String phrase = "Incorrect.. Please Try Again.. " + _spellingList.get(_position).getWord() + "... " +  _spellingList.get(_position).getWord();
                startFestivalThread(phrase);
                System.out.println(phrase);
                _attemptFlag = true;
                _status = Status.Unseen;
                return;
            }
        } else {
            if (_spellingList.get(_position).compareWords(userinput)) {
                startFestivalThread("Correct");
                _spellingList.get(_position).countUp(Status.Faulted);
                _status = Status.Faulted;
            } else {
                startFestivalThread("Incorrect");
                _spellingList.get(_position).countUp(Status.Failed);
                _status = Status.Failed;
            }
            _position++;
            _attemptFlag = false;
        }

        if (_position < _spellingList.size()) {
            String phrase = "Please Spell " + _spellingList.get(_position).getWord();
            startFestivalThread(phrase);
            System.out.println(phrase);
        } else {
            return;
        }
    }

    public Status getStatus() {
        return this._status;
    }

    private void startFestivalThread(String phrase) {
        _festivalThread = new Thread() {
            @Override
            public void run() {
                Festival.festivalTTS(phrase);
            }
        };

        _festivalThread.start();
    }

}
