package sample;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class spellingAidModel implements TableModel{
	private List<String> _wordList;
	private List<String> _failList;
	private Map<String,int[]> _wordMap;
	private HashSet<String> _failedSet;
	
	private String[] columnNames = {"Word", "#Fails", "#Faults", "#Mastered"};
	private String[] _keys;
	
	
	public spellingAidModel() throws IOException{
		_wordList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("wordlist"));
		try {
		    String line = br.readLine();

		    while (line != null) {
		        _wordList.add(line);
		        line = br.readLine();
		    }
		} finally {
		    br.close();
		}
		//http://beginnersbook.com/2013/12/how-to-serialize-hashmap-in-java/
		try {//file exists
			FileInputStream fis = new FileInputStream("wordmap.ser");
	        ObjectInputStream ois = new ObjectInputStream(fis);
	        _wordMap = (Map)ois.readObject();
	        ois.close();
	        fis.close();
			fis = new FileInputStream("failedSet.ser");
	        ois = new ObjectInputStream(fis);
	        _failedSet = (HashSet)ois.readObject();
	        ois.close();
	        fis.close();
	        
		} catch (FileNotFoundException e){//file not found so create new history file
			FileOutputStream fos = new FileOutputStream("wordmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
			_wordMap = new HashMap<String, int[]>();
			oos.writeObject(_wordMap);
	        fos.close();
	        oos.close();
	        fos = new FileOutputStream("failedSet.ser");
	        oos = new ObjectOutputStream(fos);
	        _failedSet = new LinkedHashSet<String>();
	        oos.writeObject(_failedSet);
	        fos.close();
	        oos.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String[] getReviewWords(){
		_failList = new ArrayList<String>(_failedSet);
		Collections.shuffle(_failList);
		int size = _failList.size();
		String[] testWords;
		if (size<3){
			testWords = new String[size];
			int i = 0;
			for(String word: _failList){
				testWords[i] = word;
				i++;
			}
		} else {
			testWords = new String[3];
			for (int i = 0; i < 3; i++){
				testWords[i] = _failList.get(i);
			}
			
		}
		return testWords;
	}

	
	public String[] getWords(){
		Collections.shuffle(_wordList); //http://stackoverflow.com/questions/4702036/take-n-random-elements-from-a-liste
		try{
			String[] returnWords = {_wordList.get(0), _wordList.get(1), _wordList.get(2)};
			return returnWords;
			
		} catch (NullPointerException e){
			System.out.println("not enough words");
		}
		return null;
	}
	
	public void update(String word, boolean isRight, int count, boolean isNew){
		if(isRight){
			if(count==0){//user gets it right first time
				addToMap(word, 2);
				if (!isNew){//remove from list of fail if review spelling quiz
					_failedSet.remove(word);
				}
				writeToHistory();
			} else {//user gets it right second time
				addToMap(word,1);
				writeToHistory();
			}
		} else {//user fails word
			addToMap(word,0);
			_failedSet.add(word);
			writeToHistory();
		}
	}
	
	private void addToMap(String word, int status){
		if (_wordMap.containsKey(word)){
			_wordMap.get(word)[status]++;
		} else {//not in map
			int[] intArray = {0,0,0};
			intArray[status]++;
			_wordMap.put(word, intArray);
		}
	}
	
	protected void clearHistory(){
		_wordMap = new HashMap<String, int[]>();
		_failedSet = new LinkedHashSet<String>();
		writeToHistory();
	}
	
	private void writeToHistory(){
		try {//file exists
			FileOutputStream fos = new FileOutputStream("wordmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(_wordMap);
	        fos.close();
	        oos.close();
	        fos = new FileOutputStream("failedSet.ser");
	        oos = new ObjectOutputStream(fos);
	        oos.writeObject(_failedSet);
	        fos.close();
	        oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0){
			return String.class;
		} else {
			return int.class;
		}
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return columnNames[columnIndex];
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return _wordMap.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		List<String> wordList = new ArrayList<String>(_wordMap.keySet());
		Collections.sort(wordList);
		String word = wordList.get(rowIndex);
		if (columnIndex == 0){
			return word;
		} else {
			return _wordMap.get(word)[columnIndex-1];
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}
	
}
