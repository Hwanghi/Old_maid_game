import javax.swing.*;
import java.awt.*;

/**
 *  Player의 카드를 보여주는 패널
 */
public class GamePanel extends JPanel {
    Player player;
    GamePanel() {
        setBackground(new Color(40, 120, 11));
        setLayout(new FlowLayout());
    }

    GamePanel(Player player) {
        setBackground(Color.white);
        this.player = player;
        setLayout(new FlowLayout());
        for(Card card: player.cardList) this.add(card);
    }

    public void cover() { // 모든 카드 뒷면 보이기
        for(Card card: player.cardList) card.setBack();
        refresh();
    }

    public void uncover() { // 모든 카드 앞면 보이기
        for(Card card: player.cardList) card.setForth();
        refresh();
    }

    public void refresh() {
        this.removeAll();
        for(Card card: player.cardList)
            this.add(card);
        this.revalidate();
        this.repaint();
    }
}
