package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

import java.util.ArrayList;
import java.util.List;

/**
 * 急速转移 - 2费, 消耗(强化: 保留)。
 *   - 消耗 3 (强化 2) 张手牌
 *   - 额外获得 1 回合 (简化为: 立即 +1 能量 +抽 5 张, 让玩家手动结束当前回合)
 *   - 寒芒: 消耗 3 (强化 2) 层寒芒, 下回合额外抽 2 张牌, 获得 1 点能量
 *
 * 注: 真正的"connecting turn"需要 patch 回合切换流程, 当前以近似效果替代。
 *      若用户希望严格语义可后续接入 Spire patch。
 */
public class RapidShift extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":RapidShift";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/rapid_shift.png";

    private static final int COST = 2;
    private static final int DISCARD = 3;
    private static final int UPGRADE_DISCARD_DELTA = -1;
    private static final int FROST_COST = 3;
    private static final int FROST_COST_UPG = 2;
    private static final int NEXT_TURN_DRAW = 2;
    private static final int EXTRA_ENERGY = 1;
    private static final int EXTRA_DRAW = 5;

    public RapidShift() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        this.baseMagicNumber = DISCARD;
        this.magicNumber = DISCARD;
        this.exhaust = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        final int needDiscard = this.magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                List<AbstractCard> hand = new ArrayList<>(AbstractDungeon.player.hand.group);
                int n = Math.min(needDiscard, hand.size());
                for (int i = 0; i < n; i++) {
                    AbstractCard c = hand.get(i);
                    AbstractDungeon.player.hand.moveToExhaustPile(c);
                    GameActionManager.incrementDiscard(false);
                }
            }
        });

        int frostCost = this.upgraded ? FROST_COST_UPG : FROST_COST;
        if (canConsumeFrost(frostCost)) {
            consumeFrost(frostCost);
            addToBot(new ApplyPowerAction(p, p,
                    new EnergizedPower(p, EXTRA_ENERGY + NEXT_TURN_DRAW), EXTRA_ENERGY));
        }

        addToBot(new GainEnergyAction(EXTRA_ENERGY));
        addToBot(new DrawCardAction(p, EXTRA_DRAW));
    }

    @Override
    public AbstractKroosCard makeCopy() { return new RapidShift(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DISCARD_DELTA);
            this.exhaust = false;
            this.selfRetain = true;
            upgradeDescription();
        }
    }
}
