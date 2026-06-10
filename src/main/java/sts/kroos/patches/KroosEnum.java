package sts.kroos.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

/**
 * 通过 SpireEnum 向原版枚举注入克洛丝的颜色与角色标识。
 * - PlayerClass.KROOS: 角色枚举
 * - CardColor.KROOS_COLOR: 卡牌颜色
 * - CardLibraryType.KROOS_COLOR: 卡牌图书馆分类
 */
public class KroosEnum {

    @SpireEnum
    public static AbstractPlayer.PlayerClass KROOS;

    @SpireEnum(name = "KROOS_COLOR")
    public static AbstractCard.CardColor KROOS_COLOR;

    @SpireEnum(name = "KROOS_COLOR")
    public static CardLibrary.LibraryType KROOS_COLOR_LIBRARY;
}
