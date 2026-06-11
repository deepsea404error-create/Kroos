package sts.kroos.cards.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;

/**
 * A1 小队 - 炎熔。蓝卡, 1费, 消耗, 虚无, 攻击。
 *   - 对所有敌人造成 7 (强化 11) 点伤害, 给予 1 层虚弱与易伤
 *   - 寒芒: 消耗 1 层寒芒, 额外给予 1 层虚弱与易伤
 */
public class A1_Yanrong extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":A1_Yanrong";
    private static final String IMG = KroosMod.RES_ROOT + "cards/colorless/a1_yanrong.png";

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int DEBUFF = 1;

    public A1_Yanrong() {
        super(ID, IMG, COST, CardType.ATTACK,
                AbstractCard.CardColor.COLORLESS,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.exhaust = true;
        this.isA1Squad = true;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        int n = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = this.damage;
        addToBot(new DamageAllEnemiesAction(p, arr,
                this.damageTypeForTurn, AttackEffect.FIRE, true));

        int extraDebuff = canConsumeFrost(1) ? 1 : 0;
        if (extraDebuff > 0) consumeFrost(1);
        int total = DEBUFF + extraDebuff;

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo == null || mo.isDeadOrEscaped()) continue;
            addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, total, false), total));
            addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, total, false), total));
        }
    }

    @Override
    public AbstractKroosCard makeCopy() { return new A1_Yanrong(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
