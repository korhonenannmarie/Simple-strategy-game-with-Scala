package gameStuff


abstract class Character(protected val name: String, protected val health: Int, protected var armour: Int, protected var toHit: Int,
                         protected var damagePerAttack: Int, protected val shield: Int):


  protected var waveDamage: Int       = 0             // All the damage a character does in a wave, is later saved to damageDoneTotal Mainly relevant for player characters.
  protected var damageDoneTotal: Int  = 0             // All the damage a character has done in the entire game
  protected var currentHealth         = health.max(0) // The character's curent health. Is changed by takeDamage and beHealed methods.
  protected var maxHealth             = health        // A way to save the max health of a character at the beginning of a wave, is changed by the modifyForNewWave method
  protected var defending: Boolean    = false         // Whether or not the character has their shield / protection up

  protected val armourMod: Int  // New wave modifier
  protected val healthMod: Int  // "-"
  protected val damageMod: Int  // "-"
  protected val toHitMod: Int   // "-"

  protected val rangedAttackName: String // The name of the ranged attack of each (player) character
  protected val defendingName: String // The name of the shield / defending item of each (player) character

  // Methods for instances of other classes to retrieve information about the character / boolean arguments for loops
  def characterName: String   = this.name
  def isDead: Boolean         = currentHealth <= 0
  def isInMelee: Boolean      = true // More specifically for monsters, not really used for player characters (yet?)
  def defedingDef: Boolean    = defending
  def damageDoneTotalDef: Int = damageDoneTotal
  def armourDef: Int          = armour
  def healthDef: Int          = currentHealth
  def toHitDef: Int           = toHit
  def gridStats: String     = s"$name HP:$currentHealth/$maxHealth AC:$armourDef/$armour" // used in the game's visuals


  // Basic attack for player characters, the monster class has this overridden.
  def attack(target: Character): String =

    if target.isInMelee && target.takeDamage(this.damagePerAttack, this.toHit, this) then
      waveDamage += damagePerAttack
      if target.isDead then
        s"\n${target.characterName} dies."
      else
        s"\nThe attack hits! \n${target.characterName} takes $damagePerAttack damage."
    else if !target.isInMelee then
      "\nThe target is too far away!"
    else
      "\nThe attack does not hit."
  end attack

  // Basic ranged attack for player characters, monsters do not use this (yet?)
  def rangedAttack(target: Character): String =

    if (!target.isInMelee && this.toHit >= target.toHitDef) then
      val damage = target.takeDamage(this.damagePerAttack, this.toHit, this)
      if damage then
        waveDamage += damagePerAttack
        if target.isDead then
          s"\n${target.characterName} dies.\n"
        else
          s"\n${target.characterName} takes $damagePerAttack damage from ${this.characterName}'s ${rangedAttackName}.\n"
      else
        s"\n${target.characterName}'s armour was too high. The $rangedAttackName attack does not hit. \n"
      else if (target.isInMelee)
      s"\n${target.characterName} is in melee. The $rangedAttackName attack is ranged, so it does not hit."
      else
      s"\n${target.characterName} evades the $rangedAttackName attack."
  end rangedAttack

  // Method called by other characters when this character is attacked
  def takeDamage(damage: Int, toHit: Int, attacker: Character): Boolean =
    if armour <= toHit then
      if currentHealth > 0 then
        currentHealth = (currentHealth - damage).max(0)
        true
      else
        false
    else
      false

  // Method called by other characters when this character is healed
  def beHealed(healingDone: Int): Int =
    if this.isDead then
      currentHealth = healingDone
      healingDone
    else if healingDone + currentHealth >= maxHealth then
      val a = currentHealth
      currentHealth = maxHealth
      maxHealth - a
    else
      currentHealth += healingDone
      healingDone

  // Defending system for the round for each (player) character
  def defend(): String =
    armour += shield
    defending = true
    s"\nThe ${this.name} ${this.defendingName} for the turn. Their armour is increased by $shield.\n"

  // Resets defending status (amongst possibly other things in the future) after the round is done
  def resetForNewTurn(): Unit =
    if defending then
      armour += -shield
      defending = false

  // Changes the characters statistics dependinig on how well they did on the previous round
  def modifyForNewWave(): Unit =
    maxHealth += waveDamage * healthMod
    currentHealth = maxHealth
    armour += waveDamage * armourMod
    damagePerAttack += waveDamage * damageMod
    toHit += waveDamage * toHitMod
    damageDoneTotal += waveDamage
    waveDamage = 0

  // Mainly a testing method, but can be used in the game as well.
  def rest(): String = s"\nThe ${this.characterName} rests for a while."

end Character

