package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sts.kroos.KroosMod;
import sts.kroos.util.TextureLoader;

/**
 * 中的 Power (敌方负面)。
 *
 * 含义:
 *   - 拥有中的的敌人在行动开始前受到 10 点伤害(无视格挡, HP_LOSS)
 *   - 攻击行为转化为眩晕状态 (借助 StSLib.StunMonsterPower)
 *   - 中的层数每回合减少 1
 *
 * 触发时机: atStartOfTurn (敌人回合开始, 在 takeTurn 之前)
 *
 * 天坠之火 power 会在自己内部对此处的 10 点伤害进行加成,
 * 本 power 不耦合其逻辑, 仅对外暴露 BASE_DAMAGE 常量供其他 power 读取。
 */
public class HitPower extends AbstractPower {

    public static final String POWER_ID = KroosMod.MOD_ID + ":Hit";
    private static final PowerStrings STRINGS =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = STRINGS.NAME;
    public static final String[] DESCRIPTIONS = STRINGS.DESCRIPTIONS;

    public static final int BASE_DAMAGE = 10;

    private static final String ICON_LARGE = KroosMod.RES_ROOT + "powers/hit_large.png";
    private static final String ICON_SMALL = KroosMod.RES_ROOT + "powers/hit_small.png";

    public HitPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        loadIcons();
        updateDescription();
    }

    private void loadIcons() {
        Texture large = TextureLoader.getTexture(ICON_LARGE);
        Texture small = TextureLoader.getTexture(ICON_SMALL);
        if (large != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(large, 0, 0, 128, 128);
        if (small != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(small, 0, 0, 48, 48);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount <= 0 || owner == null || owner.isDeadOrEscaped()) return;
        this.flash();

        // 1) 10 点 HP_LOSS 伤害 (类似猎人中毒触发时机)
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                owner,
                new DamageInfo(owner, BASE_DAMAGE, DamageInfo.DamageType.HP_LOSS),
                AbstractGameAction.AttackEffect.FIRE));

        // 2) 眩晕 1 回合: 借助 StSLib 的 StunMonsterPower (会跳过本回合的 takeTurn)
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                owner, owner, new StunMonsterPower(owner, 1)));

        // 3) 自然衰减 1 层 (每回合 -1)
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, owner, POWER_ID, 1));
    }
}
