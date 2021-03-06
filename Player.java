import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class Player {
	protected boolean user  = false; // 유저인지 확인
	boolean first = false; // 시작 플레이어인지 확인

	String name;  // 이름
	int location; // 게임 보드에서 위치
	int score = 0;  // 점수
	GamePanel panel;
	Player previousPlayer;
	Vector<Card> cardList; 	// 플레이어의 카드들

	Player (String name, int location) {
		this.name = name;
		this.location = location;
	}
		
	public void setCardList(Vector<Card> cardList) { this.cardList = cardList; }
	public void enableMouseListener() {
		for(Card card: this.cardList) {
			card.enableClick(new CardMouseListener());
			card.clickable = true;
		}
	}

	// 자신의 cardList에서 무작위 카드 한 장을 뽑음
	Card giveRandomCard() {
		int i = (int)(Math.random()*cardList.size());
		Card card = cardList.remove(i);
		BoardPanel.showRight("took a card from " + name);
		return card;
	}

	// 이전 플레이어의 무작위 카드 한 장을 자신의 카드리스트에 넣음
	void draw(Player prevPlayer) {
		previousPlayer = prevPlayer;
		prevPlayer.panel.setBackground(Color.white);
		panel.setBackground(Color.GRAY);
		Card givenCard = prevPlayer.giveRandomCard(); // 이전 플레이어의 무작위 카드
		givenCard.setBack();
		cardList.add(givenCard);
		panel.refresh();
	}
	
	// 플레이어의 카드에서 같은 숫자를 찾아서 하나만 버림
	public void dump() {
		// 이전 플레이어의 카드를 클릭할 수 없게 만듦
		for(Card card: previousPlayer.cardList) {
			MouseListener[] listeners = card.getMouseListeners();
			if(listeners.length > 0)
				for(int i=0; i<listeners.length; i++)
					card.removeMouseListener(listeners[i]);
			card.clickable = false;
		}
		final int size = cardList.size();
		int cardNumberList[][] = new int[size][2];	// 카드 숫자와 cardList에서 몇번째 원소 인지를 함께 저장한 이차원 배열
		for(int i=0; i<size; i++) {	// cardNumberList를 만드는 과정
			cardNumberList[i][0] = cardList.get(i).number;
			cardNumberList[i][1] = i;
		}
		// 중복 숫자를 찾기 위해서 위에서 만든 배열을 숫자를 기준으로 정렬
		Arrays.sort(cardNumberList, Comparator.comparingDouble(o -> o[0]));

		// 같은 숫자가 연속으로 두번 나오면 중복
		int prevNum, prevIndex, currentNum, currentIndex;
		for(int i=1; i<size; i++) { // 지금 카드와 이전 카드를 함께 보며 중복인지 확인

			prevNum = cardNumberList[i-1][0];
			prevIndex = cardNumberList[i-1][1];
			currentNum = cardNumberList[i][0];
			currentIndex = cardNumberList[i][1];

			if(prevNum == currentNum) {
				Card c1 = cardList.remove(currentIndex);
				Card c2 = cardList.remove(prevIndex);
				c1.setForth();
				c2.setForth();
				BoardPanel.showCenter(c1, c2);
				BoardPanel.showRight("Dump " + c1 + " and " + c2 );
				break;
			}
		}
	}

	// 게임 시작 전 중복 숫자 카드를 모두 제거
	public void dumpAll() {
		final int size = cardList.size();
		int cardNumberList[][] = new int[size][2];	// 카드 숫자와 cardList에서 몇번째 원소 인지를 함께 저장한 이차원 배열
		boolean toDelete[] = new boolean[size]; 	// cardList에서 삭제할 원소를 표시한 배열
		for(int i=0; i<size; i++) {	// cardNumberList를 만드는 과정
			cardNumberList[i][0] = cardList.get(i).number;
			cardNumberList[i][1] = i;
			toDelete[i] = false;    // 어떤 원소도 삭제하지 않게 초기화
		}
		// 중복 숫자를 찾기 위해서 위에서 만든 배열을 숫자를 기준으로 정렬
		Arrays.sort(cardNumberList, Comparator.comparingDouble(o -> o[0]));

		// 같은 숫자가 연속으로 두번 나오면 중복이므로 toDelete에 둘다 표시
		String message =  name + ": Dump cards ";
		int prev, previousIndex, current, currentIndex;
		for(int i=1; i<size; i++) { // 지금 카드와 이전 카드를 함께 보며 중복인지 확인

			prev = cardNumberList[i-1][0];
			previousIndex = cardNumberList[i-1][1];
			current = cardNumberList[i][0];
			currentIndex = cardNumberList[i][1];

			if(prev == current) {
				message += cardList.get(previousIndex) + "&" + cardList.get(currentIndex) + " ";
				toDelete[previousIndex] = true;
				toDelete[currentIndex] = true;
				i++; // 앞서 두개의 카드가 중복이면 다음 카드를 스킵하고 다다음 카드를 봄
			}
		}
		BoardPanel.showRight(message + "\n");

		// 뒤에서 부터 toDelete가 참인 카드를 제거
		for(int j=size-1; j>=0; j--) {
			if (toDelete[j]) cardList.remove(j);
		}
	}

	class CardMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			Card card = (Card)e.getSource();
			card.removeMouseListener(this);
			cardList.remove(card);
			panel.refresh();
		}
	}
}