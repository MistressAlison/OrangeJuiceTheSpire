package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.patches.FixedPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Flamethrower extends AbstractGiftCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Flamethrower.class.getSimpleName());
    public static final String IMG = makeCardPath("Flamethrower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    //private static final int EFFECT = 2;

    private static final int USES = 12;
    private static final int UPGRADE_PLUS_USES = 6;

    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 1;

    // /STAT DECLARATION/

    public Flamethrower() {

        this(USES, false);

    }

    public Flamethrower(int currentUses, boolean checkedGolden) {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = DAMAGE;
        this.damageType = DamageInfo.DamageType.THORNS;

    }


    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if(isActive() && c != this) { //Dont activate when playing itself

            //Define a damage info and set Fixed to true
            DamageInfo fixedDamage = new DamageInfo(AbstractDungeon.player, magicNumber, damageTypeForTurn);
            FixedPatches.FixedField.fixed.set(fixedDamage, true);

            //If we actually had a monster that our card was played on, hit them
            if(m != null) {
                calculateCardDamage(m);
                this.addToBot(new DamageAction(m, fixedDamage, AbstractGameAction.AttackEffect.FIRE));
            } else {
                this.addToBot(new DamageRandomEnemyAction(fixedDamage, AbstractGameAction.AttackEffect.FIRE));
            }
            this.applyEffect();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE);
            //upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Flamethrower(secondMagicNumber, checkedGolden);
    }
}
