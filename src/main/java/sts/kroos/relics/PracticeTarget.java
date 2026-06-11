package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.powers.CriticalPower;
import sts.kroos.powers.FrostPower;
import sts.kroos.util.TextureLoader;

/**
 * 练习靶 — 基础。
 * 每回合第一次造成暴击时, 获得 1 层寒芒。
 *
 * 暴击检测复用 CriticalPower 已存在的语义: onUseCard 时若 card.type==ATTACK
 * 且玩家持 CriticalPower 且 amount>0 → 视为本牌暴击。
 * 用 triggeredThisTurn flag 限制每回合一次。
 */
public class PracticeTarget extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":PracticeTarget";
    private static final String IMG = KroosMod.RES_ROOT + "relics/practice_target.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/practice_target_outline.png";

    private boolean triggeredThisTurn = false;

    public PracticeTarget() {
        super(ID, (Texture) null, RelicTier.COMMON, LandingSound.FLAT);
        this.img = TextureLoader.getTexture(IMG);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void atTurnStart() { triggeredThisTurn = false; }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (triggeredThisTurn || card == null || card.type != AbstractCard.CardType.ATTACK) return;
        if (!AbstractDungeon.player.hasPower(CriticalPower.POWER_ID)) return;
        if (AbstractDungeon.player.getPower(CriticalPower.POWER_ID).amount <= 0) return;
        triggeredThisTurn = true;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player,
                new FrostPower(AbstractDungeon.player, 1), 1));
    }

    @Override
    public AbstractRelic makeCopy() { return new PracticeTarget(); }
}
