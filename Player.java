import java.util.*;

public class Player {
	// User Ŭ�������� ����ϱ� ���� name�� location�� protected�� ����
	protected String name;  // �̸�
	protected int location; // ���� ���忡�� ��ġ
	private boolean first = false; // ���� �÷��̾����� Ȯ��
	private int score = 0;  // ����
	List<Card> cardList; 	// �÷��̾ ������ �ִ� ī���

	Player() {} // �⺻ ������ �߰�
	Player (String name, int location) {
		this.name = name;
		this.location = location;
	}
	public String getName()  { return name; }
	public int getLocation() { return location; }
	public void setFirst(boolean first) { this.first = first; }
	public boolean isFirst() { return this.first; }
	public void setCardList(List<Card> cardList) { this.cardList = cardList; }
	public void plusScore() { this.score++; } // ������ 1�� �ø���.

	// ���� �÷��̾��� cardList���� ������ ī�� �� ���� �����ϰ� �׸� ��ȯ�Ѵ�.
	Card giveRandomCard(Player prevPlayer) {
		Card card = null;
		int index = 0;
		int i = (int)(Math.random()*prevPlayer.cardList.size());
		for(Iterator<Card> iter = prevPlayer.cardList.iterator();iter.hasNext();) {
			card = iter.next();
			if (index++ == i) {
				iter.remove();
				break;
			}
		}
		return card;
	}

	// ���� �÷��̾�� ī�� �� ���� �ް� ���� ���� ī�� �� ���� ������, �� ������ ��µ��� �ʵ��� �Ѵ�.
	void draw(Player prevPlayer) {
		Card givenCard = giveRandomCard(prevPlayer); // ���� �÷��̾��� ������ ī��
		System.out.println("took a card from "+prevPlayer.getName());
		// �÷��̾��� ī�帮��Ʈ���� ���� ���ڸ� ã�ƺ���.
		for(Card card: this.cardList)    
			if(card.equals(givenCard)) { // ���� ���� ī�尡 ���� ��
				System.out.println("Dump " + card + " and " + givenCard + " from hand");
				this.cardList.remove(card);
				return;
			}
		// ���� ���� ī�尡 ���� ��
		cardList.add(givenCard);	
	}
	
	// cardList�� ī�尡 �� ������ ����Ѵ�.
	public void showCards() {
		System.out.print(name + ": has " + cardList.size() + " cards");
		System.out.println();
	}
	
	// �ڽ��� ���� ī�� �� �ߺ� ���� ī�带 ��� ������.
	public void dumpAll() {
		boolean checkedAll=false;
		int j=0;
		while(!checkedAll) {
			if(j>=cardList.size()-1) {
				checkedAll=true;
				continue;
			}
			else {
				Card card1 = cardList.get(j);
				for(int i=j+1; i < cardList.size(); i++) {
					Card card2 = cardList.get(i);
					if(card1.equals(card2)) { // ���� ���� ī�尡 ���� ��
						System.out.print(getName()+": ");
						System.out.println("Dump " + card1 + " and " + card2 + " from hand");
						//card1, card2�� cardList���� ����
						this.cardList.remove(j);
						this.cardList.remove(i);
						break;
					}
					j++;
				}
			}
		}
	}
}