package sts.kroos.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
 * 破绽确认 Power。
 * 含义: 每回合前 X 张牌对敌人造成伤害时, 施加 1 层破绽。
 *
 * 实现:
 *   - onAttack 钩子: 当玩家伤害敌人, 且本回合触发次数未达 X 次时, 施加 1 破绽
 *   - 用 lastTriggeredCard 防止同一张卡多次伤害重复计数
 *   - atStartOfTurn 重置计数与去重缓存
 */
public class FlawConfirmPower extends AbstractPower {
    public static final String POWER_ID = KroosMod.MOD_ID + ":FlawConfirm";
    private static final PowerStrings S = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = S.NAME;
    public static final String[] DESC = S.DESCRIPTIONS;
    private static final String IL = KroosMod.RES_ROOT + "powers/flaw_confirm_large.png";
    private static final String IS = KroosMod.RES_ROOT + "powers/flaw_confirm_small.png";

    private int triggersLeft;
    private AbstractCard lastTriggeredCard;

    public FlawConfirmPower(AbstractCreature owner, int amount) {
        this.name = NAME; this.ID = POWER_ID; this.owner = owner; this.amount = amount;
        this.type = PowerType.BUFF;
        this.triggersLeft = amount;
        Texture l = TextureLoader.getTexture(IL), s = TextureLoader.getTexture(IS);
        if (l != null) this.region128 = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(l, 0, 0, 84, 84);
        if (s != null) this.region48  = new com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion(s, 0, 0, 32, 32);
        updateDescription();
    }

    @Override
    public void updateDescription() { this.description = DESC[0] + this.amount + DESC[1]; }

    @Override
    public void atStartOfTurn() {
        triggersLeft = this.amount;
        lastTriggeredCard = null;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (triggersLeft <= 0 || damageAmount <= 0) return;
        if (target == null || target == owner) return;
        AbstractCard cur = AbstractDungeon.player != null ? AbstractDungeon.player.cardInUse : null;
        if (cur == null || cur == lastTriggeredCard) return;
        lastTriggeredCard = cur;
        triggersLeft--;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                target, owner, new FlawPower(target, 1), 1));
    }
}
