package gameStuff


abstract class Character(protected val name: String, protected val health: Int, protected var armour: Int, protected var toHit: Int,
                         protected var damagePerAttack: Int, protected val shield: Int):


  protected var damageDone: Int = 0
  protected var currentHealth = health
  protected var startingHealth = health

  protected val armourMod: Int
  protected val healthMod: Int
  protected val damageMod: Int
  protected val toHitMod: Int

  protected val rangedAttackName: String

  def isDead: Boolean = currentHealth <= 0
  def damageDoneInTotal: Int = damageDone
  def isInMelee: Boolean = true
  def currentArmour = armour


  def attack(target: Character): String =
    if target.isInMelee && target.takeDamage(this.damagePerAttack, this.toHit, this) then
      damageDone += damagePerAttack
      s"${target.characterName} takes $damagePerAttack damage.\n"
    else
      "The attack does not hit.\n"

  def rangedAttack(target: Character): String =
    if (!target.isInMelee && this.toHit >= target.currentToHit) then
      val damage = target.takeDamage(this.damagePerAttack, this.toHit, this)
      if damage then
        damageDone += damagePerAttack
        s"${target.characterName} takes $damagePerAttack damage from ${this.characterName}'s ${rangedAttackName}.\n"
      else
        s"${target.characterName} has armour that was too high. The $rangedAttackName attack does not hit. \n"
      else if (target.isInMelee)
      s"${target.characterName} is in melee. The $rangedAttackName attack does not hit."
      else
      s"${target.characterName} evades the $rangedAttackName attack."
      
  def characterName: String = this.name

  def currentToHit = toHit

  protected var defending: Boolean = false
  
  def defeding: Boolean = defending

  def defend(): String =
    armour += shield
    defending = true
    s"the ${this.name} raises their shield for the turn. Your armour is increased.\n"
  
  def resetForNewTurn(): Unit =
    if defending then
      armour += -shield
      defending = false

  def takeDamage(damage: Int, toHit: Int, attacker: Character): Boolean =
    if armour <= toHit then
      if currentHealth > 0 then
        currentHealth += -damage
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
    damageDone = 0
    toHit += (damageDone/2) * toHitMod

  def rest(): String = "You rest for a while.\n"

  def currentStats(): String =
    s"$name has a health of $currentHealth/$startingHealth. Their current armour is $armour. \n"
    
  def healthToAttacker: Int = currentHealth

end Character

