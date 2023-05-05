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
    
  def fireBall(target: Character): String =
    if !target.isInMelee && target.takeDamage(this.damagePerAttack, this.toHit, target.isInMelee) then
      damageDone += damagePerAttack
      s"${target.characterName} takes $damagePerAttack damage from the fireball attack.\n"
    else
      "The fireball attack does not hit. You drop the bottle.\n"