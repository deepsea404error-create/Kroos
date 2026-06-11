package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 受击伤害翻倍 Power (敌方负面)。
 *
 * 含义: 本回合该敌人所受到的攻击伤害翻倍 (×2)。
 * 与原版 Vulnerable 的 1.5 倍区分; 仅作用于 NORMAL 攻击伤害。
 *
 * 仅在玩家回合结束时(敌方 atEndOfTurn 之前)自动移除 — 视为"仅本玩家回合生效"。
 * 当前实现: 在 owner.atEndOfTurn(isPlayer=false) 时移除自身。
 */
public class DoubleIncomingPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":DoubleIncoming";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/double_incoming_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/double_incoming_small.png";

    public DoubleIncomingPower(AbstractCreature owner) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner;
        this.amount = -1; // 不可堆叠, 仅作 token
        this.type = PowerType.DEBUFF;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 128, 128);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() { this.description = DESC[0]; }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) return damage * 2F;
        return damage;
    }

    /** 玩家回合结束 → 敌方 atEndOfTurn(isPlayer=false) → 移除自身 */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) return;
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                owner, owner, POWER_ID));
    }
}
