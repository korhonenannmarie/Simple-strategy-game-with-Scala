package gameStuff

class Fighter(name: String, health: Int, armour: Int, toHit: Int, damagePerAttack: Int, shield: Int)
  extends Character(name, health, armour, toHit, damagePerAttack, shield):
  
  val armourMod = fighterArmourMod
  val healthMod = fighterHealthMod
  val damageMod = fighterDamageMod
  val toHitMod = mageToHitMod

  val rangedAttackName  = "longbow"
  val defendingName     = "raises up their shield"

  // Method specifically for the fighter. Protects the fighter AND the target.
  def protect(target: Character) =
    target.defend()
    this.defend()
    waveDamage += shield
    s"the fighter raises their shield to defend themselves and ${target.characterName}"