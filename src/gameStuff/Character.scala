package gameStuff


abstract class Character(protected val name: String, protected val health: Int, protected var armour: Int, protected val toHit: Int,
                         protected var damagePerAttack: Int, protected val shield: Int):

/** name             the name of the character
 *  health           the character's health
 *  armour           the character's armour
 *  toHit            a value that is compared to another character's armour when attacking to determine if it hits
 *  damagePerAttack  the amount of damage a regular attack does when it hits
 *  shield           a value that is added to this character's armour when using the defend method */

  protected var damageDone: Int = 0         // amount of damage this character has done in a full game
  protected var currentHealth = health      // current health
  protected var startingHealth = health     // gets reset every new wave

  protected val armourMod: Int              // these are all used...
  protected val healthMod: Int              // ...to calculate how much...
  protected val damageMod: Int              // ...damage, health and armour go up when a new wave starts

  def isDead: Boolean = health <= 0         // character is dead if its health goes below zero
  def damageDoneInTotal: Int = damageDone   // a function the game can call without being able to modify it

  // attack: calls another character's takeDamage method, then adds the damage done to this character's damageDone counter.
  def attack(character: Character): Unit =
    if character.takeDamage(damagePerAttack, toHit) then
      damageDone += damagePerAttack


  private var defending: Boolean = false
  def defeding: Boolean = defending // gameStuff.Game can now call defending without changing it
  // defend: raises this character's armour for one round, and then game resets it back to normal.
  def defend(): Unit =
    armour += shield
    defending = true
  // lowers the defence written in defend
  def unDefend(): Unit
    armour += -shield
    defending = false

  // method called by another class in different types of attacks
  def takeDamage(damage: Int, toHit: Int): Boolean =
    if armour <= toHit then
      currentHealth += -damage
      true
    else
      false

  // method called by gameStuff.Mage class when healing
  def beHealed(healingDone: Int): Unit =
    if this.isDead then
      currentHealth = healingDone
    else if healingDone + currentHealth >= startingHealth then
      currentHealth = startingHealth
    else
      currentHealth += healingDone

  // method called by gameStuff.Game to raise character's health, armour etc.
  def modifyForNewWave(): Unit =
    ???
    
  def rest(): String = "You rest for a while. Better get a move on, though. Dave-try could be in trouble!"


end Character

