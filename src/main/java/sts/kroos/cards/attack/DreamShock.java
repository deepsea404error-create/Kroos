package sts.kroos.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.kroos.KroosMod;
import sts.kroos.cards.AbstractKroosCard;
import sts.kroos.powers.DreamShockListenerPower;

/**
 * 惊梦 - 1费, 造成 6 (强化 9) 点伤害。
 *   - 退出浅眠时, 本牌回到手牌且耗能 -1
 *
 * 实现: 打出后通过 find-or-create 拿到 DreamShockListenerPower 实例, 将本牌
 * 加入 pending 列表; 下次浅眠退出时由 listener 一次性把所有 pending 取回手牌。
 */
public class DreamShock extends AbstractKroosCard {
    public static final String ID = KroosMod.MOD_ID + ":DreamShock";
    private static final String IMG = KroosMod.RES_ROOT + "cards/attack/dream_shock.png";

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 3;

    public DreamShock() {
        super(ID, IMG, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void useImpl(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AttackEffect.SLASH_HORIZONTAL));

        // find-or-create listener, append self to pending
        final DreamShock self = this;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                AbstractPlayer pl = AbstractDungeon.player;
                if (pl == null) return;
                DreamShockListenerPower lp =
                        (DreamShockListenerPower) pl.getPower(DreamShockListenerPower.POWER_ID);
                if (lp == null) {
                    lp = new DreamShockListenerPower(pl);
                    pl.powers.add(lp);
                    lp.flash();
                }
                lp.addPending(self);
            }
        });
    }

    @Override
    public AbstractKroosCard makeCopy() { return new DreamShock(); }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
