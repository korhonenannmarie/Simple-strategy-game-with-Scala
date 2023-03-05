package gameStuff

class Action(input: String):
  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ')
  private val modifiers   = commandText.drop(verb.length).trim
  
  def execute(actor: Character): Option[String] = this.verb match
    case "rest"       => Some(actor.rest())
    
  
