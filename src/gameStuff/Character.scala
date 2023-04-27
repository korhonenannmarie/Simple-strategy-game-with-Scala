package gameStuff


abstract class Character(protected val name: String, protected val health: Int, protected var armour: Int, protected var toHit: Int,
                         protected var damagePerAttack: Int, protected val shield: Int):

/** name             the name of the character
 *  health           the character's health
 *  armour           the character's armour
 *  toHit            a value that is compared to another character's armour when attacking to determine if it hits
 *  damagePerAttack  the amount of damage a regular attack does when it hits
 *  shield           a value that is added to this character's armour when using the defend method */

//todo: change all the action texts to be constants
//todo: tweak the numbers

  protected var damageDone: Int = 0         // amount of damage this character has done in a full game
  protected var currentHealth = health      // current health
  protected var startingHealth = health     // gets reset every new wave

  protected val armourMod: Int              // these are all used...
  protected val healthMod: Int              // ...to calculate how much...
  protected val damageMod: Int              // ...damage, health and armour go up when a new wave starts
  protected val toHitMod: Int

  def isDead: Boolean = currentHealth <= 0         // character is dead if its health goes to zero or below
  def damageDoneInTotal: Int = damageDone   // a function the game can call without being able to modify it
  def isInMelee: Boolean = false


  // attack: calls another character's takeDamage method, then adds the damage done to this character's damageDone counter.
  def attack(target: Character): String =
    if target.takeDamage(this.damagePerAttack, this.toHit) && target.isInMelee then // should these texts maybe come from a collection of constants?
      damageDone += damagePerAttack
      s"${target.name} takes $damagePerAttack damage.\n"
    else
      "The attack does not hit.\n"
      
  def characterName: String = this.name


  protected var defending: Boolean = false
  
  def defeding: Boolean = defending // gameStuff.Game can now call defending without changing it
  // defend: raises this character's armour for one round, and then game resets it back to normal.

  def defend(): String =
    armour += shield
    defending = true
    s"the ${this.name} raises their shield for the turn. Your armour is increased.\n"
  // lowers the defence written in defend
  
  def resetForNewTurn(): Unit =
    if defending then
      armour += -shield
      defending = false

  // method called by another class in different types of attacks
  def takeDamage(damage: Int, toHit: Int): Boolean =
    if armour <= toHit then
      if currentHealth > 0 then
        currentHealth += -damage
        true
      else
        false
    else
      false

  // method called by the Mage class when healing
  def beHealed(healingDone: Int): Int =
    if this.isDead then
      currentHealth = healingDone
      healingDone
    else if healingDone + currentHealth >= startingHealth then
      val a = currentHealth
      currentHealth = startingHealth
      startingHealth - a
    else
      currentHealth += healingDone
      healingDone

  // method called by Game to raise character's health, armour etc.
  
  def modifyForNewWave(): Unit =
    startingHealth += (damageDone/2) * healthMod
    currentHealth = startingHealth
    armour += armourMod
    damagePerAttack += damageMod
    damageDone = 0
    toHit += (damageDone/2) * toHitMod
    
    
  // mainly for testing
  def rest(): String = "You rest for a while.\n"

  // called to let the user know about current stats, will probably be changed later.
  def currentStats(): String =
    s"$name has a health of $currentHealth/$startingHealth. Their current armour is $armour. \n"
    


end Character

