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
 * A1 小组的合影 — 罕见。
 * 战斗开始时, 从 3 张随机 A1 小队卡中选 1 张加入手牌。本回合耗能为 0 (Discovery 自动)。
 */
public class A1GroupPhoto extends CustomRelic {
    public static final String ID = KroosMod.MOD_ID + ":A1GroupPhoto";
    private static final String IMG = KroosMod.RES_ROOT + "relics/a1_group_photo.png";
    private static final String OUTLINE = KroosMod.RES_ROOT + "relics/a1_group_photo_outline.png";

    public A1GroupPhoto() {
        super(ID, TextureLoader.getTexture(IMG), RelicTier.UNCOMMON, LandingSound.FLAT);
        this.outlineImg = TextureLoader.getTexture(OUTLINE);
    }

    @Override
    public String getUpdatedDescription() { return DESCRIPTIONS[0]; }

    @Override
    public void atBattleStart() {
        this.flash();
        List<AbstractCard> candidates = A1SquadFactory.randomA1Cards(3);
        AbstractDungeon.actionManager.addToBottom(
                new A1DiscoveryAction(new ArrayList<>(candidates), 1));
    }

    @Override
    public AbstractRelic makeCopy() { return new A1GroupPhoto(); }
}
