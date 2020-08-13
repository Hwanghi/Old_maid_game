import java.util.*;

public class OldMaidGameApp {
	/**
	 *  ���� ���� ���� ����, �޼ҵ�
	 */
	Scanner scanner  = new Scanner(System.in);
	boolean running  = true;  // ��ü ������ ������ ���θ� ��Ÿ���� ����.
	boolean winner   = false; // ���ڰ� ������ ���°� true�� �ٲ�� �ϱ�
	int totalPlayer  = 2;	  // ��ü �÷��̾� �� ���߿� ��������
	int rangeOfCards = 13;    // ī�� ���� ����
	/*�޼ҵ�*/
	// ���� ����
	void end() { running = false; }

	// �÷��̾� ����, ī�� ���� ����, �� ����
	void gameSetting() {
		System.out.println("Game setting...");
		System.out.print("How many players with you?: ");
		totalPlayer = scanner.nextInt();
		System.out.print("Maximum value of Cards: ");
		rangeOfCards = scanner.nextInt();
		System.out.println("Please type your name");
		System.out.print("Player's name: ");
		scanner.nextLine();
		addUser(scanner.nextLine(), 0);
		for(int i=1; i < totalPlayer; i++) {
			addPlayer("Computer"+i, i);
		} // User �� ���� ������ ��ŭ�� ���� Player Computer'n'�� �ڵ����� playerList�� �߰�
		/*
		 * ���� �ڵ�
		// �̸��� Computer�� Player�� playerList�� �߰�
		addPlayer("Computer", 0);
		scanner.nextLine();
		for(int i=1; i < totalPlayer; i++) {
			System.out.print("Player " + i + "'s name: ");
			addPlayer(scanner.nextLine(), i);
		}
		*/
		setDeck(rangeOfCards*4 + 1);   // �� ����
	}

	/**
	 *  �÷��̾� ���� ���� ����, �޼ҵ�
	 */
	ArrayList<Player> playerList = new ArrayList<>(totalPlayer);
	Player prevPlayer;     // �ٷ� �� ���� ������ �÷��̾�
	Player currentPlayer;  // ���� ���� ���� ���� �÷��̾�
	/*�޼ҵ�*/
	// currentPlayer�� ���� �÷��̾�� ����
	void nextPlayer() {
		prevPlayer = currentPlayer;
		if(currentPlayer == playerList.get(totalPlayer - 1)) // ������ ����
			currentPlayer = playerList.get(0);
		else
			currentPlayer = playerList.get(currentPlayer.getLocation() + 1);
	}
	
	// currentPlayer�� ���� �÷��̾�� ����
	void firstPlayer() {   
		for(int i = 0; i<totalPlayer; i++) {
			currentPlayer = playerList.get(i);
			if(currentPlayer.isFirst()){
				if(i == 0)
					prevPlayer = playerList.get(totalPlayer-1);
				else
					prevPlayer = playerList.get(i-1);
				break;
			}
		}
	} 

	// �̸��� ��ġ�� �޾� �÷��̾� ����
	void addPlayer(String name, int location) {
		playerList.add(new Player(name, location));
	}
	// addPlayer�� ���� ���ҷ�, User �÷��̾� ����
	void addUser(String name, int location) {
		playerList.add(new User(name, location));
	}

	/**
	 *  ī�� ���� ���� ����, �޼ҵ�
	 */
	private Vector<Card> deck; // CardList�� ���� Vector�� ����
	/*�޼ҵ�*/
	void setDeck(int sizeOfDeck) {
		String[] shapes = {"��", "��", "��", "��"}; 
		this.deck = new Vector<Card>(sizeOfDeck);
		for(String shape : shapes)
			for(int i=1; i <= rangeOfCards; i++) {
				this.deck.add(new Card(shape, i));
			}
		this.deck.add(new Card("J", 0));
	}

	/*
	 * �÷��̾�� ī�带 �����ִ� shuffle �Դϴ�.
	 * ī�带 �� ���� ���� �Ѹ��� �÷��̾��� first�� true�� �����մϴ�.
	 */
	void shuffle() {
		Collections.shuffle(this.deck);
		int randNum = (int)(Math.random()*totalPlayer);
		currentPlayer = playerList.get(randNum);
		currentPlayer.setFirst(true);
		int totalCards = rangeOfCards*4 + 1; // ��ü ī�� ��
		int shareCards = (int)(totalCards/totalPlayer);
		int startIndex = 0;
		int endIndex   = shareCards;

		// ��� �ο��� �Ȱ��� �޴� ī���
		for(int i=0; i<totalPlayer; i++) {
			List<Card> bundle = deck.subList(startIndex, endIndex);
			currentPlayer.setCardList(new Vector<Card>(bundle));
			startIndex += shareCards;
			endIndex   += shareCards;
			nextPlayer();
		}
		// ������ ī��
		for(int i=0; i < totalCards%shareCards; i++) {
			currentPlayer.cardList.add(deck.get(startIndex+i));
			nextPlayer();
		}
	}

	// ��Ŀ�� ������ �ִ� �÷��̾ ã�´�.
	void findJoker() {
		for(int i=0; i < totalPlayer; i++) {
			nextPlayer();
			for (Card card : currentPlayer.cardList)
				if (card.getNumber() == 0) {
					System.out.println(currentPlayer.getName() + " has the Joker!");
					return;
				}
		}
	}

	public void run() {
		// �÷��̾� ����, ī�� ���� ����, �� ����
		gameSetting();
		// ��Ŀ ã�� ������ ����
		while(running) {
			try {
				// 1. ���� ����, �ߺ� ī�带 ������.
				shuffle();
				
				for(int i=0; i<totalPlayer; i++) {
					playerList.get(i).dumpAll();
				}
				
				// 2. ���� �÷��̾���� ������ �����Ѵ�.
				firstPlayer();
				// 3. � �÷��̾ ī�带 ��� ���� ������ ������ �����Ѵ�.
				System.out.println("* * * * * * * * * * * * * * * * * * * *");
				System.out.println("Let's play Old maid game!!");
				while(!winner) {
					System.out.println("- - - - - - - - - - - - - -");
					System.out.print(currentPlayer.getName() + ": ");
					currentPlayer.draw(prevPlayer);
					currentPlayer.showCards();
					// ������ ī�带 ��� ������ ��� ���и� �����Ѵ�.
					if (prevPlayer.cardList.size() == 0) {
						System.out.println(prevPlayer.getName() + " is winner!!");
						winner = true;
						findJoker();
					}
					else if (currentPlayer.cardList.size() == 0) {
						System.out.println(currentPlayer.getName() + " is winner!!");
						winner = true;
						findJoker();
					}
					nextPlayer();
				}
				System.out.println("* * * * * * * * * * * * * * * * * * * *");

				// 4.����ڿ��� ���� ������ ���� ���´�.
				System.out.print("Would you like to exit game? (yes): ");
				if(scanner.next().equals("yes")) end();
				else { // �÷��̾� ������ ���ΰ� ���� �ʱ�ȭ
					firstPlayer();
					currentPlayer.setFirst(false); // ���� �÷��̾� ����
					winner = false; 			   // ���� ����
					Iterator<Player> iter = playerList.iterator();
					while (iter.hasNext()) { // �÷��̾ ������ �ִ� ī�� ��� ����
						Player player = iter.next();
						player.cardList.clear();
					}
				}
			}
			catch (Exception e) { // ���� �߻� �� end()�� ���� ���� �ϵ��� ����ó��
				System.out.println(e.getMessage());
				end();
			}
		}
	}

	public static void main(String[] args) {
		OldMaidGameApp game = new OldMaidGameApp();
		game.run();
	}
}