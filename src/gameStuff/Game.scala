package gameStuff

import gameStuff.Character

import scala.collection.mutable.Buffer
class Game:

  private var roundCount: Int = 0
  private var currentScore: Int = 0
  private var highScores = Buffer()
  private var roundIsOver: Boolean = false
  private var waveIsOver: Boolean = false

  private def setCharacters = ???
  private def newWave = ???
  private def playRound = ???
  private def usersTurn = ???
  private def monstersTurn = ???

  def playTurn(command: String) = ???
    
  def welcomeMessage = ???
  
  def goodbyeMessage = ???

  def currentRound = roundCount

  def reset(character: Character) = ???
  
  def isOver: Boolean =
    roundCount >= 10
  
  def testTurn(command: String) =
    val input = command.trim.toLowerCase
    input match
      case "yes" => "Whoop"
      case "no" => "alright nope"
      case other => None
      