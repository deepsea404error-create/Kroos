package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FrostPower;

import java.util.ArrayList;
import java.util.List;

/**
 * 战术调整 - 0费 (强化: 保留)。
 *   - 丢弃所有手牌
 *   - 每丢弃一张攻击牌, 获得 1 层寒芒
 *   - 每丢弃一张非攻击牌, 获得 4 (强化 6) 点格挡
 *   - 寒芒: 消耗 1 层寒芒, 下回合获得 1 (强化 2) 点能量
 */
public class TacticalShift extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":TacticalShift";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/tactical_shift.png";

    private static final int COST = 0;
    private static final int BLOCK_PER_NON_ATTACK = 4;
    private static final int BLOCK_PER_NON_ATTACK_UPG = 6;
    private static final int NEXT_TURN_ENERGY = 1;
    private static final int NEXT_TURN_ENERGY_UPG = 2;

    public TacticalShift() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = BLOCK_PER_NON_ATTACK;
        this.magicNumber = BLOCK_PER_NON_ATTACK;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        final int blockPer = this.magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                List<AbstractCard> hand = new ArrayList<>(AbstractDungeon.player.hand.group);
                int frostGain = 0, blockGain = 0;
                for (AbstractCard c : hand) {
                    AbstractDungeon.player.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                    if (c.type == AbstractCard.CardType.ATTACK) frostGain++;
                    else blockGain += blockPer;
                }
                if (frostGain > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            AbstractDungeon.player, AbstractDungeon.player,
                            new FrostPower(AbstractDungeon.player, frostGain), frostGain));
                }
                if (blockGain > 0) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(
                            AbstractDungeon.player, AbstractDungeon.player, blockGain));
                }
            }
        });

        if (canConsumeFrost(1)) {
            consumeFrost(1);
            int e = this.upgraded ? NEXT_TURN_ENERGY_UPG : NEXT_TURN_ENERGY;
            addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, e), e));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new TacticalShift(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(BLOCK_PER_NON_ATTACK_UPG - BLOCK_PER_NON_ATTACK);
            this.selfRetain = true;
            upgradeDescription();
        }
    }
}
