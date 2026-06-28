package sts.kroos.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
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
 * 使用 Spine 骨骼动画 (.skel 二进制格式), 参考原版 Mon3tr / Eyjafjalla 的实现。
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
    private static final String SPINE_PATH    = KroosMod.RES_ROOT + "char/kroos/char_1021_kroos2";
    private static final String SHOULDER_1    = KroosMod.RES_ROOT + "char/shoulder.png";
    private static final String SHOULDER_2    = KroosMod.RES_ROOT + "char/shoulder2.png";
    private static final String CORPSE        = KroosMod.RES_ROOT + "char/corpse.png";
    private static final float SPINE_SCALE    = 1.75F;

    // ===== Spine 动画名称 =====
    private static final String ANIM_IDLE         = "IdleVR";
    private static final String ANIM_ATTACK       = "AttackVR";
    private static final String ANIM_SKILL_BEGIN  = "Skill_BeginVR";
    private static final String ANIM_SKILL_IDLE   = "Skill_IdleVR";
    private static final String ANIM_SKILL_LOOP   = "Skill_LoopVR";
    private static final String ANIM_SKILL_END    = "Skill_EndVR";
    private static final String ANIM_DIE          = "DieVR";
    private static final String ANIM_START        = "StartVR";

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
        this.dialogY = this.drawY + 220.0F * Settings.scale;

        // 使用 Spine 动画: initializeClass 第一个参数传 null (不使用静态图片)
        initializeClass(null,
                SHOULDER_2, SHOULDER_1, CORPSE,
                getLoadout(),
                0.0F, 5.0F, 240.0F, 300.0F,
                new EnergyManager(ENERGY_PER_TURN));

        // 加载 Spine 骨骼动画 (.skel 二进制格式)
        loadSpineAnimation();
    }

    /**
     * 加载 Spine 骨骼动画。
     * STS 自带的 loadAnimation() 只支持 .json 格式,
     * 因此使用 SkeletonBinary 手动加载 .skel 二进制格式。
     * 参考 Mon3tr mod 的 Prosts.java 和 Eyjafjalla mod 的 SkinSelectScreen.java。
     */
    private void loadSpineAnimation() {
        this.atlas = new TextureAtlas(
                com.badlogic.gdx.Gdx.files.internal(SPINE_PATH + ".atlas"));
        SkeletonBinary binary = new SkeletonBinary(this.atlas);
        binary.setScale(Settings.renderScale / SPINE_SCALE);
        SkeletonData skeletonData = binary.readSkeletonData(
                com.badlogic.gdx.Gdx.files.internal(SPINE_PATH + ".skel"));
        this.skeleton = new com.esotericsoftware.spine.Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);

        // 设置初始动画为待机
        this.state.setAnimation(0, ANIM_IDLE, true);

        if (Settings.FAST_MODE) {
            state.setTimeScale(1.0F);
        }
    }

    // ===== 动画控制 =====

    /** 攻击动画: 播放攻击后回到待机 */
    public void playAttackAnimation() {
        this.state.setAnimation(0, ANIM_ATTACK, false);
        this.state.addAnimation(0, ANIM_IDLE, true, 0.0F);
    }

    /** 技能动画: 播放技能循环后回到待机 */
    public void playSkillAnimation() {
        this.state.setAnimation(0, ANIM_SKILL_LOOP, false);
        this.state.addAnimation(0, ANIM_IDLE, true, 0.0F);
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
