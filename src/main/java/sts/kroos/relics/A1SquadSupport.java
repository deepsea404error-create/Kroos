package sts.kroos.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sts.kroos.KroosMod;
import sts.kroos.actions.A1DiscoveryAction;
import sts.kroos.util.A1SquadFactory;
import sts.kroos.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A1 小队的支援 — BOSS。
 * 每 3 回合, 从 3 张随机 A1 小队卡中选 1 张加入手牌 (本回合耗能 0 由 Discovery 自管)。
 *
 * 触发时机: atTurnStart 计数, turnCount 是 3 的倍数(即 3/6/9...)触发。
 */
public class A1SquadSupport extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":A1SquadSupport";
    private static final String IMG = KroosMod.RES_ROOT + "relics/a1_squad_support.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/a1_squad_support_outline.png";

    public static final int TRIGGER_EVERY = 3;
    private int turnCount = 0;

    public A1SquadSupport() {
        super(ID, TextureLoader.getTexture(IMG), RelicTier.BOSS, LandingSound.MAGICAL);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void atBattleStart() { turnCount = 0; }

    @Override
    public void atTurnStart() {
        turnCount++;
        if (turnCount % TRIGGER_EVERY != 0) return;
        this.flash();
        List<AbstractCard> candidates = A1SquadFactory.randomA1Cards(3);
        AbstractDungeon.actionManager.addToBottom(
                new A1DiscoveryAction(new ArrayList<>(candidates), 1));
    }

    @Override
    public AbstractRelic makeCopy() { return new A1SquadSupport(); }
}
