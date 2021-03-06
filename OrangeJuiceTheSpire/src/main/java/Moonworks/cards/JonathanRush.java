package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.UpgradeRushAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.relics.LittleGull;
import Moonworks.vfx.GullVFXContainer;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import java.util.ArrayList;

import static Moonworks.OrangeJuiceMod.disableGullVfx;
import static Moonworks.OrangeJuiceMod.makeCardPath;

public class JonathanRush extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(JonathanRush.class.getSimpleName());
    public static final String IMG = makeCardPath("JonathanRush.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BONUS_DAMAGE = 2;
    private static final int UPGRADE_PLUS_BONUS_DAMAGE = 1;

    // /STAT DECLARATION/


    public JonathanRush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        AutoplayField.autoplay.set(this, true);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BONUS_DAMAGE;
        //this.returnToHand = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCreature> validTargets = new ArrayList<>();
        for (AbstractMonster abstractMonster : AbstractDungeon.getMonsters().monsters) {
            if (!abstractMonster.isDeadOrEscaped()) {
                validTargets.add(abstractMonster);
            }
        }
        if (validTargets.size() > 0) {
            AbstractCreature t = validTargets.get(AbstractDungeon.cardRandomRng.random(0, validTargets.size()-1));
            calculateCardDamage((AbstractMonster) t);
            boolean hasThorns = t.hasPower(ThornsPower.POWER_ID);
            int gullThrows = Math.min(20, Math.max(1, MathUtils.floor(damage / 3f)));

            //If we have not disabled gull vfx...
            if (!disableGullVfx) {
                //Throws multiple gulls at high attack damage
                GullVFXContainer.rushAttackVFX(t, hasThorns, damage > 15, gullThrows);
            }

            //Do the damage action, but don't play an extra sound if we played the VFX, since that handles it
            if (!disableGullVfx) {
                this.addToBot(new DamageAction(t, new DamageInfo(p, damage, damageTypeForTurn)));
            } else {
                this.addToBot(new DamageAction(t, new DamageInfo(p, damage, damageTypeForTurn),
                        hasThorns ? AbstractGameAction.AttackEffect.SLASH_HORIZONTAL :
                                damage > 15 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }

            //Upgrade the rushes you have
            this.addToBot(new UpgradeRushAction(this, magicNumber));

            //If you have Little Gull, trigger it
            if (AbstractDungeon.player.hasRelic(LittleGull.ID)) {
                LittleGull lg = (LittleGull) AbstractDungeon.player.getRelic(LittleGull.ID);
                lg.doGullDamage();
            }

        }
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_DAMAGE);
            //upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}