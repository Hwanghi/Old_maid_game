public class Card {
	private int number;    // 1~13������ ��, 0(��Ŀ)
	private String shape;  // ���̾Ƹ��, ��Ʈ, �����̵�, Ŭ�ι�, ��Ŀ

	Card (String shape, int number) {
		this.shape = shape;
		this.number = number;
	}

	public int getNumber() { return number;}
	public String getShape() { return shape;}

	@Override
	public boolean equals(Object o) {
		Card c = (Card) o;
		if (c.getNumber() == this.getNumber())
			return true;
		else
			return false;
	} // �� ���� ī���� ���ڰ� ������ true ��ȯ
	@Override
	public String toString() {
		return this.shape + this.number;
	} 
	// Player�� Card�� ���� ��ӹ��� �ʿ䰡 ���� �� ���ƿ� ���߿� User�� Computer�� ���� �������ٸ� �׶�
	// Player�� ��ӹ��� �� �ְڳ׿�!
}