import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GuiView extends JFrame{

	private JLayeredPane boardPane;
	private JLabel boardLabel;
	private JTextArea gameLog;

	private JPanel actionPanel;
	private JLabel promptLabel;
	private JPanel buttonPanel;
	
	private JPanel bottomWrapperPanel;
	private JPanel radioPanel;
	private ButtonGroup currentRadioGroup;

	//tracking user input	
	private volatile String stringResponse = null;
	private volatile Boolean buttonResponse = null;

	public GuiView(){
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Setup game board
		this.boardPane = new JLayeredPane();
		ImageIcon gameBoardImage = new ImageIcon("Images/board.jpg");

		// Making the window smaller and scaling the board image to fit.
		Image scaledImage = gameBoardImage.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
	    gameBoardImage = new ImageIcon(scaledImage);

		this.boardLabel = new JLabel(gameBoardImage);

		//set size of the board
		int width = gameBoardImage.getIconWidth();
		int height = gameBoardImage.getIconHeight();

		// Size of the board pane and position the board label
		this.boardPane.setPreferredSize(new Dimension(width, height));
		this.boardLabel.setBounds(0, 0, width + 100, height + 50);
		
		// Shift the board label to the left and up to make room for the side panel
		this.boardLabel.setLocation(-50, -25);

		// Add the board label to the layered pane
		this.boardPane.add(boardLabel, Integer.valueOf(0)); // Add board to the lowest layer
		this.add(boardPane, BorderLayout.CENTER);

		// Setup game log
		this.gameLog = new JTextArea(10, 30);
		this.gameLog.setEditable(false);
		this.gameLog.setLineWrap(true);
		this.gameLog.setWrapStyleWord(true);

		// Wrap the game log in a scroll pane
		JScrollPane scrollPane = new JScrollPane(this.gameLog);

		//Side panel to hold game log and action panel
		JPanel sidePanel = new JPanel(new BorderLayout());
		sidePanel.add(new JLabel("Game Log"), BorderLayout.NORTH);
		sidePanel.add(scrollPane, BorderLayout.CENTER);
		sidePanel.setPreferredSize(new Dimension(300,10));

		// Action panel for prompts and buttons
		this.actionPanel = new JPanel();
		this.promptLabel = new JLabel("Initializing...");
		this.promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.buttonPanel = new JPanel(new FlowLayout());

		this.actionPanel.add(this.promptLabel);
		this.actionPanel.add(this.buttonPanel);
		sidePanel.add(this.actionPanel, BorderLayout.SOUTH);
		this.add(sidePanel, BorderLayout.EAST);
		
		//make sure prompts, action buttons, and text are not cut off
		this.buttonPanel.setPreferredSize(new Dimension(280, 120));

		// add some padding around action panel
		this.actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		
		// Radio buttons for current player display
		this.radioPanel = new JPanel(new FlowLayout());
		this.bottomWrapperPanel = new JPanel();
		this.bottomWrapperPanel.setLayout(new BoxLayout(this.bottomWrapperPanel, BoxLayout.Y_AXIS));
		this.bottomWrapperPanel.add(this.radioPanel);
		this.bottomWrapperPanel.add(this.actionPanel);
		sidePanel.add(this.bottomWrapperPanel, BorderLayout.SOUTH);

		//finalize...
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	//startup prompt
	public boolean askStart() {
		this.buttonResponse = null;
		this.promptLabel.setText("Ready to start the game?");
		this.buttonPanel.removeAll();

		JButton yesButton = new JButton("Yes");
		JButton noButton = new JButton("No");

		yesButton.addActionListener(e -> {
			this.buttonResponse = true;
			refreshActionPanel();
		});
		noButton.addActionListener(e -> {
			showMessage("Well... We don't need you anyway. Bye!");
			System.exit(0);
		});

		this.buttonPanel.add(yesButton);
		this.buttonPanel.add(noButton);

		refreshActionPanel();

		while (this.buttonResponse == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.buttonResponse;
	}	
	public void printFullBoard(Board board) {
		showMessage("______CURRENT BOARD_____");
		
		for (Room room : board.getAllRooms()) {
			if(room instanceof Set ) {
				Set set = (Set) room;
				if(set.getActiveCard() != null) {
					showMessage("Room: " + room.getName() + " Shot Count: " + set.getShotCount() + " Budget: " + set.getActiveCard().getBudget());
					showMessage(" Adjacent to: " + room.getAdjacent());
		            if (room.getPlayers() != null && !room.getPlayers().isEmpty()) {
		                System.out.print(" Players in room: ");
		                for (Player player : room.getPlayers()) {
		                    System.out.print(player.getName() + ", ");
		                }
		                showMessage("");
		            }
	            } else {
	            	showMessage("Room: " + room.getName() + ". The card is flipped and the scene is closed currently.");
					showMessage(" Adjacent to: " + room.getAdjacent());
		            if (room.getPlayers() != null && !room.getPlayers().isEmpty()) {
		                System.out.print(" Players in room: ");
		                for (Player player : room.getPlayers()) {
		                    System.out.print(player.getName() + ", ");
		                }
		                showMessage("");
		            }
	            	
	            }
	        } else {
	        	showMessage("Room: " + room.getName());
				showMessage(" Adjacent to: " + room.getAdjacent());
	            if (room.getPlayers() != null && !room.getPlayers().isEmpty()) {
	                System.out.print(" Players in room: ");
	                for (Player player : room.getPlayers()) {
	                    System.out.print(player.getName() + ", ");
	                }
	                showMessage("");
	            }
	        }
            
		}
	showMessage("__________________");
    }

	public void showMessage(String message) {
		this.gameLog.append(message + "\n");
		this.gameLog.setCaretPosition(this.gameLog.getDocument().getLength());
	}

	public void refreshActionPanel() {
		this.revalidate();
		this.repaint();
	}
public List<String> getPlayerNames(){
    Boolean valid = false;
    List<String> names = new ArrayList<>();
    
    while (!valid) {
		this.stringResponse = null;
		this.promptLabel.setText("Enter player names separated by commas");
		this.buttonPanel.removeAll();

		JTextField input = new JTextField(15);
		JButton submitButton = new JButton("Submit");

		submitButton.addActionListener(e -> {
			this.stringResponse = input.getText();
		});
		input.addActionListener(e -> {
			this.stringResponse = input.getText();
		});

		this.buttonPanel.add(input);
		this.buttonPanel.add(submitButton);
        refreshActionPanel();

		//game pauses while waiting for input
		while (this.stringResponse == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String raw = this.stringResponse;
		String[] splitNames = raw.split(",");
		for (String name : splitNames) {
			name = name.trim();
			if (!name.isEmpty()) {
				names.add(name.trim());
			}
		}
		if (names.size() >= 2 && names.size() <= 8) {
			valid = true;
		} else {
			showMessage("ERROR: PLEASE FOLLOW THE INSTRUCTIONS AND ENTER BETWEEN 2 AND 8 NAMES SEPARATED BY COMMAS");
			names.clear();
		}
	}
	return names;
	}
	
public void printAvailableRoles(Set destination) {
    showMessage("\nAvailable roles at " + destination.getName() + ":");
    boolean foundRoles = false;

    //Check Off-Card Roles (Extras)
    if (destination.getOffCardRoles() != null) { 
        for (Role r : destination.getOffCardRoles()) {
            if (!r.getOccupied()) {
                showMessage("-Extra: " + r.getName() + " (Req. Rank " + r.getRank() + ")");
                foundRoles = true;
      
            }}}

    //Check On-Card Roles (Starring)
    if (destination.getOnCardroles() != null) {
        for (Role r : destination.getOnCardroles()) {
            if (!r.getOccupied()) {
                showMessage("-Starring: " + r.getName() + " (Req. Rank " + r.getRank() + ")");
                foundRoles = true;
            }}}
    if (!foundRoles) {
        showMessage("  (No roles currently available here)");
    }
}
public void render(Packet packet) {
    if (packet.getLastEvent() == null) return;
    
    switch (packet.getLastEvent()) {
    case TURN_START:
		List<Player> allPlayers = packet.getAllPlayers();
		List<String> playerNames = new ArrayList<>();
		if (allPlayers != null) {
			for (Player p : allPlayers) {
				playerNames.add(p.getName());
			}
		}
		updatePlayerDisplay(packet.getPlayer().getName(), playerNames);

    	showMessage("Hey " + packet.getPlayer().getName() + "! You're on now!" );
    	showMessage("Current player: " + packet.getPlayer().getName());
    	showMessage("Location: " + packet.getLocation().getName());
        showMessage("Money: " + packet.getPlayer().getMoney() + ", Credits: " + packet.getPlayer().getCredits() + ", Rank: " + packet.getPlayer().getRank());
    	break; 	
    case INVALID_ACTION:
    	showMessage("Invalid action... Don't do that again...");
    	break;
    case MOVED:
    	String roomName = packet.getLastLocation().getName();
    	
    	if(packet.getLocation() instanceof Set) {
    		Set set = (Set) packet.getLocation();
    		if(set.getActiveCard() != null)
    			showMessage("Moved from: " + roomName + ", Moved to: " + packet.getTargetLocation().getName() + ", Budget: " + set.getActiveCard().getBudget());
    		else {
    			showMessage("Moved from: " + roomName + ", Moved to: " + packet.getTargetLocation().getName() + ". The card is flipped and the scene is closed currently.");	
    		}
    	} else {
    		showMessage("Moved from: " + roomName + ", Moved to: " + packet.getTargetLocation().getName());
    		
    	}
    	break;
    case SCENE_REVEALED:
    	showMessage(packet.getTargetLocation().getName() + " is the new scene!");
    	break;	
    case ACT_SUCCESS:
    	showMessage("Winner winner chicken dinner!");
    	showMessage("Money: " + packet.getPlayer().getMoney() + " | Credits: " + packet.getPlayer().getCredits() + " | Rank: " + packet.getPlayer().getRank() + "| Rehearsal credits: " + packet.getPlayer().getPracticeChips() + "\n");
    	Set set = (Set) packet.getLocation();
    	showMessage("Shots left:" + set.getShotCount());
    	
    case REHEARSED:
    	showMessage("rehersal credits: " + packet.getPlayer().getPracticeChips() + "\n");
    	break;
    case ACT_FAIL:
    	showMessage("You're a loser and you failed.");
    	showMessage("Money: " + packet.getPlayer().getMoney() + " | Credits: " + packet.getPlayer().getCredits() + " | Rank: " + packet.getPlayer().getRank() + "| Rehearsal credits: " + packet.getPlayer().getPracticeChips() + "\n");
    	set = (Set) packet.getLocation();
    	showMessage("Shots left:" + set.getShotCount());
    	break;
    case QUERY_WORK:
    	showMessage("rehersal credits: " + packet.getPlayer().getPracticeChips() + "\n");
    	break;
    case SCENE_WRAPPED:
    	showMessage("The scene is over and you should leave...");
    	break;
    case UPGRADED:
    	showMessage("Congrats you got a new rank, you upgraded to " + packet.getPlayer().getRank());
    	break;
    case GAME_OVER:
        showMessage("\nGAME OVER: Final Standings");
        List<Player> ranking = packet.getFinalRanking();
        List<Integer> scores = packet.getFinalScores();
        if (ranking != null && scores != null && ranking.size() == scores.size()) {
            for (int i = 0; i < ranking.size(); i++) {
                Player p = ranking.get(i);
                int s = scores.get(i);
                showMessage((i+1) + ". " + p.getName() + " - Score: " + s + " (Money:" + p.getMoney() + ", Credits:" + p.getCredits() + ", Rank:" + p.getRank() + ")");
            }
        } else {
            showMessage("No final standings available.");
        }
        showMessage("\n");
    	break;
    default:
    	break;
    
    }
}
public String renderAndRequestAction(Packet packet) {
    render(packet);
    this.buttonPanel.removeAll();
	this.stringResponse = null;
	String promptMessage = "";
	if (packet.getLastEvent() == Packet.EventType.QUERY_DESTINATION){
		promptMessage = "Alright, so where are you goin'?";
		Room currentLocation = packet.getPlayer().currentLocation();

		if (currentLocation != null && !currentLocation.getAdjacent().isEmpty()) {
			for (Room r : currentLocation.getAdjacent()) {
				JButton roomButton = new JButton(r.getName());
				roomButton.addActionListener(e -> {
					this.stringResponse = r.getName();
				});
				this.buttonPanel.add(roomButton);
			}
		}else{
			showMessage("ERROR: I have no idea how you got here but you have no adjacent rooms to move to... Good job, now you're stuck.");
		}
	}else if (packet.getLastEvent() == Packet.EventType.QUERY_MOVE){
		promptMessage = "Are ya sure you want to move?";
		JButton yesButton = new JButton("Yes");
		JButton noButton = new JButton("No");

		yesButton.addActionListener(e -> {
			this.stringResponse = "yes";
		});
		noButton.addActionListener(e -> {
			this.stringResponse = "no";
		});
		this.buttonPanel.add(yesButton);
		this.buttonPanel.add(noButton);
	} else{
		promptMessage = "What do ya want to do?";

		this.currentRadioGroup = new ButtonGroup();
		for(String actions : packet.getAvailableActions()) {
			if(!actions.equals("View Board")) {
				JButton actionButton = new JButton(actions);
				actionButton.addActionListener(e -> {
					this.stringResponse = actions;
				});
				this.buttonPanel.add(actionButton);
			}
		}
	} 

	this.promptLabel.setText("<html><center>" + promptMessage + "</center></html>");

	refreshActionPanel();
	//game pauses while waiting for input
	while (this.stringResponse == null) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	return this.stringResponse.trim().toLowerCase();
}
//Radio button method for current player
public void updatePlayerDisplay(String currentPlayerName, List<String> playerNames) {
	this.radioPanel.removeAll();
	this.currentRadioGroup = new ButtonGroup();
	for (String name : playerNames) {
		JRadioButton playerButton = new JRadioButton(name);
		playerButton.setEnabled(false);
		if (name.equals(currentPlayerName)) {
			playerButton.setSelected(true);
		}
		this.currentRadioGroup.add(playerButton);
		this.radioPanel.add(playerButton);
	}
	refreshActionPanel();
}
    public void exitMessage() {
        showMessage("You should leave...");
        this.promptLabel.setText("Game's over! Thanks for playing!");
		this.buttonPanel.removeAll();
		refreshActionPanel();
    }
}