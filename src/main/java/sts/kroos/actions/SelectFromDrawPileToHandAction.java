package sts.kroos.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * 打开抽牌堆网格选择, 让玩家挑 1 张牌加入手牌。
 *
 * 用于"支援号令"等"从抽牌堆选择 1 张牌加入手牌"类卡。
 *
 * 行为:
 *   - 抽牌堆为空: 直接结束
 *   - 手牌已满 (>=10): 改为弃牌堆 (与原版一致行为)
 */
public class SelectFromDrawPileToHandAction extends AbstractGameAction {

    private static final String PROMPT = "选择 1 张牌加入手牌";
    private final String prompt;
    private boolean opened = false;

    public SelectFromDrawPileToHandAction(String prompt) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.prompt = prompt != null ? prompt : PROMPT;
    }

    public SelectFromDrawPileToHandAction() { this(PROMPT); }

    @Override
    public void update() {
        CardGroup drawPile = AbstractDungeon.player.drawPile;
        if (!opened) {
            opened = true;
            if (drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(
                    CardGroup.getGroupWithoutBottledCards(drawPile.group),
                    1, prompt, false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (AbstractDungeon.player.hand.size() >= 10) {
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                } else {
                    AbstractDungeon.player.drawPile.removeCard(c);
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
