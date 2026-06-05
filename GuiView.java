import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GuiView{

	private JLayeredPane boardPane;
	private JLabel boardLabel;
	private JTextArea gameLog;

	private JFrame frame;
	private JPanel actionPanel;
	private JLabel promptLabel;
	private JPanel buttonPanel;

	private Map<Player, JLabel> playerIcons = new HashMap<>();
	private double boardScaleX = 1.0;
	private double boardScaleY = 1.0;
	
	private JPanel bottomWrapperPanel;
	private JPanel radioPanel;
	private ButtonGroup currentRadioGroup;
	private Map<String, JLabel> activeSceneIcons = new HashMap<>();
	private Map<Room, List<JLabel>> activeShotIcons = new HashMap<>();

	public void updateShotDisplay(Set set){
		if (activeShotIcons.containsKey(set)) {
			List<JLabel> shotIcons = activeShotIcons.get(set);
			for (JLabel icon : shotIcons) {
				boardPane.remove(icon);
			}
		}
		List<JLabel> newShotIcons = new ArrayList<>();
		int shotsLeft = set.getShotCount();
		ArrayList<Area> takePositions = set.getTakePositions();
		if (takePositions == null){
			return;
		}
		for(int i = 0; i < shotsLeft; i++){
			if(i >= takePositions.size()){
				break;
			}
			Area takeArea = takePositions.get(i);

			// Use the same board-label coordinate mapping as the player tokens.
			int x = (int) Math.round(takeArea.getXPos() * boardScaleX);
			int y = (int) Math.round(takeArea.getYPos() * boardScaleY);
			int width = (int) Math.round(takeArea.getWidth() * boardScaleX);
			int height = (int) Math.round(takeArea.getHeight() * boardScaleY);

			JLabel shotIcon = new JLabel(new ImageIcon("Images/shot.png"));
			ImageIcon originalShotIcon = (ImageIcon) shotIcon.getIcon();

			if(shotIcon.getIcon().getIconWidth()> 0 && shotIcon.getIcon().getIconHeight() > 0) {
				Image scaledImage = originalShotIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
				shotIcon.setIcon(new ImageIcon(scaledImage));
			} else {
				shotIcon.setText("X");
				shotIcon.setHorizontalAlignment(SwingConstants.CENTER);
				shotIcon.setVerticalAlignment(SwingConstants.CENTER);
				shotIcon.setOpaque(true);
				shotIcon.setBackground(new Color(255, 0, 0, 200));
				shotIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
			shotIcon.setBounds(x, y, width, height);

			boardPane.add(shotIcon, Integer.valueOf(2));
			newShotIcons.add(shotIcon);
		}
		activeShotIcons.put(set, newShotIcons);
		boardPane.revalidate();
		boardPane.repaint();
			
		}

	//tracking user input	
	private volatile String stringResponse = null;
	private volatile Boolean buttonResponse = null;

	public GuiView(){

		// Setup game board
		this.frame = new JFrame("Deadwood");
		this.frame.setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon("Images\\deadwood_icon.png");
		this.frame.setIconImage(icon.getImage());
		
		this.boardPane = new JLayeredPane();
		ImageIcon gameBoardImage = new ImageIcon("Images/board.jpg");

	// Keep room coordinates aligned with the board image, even if scaled.
	Image rawBoardImage = gameBoardImage.getImage();
	int rawWidth = rawBoardImage.getWidth(null);
	int rawHeight = rawBoardImage.getHeight(null);
	if (rawWidth > 0 && rawHeight > 0) {
		boardScaleX = 800.0 / rawWidth;
		boardScaleY = 600.0 / rawHeight;
	}

	// Making the window smaller and scaling the board image to fit.
	Image scaledImage = rawBoardImage.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
	gameBoardImage = new ImageIcon(scaledImage);
	this.boardLabel = new JLabel(gameBoardImage);

		//set size of the board
		int width = gameBoardImage.getIconWidth();
		int height = gameBoardImage.getIconHeight();

		// Size of the board pane and position the board label
		this.boardPane.setPreferredSize(new Dimension(width, height));
		this.boardLabel.setBounds(0, 0, width, height);
		this.boardLabel.setLocation(0, 0);

		// Add the board label to the layered pane
		this.boardPane.add(boardLabel, Integer.valueOf(0)); // Add board to the lowest layer
		this.frame.add(boardPane, BorderLayout.CENTER);

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
		this.frame.add(sidePanel, BorderLayout.EAST);
		
		//make sure prompts, action buttons, and text are not cut off
		this.buttonPanel.setPreferredSize(new Dimension(280, 120));

		// add some padding around action panel
		this.actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		
		// Radio buttons for current player display
		this.radioPanel = new JPanel(new FlowLayout());
		this.radioPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

		// Wrapper panel to hold both radio buttons and action buttons
		this.bottomWrapperPanel = new JPanel();
		this.bottomWrapperPanel.setLayout(new BoxLayout(this.bottomWrapperPanel, BoxLayout.Y_AXIS));
		this.bottomWrapperPanel.add(this.radioPanel);
		this.bottomWrapperPanel.add(this.actionPanel);
		sidePanel.add(this.bottomWrapperPanel, BorderLayout.SOUTH);

		//finalize...
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);

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
		this.frame.revalidate();
		this.frame.repaint();
	}
public List<String> getPlayerNames(){
    Boolean valid = false;
    List<String> names = new ArrayList<>();
    
    while (!valid) {
		this.stringResponse = null;
		//makes text smaller
		this.promptLabel.setText("<html>Enter player names, limit 12 characters.<br>  separated by commas (Up to 8 players):</html>");
		this.buttonPanel.removeAll();

		JTextField input = new JTextField(15);
		JButton submitButton = new JButton("Play");

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
			if(name.trim().length() > 12) {
				showMessage("Name '" + name.trim() + "' is too long. Please enter names with 5 characters or fewer.");
				return getPlayerNames(); 
			}
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
	//make the radio panel bigger if too many players so it doesn't cut off the names
	if(names.size() > 4) {
		this.radioPanel.setPreferredSize(new Dimension(280, 20 * names.size()));
		refreshActionPanel();
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

private JLabel createCardLabel(Card card, Room room, boolean isRevealed) {
	JLabel cardLabel = new JLabel();
	// Use the same board-label coordinate mapping as the player tokens.
	int x = (int) Math.round(room.getArea().getXPos() * boardScaleX);
	int y = (int) Math.round(room.getArea().getYPos() * boardScaleY);
	int width = (int) Math.round(room.getArea().width * boardScaleX);
	int height = (int) Math.round(room.getArea().height * boardScaleY);

	cardLabel.setBounds(x, y, width, height);
	String imagePath = isRevealed ? ("Images/" + card.getImgFileName()) : "Images/Cardback.png";
	ImageIcon originalIcon = new ImageIcon(imagePath);
	if(originalIcon.getIconWidth() > 0 && originalIcon.getIconHeight() > 0) {
		Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		cardLabel.setIcon(new ImageIcon(scaledImage));
	} else {
		cardLabel.setText(card.getName());
		cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cardLabel.setVerticalAlignment(SwingConstants.CENTER);
		cardLabel.setOpaque(true);
		cardLabel.setBackground(new Color(255, 255, 255, 200));
		cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	return cardLabel;
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
		if(packet.getLocation() instanceof Set) {
			updateShotDisplay((Set) packet.getLocation());
		}
		boardPane.revalidate();
		boardPane.repaint();
		break; 
	case SCENE_DEALT:
		JLabel cardLabel = createCardLabel(packet.getCurrentCard(), packet.getLocation(), false);
		boardPane.add(cardLabel, Integer.valueOf(1));
		activeSceneIcons.put(packet.getLocation().getName(), cardLabel);
		if(packet.getLocation() instanceof Set) {
			updateShotDisplay((Set) packet.getLocation());
		}
		boardPane.revalidate();
		boardPane.repaint();
    	break;
    case TOOK_ROLE:
        if (packet.getPlayer() != null && packet.getTargetRole() != null) {
            movePlayerToRole(packet.getPlayer(), packet.getTargetRole());
            showMessage(packet.getPlayer().getName() + " took role: " + packet.getTargetRole().getName());
        }
        break;
    case INVALID_ACTION:
    	showMessage("Invalid action... Don't do that again...");
    	break;
    case MOVED:
        if (packet.getPlayer() != null && packet.getLocation() != null) {
            registerPlayerIcon(packet.getPlayer(), packet.getLocation());
        }
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
		Room room = packet.getTargetLocation();
		Card card = packet.getCurrentCard();

		JLabel revealedCardLabel = activeSceneIcons.get(room.getName());
		if (revealedCardLabel == null) {
			System.out.println("DEBUG: No card label found for room " + room.getName());
		} else if(card != null) {
			//calculate scaled dimensions for the card image
			int width = (int) Math.round(room.getArea().getWidth() * boardScaleX);
			int height = (int) Math.round(room.getArea().getHeight() * boardScaleY);
			// Flip the image
			ImageIcon originalIcon = new ImageIcon("Images/" + card.getImgFileName());
			Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			revealedCardLabel.setIcon(new ImageIcon(scaledImage));
			//force gui to update 
			boardPane.moveToFront(revealedCardLabel);
			boardPane.revalidate();
			boardPane.repaint();
			System.out.println("DEBUG: Revealed card " + card.getName() + " in room " + room.getName());
		}
    	break;	
    case ACT_SUCCESS:
    	showMessage("Winner winner chicken dinner!");
    	showMessage("Money: " + packet.getPlayer().getMoney() + " | Credits: " + packet.getPlayer().getCredits() + " | Rank: " + packet.getPlayer().getRank() + "| Rehearsal credits: " + packet.getPlayer().getPracticeChips() + "\n");
    	Set set = (Set) packet.getLocation();
    	showMessage("Shots left:" + set.getShotCount());
		updateShotDisplay(set);
		break;    	
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
		
		JLabel wrappedCard = activeSceneIcons.remove(packet.getLocation().getName());
		if(wrappedCard != null){
			boardPane.remove(wrappedCard);
			boardPane.revalidate();
			boardPane.repaint();
		}
    	break;
		
	 case SCENE_RESET:
		JLabel resetCard = activeSceneIcons.remove(packet.getLocation().getName());
		if(resetCard != null){
			boardPane.remove(resetCard);
			boardPane.revalidate();
			boardPane.repaint();
		}
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
		List<Room> adjRooms = packet.getAdjacentRooms();
		if (adjRooms != null && !adjRooms.isEmpty()) {
			for (Room r : adjRooms) {
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

    public void registerPlayerIcon(Player player, Room location) {
        if (player == null || location == null) {
            return;
        }
        if (!playerIcons.containsKey(player)) {
            String rawName = player.getName();
            if (rawName == null) {
                rawName = "";
            }
            rawName = rawName.trim().replaceAll("\\s+", " ");
            if (rawName.length() > 14) {
                rawName = rawName.substring(0, 14).trim();
            }

            String labelText;
            String[] lines;
            if (rawName.contains(" ")) {
                int splitIndex = rawName.indexOf(' ');
                String firstLine = rawName.substring(0, splitIndex);
                String secondLine = rawName.substring(splitIndex + 1);
                lines = new String[] { firstLine, secondLine };
                labelText = "<html><div style='text-align:center; margin:0px; padding:0px;'>" + firstLine + "<br>" + secondLine + "</div></html>";
            } else {
                lines = new String[] { rawName };
                labelText = rawName;
            }

            JLabel token = new JLabel(labelText, SwingConstants.CENTER);
            token.setOpaque(true);
            token.setForeground(Color.WHITE);
            token.setBackground(getPlayerColor(player.getUserNumber()));
            token.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            token.setHorizontalAlignment(SwingConstants.CENTER);
            token.setVerticalAlignment(SwingConstants.CENTER);

            int fixedSize = 40;
            float fontSize = 30f;
            Font font = token.getFont().deriveFont(Font.BOLD, fontSize);
            token.setFont(font);

            int padding = 2;
            FontMetrics fm = token.getFontMetrics(font);
            int maxLineWidth = 0;
            for (String line : lines) {
                maxLineWidth = Math.max(maxLineWidth, fm.stringWidth(line));
            }
            int totalHeight = fm.getHeight() * lines.length;
            while (maxLineWidth + padding * 2 > fixedSize || totalHeight + padding * 2 > fixedSize) {
                fontSize -= 0.5f;
                font = font.deriveFont(Font.BOLD, fontSize);
                token.setFont(font);
                fm = token.getFontMetrics(font);
                maxLineWidth = 0;
                for (String line : lines) {
                    maxLineWidth = Math.max(maxLineWidth, fm.stringWidth(line));
                }
                totalHeight = fm.getHeight() * lines.length;
            }

            token.setPreferredSize(new Dimension(fixedSize, fixedSize));
            token.setMinimumSize(new Dimension(fixedSize, fixedSize));
            token.setMaximumSize(new Dimension(fixedSize, fixedSize));
            token.setSize(fixedSize, fixedSize);
            playerIcons.put(player, token);
            boardPane.add(token, Integer.valueOf(2));
            Point center = getRoomSlotCenter(location, player);
            int x = center.x - (fixedSize / 2);
            int y = center.y - (fixedSize / 2);
            token.setLocation(x, y);
            boardPane.repaint();
            return;
        }
        movePlayerIcon(player, location);
    }

    /*public void movePlayerAlongPath(Player player, List<Room> path) {
        if (player == null || path == null || path.size() < 2) {
            return;
        }
        registerPlayerIcon(player, path.get(0));
        for (int i = 1; i < path.size(); i++) {
            Room nextRoom = path.get(i);
            movePlayerIcon(player, nextRoom);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
*/

    /*private List<Room> shortestPath(Board board, Room start, Room destination) {
        if (board == null || start == null || destination == null) {
            return null;
        }

        Queue<Room> queue = new LinkedList<>();
        Map<Room, Room> previous = new HashMap<>();
        java.util.Set<Room> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Room current = queue.poll();
            if (current == destination) {
                break;
            }
            for (Room neighbor : current.getAdjacent()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    previous.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }

        if (!visited.contains(destination)) {
            return null;
        }

        LinkedList<Room> path = new LinkedList<>();
        Room step = destination;
        while (step != null) {
            path.addFirst(step);
            step = previous.get(step);
        }
        return path;
    }
*/
    public void movePlayerToRole(Player player, Role role) {
        if (player == null || role == null) {
            return;
        }
        if (!playerIcons.containsKey(player)) {
            registerPlayerIcon(player, player.currentLocation());
        }

        JLabel token = playerIcons.get(player);
        if (token == null) {
            return;
        }

        Area roleArea = role.getArea();
        if (roleArea == null) {
            if (player.currentLocation() != null) {
                movePlayerIcon(player, player.currentLocation());
            }
            return;
        }

        Point center = getAreaCenter(roleArea);
        int size = token.getWidth();
        int targetX = center.x - (size / 2);
        int targetY = center.y - (size / 2);
        SwingUtilities.invokeLater(() -> animateTokenTo(token, targetX, targetY));
    }

    private void movePlayerIcon(Player player, Room location) {
        JLabel token = playerIcons.get(player);
        if (token == null || location == null) {
            return;
        }
        Point center = getRoomSlotCenter(location, player);
        int size = token.getWidth();
        int targetX = center.x - (size / 2);
        int targetY = center.y - (size / 2);
        SwingUtilities.invokeLater(() -> animateTokenTo(token, targetX, targetY));
    }

    private void animateTokenTo(JLabel token, int targetX, int targetY) {
        Object existingTimer = token.getClientProperty("moveTimer");
        if (existingTimer instanceof javax.swing.Timer) {
            ((javax.swing.Timer) existingTimer).stop();
        }

        Point startLocation = token.getLocation();
        int startX = startLocation.x;
        int startY = startLocation.y;
        int deltaX = targetX - startX;
        int deltaY = targetY - startY;
        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        int maxDistance = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        int steps = Math.max(6, Math.min(30, maxDistance / 4));
        final int totalSteps = steps;
        final int delayMs = 30;
        final double logDenominator = Math.log1p(9.0);
        final int[] currentStep = {0};

        javax.swing.Timer timer = new javax.swing.Timer(delayMs, e -> {
            currentStep[0]++;
            if (currentStep[0] >= totalSteps) {
                token.setLocation(targetX, targetY);
                ((javax.swing.Timer) e.getSource()).stop();
                token.putClientProperty("moveTimer", null);
            } else {
                double t = currentStep[0] / (double) totalSteps;
                double eased = Math.log1p(t * 9.0) / logDenominator;
                int nextX = startX + (int) Math.round(deltaX * eased);
                int nextY = startY + (int) Math.round(deltaY * eased);
                token.setLocation(nextX, nextY);
            }
            boardPane.repaint();
        });

        token.putClientProperty("moveTimer", timer);
        timer.start();
    }

    private Point getAreaCenter(Area area) {
        if (area == null) {
            return new Point(0, 0);
        }
        int centerX = (int) Math.round((area.getXPos() + (area.getWidth() / 2.0)) * boardScaleX);
        int centerY = (int) Math.round((area.getYPos() + (area.getHeight() / 2.0)) * boardScaleY);
        return new Point(centerX, centerY);
    }

    private Point getRoomCenter(Room room) {
        if (room == null) {
            return new Point(0, 0);
        }
        return getAreaCenter(room.getArea());
    }

    private Point getRoomSlotCenter(Room room, Player player) {
        if (room == null || room.getArea() == null) {
            return getRoomCenter(room);
        }

        int slotIndex = 0;
        if (room.getPlayers() != null && room.getPlayers().contains(player)) {
            slotIndex = room.getPlayers().indexOf(player);
        }

        slotIndex = Math.max(0, Math.min(slotIndex, 7));
        int column = slotIndex % 4;
        int row = slotIndex / 4;

        Area area = room.getArea();
        double cellWidth = area.getWidth() / 4.0;
        double cellHeight = area.getHeight() / 2.0;
        int centerX = (int) Math.round((area.getXPos() + (column + 0.5) * cellWidth) * boardScaleX);
        int centerY = (int) Math.round((area.getYPos() + (row + 0.5) * cellHeight) * boardScaleY);
        return new Point(centerX, centerY);
    }

    private Color getPlayerColor(int playerNumber) {
        Color[] palette = new Color[] {
            new Color(220, 20, 60),
            new Color(34, 139, 34),
            new Color(30, 144, 255),
            new Color(255, 140, 0),
            new Color(128, 0, 128),
            new Color(0, 128, 128),
            new Color(184, 134, 11),
            new Color(199, 21, 133)
        };
        return palette[playerNumber % palette.length];
    }
    public void exitMessage() {
        showMessage("You should leave...");
        this.promptLabel.setText("Game's over! Thanks for playing!");
		this.buttonPanel.removeAll();
		refreshActionPanel();
    }
}