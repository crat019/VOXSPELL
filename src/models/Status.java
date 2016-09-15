package models;

/**
 * Created by edson on 15/09/16.
 */
public enum Status {
    Mastered(3),
    Faulted(2),
    Failed(1),
    Unseen(0);

    private int _statusNumber;

    Status(int number){
        _statusNumber = number;
    }

    public int getStatus(){
        return _statusNumber;
    }


}
