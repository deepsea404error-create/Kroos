package sts.kroos.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * A1 小队卡选择 Action (替换虚构的 DiscoveryAction)。
 *
 * 行为:
 *   - 打开网格选择界面, 展示传入的候选卡列表, 让玩家选 picks 张
 *   - 选中的卡加入手牌, 本回合耗能设为 0
 */
public class A1DiscoveryAction extends AbstractGameAction {

    private final ArrayList<AbstractCard> candidates;
    private final int picks;
    private boolean opened = false;

    public A1DiscoveryAction(ArrayList<AbstractCard> candidates, int picks) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.candidates = candidates;
        this.picks = picks;
    }

    @Override
    public void update() {
        if (!opened) {
            opened = true;
            if (candidates == null || candidates.isEmpty()) {
                this.isDone = true;
                return;
            }
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : candidates) tmp.group.add(c);
            AbstractDungeon.gridSelectScreen.open(tmp, picks, "选择卡牌加入手牌", false, false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (AbstractDungeon.player.hand.size() >= 10) {
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                } else {
                    c.setCostForTurn(0);
                    c.costForTurn = 0;
                    c.isCostModified = true;
                    AbstractDungeon.player.hand.addToHand(c);
                }
                c.lighten(false);
                c.applyPowers();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
            this.isDone = true;
        }
        tickDuration();
    }
}
