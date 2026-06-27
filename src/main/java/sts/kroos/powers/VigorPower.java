package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 蓄力 Power (等效原版 VigorPower)。
 *
 * 含义: 下一次攻击造成 X 点额外伤害; 攻击后消耗全部层数。
 * 用于 "上弦"卡牌 和 遗物"成长的证明"。
 *
 * 实现:
 *   - atDamageGive: 若类型为 NORMAL, 返回 damage + this.amount
 *   - onUseCard: 若打出 ATTACK 牌, 入队 RemoveSpecificPowerAction 清除本 power
 */
public class VigorPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":Vigor";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/vigor_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/vigor_small.png";

    public VigorPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 128, 128);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0] + this.amount + DESC[1];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (this.amount > 0 && type == DamageInfo.DamageType.NORMAL) {
            return damage + this.amount;
        }
        return damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card != null && card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }
}
