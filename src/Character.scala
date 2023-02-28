
abstract class Character(protected val name: String, protected val health: Int, protected var armour: Int, protected val toHit: Int,
                         protected var damagePerAttack: Int, protected val shield: Int):


  protected var damageDone: Int = 0 // how much the Character has done damage in the wave

  protected val armourMod: Int
  protected val healthMod: Int
  protected val damageMod: Int

  protected var currentHealth = health


  def isDead: Boolean = health <= 0
  def damageDoneInTotal: Int = damageDone

  def attack(character: Character): Unit =
    if character.takeDamage(damagePerAttack, toHit) then
      damageDone += damagePerAttack

  def defend(): Unit = ??? // while roundCount = currentRoundCount, armour = +shield

  def takeDamage(damage: Int, toHit: Int): Boolean =
    if armour <= toHit then
      currentHealth += -damage
      true
    else
      false

  def beHealed(healingDone: Int): Unit =
    currentHealth += healingDone

  def modifyForNewWave(): Unit =
    damagePerAttack += damageMod
    currentHealth += health + (healthMod * damageDone) // nope, this does not work...
    // when new waves are done it makes everything difficult
    armour += armourMod * damageDone
    damageDone = 0


end Character

