package models;

import javafx.concurrent.Task;

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
    private Task _festivalTask;
    private Status _status;
    private boolean _finished;
    private String _phrase;

    public void setUpSpellingQuiz(WordModel wordModel) {
        this._wordModel = wordModel;
        _finished = false;
        _setUpFlag = false;
        _attemptFlag = false;
        _position = 0;
        _spellingList = _wordModel.getSpellingList();
        _phrase = "";
        spellingLogic("");
    }

    public void spellingLogic(String userinput) {
        if (!_setUpFlag) {
            _phrase = "Please Spell " + _spellingList.get(_position).getWord();
            System.out.println(_phrase);
            startFestivalThread(_phrase);
            _setUpFlag = true;
            _status = Status.Unseen;
            return;
        } else if (!_attemptFlag) {
            //correct on first try
            if (_spellingList.get(_position).compareWords(userinput)) {
                _phrase = "Correct..";
                _spellingList.get(_position).countUp(Status.Mastered);
                _position++;
                _status = Status.Mastered;
                _spellingList.get(_position-1).countUp(_status);
            } else {
                _phrase = "Incorrect.. Please Try Again.. " + _spellingList.get(_position).getWord() + "... " +  _spellingList.get(_position).getWord();
                startFestivalThread(_phrase);
                System.out.println(_phrase);
                _attemptFlag = true;
                _status = Status.Unseen;
                return;
            }
        } else {
            //correct on second try
            if (_spellingList.get(_position).compareWords(userinput)) {
                _phrase = "Correct..";
                _spellingList.get(_position).countUp(Status.Faulted);
                _status = Status.Faulted;
                _spellingList.get(_position-1).countUp(_status);
            } else {
                //incorrect on both tries
                _phrase = "Incorrect..";
                _spellingList.get(_position).countUp(Status.Failed);
                _status = Status.Failed;
                _spellingList.get(_position-1).countUp(_status);
            }
            _position++;
            _attemptFlag = false;
        }



        if (_position < _spellingList.size()) {
            _phrase = _phrase + " Please Spell " + _spellingList.get(_position).getWord();
            startFestivalThread(_phrase);
            System.out.println(_phrase);
        } else {
            startFestivalThread(_phrase);
            _finished = true;
            return;
        }


    }


    public Status getStatus() {
        return this._status;
    }

    public boolean getFinishedStatus() {
        return this._finished;
    }

    private void startFestivalThread(String phrase) {
        _festivalTask = new Task() {
            @Override
            protected Object call() throws Exception {
                Festival.festivalTTS(_phrase);
                return null;
            }
        };

        new Thread(_festivalTask).start();
    }

    public void repeatWord() {
        startFestivalThread(_spellingList.get(_position).getWord());
    }

}
