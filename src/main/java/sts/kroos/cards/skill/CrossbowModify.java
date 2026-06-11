package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FocusPower;

import java.util.ArrayList;
import java.util.List;

/**
 * 弩箭改装 - 1费。
 *   - 获得 1 力量, 1 (强化 2) 层专注
 *   - 升级抽牌堆中 2 张随机攻击牌 (强化: 升级抽牌堆中所有攻击牌)
 *   - 寒芒: 消耗 1 层寒芒, 获得 2 (强化 3) 点活性肌肉 (= BufferPower)
 *
 * 活性肌肉沿用原版 BufferPower (每层抵消下次 1 次伤害)。
 */
public class CrossbowModify extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":CrossbowModify";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/crossbow_modify.png";

    private static final int COST = 1;
    private static final int STRENGTH = 1;
    private static final int FOCUS = 1;
    private static final int UPGRADE_FOCUS = 1;
    private static final int UPGRADE_RANDOM = 2;
    private static final int BUFFER = 2;
    private static final int BUFFER_UPG = 3;

    public CrossbowModify() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = FOCUS;
        this.magicNumber = FOCUS;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new StrengthPower(p, STRENGTH), STRENGTH));
        addToBot(new ApplyPowerAction(p, p,
                new FocusPower(p, this.magicNumber), this.magicNumber));

        final boolean upgradeAll = this.upgraded;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                CardGroup dp = AbstractDungeon.player.drawPile;
                List<AbstractCard> pool = new ArrayList<>();
                for (AbstractCard c : dp.group) {
                    if (c.type == AbstractCard.CardType.ATTACK && c.canUpgrade()) pool.add(c);
                }
                if (upgradeAll) {
                    for (AbstractCard c : pool) { c.upgrade(); c.applyPowers(); }
                } else {
                    int n = Math.min(UPGRADE_RANDOM, pool.size());
                    for (int i = 0; i < n; i++) {
                        int idx = AbstractDungeon.cardRandomRng.random(pool.size() - 1);
                        AbstractCard c = pool.remove(idx);
                        c.upgrade();
                        c.applyPowers();
                    }
                }
            }
        });

        if (canConsumeFrost(1)) {
            consumeFrost(1);
            int buf = this.upgraded ? BUFFER_UPG : BUFFER;
            addToBot(new ApplyPowerAction(p, p, new BufferPower(p, buf), buf));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new CrossbowModify(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FOCUS);
            upgradeDescription();
        }
    }
}
