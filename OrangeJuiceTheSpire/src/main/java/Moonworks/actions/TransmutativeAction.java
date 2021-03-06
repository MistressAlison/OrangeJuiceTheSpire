package Moonworks.actions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.TransmutativeModifier;
import Moonworks.cards.MiracleWalker;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransmutativeAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final TransmutativeModifier cardMod;
    private final AbstractCard card;
    private final boolean normaEffect;

    public TransmutativeAction(TransmutativeModifier cardMod, AbstractCard card, boolean normaEffect) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.card = card;
        this.cardMod = cardMod;
        this.normaEffect = normaEffect;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST){
            //Don't let this card transform into a gift card. They pretty much never work
            AbstractCard tempCard = null;
            while (tempCard == null || tempCard.cost == -2) {
                tempCard = AbstractDungeon.returnTrulyRandomCard().makeCopy();
            }
            int index = AbstractDungeon.player.hand.group.indexOf(card);
            if(card.upgraded && tempCard.canUpgrade()) {
                tempCard.upgrade();
            }
            tempCard.name += "?";
            if(cardMod.free) {
                tempCard.costForTurn = 0;
                tempCard.isCostModifiedForTurn = true;
            }
            if(normaEffect && (tempCard.type == AbstractCard.CardType.ATTACK || tempCard.type == AbstractCard.CardType.SKILL)) {
                tempCard.exhaust = false;
                tempCard.isEthereal = false;
            }
            if(!cardMod.infinite) {
                cardMod.uses--;
            }
            if(cardMod.infinite || cardMod.uses > 0) {
                CardModifierManager.addModifier(tempCard, cardMod);
            }
            tempCard.current_x = card.current_x;
            tempCard.current_y = card.current_y;
            tempCard.target_x = card.target_x;
            tempCard.target_y = card.target_y;
            tempCard.drawScale = 1.0F;
            tempCard.targetDrawScale = card.targetDrawScale;
            tempCard.angle = card.angle;
            tempCard.targetAngle = card.targetAngle;
            tempCard.superFlash(Color.WHITE.cpy());
            AbstractDungeon.player.hand.group.set(index, tempCard);
            AbstractDungeon.player.hand.glowCheck();
        }
        this.tickDuration();
    }
}
