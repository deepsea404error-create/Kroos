package sts.kroos;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.google.gson.Gson;
import sts.kroos.cards.attack.Awareness;
import sts.kroos.cards.attack.DoubleShot;
import sts.kroos.cards.attack.ExplosiveArrow;
import sts.kroos.cards.attack.FlawShot;
import sts.kroos.cards.attack.FrostPierce;
import sts.kroos.cards.attack.GlitteringEdge;
import sts.kroos.cards.attack.Hunt;
import sts.kroos.cards.attack.PrecisionShot;
import sts.kroos.cards.attack.RainbowArrow;
import sts.kroos.cards.attack.RapidFire;
import sts.kroos.cards.attack.ReinforcedArrow;
import sts.kroos.cards.attack.SkyVolley;
import sts.kroos.cards.attack.SlowBurn;
import sts.kroos.cards.attack.Strike;
import sts.kroos.cards.attack.Volley;
import sts.kroos.cards.attack.WarningShot;
import sts.kroos.cards.attack.Yawn;
import sts.kroos.cards.power.ArrowImprovement;
import sts.kroos.cards.power.DreamShadow;
import sts.kroos.cards.power.FlawConfirm;
import sts.kroos.cards.power.HeartScar;
import sts.kroos.cards.power.Heartbeat;
import sts.kroos.cards.power.Insomnia;
import sts.kroos.cards.power.PhantomCamo;
import sts.kroos.cards.power.Resonance;
import sts.kroos.cards.power.SkyfireFlame;
import sts.kroos.cards.power.SupplyDrop;
import sts.kroos.cards.power.Transparent;
import sts.kroos.cards.power.Unlit;
import sts.kroos.cards.power.Vigilance;
import sts.kroos.cards.skill.AimForVital;
import sts.kroos.cards.skill.Defend;
import sts.kroos.cards.skill.EmergencyEvasion;
import sts.kroos.cards.skill.EmergencyReload;
import sts.kroos.cards.skill.EquipmentUpgrade;
import sts.kroos.cards.skill.GoodDream;
import sts.kroos.cards.skill.MechanicalSight;
import sts.kroos.cards.skill.OpenEyes;
import sts.kroos.cards.skill.PreparedShot;
import sts.kroos.cards.skill.Rest;
import sts.kroos.cards.skill.Scout;
import sts.kroos.cards.skill.SimpleFortification;
import sts.kroos.cards.skill.WindUp;
import sts.kroos.characters.Kroos;
import sts.kroos.patches.KroosEnum;
import sts.kroos.relics.KroosBadge;

/**
 * 寒芒克洛丝 mod 主入口。
 * 负责注册颜色、角色、卡牌、遗物以及本地化资源。
 */
@SpireInitializer
public class KroosMod implements
        EditCharactersSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        OnStartBattleSubscriber,
        PostInitializeSubscriber {

    public static final String MOD_ID = "kroosmod";

    // === 配色 (寒芒：偏冷的银白色) ===
    public static final java.awt.Color KROOS_COLOR_AWT = new java.awt.Color(176, 196, 222, 255);

    // === 资源路径 (统一前缀, 不使用占位符) ===
    public static final String RES_ROOT = "kroosmod/images/";
    // 卡牌底纹
    public static final String ATTACK_BG       = RES_ROOT + "cardback/bg_attack_kroos.png";
    public static final String SKILL_BG        = RES_ROOT + "cardback/bg_skill_kroos.png";
    public static final String POWER_BG        = RES_ROOT + "cardback/bg_power_kroos.png";
    public static final String ATTACK_BG_P     = RES_ROOT + "cardback/bg_attack_kroos_p.png";
    public static final String SKILL_BG_P      = RES_ROOT + "cardback/bg_skill_kroos_p.png";
    public static final String POWER_BG_P      = RES_ROOT + "cardback/bg_power_kroos_p.png";
    // 能量球
    public static final String ENERGY_ORB      = RES_ROOT + "ui/energy_orb_kroos.png";
    public static final String ENERGY_ORB_P    = RES_ROOT + "ui/energy_orb_kroos_p.png";
    public static final String CARD_ENERGY_ORB = RES_ROOT + "ui/card_energy_orb_kroos.png";
    public static final String CHAR_BTN        = RES_ROOT + "char/charSelect_button.png";
    public static final String CHAR_BG         = RES_ROOT + "char/charSelect_bg.png";

    public KroosMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(
                KroosEnum.KROOS_COLOR,
                KROOS_COLOR_AWT,
                ATTACK_BG, SKILL_BG, POWER_BG,
                ENERGY_ORB,
                ATTACK_BG_P, SKILL_BG_P, POWER_BG_P,
                ENERGY_ORB_P,
                CARD_ENERGY_ORB
        );
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        new KroosMod();
    }

    // === 角色注册 ===
    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(
                new Kroos(CardCrawlGame.playerName),
                CHAR_BTN,
                CHAR_BG,
                KroosEnum.KROOS
        );
    }

    // === 卡牌注册 ===
    @Override
    public void receiveEditCards() {
        // 初始牌
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new DoubleShot());
        BaseMod.addCard(new PreparedShot());
        // 白卡攻击
        BaseMod.addCard(new RapidFire());
        BaseMod.addCard(new Hunt());
        BaseMod.addCard(new FrostPierce());
        BaseMod.addCard(new FlawShot());
        BaseMod.addCard(new Volley());
        BaseMod.addCard(new WarningShot());
        BaseMod.addCard(new Yawn());
        // 白卡技能
        BaseMod.addCard(new SimpleFortification());
        BaseMod.addCard(new EmergencyEvasion());
        BaseMod.addCard(new Rest());
        BaseMod.addCard(new OpenEyes());
        BaseMod.addCard(new WindUp());
        BaseMod.addCard(new AimForVital());
        BaseMod.addCard(new GoodDream());
        BaseMod.addCard(new Scout());
        BaseMod.addCard(new EmergencyReload());
        BaseMod.addCard(new EquipmentUpgrade());
        BaseMod.addCard(new MechanicalSight());
        // 蓝卡能力
        BaseMod.addCard(new Insomnia());
        BaseMod.addCard(new FlawConfirm());
        BaseMod.addCard(new HeartScar());
        BaseMod.addCard(new Transparent());
        BaseMod.addCard(new Unlit());
        BaseMod.addCard(new Resonance());
        BaseMod.addCard(new SupplyDrop());
        BaseMod.addCard(new DreamShadow());
        BaseMod.addCard(new Vigilance());
        BaseMod.addCard(new SkyfireFlame());
        BaseMod.addCard(new ArrowImprovement());
        BaseMod.addCard(new PhantomCamo());
        BaseMod.addCard(new Heartbeat());
        // 蓝卡攻击
        BaseMod.addCard(new SkyVolley());
        BaseMod.addCard(new ExplosiveArrow());
        BaseMod.addCard(new ReinforcedArrow());
        BaseMod.addCard(new RainbowArrow());
        BaseMod.addCard(new GlitteringEdge());
        BaseMod.addCard(new SlowBurn());
        BaseMod.addCard(new Awareness());
        BaseMod.addCard(new PrecisionShot());

        UnlockTracker.unlockCard(Strike.ID);
        UnlockTracker.unlockCard(Defend.ID);
        UnlockTracker.unlockCard(DoubleShot.ID);
        UnlockTracker.unlockCard(PreparedShot.ID);
        UnlockTracker.unlockCard(RapidFire.ID);
        UnlockTracker.unlockCard(Hunt.ID);
        UnlockTracker.unlockCard(FrostPierce.ID);
        UnlockTracker.unlockCard(FlawShot.ID);
        UnlockTracker.unlockCard(Volley.ID);
        UnlockTracker.unlockCard(WarningShot.ID);
        UnlockTracker.unlockCard(Yawn.ID);
        UnlockTracker.unlockCard(SimpleFortification.ID);
        UnlockTracker.unlockCard(EmergencyEvasion.ID);
        UnlockTracker.unlockCard(Rest.ID);
        UnlockTracker.unlockCard(OpenEyes.ID);
        UnlockTracker.unlockCard(WindUp.ID);
        UnlockTracker.unlockCard(AimForVital.ID);
        UnlockTracker.unlockCard(GoodDream.ID);
        UnlockTracker.unlockCard(Scout.ID);
        UnlockTracker.unlockCard(EmergencyReload.ID);
        UnlockTracker.unlockCard(EquipmentUpgrade.ID);
        UnlockTracker.unlockCard(MechanicalSight.ID);
        UnlockTracker.unlockCard(Insomnia.ID);
        UnlockTracker.unlockCard(FlawConfirm.ID);
        UnlockTracker.unlockCard(HeartScar.ID);
        UnlockTracker.unlockCard(Transparent.ID);
        UnlockTracker.unlockCard(Unlit.ID);
        UnlockTracker.unlockCard(Resonance.ID);
        UnlockTracker.unlockCard(SupplyDrop.ID);
        UnlockTracker.unlockCard(DreamShadow.ID);
        UnlockTracker.unlockCard(Vigilance.ID);
        UnlockTracker.unlockCard(SkyfireFlame.ID);
        UnlockTracker.unlockCard(ArrowImprovement.ID);
        UnlockTracker.unlockCard(PhantomCamo.ID);
        UnlockTracker.unlockCard(Heartbeat.ID);
        UnlockTracker.unlockCard(SkyVolley.ID);
        UnlockTracker.unlockCard(ExplosiveArrow.ID);
        UnlockTracker.unlockCard(ReinforcedArrow.ID);
        UnlockTracker.unlockCard(RainbowArrow.ID);
        UnlockTracker.unlockCard(GlitteringEdge.ID);
        UnlockTracker.unlockCard(SlowBurn.ID);
        UnlockTracker.unlockCard(Awareness.ID);
        UnlockTracker.unlockCard(PrecisionShot.ID);
    }

    // === 遗物注册 ===
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new KroosBadge(), KroosEnum.KROOS_COLOR);
        UnlockTracker.markRelicAsSeen(KroosBadge.ID);
    }

    // === 本地化资源 ===
    @Override
    public void receiveEditStrings() {
        String lang = "zh_CN";
        String dir = "kroosmod/localization/" + lang + "/";
        BaseMod.loadCustomStringsFile(CardStrings.class,      dir + "Cards-strings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class,     dir + "Relics-strings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class,     dir + "Powers-strings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, dir + "Character-strings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal("kroosmod/localization/zh_CN/Keywords-strings.json").readString("UTF-8");
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword k : keywords) {
                BaseMod.addKeyword(MOD_ID, k.PROPER_NAME, k.NAMES, k.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        // 预留：badge / mod 设置面板
    }

    @Override
    public void receiveOnBattleStart(com.megacrit.cardcrawl.rooms.AbstractRoom room) {
        // 战斗开始时清空战斗范围计数器 (强化箭/精准射击等持续计数依赖)
        sts.kroos.util.BattleCounters.resetAll();
    }
}
