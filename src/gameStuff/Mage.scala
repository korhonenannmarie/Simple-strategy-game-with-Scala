package gameStuff

import gameStuff.Character


class Mage(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = mageArmourMod
  val healthMod = mageHealthMod
  val damageMod = mageDamageMod
  val healingDone = mageHealingDone
  val toHitMod = mageToHitMod
  val fireBallMod = mageRangedMod + waveDamage / 5

  val rangedAttackName = "fireball"
  val defendingName    = "raises up their magical wards"

  // Method specifically for the mage. Heals the target.
  def heal(target: Character): String =
    val a = target.beHealed(healingDone)
    waveDamage += healingDone
    s"${target.characterName} is healed $a health points."
    
  override def rangedAttack(target: Character): String =

    if (!target.isInMelee && this.toHit >= target.toHitDef) then
      val damage = target.takeDamage(this.damagePerAttack + fireBallMod, this.toHit, this)
      if damage then
        waveDamage += damagePerAttack
        s"${target.characterName} takes $damagePerAttack damage from ${this.characterName}'s ${rangedAttackName}.\n"
      else
        s"The $rangedAttackName attack does not hit. The ${this.characterName} drops the bottle.\n"
      else if (target.isInMelee)
      s"${target.characterName} is in melee. The $rangedAttackName attack is ranged, so it does not hit. ${this.characterName} drinks some fireball."
      else
      s"${target.characterName} evades the $rangedAttackName attack."
  end rangedAttack