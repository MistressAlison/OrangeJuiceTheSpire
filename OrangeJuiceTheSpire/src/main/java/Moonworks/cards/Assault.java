package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Melter;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Assault extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Assault.class.getSimpleName());
    public static final String IMG = makeCardPath("Assault.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public Assault() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        damage = baseDamage = DAMAGE;
        //damageType = damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.DAMAGEMOD, 2, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int removedBlock = m.currentBlock;
        this.addToBot(new RemoveAllBlockAction(m, p));
        /*AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                m.currentBlock = 0;
                this.isDone = true;
            }});*/
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                m.currentBlock = removedBlock;
                this.isDone = true;
            }});
        //this.addToBot(new GainBlockAction(m, removedBlock, true));
        //m.currentBlock = removedBlock;
    }

    //Stops powers from effecting the card
    @Override
    public void applyPowers() {}
    @Override
    public void calculateCardDamage(AbstractMonster m) {}

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.initializeDescription();
        }
    }
}