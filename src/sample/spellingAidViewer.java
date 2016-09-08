package sample;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class spellingAidViewer extends JFrame{
	private spellingAidModel _model;
	
	private JPanel menuPanel;
	private JButton newQuiz;
	private JButton reviewQuiz;
	private JButton clearStats;
	private JButton viewStats;
	
	private JLabel _spellPrompt;
	private JTextField _userField;
	private JButton _checkButton = new JButton("Check");
	private JButton _nextButton = new JButton("Next");
	private JButton _returnButton;
	
	private String[] _testWords;
	private int _counter;
	private int _faultCounter;
	
	private JTextArea _programTexts;
	
	private boolean _notInputted;
	private boolean _isNew;
	
	private final int COUNT = 3;
	
	private SwingWorker _ttsWorker;
	
	
	public spellingAidViewer() throws IOException{
		
		super("Spelling Aid");

		_model = new spellingAidModel();
		
		setLayout(new BorderLayout());
		
		JLabel greetingLabel = new JLabel("Greetings! Welcome to Spelling Aid!");
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(2,2));
		menuPanel.setSize(300,150);
		newQuiz = new JButton("New Quiz");
		reviewQuiz = new JButton("Review Quiz");
		clearStats = new JButton("Clear Stats");
		viewStats = new JButton("View Statistics");
		menuPanel.add(newQuiz);
		menuPanel.add(reviewQuiz);
		menuPanel.add(clearStats);
		menuPanel.add(viewStats);
		setupEventHandlers();
		add(menuPanel, BorderLayout.PAGE_START);
		
		_counter=0;
		_faultCounter=0;
		
		_programTexts = new JTextArea("Welcome to Spelling Aid!\n");
		_programTexts.setEditable(false);
		JScrollPane textScrollPane = new JScrollPane(_programTexts);
		add(textScrollPane, BorderLayout.CENTER);
		
		JPanel userPanel = new JPanel();
		JPanel insertPanel = new JPanel();
		insertPanel.setLayout(new BorderLayout());
		userPanel.setLayout(new GridLayout(2,1));
		_userField = new JTextField();
		_checkButton.setEnabled(false);//grey out check
		insertPanel.add(new JLabel("Enter word:"), BorderLayout.WEST);
		insertPanel.add(_userField);
		insertPanel.add(_checkButton, BorderLayout.EAST);
		userPanel.add(insertPanel);
		add(userPanel, BorderLayout.PAGE_END);
		pack();
		setSize(600,500);
	    setLocationRelativeTo(null);
	    setResizable(true);
		setVisible(true);
		
		
	}
	
	private SwingWorker setupSpellWorker(int count){
		SwingWorker spellWorker = new SwingWorker(){

			@Override
			protected Object doInBackground() throws Exception {
				try {

					String word = _testWords[count];
					String[] wordList = word.split("");
					String constituent="";
					for(String letter : wordList){
						constituent += letter + " ... ";
					}
					String command = "echo The spelling of  "+ _testWords[count] + 
							" is " + constituent + ". Try spelling it now | festival --tts";
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

					Process process = pb.start();

					BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					
					int exitStatus = process.waitFor();
					
					if (exitStatus == 0) {
						String line;
						while ((line = stdout.readLine()) != null) {
							System.out.println(line);
						}
					} else {
						String line;
						while ((line = stderr.readLine()) != null) {
							System.err.println(line);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		
		};
		return spellWorker;
	}
	
	private SwingWorker setupSwingWorkers(int count){
		SwingWorker ttsWorker = new SwingWorker(){

			@Override
			protected Object doInBackground() throws Exception {
				try {
					_programTexts.append("Spell word " + (count+1) +" of " + _testWords.length + ": ");
					String command = "echo Please spell "+
							_testWords[count]+ " ... " +
							_testWords[count] + " | festival --tts";
					ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

					Process process = pb.start();

					BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					
					int exitStatus = process.waitFor();
					
					if (exitStatus == 0) {
						String line;
						while ((line = stdout.readLine()) != null) {
							System.out.println(line);
						}
					} else {
						String line;
						while ((line = stderr.readLine()) != null) {
							System.err.println(line);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		
			
		};
		return ttsWorker;
	}
	

	private boolean isAlphabet(String word){
		return word.matches("[a-zA-z]+");
	}
	
	
	private void setupEventHandlers(){
		_checkButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean spellWord = false;
				
				String attempt = _userField.getText();
				if (isAlphabet(attempt)){
					//convert to lower case
					attempt = attempt.toLowerCase();
					if (attempt.equals(_testWords[_counter])){
						if (_faultCounter == 0){//user gets it right first time
							_programTexts.append("Correct!\n");
							_model.update(_testWords[_counter],true, 0, _isNew);
							_counter++;
						} else {//user gets it right the second time
							_faultCounter=0;//reset
							_programTexts.append("Correct!\n");
							_model.update(_testWords[_counter],true, 1, _isNew);
							_counter++;
						}
					} else {//user gets it wrong
						if (_faultCounter == 0){//user wrong first time
							_programTexts.append("Incorrect! Try Again!\n");
							_faultCounter++;
						} else {
							_programTexts.append("Incorrect!\n");
							_faultCounter = 0;//reset
							_model.update(_testWords[_counter],false, 0, _isNew);
							//if review quiz, spell it out for them
							if(!_isNew){
								spellWord = true;
							} else {
								_counter++;
							}
						}

					}
					if (_counter==_testWords.length){//check if all three words are asked
						_programTexts.append("Game Finished!\n");
						_counter = 0;//reset
						_checkButton.setEnabled(false);
						enableMenu(true);
					} else if (spellWord){//check if need to spell word
						SwingWorker spellWorker = setupSpellWorker(_counter);
						spellWorker.execute();
						spellWord = false;//reset
					} else {//say the next word via TTS
						SwingWorker toSpeech = setupSwingWorkers(_counter);
						toSpeech.execute();

					}

				} else {//user does not type only letters
					_programTexts.append("Invalid input! Please only type characters.\n");
				}
				
			}

		});

		newQuiz.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				enableMenu(false);
				_isNew = true;
				_testWords = _model.getWords();
				_checkButton.setEnabled(true);
				SwingWorker ttsWorker = setupSwingWorkers(0);
				ttsWorker.execute();//bash fest say word1
				
			}
			
		});
		reviewQuiz.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				_isNew = false;
				_programTexts.append("Review Quiz!\n");
				_testWords = _model.getReviewWords();
				if(_testWords.length == 0){
					_programTexts.append("Not enough words! Please play a new game!\n");
				} else {
					enableMenu(false);
					_checkButton.setEnabled(true);
					SwingWorker toSpeech = setupSwingWorkers(0);
					toSpeech.execute();

					
				}
			}
		});
		clearStats.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				_programTexts.append("History Cleared!\n");
				_model.clearHistory();
				
			}
		});
		viewStats.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JTable statsTable = new JTable(_model);
				statsTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
				JScrollPane scrollPane = new JScrollPane(statsTable);
				JFrame statsFrame = new JFrame("Spelling Aid Statistics");
				statsFrame.add(scrollPane);
				statsFrame.pack();
				statsFrame.setVisible(true);
						
				
			}
		});
		
	}
	
	private void enableMenu(boolean isEnabled){
		newQuiz.setEnabled(isEnabled);
		reviewQuiz.setEnabled(isEnabled);
		clearStats.setEnabled(isEnabled);
		viewStats.setEnabled(isEnabled);
	}
	
		
	

	private static void createAndShowGUI() {
		try {
			new spellingAidViewer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});

	}
	
	

}
