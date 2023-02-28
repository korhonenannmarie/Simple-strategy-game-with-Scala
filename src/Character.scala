
abstract class Character(protected val name: String, protected var health: Int, protected var armour: Int, protected val toHit: Int,
                         protected val damagePerAttack: Int, protected val shield: Int):

  
  var damageDone: Int = 0

  def isDead: Boolean = health <= 0
  def damageDoneInTotal: Int = damageDone
  def currentHealth: Int = health

  def attack(character: Character): Unit =
    if character.takeDamage(damagePerAttack, toHit) then
      damageDone += damagePerAttack

  def defend(): Unit = ??? // while roundCount = currentRoundCount, armour = +shield

  def takeDamage(damage: Int, toHit: Int): Boolean =
    if armour <= toHit then
      health += -damage
      true
    else
      false

  def beHealed(healingDone: Int): Unit =
    health += healingDone
    
  def modifyForNewWave(): Unit = ??? // Changes the character's stats when a new wave is started 

end Character

