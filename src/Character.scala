
abstract class Character:

  // base characteristics of the Character class such as
  var name: String
  var health: Int
  var armour: Int
  var toHit: Int
  var damagePerAttack: Int

  def isDead: Boolean
  def damageDoneInTotal: Int
  def currentHealth: Int

  def attack(monster: Monster): Unit
  def defend(): Unit
  def takeDamage(damage: Int, toHit: Int): Unit
  def beHealed(healingDone: Int): Unit



