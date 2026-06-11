package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.cards.colorless.NormalArrow;

import java.util.ArrayList;
import java.util.List;

/**
 * 速射 - 1 (强化 0) 费。丢弃所有手牌。每丢弃 1 张, 添加 1 张普通箭矢至手牌。
 *   - 寒芒: 消耗 2 层寒芒, 添加的普通箭矢全部升级
 */
public class RapidShoot extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":RapidShoot";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/rapid_shoot.png";

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public RapidShoot() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        boolean upgradeArrows = canConsumeFrost(2);
        if (upgradeArrows) consumeFrost(2);

        // 用自定义 action 捕获丢弃数: 在丢弃前快照手牌大小(不含本牌, 因本牌已在 limbo)
        final boolean upgArrows = upgradeArrows;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                List<AbstractCard> toDiscard = new ArrayList<>(AbstractDungeon.player.hand.group);
                int n = toDiscard.size();
                for (AbstractCard c : toDiscard) {
                    AbstractDungeon.player.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    com.megacrit.cardcrawl.actions.GameActionManager.incrementDiscard(false);
                }
                for (int i = 0; i < n; i++) {
                    AbstractCard arrow = new NormalArrow();
                    if (upgArrows) arrow.upgrade();
                    AbstractDungeon.actionManager.addToBottom(
                            new MakeTempCardInHandAction(arrow, false));
                }
            }
        });
    }

    @Override
    public AbstractKroosCard makeCopy() { return new RapidShoot(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
