package gameStuff

import gameStuff.Character


class Mage(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = mageArmourMod
  val healthMod = mageHealthMod
  val damageMod = mageDamageMod
  val healingDone = mageHealingDone
  val toHitMod = mageToHitMod

  def heal(target: Character): String =
    val a = target.beHealed(healingDone)
    s"${target.characterName} is healed $a health points."
    
  