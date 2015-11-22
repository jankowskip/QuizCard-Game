import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class QuizGame {
	private JTextArea question;
	private JTextArea answer;
	private ArrayList<QuizCard> cardlist;
	private QuizCard currentCard;
	private int indexOfCurrentCard;
	private JFrame frame;
	private JButton buttonNextCard;
	private boolean IsAnswerPresented;

	public static void main(String[] args) {
		QuizGame game = new QuizGame();
		game.doWork();
	}

	public void doWork() {
		frame = new JFrame("Quiz");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		Font bigFont = new Font("sanserif", Font.BOLD,24);
		
		question = new JTextArea(10,20);
		question.setFont(bigFont);
		question.setLineWrap(true);
		question.setEditable(false);
		
		JScrollPane scrollQ = new JScrollPane(question);
		scrollQ.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollQ.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		buttonNextCard = new JButton("Pokaz pytanie");
		mainPanel.add(scrollQ);
		mainPanel.add(buttonNextCard);
		buttonNextCard.addActionListener(new NextCardListener());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("Plik");
		JMenuItem optionOpen = new JMenuItem("Open card collection");
		optionOpen.addActionListener(new OpenMenuListener());
		menuFile.add(optionOpen);
		menuBar.add(menuFile);
		frame.setJMenuBar(menuBar);   // zwroc uwage na wywolanie set zamiast add
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(640,500);
		frame.setVisible(true);
		
	}
	
	public class NextCardListener implements ActionListener {
		public void actionPerformed(ActionEvent ae){
			if (IsAnswerPresented){ // zwykla flaga tez tak robiles
				question.setText(currentCard.getAnswer());
				buttonNextCard.setText("Next card");
				IsAnswerPresented = false;		
			} else {
				if (indexOfCurrentCard < cardlist.size()){
					showNextCard();
				} else {
					question.setText("That was last card");
					buttonNextCard.setEnabled(false);
				}
			}
		}
	}
	
	public class OpenMenuListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			JFileChooser dialogFile = new JFileChooser();
			dialogFile.showOpenDialog(frame);
			loadFile(dialogFile.getSelectedFile());
		}
	}
	private void loadFile(File file){
		cardlist = new ArrayList<QuizCard>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null){
				createCard(line);
			}
			reader.close();
		} catch(Exception ex){
			System.out.println("Nie mozna odczytac pliku z kartami");
			ex.printStackTrace();
		}
		showNextCard();
	}
	private void createCard(String dataLine){
		String[] results = dataLine.split("/");
		QuizCard card = new QuizCard(results[0],results[1]);
		cardlist.add(card);
		System.out.println("Created card");
	}
	private void showNextCard(){
		currentCard = cardlist.get(indexOfCurrentCard);
		indexOfCurrentCard++;
		question.setText(currentCard.getQuestion());
		buttonNextCard.setText("Show Answer");
		IsAnswerPresented = true;
	}
}
