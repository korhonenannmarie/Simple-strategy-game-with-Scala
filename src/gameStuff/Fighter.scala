package gameStuff

class Fighter(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = fighterArmourMod
  val healthMod = fighterHealthMod
  val damageMod = fighterDamageMod
  val toHitMod = mageToHitMod
  
  
