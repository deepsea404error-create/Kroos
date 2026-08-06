package sts.kroos.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.FlawPower;
import sts.kroos.powers.OneInstantListenerPower;

/**
 * 一瞬 - 1费 (强化: 0费, 保留)。
 *   - 本回合每打出 1 张攻击牌, 施加 1 层破绽 (OneInstantListenerPower)
 *   - 寒芒: 消耗 1 层寒芒, 施加 1 层破绽 (对随机敌人)
 */
public class OneInstant extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":OneInstant";
    private static final String IMG = KroosMod.RES_ROOT + "cards/skill/one_instant.png";

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public OneInstant() {
        super(ID, IMG, COST, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new OneInstantListenerPower(p)));

        if (canConsumeFrost(1)) {
            consumeFrost(1);
            AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
            if (target != null) {
                addToBot(new ApplyPowerAction(target, p, new FlawPower(target, 1), 1));
            }
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new OneInstant(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            this.selfRetain = true;
            upgradeDescription();
        }
    }
}
