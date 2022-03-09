package com.example.tictactoe

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity() : AppCompatActivity() {
    var mycount=0
    var bool=true
    private var playerOneTurn =true
    private var playerOneMoves= mutableListOf<Int>()
    private var playerTwoMoves= mutableListOf<Int>()

    private val possibleWins:Array<List<Int>> = arrayOf(
        listOf(1,2,3),
        listOf(4,5,6),
        listOf(7,8,9),

        listOf(1,4,7),
        listOf(2,5,8),
        listOf(3,6,9),

        listOf(1,5,9),
        listOf(3,5,7)
    )

    constructor(parcel: Parcel) : this() {
        mycount = parcel.readInt()
        bool = parcel.readByte() != 0.toByte()
        playerOneTurn = parcel.readByte() != 0.toByte()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBoard()
        reset.setOnClickListener {
            restartActivity(it)

        }

    }
    private fun restartActivity(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun setupBoard() {
        var counter=1
        val param1= LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,0
        )
        val params2= LinearLayout.LayoutParams(
            0, LinearLayout.LayoutParams.MATCH_PARENT
        )

        for (i in 1..3){
            val linearLayout= LinearLayout(this)
            linearLayout.orientation= LinearLayout.HORIZONTAL
            linearLayout.layoutParams= param1
            param1.weight=1.0F

            for(j in 1..3){
                val button= Button(this)
                button.id= counter
                button.textSize=42.0F
                button.setTextColor(ContextCompat.getColor(this,R.color.purple_200))
                //button.setBackgroundResource(R.color.black)

                button.layoutParams=params2
                params2.weight=1.0F
                button.setOnClickListener {
                    recordMove(it)
                }
                linearLayout.addView(button)
                counter++
            }
            board.addView(linearLayout)
        }
    }

    private fun recordMove(view: View) {
        val button= view as Button
        val id = button.id

        if(playerOneTurn){
            playerOneMoves.add(id)
            mycount++
            button.text="0"
            button.isEnabled=false
            if(checkWin(playerOneMoves)){
                showWinMessage(player_one)
            }
            else{
                playerOneTurn=false
                togglePlayerTurn(player_two_label,player_one_label)
            }
        }
        else{
            playerTwoMoves.add(id)
            mycount++
            button.text="X"
            button.isEnabled=false
            if(checkWin(playerTwoMoves)){
                showWinMessage(player_two)
            }
            else{
                playerOneTurn=true
                togglePlayerTurn(player_one_label,player_two_label)
            }
        }

    }

    private fun togglePlayerTurn(playerOn: TextView, playerOff: TextView){
        playerOff.setTextColor(ContextCompat.getColor(this,R.color.design_default_color_primary_dark))
        playerOff.setTypeface(null, Typeface.NORMAL)
        playerOn.setTextColor(ContextCompat.getColor(this,R.color.purple_200))
        playerOn.setTypeface(null, Typeface.BOLD)
        if(bool && mycount==9){
            Toast.makeText(this,"Woops! Match Drawn.", Toast.LENGTH_LONG).show()

        }
    }

    private fun showWinMessage(player: EditText) {
        var playerName= player.text.toString()
        if(playerName.isBlank()){
            playerName=player.hint.toString()
        }
        Toast.makeText(this,"Congratulations: $playerName You Won!", Toast.LENGTH_SHORT).show()
        bool=false
    }
    private fun checkWin(moves:List<Int>): Boolean {
        var won=false
        if(moves.size>=3){
            run loop@{
                possibleWins.forEach {
                    if(moves.containsAll(it)){
                        won=true
                        return@loop
                    }
                }
            }
        }
        return won

    }
}
