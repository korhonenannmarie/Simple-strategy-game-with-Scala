package gameStuff


abstract class Character(protected val name: String, protected val health: Int, protected var armour: Int, protected var toHit: Int,
                         protected var damagePerAttack: Int, protected val shield: Int):


  protected var damageDone: Int = 0 // All the damage a character does in a wave, is later saved to damageDoneTotal Mainly relevant for player characters.
  protected var damageDoneTotal: Int = 0 // All the damage a character has done in the entire game
  protected var currentHealth = health.max(0) // The character's curent health. Is changed by takeDamage and beHealed methods.
  protected var startingHealth = health // A way to save the max health of a character at the beginning of a wave

  protected val armourMod: Int
  protected val healthMod: Int
  protected val damageMod: Int
  protected val toHitMod: Int

  protected val rangedAttackName: String

  def isDead: Boolean = currentHealth <= 0
  def damageDoneTotalDef: Int = damageDoneTotal
  def isInMelee: Boolean = true
  def currentArmour = armour
  def healthToAttacker: Int = currentHealth


  def attack(target: Character): String =
    if target.isInMelee && target.takeDamage(this.damagePerAttack, this.toHit, this) then
      damageDone += damagePerAttack
      s"The attack hits! \n${target.characterName} takes $damagePerAttack damage."
    else
      "\nThe attack does not hit."

  def rangedAttack(target: Character): String =
    if (!target.isInMelee && this.toHit >= target.currentToHit) then
      val damage = target.takeDamage(this.damagePerAttack, this.toHit, this)
      if damage then
        damageDone += damagePerAttack
        s"${target.characterName} takes $damagePerAttack damage from ${this.characterName}'s ${rangedAttackName}.\n"
      else
        s"${target.characterName}'s armour was too high. The $rangedAttackName attack does not hit. \n"
      else if (target.isInMelee)
      s"${target.characterName} is in melee. The $rangedAttackName attack is ranged, so it does not hit."
      else
      s"${target.characterName} evades the $rangedAttackName attack."
      
  def characterName: String = this.name

  def currentToHit = toHit

  protected var defending: Boolean = false
  
  def defeding: Boolean = defending

  def defend(): String =
    armour += shield
    defending = true
    s"The ${this.name} raises their shield for the turn. Their armour is increased.\n"
  
  def resetForNewTurn(): Unit =
    if defending then
      armour += -shield
      defending = false

  def takeDamage(damage: Int, toHit: Int, attacker: Character): Boolean =
    if armour <= toHit then
      if currentHealth > 0 then
        currentHealth = (currentHealth - damage).max(0)
        true
      else
        false
    else
      false


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

  
  def modifyForNewWave(): Unit =
    startingHealth += (damageDone/2) * healthMod
    currentHealth = startingHealth
    armour += armourMod
    damagePerAttack += damageMod
    damageDoneTotal += damageDone
    damageDone = 0
    toHit += (damageDone/2) * toHitMod

  def rest(): String = s"The ${this.characterName} rests for a while.\n"

  def gridStats(): String =
    s"$name HP:$currentHealth/$startingHealth AC:$currentArmour/$armour"
    


end Character

