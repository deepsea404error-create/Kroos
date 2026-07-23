package sts.kroos.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import sts.kroos.KroosMod;
import sts.kroos.cards.attack.DoubleShot;
import sts.kroos.cards.attack.Strike;
import sts.kroos.cards.skill.Defend;
import sts.kroos.cards.skill.PreparedShot;
import sts.kroos.patches.KroosEnum;
import sts.kroos.relics.KroosBadge;

import java.util.ArrayList;

/**
 * 寒芒克洛丝 - 角色定义。
 * 使用静态 PNG 贴图 (kroos.png) 显示人物。
 */
public class Kroos extends CustomPlayer {

    // ===== 角色基础属性 =====
    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 72;
    public static final int MAX_HP = 72;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    public static final String CHAR_ID = "KROOS";
    private static final CharacterStrings CHAR_STRINGS =
            CardCrawlGame.languagePack.getCharacterString(CHAR_ID);
    public static final String NAME = CHAR_STRINGS.NAMES[0];
    public static final String DESCRIPTION = CHAR_STRINGS.TEXT[0];

    // ===== 资源路径 =====
    private static final String CHAR_IMG      = KroosMod.RES_ROOT + "char/kroos.png";
    private static final String SHOULDER_1    = KroosMod.RES_ROOT + "char/shoulder.png";
    private static final String SHOULDER_2    = KroosMod.RES_ROOT + "char/shoulder2.png";
    private static final String CORPSE        = KroosMod.RES_ROOT + "char/corpse.png";

    // CustomEnergyOrb 期望 11 元素: [0..4]=正常层, [5]=base底层, [6..10]=暗层
    private static final String[] ORB_TEX = {
            KroosMod.RES_ROOT + "ui/energy/layer1.png",
            KroosMod.RES_ROOT + "ui/energy/layer2.png",
            KroosMod.RES_ROOT + "ui/energy/layer3.png",
            KroosMod.RES_ROOT + "ui/energy/layer4.png",
            KroosMod.RES_ROOT + "ui/energy/layer5.png",
            KroosMod.RES_ROOT + "ui/energy/layer0.png",    // base layer
            KroosMod.RES_ROOT + "ui/energy/layer1d.png",
            KroosMod.RES_ROOT + "ui/energy/layer2d.png",
            KroosMod.RES_ROOT + "ui/energy/layer3d.png",
            KroosMod.RES_ROOT + "ui/energy/layer4d.png",
            KroosMod.RES_ROOT + "ui/energy/layer5d.png"
    };
    private static final String ORB_VFX = KroosMod.RES_ROOT + "ui/energy/vfx.png";

    private static final float[] LAYER_SPEED = new float[] {
            -20.0F, 20.0F, -40.0F, 40.0F, 360.0F,
            -10.0F, 8.0F, -5.0F, 5.0F, 0.0F
    };

    public Kroos(String name) {
        super(name, KroosEnum.KROOS, ORB_TEX, ORB_VFX, LAYER_SPEED,
                (String) null, (String) null);

        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 240.0F * Settings.scale;

        // 使用静态贴图: initializeClass 第一个参数传图片路径
        initializeClass(CHAR_IMG,
                SHOULDER_1, SHOULDER_2, CORPSE,
                getLoadout(),
                0.0F, -5.0F, 260.0F, 240.0F,
                new EnergyManager(ENERGY_PER_TURN));
    }

    // ===== 原有角色方法 =====

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> deck = new ArrayList<>();
        deck.add(Strike.ID);
        deck.add(Strike.ID);
        deck.add(Strike.ID);
        deck.add(Strike.ID);
        deck.add(Defend.ID);
        deck.add(Defend.ID);
        deck.add(Defend.ID);
        deck.add(Defend.ID);
        deck.add(DoubleShot.ID);
        deck.add(PreparedShot.ID);
        return deck;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> relics = new ArrayList<>();
        relics.add(KroosBadge.ID);
        UnlockTracker.markRelicAsSeen(KroosBadge.ID);
        return relics;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                NAME, DESCRIPTION,
                STARTING_HP, MAX_HP, ORB_SLOTS,
                STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return KroosEnum.KROOS_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return new Color(0.69F, 0.77F, 0.87F, 1.0F);
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new DoubleShot();
    }

    @Override
    public Color getCardTrailColor() {
        return new Color(0.69F, 0.77F, 0.87F, 1.0F);
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return com.megacrit.cardcrawl.helpers.FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public void playDeathAnimation() {
        // 确保 corpseImg 非空; 若贴图缺失则回退到正常 img
        this.img = (this.corpseImg != null) ? this.corpseImg : this.img;
        this.renderCorpse = true;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Kroos(name);
    }

    @Override
    public String getSpireHeartText() {
        return "...";
    }

    @Override
    public Color getSlashAttackColor() {
        return new Color(0.69F, 0.77F, 0.87F, 1.0F);
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        };
    }

    @Override
    public String getVampireText() {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[1];
    }
}
