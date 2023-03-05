package gameStuff
import gameStuff.Game

object UserInterface:
  private val game = Game()
  this.run()
  
  private def run() =
    println(this.game.welcomeMessage)
    while !this.game.isOver do
      this.game.playGame()
    println("\n" + this.game.goodbyeMessage)
    
end UserInterface