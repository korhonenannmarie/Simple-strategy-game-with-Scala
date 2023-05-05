package gameStuff
import gameStuff.Game

import scala.io.StdIn.readLine

object UserInterface extends App:
  private val game = Game()
    this.run()

  private def run() =
    game.playGame()
    
end UserInterface